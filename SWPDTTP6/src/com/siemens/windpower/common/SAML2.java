package com.siemens.windpower.common;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Date;

import oracle.security.crypto.util.Utils;
import oracle.security.xmlsec.saml2.core.Assertion;
import oracle.security.xmlsec.saml2.core.AudienceRestriction;
import oracle.security.xmlsec.saml2.core.AuthnContext;
import oracle.security.xmlsec.saml2.core.AuthnStatement;
import oracle.security.xmlsec.saml2.core.Conditions;
import oracle.security.xmlsec.saml2.core.Issuer;
import oracle.security.xmlsec.saml2.core.NameID;
import oracle.security.xmlsec.saml2.core.Subject;
import oracle.security.xmlsec.saml2.util.SAML2Initializer;
import oracle.security.xmlsec.saml2.util.SAML2URI;
import oracle.security.xmlsec.util.XMLUtils;
import oracle.security.xmlsec.wss.WSSecurity;
import oracle.security.xmlsec.wss.soap.WSSOAPEnvelope;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.siemens.windpower.common.PrimaveraUserValidation.Config;

public class SAML2 {
	// ~ Static fields/initializers
	// -----------------------------------------------------------------

	private static final String SAML_ISSUER = "http://your.saml.issuer.com";

	// ~ Instance fields
	// ----------------------------------------------------------------------------

	// ~ Constructors
	// -------------------------------------------------------------------------------

	public SAML2() {
	}

	// ~ Methods
	// ------------------------------------------------------------------------------------

	public static Element addSAMLAssertion(WSSecurity sec,
			WSSOAPEnvelope wsEnvelope, PrimaveraUserValidation.Config config)
			throws Exception {
		SAML2Initializer.initialize();

		Document aDoc = wsEnvelope.getOwnerDocument();

		// Create all the information that we need for our own SAML assertion
		// And since we're acting as the identity provider, we also specify how
		// the user authenticated
		String assertionId = XMLUtils.randomName();
		Date notBefore = new Date();
		Date notOnOrAfter = Utils.minutesFrom(notBefore, 5);

		// Create the assertion element we need based on all the information
		// above
		Assertion assertion = createAssertion(aDoc, assertionId, SAML_ISSUER,
				notBefore, notOnOrAfter, SAML_ISSUER, config);
		sec.appendChild(assertion);

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

	private static Assertion createAssertion(Document doc, String assertionID,
			String issuer, Date notBefore, Date notOnOrAfter, String audience,
			PrimaveraUserValidation.Config config) throws Exception {
		// Creates Assertion instance
		Assertion assertion = new Assertion(doc);
		assertion.setID(assertionID);
		assertion.setIssueInstant(new Date());
		assertion.setVersion("2.0");

		// issuer
		Issuer issuerElement = new Issuer(doc);
		issuerElement.setValue(issuer);
		assertion.setIssuer(issuerElement);

		// subject
		Subject subject = createSAMLSubject(doc, config.username);
		assertion.setSubject(subject);

		// Creates Conditions element
		Conditions cs = new Conditions(doc);
		cs.setNotBefore(notBefore);
		cs.setNotOnOrAfter(notOnOrAfter);

		if (audience != null) {
			// Creates AudienceRestrictionCondition element
			AudienceRestriction ar = new AudienceRestriction(doc);
			ar.addAudience(audience);

			// Appends arc to cs
			cs.addCondition(ar);
		}

		// Appends cs to assertion
		assertion.setConditions(cs);

		// Appends statement to assertion
		AuthnStatement statement = createAuthnStatement(doc);
		assertion.addStatement(statement);

		return assertion;
	}

	private static AuthnStatement createAuthnStatement(Document doc) {
		AuthnStatement statement = new AuthnStatement(doc);
		statement.setAuthnInstant(new Date());

		AuthnContext authnContext = new AuthnContext(doc);
		authnContext.setAuthnContextDecl(SAML2URI.ns_samlacClasses_password,
				null);
		// authnContext.setAuthnContextDeclRef(SAML2URI.authentication_method_password);
		statement.setAuthnContext(authnContext);

		return statement;
	}

	private static Subject createSAMLSubject(Document doc, String userName) {
		// Create the saml subject for the user we wish to login as
		NameID subjectName = new NameID(doc);
		subjectName.setValue(userName);

		return new Subject(doc, subjectName);
	}
}
