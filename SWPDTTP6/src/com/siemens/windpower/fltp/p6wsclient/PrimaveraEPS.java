package com.siemens.windpower.fltp.p6wsclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.eps.EPSFieldType;
import com.primavera.ws.p6.eps.EPSPortType;
import com.primavera.ws.p6.eps.EPSService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraEPS {
	private static final String EPS_SERVICE = DTTConstants.EPS_SERVICE;
	ProjectMasterBean epsmasterbean = null;
	Logger logger = null;
	URL wsdlURL = null;
	EPSService service = null;
	static EPSPortType servicePort = null;
	Client client = null;
	static List<EPSFieldType> epsFields = null;

	PrimaveraEPS() throws MalformedURLException {
		logger = Logger.getLogger(PrimaveraWSClient.class);
		if (servicePort == null) {
			initlialiseEPS();

		}

	}

	public void initlialiseEPS() throws MalformedURLException {

		epsmasterbean = new ProjectMasterBean();
		String url = PrimaveraUserValidation.makeHttpURLString(EPS_SERVICE);
		 logger.info("EPS"+url);
		wsdlURL = new URL(url);
		service = new EPSService(wsdlURL);
		logger.info("service "+service);
		servicePort = service.getEPSPort();
		logger.info("service "+servicePort);
		client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		logger.info("PrimaveraUserValidation 1 "+client);
		epsFields = new ArrayList<EPSFieldType>();
		epsFields.add(EPSFieldType.ID);
		epsFields.add(EPSFieldType.OBJECT_ID);
		epsFields.add(EPSFieldType.PARENT_OBJECT_ID);
		epsFields.add(EPSFieldType.NAME);

	}

	public List readEPS() throws Exception {
		List<EPS> EPSs = null;
		try {

			// location="Id='"+location+"'";
			EPSs = servicePort.readEPS(epsFields, null, null);

			if ((EPSs == null) || (EPSs.size() == 0)) {
				// logger.info("No EPS node available");

				return null;
			}

		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR012 + e);
			throw e;

		}
		return EPSs;

	}

	public List readWSProjectEPS(int objectid) throws Exception {
		List<EPS> EPSs = null;
		try {

			String ParentObjectId = "ParentObjectId=" + objectid;
			/* System.out.println(ParentObjectId); */
			EPSs = servicePort.readEPS(epsFields, ParentObjectId, null);

			if ((EPSs == null) || (EPSs.size() == 0)) {
				System.out.println("No EPS node available");

				return null;
			}

		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR013 + e);
			throw e;
		}
		return EPSs;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> CreateEPS(List<EPS> epscreateelement) throws Exception {
		List<Integer> EPSs = null;
		try {

			EPSs = servicePort.createEPS(epscreateelement);

			if ((EPSs == null) || (EPSs.size() == 0)) {
				System.out.println("No EPS node available");

			} else {
				System.out.println("EPS node Created" + EPSs.size());

			}
		}

		catch (Exception e) {
			logger.error(DTTErrorConstants.ERR014 + e);
			e.printStackTrace();
			throw e;
		}
		return EPSs;

	}

}
