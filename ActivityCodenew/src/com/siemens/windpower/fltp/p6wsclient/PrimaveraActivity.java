package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.JAXBElement;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.activity.ActivityFieldType;
import com.primavera.ws.p6.activity.ActivityPortType;
import com.primavera.ws.p6.activity.ActivityService;
import com.primavera.ws.p6.activity.ObjectFactory;
import com.primavera.ws.p6.baselineproject.BaselineProject;
import com.primavera.ws.p6.baselineproject.BaselineProjectFieldType;
import com.primavera.ws.p6.baselineproject.BaselineProjectPortType;
import com.primavera.ws.p6.baselineproject.BaselineProjectService;
import com.primavera.ws.p6.eps.EPSFieldType;
import com.primavera.ws.p6.wbs.WBSFieldType;
import com.primavera.ws.p6.wbs.WBSPortType;
import com.primavera.ws.p6.wbs.WBSService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;

public class PrimaveraActivity {

	private static final String ACTIVITY_SERVICE = DTTConstants.ACTIVITY_SERVICE;
	private static final String PROJECT_BASELINE_SERVICE = DTTConstants.PROJECT_BASELINE_SERVICE;

	static ActivityService service = null;
	static BaselineProjectService baselineprojectservice = null;

	ProjectMasterBean projectmasterbean = null;

	Logger logger = null;
	static List<ActivityFieldType> fields = null;
	static List<BaselineProjectFieldType> baselinefields = null;

	PrimaveraActivity() throws Exception {
		if (service == null || baselineprojectservice == null) {
			initlialiseActivity();
		}

	}

	public void initlialiseActivity() throws Exception {
		logger = Logger.getLogger(PrimaveraActivity.class);
		service = createActivityService();
		baselineprojectservice = createProjectBaselineService();
		fields = new ArrayList<ActivityFieldType>();
		fields.add(ActivityFieldType.ID);
		fields.add(ActivityFieldType.OBJECT_ID);
		fields.add(ActivityFieldType.PROJECT_OBJECT_ID);
		fields.add(ActivityFieldType.NAME);
		fields.add(ActivityFieldType.PROJECT_ID);
		fields.add(ActivityFieldType.WBS_OBJECT_ID);
		fields.add(ActivityFieldType.PLANNED_LABOR_UNITS);
		fields.add(ActivityFieldType.DURATION_TYPE);
		fields.add(ActivityFieldType.TYPE);
		fields.add(ActivityFieldType.BASELINE_FINISH_DATE);
		fields.add(ActivityFieldType.BASELINE_START_DATE);
		fields.add(ActivityFieldType.LAST_UPDATE_DATE);
		fields.add(ActivityFieldType.START_DATE);

		baselinefields = new ArrayList<BaselineProjectFieldType>();
		baselinefields.add(BaselineProjectFieldType.ID);
		baselinefields.add(BaselineProjectFieldType.OBJECT_ID);
		baselinefields.add(BaselineProjectFieldType.BASELINE_TYPE_NAME);
		baselinefields.add(BaselineProjectFieldType.NAME);
		baselinefields.add(BaselineProjectFieldType.BASELINE_TYPE_OBJECT_ID);
		baselinefields.add(BaselineProjectFieldType.WBS_OBJECT_ID);
		baselinefields.add(BaselineProjectFieldType.LAST_BASELINE_UPDATE_DATE);
		baselinefields.add(BaselineProjectFieldType.ORIGINAL_PROJECT_OBJECT_ID);
		baselinefields.add(BaselineProjectFieldType.LAST_UPDATE_DATE);

	}

	ActivityService createActivityService() throws Exception {
		String url = PrimaveraUserValidation
				.makeHttpURLString(ACTIVITY_SERVICE);
		URL wsdlURL = new URL(url);
		service = new ActivityService(wsdlURL);

		return service;
	}

	BaselineProjectService createProjectBaselineService() throws Exception {
		String url = PrimaveraUserValidation
				.makeHttpURLString(PROJECT_BASELINE_SERVICE);
		URL wsdlURL = new URL(url);
		baselineprojectservice = new BaselineProjectService(wsdlURL);

		return baselineprojectservice;
	}

	List<Integer> createActivities(List<Activity> activities) throws Exception {
		ActivityPortType servicePort = service.getActivityPort();
		Client client = ClientProxy.getClient(servicePort);
		
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		// logger.info("Activity Create Primavera End"+java.util.Calendar.getInstance().getTime());
		List<Integer> objIds = servicePort.createActivities(activities);
		// logger.info("Activity Create Primavera End"+java.util.Calendar.getInstance().getTime());

		return objIds;
	}

