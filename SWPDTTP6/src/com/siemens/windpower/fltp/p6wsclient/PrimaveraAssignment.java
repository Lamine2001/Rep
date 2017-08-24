package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.resourceassignment.ObjectFactory;
import com.primavera.ws.p6.resourceassignment.ResourceAssignment;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentFieldType;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentPortType;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentService;
import com.primavera.ws.p6.role.Role;
import com.primavera.ws.p6.role.RoleFieldType;
import com.primavera.ws.p6.role.RolePortType;
import com.primavera.ws.p6.role.RoleService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;

public class PrimaveraAssignment {

	private static final String RESOURCE_SERVICE = DTTConstants.RESOURCE_SERVICE;
	private static final String ROLE_SERVICE = DTTConstants.ROLE_SERVICE;

	ProjectMasterBean projectmasterbean = null;
	static ResourceAssignmentPortType servicePort = null;
	static RolePortType roleserviceport = null;
	static ResourceAssignmentService service = null;
	static RoleService roleservice = null;
	Logger logger = Logger.getLogger(PrimaveraAssignment.class);
	static List<ResourceAssignmentFieldType> fields = null;
	static List<RoleFieldType> rolefields = null;

	PrimaveraAssignment() throws Exception {
		if (service == null || roleservice == null) {
			initlialiseAssignment();
		}

	}

	public void initlialiseAssignment() throws Exception {

		service = createAssignmentResourceService();
		roleservice = createAssignmentRoleService();
		fields = new ArrayList<ResourceAssignmentFieldType>();
		fields.add(ResourceAssignmentFieldType.ACTIVITY_OBJECT_ID);
		fields.add(ResourceAssignmentFieldType.ROLE_OBJECT_ID);
		fields.add(ResourceAssignmentFieldType.ROLE_NAME);
		fields.add(ResourceAssignmentFieldType.ROLE_ID);
		fields.add(ResourceAssignmentFieldType.PLANNED_UNITS);
		fields.add(ResourceAssignmentFieldType.REMAINING_UNITS);

		rolefields = new ArrayList<RoleFieldType>();
		rolefields.add(RoleFieldType.ID);
		rolefields.add(RoleFieldType.OBJECT_ID);
		rolefields.add(RoleFieldType.PARENT_OBJECT_ID);
		rolefields.add(RoleFieldType.NAME);

	}

	private ResourceAssignmentService createAssignmentResourceService()
			throws Exception {
		String url = PrimaveraUserValidation
				.makeHttpURLString(RESOURCE_SERVICE);
		URL wsdlURL = new URL(url);
		service = new ResourceAssignmentService(wsdlURL);

		return service;
	}

	private RoleService createAssignmentRoleService() throws Exception {
		String url = PrimaveraUserValidation.makeHttpURLString(ROLE_SERVICE);
		URL wsdlURL = new URL(url);
		roleservice = new RoleService(wsdlURL);

		return roleservice;
	}

	List<Integer> createRole(List<ResourceAssignment> roles) throws Exception {
		// logger.info("Assignment Create  Role Primavera "+java.util.Calendar.getInstance().getTime());
		if (service == null) {
			initlialiseAssignment();
		}

		servicePort = service.getResourceAssignmentPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		List<Integer> objIds = servicePort.createResourceAssignments(roles);
		// logger.info("Assignment Create  Role Primavera End "+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	boolean updateRole(List<ResourceAssignment> roles) throws Exception {
		// logger.info("Assignment Update  Role Primavera  "+java.util.Calendar.getInstance().getTime());
		if (service == null) {
			initlialiseAssignment();
		}
		servicePort = service.getResourceAssignmentPort();
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		boolean objIds = servicePort.updateResourceAssignments(roles);
		// logger.info("Assignment Update  Role Primavera End "+java.util.Calendar.getInstance().getTime());

		return objIds;
	}

	List<ResourceAssignment> readRoleAssign(String activityObjectId)
			throws Exception {
		if (service == null) {
			initlialiseAssignment();
		}

		logger.info(service);
		servicePort = service.getResourceAssignmentPort();
		logger.info(servicePort);
		Client client = ClientProxy.getClient(servicePort);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		logger.info(client);
		// logger.info("Assignment read role  Primavera  "+java.util.Calendar.getInstance().getTime());

		activityObjectId = "ActivityObjectId=" + activityObjectId;

		logger.info(activityObjectId);

		List<ResourceAssignment> objIds = servicePort.readResourceAssignments(
				fields, activityObjectId, null);
		// logger.info("Assignment read role  Primavera End "+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	List<Role> readRole() throws Exception {
		if (roleservice == null) {
			initlialiseAssignment();
		}

		// logger.info("Assignment read role  Primavera  "+java.util.Calendar.getInstance().getTime());

		roleserviceport = roleservice.getRolePort();
		Client client = ClientProxy.getClient(roleserviceport);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		// projectObjectId ="projectObjectId="+projectObjectId;

		List<Role> objIds = roleserviceport.readRoles(rolefields, null, null);

		// logger.info("Assignment read role  Primavera END "+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	List<Role> readParentRole(String parentobjectid) throws Exception {
		if (roleservice == null) {
			initlialiseAssignment();
		}
		// logger.info("Assignment read parent role  Primavera  "+java.util.Calendar.getInstance().getTime());
		roleserviceport = roleservice.getRolePort();
		Client client = ClientProxy.getClient(roleserviceport);
		PrimaveraUserValidation.setCookieOrUserTokenData(client);
		parentobjectid = "ParentObjectId=" + parentobjectid;

		List<Role> objIds = roleserviceport.readRoles(rolefields,
				parentobjectid, null);
		// logger.info("Assignment read parent role  Primavera END "+java.util.Calendar.getInstance().getTime());
		return objIds;
	}

	public JAXBElement<Integer> constructRoleObject(int roleid) {
		ObjectFactory objectfactory = new ObjectFactory();
		return objectfactory.createResourceAssignmentRoleObjectId(roleid);
	}

}
