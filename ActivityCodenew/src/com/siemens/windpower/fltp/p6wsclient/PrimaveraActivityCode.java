package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.ActivityFieldType;
import com.primavera.ws.p6.activity.ActivityPortType;
import com.primavera.ws.p6.activitycode.ActivityCode;
import com.primavera.ws.p6.activitycode.ActivityCodeFieldType;
import com.primavera.ws.p6.activitycode.ActivityCodePortType;
import com.primavera.ws.p6.activitycode.ActivityCodeService;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignment;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignmentFieldType;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignmentPortType;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignmentService;
import com.primavera.ws.p6.activitycodeassignment.CreateActivityCodeAssignmentsResponse.ObjectId;
import com.primavera.ws.p6.activitycodetype.ActivityCodeType;
import com.primavera.ws.p6.activitycodetype.ActivityCodeTypeFieldType;
import com.primavera.ws.p6.activitycodetype.ActivityCodeTypePortType;
import com.primavera.ws.p6.activitycodetype.ActivityCodeTypeService;
import com.primavera.ws.p6.baselineproject.BaselineProjectFieldType;
import com.primavera.ws.p6.baselineproject.BaselineProjectPortType;
import com.primavera.ws.p6.eps.EPSFieldType;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.project.ProjectPortType;
import com.primavera.ws.p6.project.ProjectService;
import com.primavera.ws.p6.projectcode.ProjectCode;
import com.primavera.ws.p6.projectcode.ProjectCodeFieldType;
import com.primavera.ws.p6.projectcode.ProjectCodePortType;
import com.primavera.ws.p6.projectcode.ProjectCodeService;
import com.primavera.ws.p6.udfcode.UDFCode;
import com.primavera.ws.p6.udfcode.UDFCodePortType;
import com.primavera.ws.p6.udfcode.UDFCodeService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.fltp.beans.ActivityMasterBean;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraActivityCode {

	private static final String ACTIVITY_CODE_ASSIGNMENT_SERVICE = DTTConstants.ACTIVITY_CODE_ASSIGNMENT_SERVICE;
	private static final String ACTIVITY_CODE_SERVICE = DTTConstants.ACTIVITY_CODE_SERVICE;
	private static final String ACTIVITY_CODE_TYPE_SERVICE = DTTConstants.ACTIVITY_CODE_TYPE_SERVICE;

	ActivityMasterBean activitymasterbean = null;

	ProjectMasterBean projectmasterbean = null;
	static ActivityCodePortType servicePort = null;
	static ActivityCodeAssignmentPortType activitycodeassignmentserviceport = null;
	static ActivityCodeTypePortType servicetypePort = null;
	Logger logger = null;
	static List<ActivityCodeFieldType> fields = null;
	static List<ActivityCodeAssignmentFieldType> codeassignmentfields = null;
	static List<ActivityCodeTypeFieldType> typefields = null;

	public PrimaveraActivityCode() throws Exception {
		activitymasterbean = new ActivityMasterBean();
		if (servicePort == null || activitycodeassignmentserviceport == null ||servicetypePort == null ) {
			initlialiseActivityCode();
		}

	}

	public void initlialiseActivityCode() throws Exception {
		logger = Logger.getLogger(PrimaveraActivityCode.class);
		servicePort = createActivitycodeServicePort();
		servicetypePort = createActivitycodeTypeServicePort();
		activitycodeassignmentserviceport = createActivitycodeAssingmentServicePort();
		fields = new ArrayList<ActivityCodeFieldType>();
		fields.add(ActivityCodeFieldType.CODE_TYPE_NAME);
		fields.add(ActivityCodeFieldType.CODE_TYPE_OBJECT_ID);
		fields.add(ActivityCodeFieldType.PARENT_OBJECT_ID);
		fields.add(ActivityCodeFieldType.OBJECT_ID);
		fields.add(ActivityCodeFieldType.CODE_VALUE);
		fields.add(ActivityCodeFieldType.DESCRIPTION);
		fields.add(ActivityCodeFieldType.CODE_TYPE_SCOPE);
		
		typefields = new ArrayList<ActivityCodeTypeFieldType>();
		typefields.add(ActivityCodeTypeFieldType.NAME);
		typefields.add(ActivityCodeTypeFieldType.EPS_OBJECT_ID);
		typefields.add(ActivityCodeTypeFieldType.OBJECT_ID);
		typefields.add(ActivityCodeTypeFieldType.PROJECT_OBJECT_ID);
		typefields.add(ActivityCodeTypeFieldType.EPS_CODE_TYPE_HIERARCHY);
		typefields.add(ActivityCodeTypeFieldType.EPS_OBJECT_ID);
		typefields.add(ActivityCodeTypeFieldType.SCOPE);

		codeassignmentfields = new ArrayList<ActivityCodeAssignmentFieldType>();
		codeassignmentfields
				.add(ActivityCodeAssignmentFieldType.ACTIVITY_OBJECT_ID);
		codeassignmentfields
				.add(ActivityCodeAssignmentFieldType.ACTIVITY_CODE_OBJECT_ID);
		codeassignmentfields
				.add(ActivityCodeAssignmentFieldType.ACTIVITY_CODE_TYPE_NAME);
		codeassignmentfields
				.add(ActivityCodeAssignmentFieldType.ACTIVITY_CODE_VALUE);
		codeassignmentfields
				.add(ActivityCodeAssignmentFieldType.ACTIVITY_CODE_TYPE_OBJECT_ID);

	}

	private ActivityCodePortType createActivitycodeServicePort()
			throws Exception {
		String url = PrimaveraUserValidation
				.makeHttpURLString(ACTIVITY_CODE_SERVICE);
		URL wsdlURL = new URL(url);
		ActivityCodeService service = new ActivityCodeService(wsdlURL);
		ActivityCodePortType servicePort = service.getActivityCodePort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);

		return servicePort;
	}
	private ActivityCodeTypePortType createActivitycodeTypeServicePort()
			throws Exception {
		String url = PrimaveraUserValidation
				.makeHttpURLString(ACTIVITY_CODE_TYPE_SERVICE);
		URL wsdlURL = new URL(url);
		ActivityCodeTypeService service = new ActivityCodeTypeService(wsdlURL);
		ActivityCodeTypePortType servicetypePort = service.getActivityCodeTypePort();
		Client client = ClientProxy.getClient(servicetypePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);

		return servicetypePort;
	}

	private ActivityCodeAssignmentPortType createActivitycodeAssingmentServicePort()
			throws Exception {
		String url = PrimaveraUserValidation
				.makeHttpURLString(ACTIVITY_CODE_ASSIGNMENT_SERVICE);
		URL wsdlURL = new URL(url);
		ActivityCodeAssignmentService service = new ActivityCodeAssignmentService(
				wsdlURL);
		ActivityCodeAssignmentPortType servicePort = service
				.getActivityCodeAssignmentPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);

		return servicePort;
	}

	public List<ActivityCode> readActivityCode() throws Exception {

		// projectmasterbean.setContractname("Test");

		// logger.info("Activity Code Primavera "+java.util.Calendar.getInstance().getTime());
		// activityobjectid="ParentObjectId="+activityobjectid;
		List<ActivityCode> objIds = servicePort.readActivityCodes(fields, null,
				null);
		// logger.info("Activity Code Primavera End"+java.util.Calendar.getInstance().getTime());
		// //logger.info(objIds.size() + " Activity read:");

		return objIds;
	}
	public List<ActivityCodeType> readActivityCodeType() throws Exception {

		// projectmasterbean.setContractname("Test");

		// logger.info("Activity Code Primavera "+java.util.Calendar.getInstance().getTime());
		// activityobjectid="ParentObjectId="+activityobjectid;
		List<ActivityCodeType> objIds = servicetypePort.readActivityCodeTypes(typefields, null,
				null);
		// logger.info("Activity Code Primavera End"+java.util.Calendar.getInstance().getTime());
		// //logger.info(objIds.size() + " Activity read:");

		return objIds;
	}
	
	public List<ActivityCode> readActivityCodes(String activityobjectid)
			throws Exception {

		// projectmasterbean.setContractname("Test");

		// logger.info("Activity Code Primavera "+java.util.Calendar.getInstance().getTime());
		activityobjectid = "ParentObjectId=" + activityobjectid;
		List<ActivityCode> objIds = servicePort.readActivityCodes(fields,
				activityobjectid, null);
		// logger.info("Activity Code Primavera End"+java.util.Calendar.getInstance().getTime());
		// //logger.info(objIds.size() + " Activity read:");

		return objIds;
	}

	public List createActivityCode(List<ActivityCode> activitycodes) throws Exception {
		List<Integer> objids = null;

		// logger.info("Activity Code Create Primavera "+java.util.Calendar.getInstance().getTime());

		objids = servicePort.createActivityCodes(activitycodes);
		// logger.info(objids.size() + " Activity Code created:");
		// logger.info("Activity Code Create Primavera  End"+java.util.Calendar.getInstance().getTime());

		return objids;
	}
	
	
	public List<Integer> createActivityCodeType(List<ActivityCodeType> activitycodeTypes) throws Exception {
		List<Integer> objids = null;

		// logger.info("Activity Code Create Primavera "+java.util.Calendar.getInstance().getTime());

		objids = servicetypePort.createActivityCodeTypes(activitycodeTypes);
		// logger.info(objids.size() + " Activity Code created:");
		// logger.info("Activity Code Create Primavera  End"+java.util.Calendar.getInstance().getTime());

		return objids;
	}


	public List createActivityCodeAssignment(List<ActivityCodeAssignment> activitysudf)
			throws Exception {

		// List<ActivityCode> activitysudf = new ArrayList<ActivityCode>();
		// activitymasterbean.setContractname("Test");

		/*
		 * // Create activity with required fields ActivityCode proj = new
		 * ActivityCode(); proj.getCodeTypeObjectId();
		 * //proj.setId(<activityId>);
		 * proj.setCodeValue(activitymasterbean.getSapactivityid());
		 * activitysudf.add(proj);
		 */
		// logger.info("Activity Code Assignment Create Primavera "+java.util.Calendar.getInstance().getTime());
		List<ObjectId> objIds = activitycodeassignmentserviceport.createActivityCodeAssignments(activitysudf);
		System.out.println(objIds.size() + " Activity Code Assignment Created:");
		// logger.info("Activity Code Assignment Create Primavera End "+java.util.Calendar.getInstance().getTime());

		return objIds;
	}

	boolean updateActivityCodeAssignment(
			List<ActivityCodeAssignment> activitysudf) throws Exception {

		// logger.info("Activity Code Assignment Update Primavera "+java.util.Calendar.getInstance().getTime());
		boolean objIds = activitycodeassignmentserviceport
				.updateActivityCodeAssignments(activitysudf);
		// logger.info(" Activity Code Update Assignment Done:");
		// logger.info("Activity Code Assignment Update Primavera End "+java.util.Calendar.getInstance().getTime());

		return objIds;
	}

	List<ActivityCodeAssignment> readActivityCodeAssignment(
			String activityobjectid) throws Exception {
		// logger.info("Activity Code Assignment Read Primavera "+java.util.Calendar.getInstance().getTime());
		activityobjectid = "ActivityObjectId=" + activityobjectid;
		List<ActivityCodeAssignment> objIds = activitycodeassignmentserviceport
				.readActivityCodeAssignments(codeassignmentfields,
						activityobjectid, null);
		// logger.info(objIds.size() + " Activity Read:");
		// logger.info("Activity Code Assignment Read Primavera End "+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	private void UpdateActivityCodeAssignment(List activitycodes)
			throws Exception {

		List<ActivityCode> activitysudf = new ArrayList<ActivityCode>();
		// activitymasterbean.setContractname("Test");

		// Create activity with required fields
		ActivityCode proj = new ActivityCode();
		proj.getCodeTypeObjectId();
		// proj.setId(<activityId>);
		// proj.setCodeValue(activitymasterbean.getSapactivityid());
		activitysudf.add(proj);

		// logger.info(" Activity code updated:");

		/* return objIds.get(0).intValue(); */
	}

}
