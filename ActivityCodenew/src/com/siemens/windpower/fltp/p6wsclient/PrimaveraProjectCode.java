package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.projectcodeassignment.IntegrationFault;

import com.primavera.ws.p6.projectcode.ProjectCode;
import com.primavera.ws.p6.projectcode.ProjectCodeFieldType;
import com.primavera.ws.p6.projectcode.ProjectCodePortType;
import com.primavera.ws.p6.projectcode.ProjectCodeService;
import com.primavera.ws.p6.projectcodeassignment.CreateProjectCodeAssignmentsResponse.ObjectId;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignment;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignmentFieldType;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignmentPortType;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignmentService;
import com.primavera.ws.p6.udfcode.UDFCode;
import com.primavera.ws.p6.udfcode.UDFCodePortType;
import com.primavera.ws.p6.udfcode.UDFCodeService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraProjectCode {

	private static final String PROJECT_CODE_SERVICE = DTTConstants.PROJECT_CODE_SERVICE;
	private static final String PROJECT_CODE_ASSIGNMENT_SERVICE = DTTConstants.PROJECT_CODE_ASSIGNMENT_SERVICE;

	ProjectMasterBean projectmasterbean = null;
	Logger logger = null;
	static ProjectCodePortType servicePort = null;
	static ProjectCodeAssignmentPortType projectcodeservicePort = null;
	static List<ProjectCodeAssignmentFieldType> projectFields = null;
	static List<ProjectCodeFieldType> projectcodeFields = null;

	PrimaveraProjectCode() throws Exception {
		if (servicePort == null || projectcodeservicePort == null) {
			initlialiseProjectCode();
		}
	}

	public void initlialiseProjectCode() throws Exception {
		logger = Logger.getLogger(PrimaveraProjectCode.class);
		projectmasterbean = new ProjectMasterBean();
		servicePort = createProjectcodeServicePort();
		projectcodeFields = new ArrayList<ProjectCodeFieldType>();
		projectcodeFields.add(ProjectCodeFieldType.CODE_TYPE_NAME);
		projectcodeFields.add(ProjectCodeFieldType.CODE_TYPE_OBJECT_ID);
		projectcodeFields.add(ProjectCodeFieldType.PARENT_OBJECT_ID);
		projectcodeFields.add(ProjectCodeFieldType.OBJECT_ID);
		projectcodeFields.add(ProjectCodeFieldType.CODE_VALUE);
		projectcodeFields.add(ProjectCodeFieldType.DESCRIPTION);

		projectcodeservicePort = createProjectcodeAssignmentServicePort();
		projectFields = new ArrayList<ProjectCodeAssignmentFieldType>();
		projectFields
				.add(ProjectCodeAssignmentFieldType.PROJECT_CODE_OBJECT_ID);
		projectFields
				.add(ProjectCodeAssignmentFieldType.PROJECT_CODE_TYPE_NAME);
		projectFields
				.add(ProjectCodeAssignmentFieldType.PROJECT_CODE_TYPE_OBJECT_ID);
		projectFields.add(ProjectCodeAssignmentFieldType.PROJECT_CODE_VALUE);
		projectFields.add(ProjectCodeAssignmentFieldType.PROJECT_OBJECT_ID);
		projectFields.add(ProjectCodeAssignmentFieldType.PROJECT_ID);
		projectFields
				.add(ProjectCodeAssignmentFieldType.PROJECT_CODE_DESCRIPTION);

	}

	private ProjectCodePortType createProjectcodeServicePort() throws Exception {

		try {

			String url = PrimaveraUserValidation
					.makeHttpURLString(PROJECT_CODE_SERVICE);
			URL wsdlURL = new URL(url);
			ProjectCodeService service = new ProjectCodeService(wsdlURL);
			servicePort = service.getProjectCodePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR021 + e);
			throw e;
		}
		return servicePort;
	}

	private ProjectCodeAssignmentPortType createProjectcodeAssignmentServicePort()
			throws Exception {

		try {
			// logger.info("Creating Project code assignment service port");
			String url = PrimaveraUserValidation
					.makeHttpURLString(PROJECT_CODE_ASSIGNMENT_SERVICE);
			URL wsdlURL = new URL(url);
			ProjectCodeAssignmentService service = new ProjectCodeAssignmentService(
					wsdlURL);
			projectcodeservicePort = service.getProjectCodeAssignmentPort();
			Client client = ClientProxy.getClient(projectcodeservicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR022 + e);
			throw e;
		}
		return projectcodeservicePort;

	}

	private List opputunityCreateProjectCode(List<ProjectCode> projectsudf)
			throws Exception {
		List<Integer> objids = null;
		try {

			System.out.println("Project  Code Create  Primavera  "
					+ java.util.Calendar.getInstance().getTime());
			// Create project with required fields
			ProjectCode proj = new ProjectCode();
			proj.getCodeTypeObjectId();
			// proj.setId(<projectId>);
			proj.setCodeValue(projectmasterbean.getSapprojectid());
			projectsudf.add(proj);

			objids = servicePort.createProjectCodes(projectsudf);
			System.out.println(objids.size() + " Project created:");
			System.out.println("Project  Code Create  Primavera END "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			throw e;
		}
		return objids;
	}

	List createProjectCode(List<ProjectCode> proejctcodes) throws Exception,
			IntegrationFault

	{
		List<Integer> objids = null;
		try {

			System.out.println("Project  Code Create  Primavera  "
					+ java.util.Calendar.getInstance().getTime());
			objids = servicePort.createProjectCodes(proejctcodes);
			System.out.println(objids.size() + " Project created:");
			System.out.println("Project  Code Create  Primavera end "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			// logger.info(DTTErrorConstants.ERR023 + e);
			throw e;
		}
		return objids;
	}

	List createProjectCodeAssignment(List<ProjectCodeAssignment> projectscode)
			throws IntegrationFault, Exception {
		List<ObjectId> objids = null;
		try {
			System.out.println("Project  Code Assignment create  Primavera  "
					+ java.util.Calendar.getInstance().getTime());

			objids = projectcodeservicePort
					.createProjectCodeAssignments(projectscode);
			System.out.println(objids.size()
					+ " Project code assingment have done:");
			System.out
					.println("Project  Code Assignment create  Primavera end "
							+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			// logger.info(DTTErrorConstants.ERR024 + e);
			throw e;
		}
		return objids;
	}

	boolean updateProjectCodeAssignment(List<ProjectCodeAssignment> projectscode)
			throws Exception {
		boolean objids = false;
		try {
			System.out.println("Project  Code Assignment update  Primavera  "
					+ java.util.Calendar.getInstance().getTime());

			objids = projectcodeservicePort
					.updateProjectCodeAssignments(projectscode);
			System.out.println(" Project code assingment have done:");
			System.out
					.println("Project  Code Assignment update  Primavera End "
							+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			// logger.info(DTTErrorConstants.ERR025 + e);
			throw e;
		}
		return objids;
	}

	List<ProjectCodeAssignment> readProjectCodeAssingment(String projectobjectid)
			throws Exception {
		List<ProjectCodeAssignment> objids = null;
		try {
			/* //logger.info("Calling read Project code assignment service"); */

			/*
			 * List<ProjectCodeAssignment> projectsudf = new
			 * ArrayList<ProjectCodeAssignment>();
			 */
			// projectmasterbean.setContractname("Test");
			projectobjectid = "ProjectObjectId=" + projectobjectid;
			System.out.println("Project  Code Assignment read  Primavera  "
					+ java.util.Calendar.getInstance().getTime());
			objids = projectcodeservicePort.readProjectCodeAssignments(
					projectFields, projectobjectid, null);
			System.out.println(objids.size() + " Project read:");
			System.out.println("Project  Code Assignment read  Primavera end "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			// logger.info(DTTErrorConstants.ERR026 + e);
			throw e;
		}
		return objids;
	}

	List<ProjectCode> readProjectCode() throws Exception {
		List<ProjectCode> objids = null;
		try {
			/* //logger.info("Calling read Project code  service"); */

			// projectmasterbean.setContractname("Test");
			System.out.println("Project  Code  read  Primavera  "
					+ java.util.Calendar.getInstance().getTime());

			objids = servicePort
					.readProjectCodes(projectcodeFields, null, null);
			System.out.println(objids.size() + " Project read:");
			System.out.println("Project  Code  read  Primavera end "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR020 + e);
			throw e;
		}
		return objids;
	}

}
