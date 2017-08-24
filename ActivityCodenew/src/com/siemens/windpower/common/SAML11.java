package com.siemens.windpower.common;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Date;

import oracle.security.crypto.util.Utils;
import oracle.security.xmlsec.saml.Assertion;
import oracle.security.xmlsec.saml.AudienceRestrictionCondition;
import oracle.security.xmlsec.saml.AuthenticationStatement;
import oracle.security.xmlsec.saml.Conditions;
import oracle.security.xmlsec.saml.NameIdentifier;
import oracle.security.xmlsec.saml.SAMLInitializer;
import oracle.security.xmlsec.saml.SAMLURI;
import oracle.security.xmlsec.saml.Statement;
import oracle.security.xmlsec.saml.Subject;
import oracle.security.xmlsec.util.XMLUtils;
import oracle.security.xmlsec.wss.WSSecurity;
import oracle.security.xmlsec.wss.saml.SAMLAssertionToken;
import oracle.security.xmlsec.wss.soap.WSSOAPEnvelope;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.siemens.windpower.common.PrimaveraUserValidation.Config;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class SAML11 {
	// ~ Static fields/initializers
	// -----------------------------------------------------------------

	private static final String SAML_ISSUER = DTTConstants.SAML_ISSUER;

	// ~ Instance fields
	// ----------------------------------------------------------------------------

	// ~ Constructors
	// -------------------------------------------------------------------------------

	public SAML11() {
	}

	// ~ Methods
	// ------------------------------------------------------------------------------------

	public static Element addSAMLAssertion(WSSecurity sec,
			WSSOAPEnvelope wsEnvelope, PrimaveraUserValidation.Config config)
			throws Exception {
		SAMLInitializer.initialize(1, 1);

		Document aDoc = wsEnvelope.getOwnerDocument();

		// Create all the information that we need for our own SAML assertion
		// And since we're acting as the identity provider, we also specify how
		// the user authenticated
		AuthenticationStatement statement = new AuthenticationStatement(aDoc);
		statement
				.setAuthenticationMethod(SAMLURI.authentication_method_password);
		statement.setAuthenticationInstant(new Date());
		statement.setSubject(createSAMLSubject(aDoc, config.username));
		String assertionId = XMLUtils.randomName();
		Date notBefore = new Date();
		Date notOnOrAfter = Utils.minutesFrom(notBefore, 5);

		// Create the assertion element we need based on all the information
		// above
		Assertion assertion = createAssertion(aDoc, assertionId, SAML_ISSUER,
				notBefore, notOnOrAfter, SAML_ISSUER, statement);
		SAMLAssertionToken samlToken = new SAMLAssertionToken(assertion);
		sec.addSAMLAssertionToken(samlToken);

		// Finally, to prove that the assertion that we're sending out is
		// actually from the identity provider (us),
		// we can sign the message with our private key.
		if (config.samlSigned) {
			// We just need to load the digital certificate and private key from
			// the keystore specified
			KeyStore keyStore = KeyStore.getInstance(config.samlKeystoreType);
			keyStore.load(new FileInputStream(config.samlKeystore),
					config.samlKeystorepass.toCharArray());
			String privateKeyPassword = config.samlKeypass;
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(
					config.samlAlias, privateKeyPassword.toCharArray());

			// And we can use the private key to sign our assertion, verifying
			// that the message comes from us
			assertion.sign(privateKey, null);
		}

		return assertion.getElement();
	}

	private static Subject createSAMLSubject(Document doc, String userName) {
		// Create the saml subject for the user we wish to login as
		Subject subject = new Subject(doc);
		NameIdentifier name = new NameIdentifier(doc);
		name.setValue(userName);
		subject.setNameIdentifier(name);

		return subject;
	}

	private static Assertion createAssertion(Document doc, String assertionID,
			String issuer, Date notBefore, Date notOnOrAfter, String audience,
			Statement statement) throws Exception {
		// Creates Assertion instance
		Assertion assertion = new Assertion(doc);
		assertion.setAssertionID(assertionID);
		assertion.setIssuer(issuer);
		assertion.setIssueInstant(new Date());
		assertion.setVersion(1, 1);

		// Creates Conditions element
		Conditions cs = new Conditions(doc);
		cs.setNotBefore(notBefore);
		cs.setNotOnOrAfter(notOnOrAfter);

		if (audience != null) {
			// Creates AudienceRestrictionCondition element
			AudienceRestrictionCondition arc = new AudienceRestrictionCondition(
					doc);
			arc.addAudience(audience);

			// Appends arc to cs
			cs.addCondition(arc);
		}

		// Appends cs to assertion
		assertion.setConditions(cs);

		// Appends statement to assertion
		assertion.addStatement(statement);

		return assertion;
	}
}
