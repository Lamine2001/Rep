package com.siemens.windpower.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import oracle.security.xmlsec.enc.XEReferenceList;
import oracle.security.xmlsec.util.XMLUtils;
import oracle.security.xmlsec.wss.WSSecurity;
import oracle.security.xmlsec.wss.soap.WSSOAPEnvelope;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.SoapVersion;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class InInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
	// ~ Constructors
	// -------------------------------------------------------------------------------
	
	
	
	Logger logger = Logger.getLogger(InInterceptor.class);
	public InInterceptor() {
		
		super(Phase.RECEIVE);
		
	}

	// ~ Methods
	// ------------------------------------------------------------------------------------

	public void handleMessage(SoapMessage message) throws Fault {
		SoapVersion version = message.getVersion();

		try {
			// First we need to do some general setup to get the Soap
			// information we need
		//	logger.info("Begin");
			InputStream inputStream = message.getContent(InputStream.class);
			//logger.info("inputStream"+inputStream);
			DocumentBuilder db = XMLUtils.createDocBuilder();
			//logger.info("DocumentBuilder"+db);
			Document doc = db.parse(inputStream);
		//	logger.info("Document"+doc);
			WSSOAPEnvelope wsEnvelope = new WSSOAPEnvelope(
					doc.getDocumentElement());
			//logger.info("WSSOAPEnvelope"+wsEnvelope);
			List<?> securityList = wsEnvelope.getSecurity();
			WSSecurity wsSecurity = (WSSecurity) securityList.get(0);
			//logger.info("wsSecurity"+wsSecurity);
			// Once we have all this, we can get all the encrypted information
			List<XEReferenceList> xRefList = wsSecurity.getReferenceLists();

			for (XEReferenceList xerRef : xRefList) {
				// And decrypt it all one by one
				WSSecurity.decrypt(xerRef, SecretKeyHolder.getSecretKey());
			}

			// And lastly we make sure that the message is updated with the
			// unencrypted information
			//logger.info("message"+message);
			message.setContent(InputStream.class, new ByteArrayInputStream(
					XMLUtils.toBytesXML(wsEnvelope, false, false)));
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			throw cce;
		} catch (Exception e) {
			throw new SoapFault(DTTErrorConstants.ERR005, e,
					version.getSender());
		}
	}
}
