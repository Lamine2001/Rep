package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.project.ProjectFieldType;
import com.primavera.ws.p6.project.ProjectPortType;
import com.primavera.ws.p6.project.ProjectService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraProject {

	private static final String PROJECT_SERVICE = DTTConstants.PROJECT_SERVICE;
	ProjectMasterBean projectmasterbean = null;
	Logger logger = null;
	static ProjectPortType servicePort = null;
	static List<ProjectFieldType> fields = null;

	PrimaveraProject() throws Exception {
		if (servicePort == null) {
			initlialiseProject();
		}

	}

	public void initlialiseProject() throws Exception {
		logger = Logger.getLogger(PrimaveraWSClient.class);
		projectmasterbean = new ProjectMasterBean();
		servicePort = createProjectServicePort();
		fields = new ArrayList<ProjectFieldType>();
		fields.add(ProjectFieldType.OBJECT_ID);
		fields.add(ProjectFieldType.ID);
		fields.add(ProjectFieldType.OBS_NAME);
		fields.add(ProjectFieldType.PARENT_EPS_OBJECT_ID);

	}

	private ProjectPortType createProjectServicePort() throws Exception {

		try {
			String url = PrimaveraUserValidation
					.makeHttpURLString(PROJECT_SERVICE);
			URL wsdlURL = new URL(url);
			ProjectService service = new ProjectService(wsdlURL);
			servicePort = service.getProjectPort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR017 + e);
			e.printStackTrace();
			throw e;
		}
		return servicePort;
	}

	private List<Integer> createProject(List<Project> projects)
			throws Exception {
		List<Integer> objids = null;
		try {

			System.out.println("Project  create  Primavera  "
					+ java.util.Calendar.getInstance().getTime());

			objids = servicePort.createProjects(projects);
			System.out.println(objids.size() + " Project created:");
			System.out.println("Project  create  Primavera  End"
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			// logger.info(DTTErrorConstants.ERR019 + e);
			throw e;
		}
		return objids;
	}

	private List<Project> readProject(String objectid) throws Exception {
		List<Project> projects = new ArrayList<Project>();
		try {
			// System.out.println("Project  Read  Primavera  "+java.util.Calendar.getInstance().getTime());

			// //logger.info("servicePort"+servicePort);
			// Load project with specific Id
			String ParentObjectId = "ParentEPSObjectId=" + objectid;
			projects = servicePort.readProjects(fields, ParentObjectId, null);
			// System.out.println("Project  Read  Primavera End "+java.util.Calendar.getInstance().getTime());

		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR018 + e);
			throw e;
		}

		return projects;
	}

	private void updateProject(List<Project> projects) throws Exception {

		boolean objids = servicePort.updateProjects(projects);
		System.out.println(" Project updated:");

		// return objids.get(0).intValue();
	}

	List<Project> opputunityCreateProject(List<Project> projectdata)
			throws Exception {
		createProject(projectdata);
		return projectdata;
	}

	List<Project> opputunityReadProject(String siteid) throws Exception {
		List<Project> projectdata = new ArrayList<Project>();
		projectdata = readProject(siteid);
		return projectdata;
	}

	void opputunityUpdateProject(List<Project> projectdata) throws Exception {
		updateProject(projectdata);
	}

	void maintainanceReadProject() throws Exception {
		// readProject();
	}

	List<Project> maintainanceCreateProject(List<Project> projectdata)
			throws Exception {

		createProject(projectdata);
		return projectdata;

	}

	void maintainanceUpdateProject(List<Project> projectdata) throws Exception {
		updateProject(projectdata);
	}
}