	boolean updateActivities(List<Activity> activities) throws Exception {
		/*
		 * String url = PrimaveraUserValidation
		 * .makeHttpURLString(ACTIVITY_SERVICE); URL wsdlURL = new URL(url);
		 * ActivityService service = new ActivityService(wsdlURL);
		 * ActivityPortType servicePort = service.getActivityPort(); Client
		 * client = ClientProxy.getClient(servicePort);
		 * PrimaveraUserValidation.setCookieOrUserTokenData(client);
		 */if (service == null) {
			initlialiseActivity();
		}
		// logger.info("Activity Update Primavera "+java.util.Calendar.getInstance().getTime());
		ActivityPortType servicePort = service.getActivityPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		boolean objIds = servicePort.updateActivities(activities);

		// logger.info("Activity Update Primavera End"+java.util.Calendar.getInstance().getTime());

		return objIds;
	}

	List<BaselineProject> readBaselineProjectsOutbound(String todaysdate)
			throws Exception {

		if (baselineprojectservice == null) {
			initlialiseActivity();
		}
		BaselineProjectPortType baselineservicePort = baselineprojectservice
				.getBaselineProjectPort();
		Client client = ClientProxy.getClient(baselineservicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);

		String LastBaselineUpdateDate = "LastBaselineUpdateDate>=CONVERT(datetime,'"
				+ todaysdate + "',120)";
		List<BaselineProject> objIds = baselineservicePort
				.readBaselineProjects(baselinefields, LastBaselineUpdateDate,
						null);
		return objIds;

	}

	List<Activity> readActivitiesOutbound(Integer wbsobjectid,
			String lasatupdatedate) throws Exception {
		/*
		 * String url = PrimaveraUserValidation
		 * .makeHttpURLString(ACTIVITY_SERVICE); URL wsdlURL = new URL(url);
		 * ActivityService service = new ActivityService(wsdlURL);
		 * ActivityPortType servicePort = service.getActivityPort(); Client
		 * client = ClientProxy.getClient(servicePort);
		 * PrimaveraUserValidation.setCookieOrUserTokenData(client);
		 */
		// String lasatupdatedate="2015-03-16 00:00:00";
		if (service == null) {
			initlialiseActivity();
		}
		/*
		 * List<ActivityFieldType> activities = new
		 * ArrayList<ActivityFieldType>();
		 */
		ActivityPortType servicePort = service.getActivityPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);

		String wbsobject = "ProjectObjectId=" + wbsobjectid.toString()
				+ " AND " + "LastUpdateDate>=CONVERT(datetime,'"
				+ lasatupdatedate + "',120)";
		// logger.info("Activity Read outbound "+java.util.Calendar.getInstance().getTime());
		List<Activity> objIds = servicePort.readActivities(fields, wbsobject,
				null);
		// logger.info("Activity Read outbound End"+java.util.Calendar.getInstance().getTime());

		return objIds;
	}

	List<Activity> readActivities(int wbsObjectId) throws Exception {
		/*
		 * String url = PrimaveraUserValidation
		 * .makeHttpURLString(ACTIVITY_SERVICE); URL wsdlURL = new URL(url);
		 * ActivityService service = new ActivityService(wsdlURL);
		 * ActivityPortType servicePort = service.getActivityPort(); Client
		 * client = ClientProxy.getClient(servicePort);
		 * PrimaveraUserValidation.setCookieOrUserTokenData(client);
		 */
		if (service == null) {
			initlialiseActivity();
		}
		ActivityPortType servicePort = service.getActivityPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		String type = "Task Dependent";

		String wbsobjectId = "WBSObjectId=" + String.valueOf(wbsObjectId)
				+ " AND Type='Task Dependent'";
		// logger.info("Activity Read "+java.util.Calendar.getInstance().getTime());
		List<Activity> objIds = servicePort.readActivities(fields, wbsobjectId,
				null);
		// logger.info("Activity Read end"+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	List<Activity> readActivitieforAssignment(int wbsObjectId) throws Exception {
		/*
		 * String url = PrimaveraUserValidation
		 * .makeHttpURLString(ACTIVITY_SERVICE); URL wsdlURL = new URL(url);
		 * ActivityService service = new ActivityService(wsdlURL);
		 * ActivityPortType servicePort = service.getActivityPort(); Client
		 * client = ClientProxy.getClient(servicePort);
		 * PrimaveraUserValidation.setCookieOrUserTokenData(client);
		 */
		if (service == null) {
			initlialiseActivity();
		}
		ActivityPortType servicePort = service.getActivityPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);

		String wbsobjectId = "WBSObjectId=" + String.valueOf(wbsObjectId);
		// logger.info("Activity Read "+java.util.Calendar.getInstance().getTime());
		List<Activity> objIds = servicePort.readActivities(fields, wbsobjectId,
				null);
		// logger.info("Activity Read end"+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	public JAXBElement<Integer> constructWBSObject(int wbsobjectid) {
		ObjectFactory objectfactory = new ObjectFactory();
		return objectfactory.createActivityWBSObjectId(wbsobjectid);
	}

}
