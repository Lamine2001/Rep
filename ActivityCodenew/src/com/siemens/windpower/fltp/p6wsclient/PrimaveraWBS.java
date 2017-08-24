package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.project.ProjectFieldType;
import com.primavera.ws.p6.project.ProjectPortType;
import com.primavera.ws.p6.project.ProjectService;
import com.primavera.ws.p6.wbs.WBS;
import com.primavera.ws.p6.wbs.WBSFieldType;
import com.primavera.ws.p6.wbs.WBSPortType;
import com.primavera.ws.p6.wbs.WBSService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraWBS {

	private static final String WBS_SERVICE = DTTConstants.WBS_SERVICE;
	ProjectMasterBean projectmasterbean = null;
	static WBSPortType servicePort = null;
	static WBSService service = null;
	Logger logger = null;
	static List<WBSFieldType> fields = null;

	PrimaveraWBS() throws Exception {
		if (service == null) {
			initlialiseWBS();
		}

	}

	public void initlialiseWBS() throws Exception {
		logger = Logger.getLogger(PrimaveraWBS.class);
		service = createWBSService();
		fields = new ArrayList<WBSFieldType>();
		fields.add(WBSFieldType.OBJECT_ID);
		fields.add(WBSFieldType.PROJECT_OBJECT_ID);
		fields.add(WBSFieldType.OBS_NAME);
		fields.add(WBSFieldType.PARENT_OBJECT_ID);
		fields.add(WBSFieldType.NAME);
		fields.add(WBSFieldType.CODE);
		fields.add(WBSFieldType.OBS_OBJECT_ID);
		fields.add(WBSFieldType.WBS_CATEGORY_OBJECT_ID);
	}

	WBSService createWBSService() throws Exception {
		String url = PrimaveraUserValidation.makeHttpURLString(WBS_SERVICE);
		URL wsdlURL = new URL(url);
		service = new WBSService(wsdlURL);

		return service;
	}

	List<Integer> createWBS(List<WBS> wbs) throws Exception {
		List<Integer> objIds = null;
		try {
			servicePort = service.getWBSPort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);

			objIds = servicePort.createWBS(wbs);
			// logger.info(objIds.size() + " Project created:");
		} catch (Exception e) {
			// logger.info("Error while creating wbs" + e);
			e.printStackTrace();
			throw e;
		}
		return objIds;
	}

	boolean updateWBS(List<WBS> wbs) throws Exception {
		servicePort = service.getWBSPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		boolean objIds = servicePort.updateWBS(wbs);
		// logger.info(" WBS Updates have Done:");

		return objIds;
	}

	List readWBS(String objectid) throws Exception {

		servicePort = service.getWBSPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		// Load project with specific Id
		String ParentObjectId = "ProjectObjectId=" + objectid;
		List<WBS> wbss = servicePort.readWBS(fields, ParentObjectId, null);

		/*
		 * if ((wbss == null) || (wbss.size() == 0)) { return null; }
		 */

		return wbss;
	}

}
