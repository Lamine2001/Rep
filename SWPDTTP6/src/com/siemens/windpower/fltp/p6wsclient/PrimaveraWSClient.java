package com.siemens.windpower.fltp.p6wsclient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignment;
import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignment;
import com.primavera.ws.p6.resourceassignment.ResourceAssignment;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.fltp.dao.ActivityMasterDAO;
import com.siemens.windpower.fltp.dao.AssignmentMasterDAO;
import com.siemens.windpower.fltp.dao.DAOManagerForDTT;
import com.siemens.windpower.fltp.dao.InconsistantRecordsDAO;
import com.siemens.windpower.fltp.dao.ProjectMasterDAO;
import com.siemens.windpower.fltp.dao.UpdateLastReadDAO;
import com.siemens.windpower.fltp.dao.UpdateProcessedRecords;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.common.ReadProperties;

public class PrimaveraWSClient {
	PrimaveraEPSWSClient epswsclient = null;
	PrimaveraProjectWSClient projectwsclient = null;
	PrimaveraWBSWSClient wbswsclient = null;
	PrimaveraActivityWSClient activitywsclient = null;

	PrimaveraAssignmentWSClient assignmentwsclient = null;
	UpdateLastReadDAO updatedao = null;
	UpdateProcessedRecords updatestatus = null;
	PrimaveraProject proejctws = null;

	PrimaveraProjectUDF udfws = null;
	PrimaveraWBS wbsws = null;

	PrimaveraActivity activityws = null;
	PrimaveraActivityCode activitycodews = null;

	PrimaveraAssignment assignmentws = null;

	PrimaveraErrorHandling errorhandle = null;
	DTTUpdateProcessedRecords processed = null;
	InconsistantRecordsDAO inconsistantrecords = null;
	Logger logger = null;
	ReadProperties read = null;
	Map prop;

	public PrimaveraWSClient() throws Exception {
		epswsclient = new PrimaveraEPSWSClient();
		projectwsclient = new PrimaveraProjectWSClient();
		wbswsclient = new PrimaveraWBSWSClient();
		activitywsclient = new PrimaveraActivityWSClient();

		assignmentwsclient = new PrimaveraAssignmentWSClient();
		updatedao = new UpdateLastReadDAO();
		proejctws = new PrimaveraProject();

		// PrimaveraProjectUDF updateudfws = new PrimaveraProjectUDF();
		udfws = new PrimaveraProjectUDF();
		wbsws = new PrimaveraWBS();

		activityws = new PrimaveraActivity();
		activitycodews = new PrimaveraActivityCode();

		assignmentws = new PrimaveraAssignment();

		errorhandle = new PrimaveraErrorHandling();
		processed = new DTTUpdateProcessedRecords();
		inconsistantrecords = new InconsistantRecordsDAO();

		logger = Logger.getLogger(PrimaveraWSClient.class);
		read = new ReadProperties();
		prop = read.getPropertiesMap();

	}

	public void wsEPSCreate() throws Exception {
		List recordlist = new ArrayList();
		try {
			// Create EPS
			logger.info(" Site EPS Create Started");
			PrimaveraEPS epsws = new PrimaveraEPS();
			List<EPS> epsdetails = new ArrayList<EPS>();

			epsdetails = epswsclient.readAndCreateEPS();
			logger.info(" Site EPS Size" + epsdetails.size());
			List<List<EPS>> epssublist = splitListToSubLists(epsdetails, 500);

			ListIterator<List<EPS>> epssublistitr = epssublist.listIterator();
			while (epssublistitr.hasNext()) {
				List<EPS> epscreate = epssublistitr.next();
				epsws.CreateEPS(epscreate);
				recordlist = epscreate;

				/*
				 * processed.updateProcessedRecords(DTTConstants.EPS,DTTConstants
				 * .PROJECTMASTER , recordlist, "Y", DTTConstants.UPDATE);
				 */
				// logger.info("List Iterator" + epscreate.size());
				// logger.info("Site EPS Created");
			}

		} catch (Exception e) {

			try {
				logger.error(DTTErrorConstants.ERR033, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {

				processed.updateProcessedRecords(DTTConstants.EPS,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to create Site");

			}

		}
	}

	public void wsOppProjectCreate() throws Exception {
		// logger.info(" Opp Project Create Started");
		List recordlist = new ArrayList();
		try {
			List<Project> projectoppdetails = new ArrayList<Project>();
			projectoppdetails = projectwsclient.checkAndCreateOppProject();
			logger.info(" Opp Project Create Size" + projectoppdetails.size());
			List<List<Project>> projectoppsublist = splitListToSubLists(
					projectoppdetails, 500);

			ListIterator<List<Project>> projectoppsublistitr = projectoppsublist
					.listIterator();
			while (projectoppsublistitr.hasNext()) {
				List<Project> projectoppcreate = projectoppsublistitr.next();

				proejctws.opputunityCreateProject(projectoppcreate);
				recordlist = projectoppcreate;

				/*
				 * processed.updateProcessedRecords(DTTConstants.OPPPROJECT,
				 * DTTConstants.PROJECTMASTER, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				// logger.info(" Opp Projects  Created");
				/*
				 * //logger.info("Opp Project List Iterator" +
				 * projectoppcreate.size());
				 */

			}
		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR034, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(DTTConstants.OPPPROJECT,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to create oppotunity project");

			}

		}

	}

	public void wsOppProjectUpdate() throws Exception {
		// logger.info(" Opp Project Update Started");
		List recordlist = new ArrayList();
		try {
			PrimaveraProject proejctupdatews = new PrimaveraProject();
			List<Project> projectoppupdatedetails = new ArrayList<Project>();
			projectoppupdatedetails = projectwsclient
					.checkAndUpdateOppProject();
			/*
			 * //logger.info(" Opp Project update Size" +
			 * projectoppupdatedetails.size());
			 */
			List<List<Project>> projectoppupdatesublist = splitListToSubLists(
					projectoppupdatedetails, 500);

			ListIterator<List<Project>> projectoppupdatesublistitr = projectoppupdatesublist
					.listIterator();
			while (projectoppupdatesublistitr.hasNext()) {
				List<Project> projectoppupdate = projectoppupdatesublistitr
						.next();
				recordlist = projectoppupdate;
				proejctupdatews.opputunityUpdateProject(projectoppupdate);

				processed.updateProcessedRecords(DTTConstants.OPPPROJECTUPDATE,
						DTTConstants.PROJECTMASTER, recordlist, "Y",
						DTTConstants.UPDATE);
				/*
				 * //logger.info("Opp Update Project List Iterator" +
				 * projectoppupdate.size());
				 * //logger.info(" Opp Projects Updated ");
				 */
			}
		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR035, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(DTTConstants.OPPPROJECTUPDATE,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to update oppotunity project");

			}

		}

	}

	public void wsProjectCreate() throws Exception {
		// logger.info("  Projects Creation Started ");
		List recordlist = new ArrayList();
		try {
			List<Project> projectdetails = new ArrayList<Project>();

			projectdetails = projectwsclient.checkAndCreateProject();
			logger.info("  Project Create Size" + projectdetails.size());
			List<List<Project>> projectsublist = splitListToSubLists(
					projectdetails, 500);

			ListIterator<List<Project>> projectsublistitr = projectsublist
					.listIterator();
			while (projectsublistitr.hasNext()) {
				List<Project> projectcreate = projectsublistitr.next();
				recordlist = projectcreate;
				proejctws.maintainanceCreateProject(projectcreate);

				/*
				 * processed.updateProcessedRecords(DTTConstants.CREATEPROJECT,
				 * DTTConstants.PROJECTMASTER, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info(" Project List Iterator" +
				 * projectcreate.size()); //logger.info("  Projects Created ");
				 */
			}

		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR036, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			} finally {
				processed.updateProcessedRecords(DTTConstants.CREATEPROJECT,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to create project");

			}

		}

	}

	public void wsProjectUpdate() throws Exception {
		// logger.info("  Projects Update Started ");
		List recordlist = new ArrayList();

		try {

			List<Project> projectupdatedetails = new ArrayList<Project>();
			projectupdatedetails = projectwsclient.checkAndUpdateProject();
			logger.info("  Project Update Size" + projectupdatedetails.size());
			List<List<Project>> projectupdatesublist = splitListToSubLists(
					projectupdatedetails, 500);

			ListIterator<List<Project>> projectupdatesublistitr = projectupdatesublist
					.listIterator();
			while (projectupdatesublistitr.hasNext()) {
				List<Project> projectupdate = projectupdatesublistitr.next();
				recordlist = projectupdate;
				proejctws.maintainanceUpdateProject(projectupdate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.UPDATEPROJECT,
				 * DTTConstants.PROJECTMASTER, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info(" Project Update List Iterator" +
				 * projectupdate.size());
				 */
				// logger.info("  Projects Update have Done ");

			}
		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR037, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(DTTConstants.UPDATEPROJECT,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to update project");

			}

		}

	}

	public void wsProjectCodeCreate() throws Exception {
		// logger.info("  Projects Code creation started ");
		List recordlist = new ArrayList();
		try {

			PrimaveraProjectCode projectcodews = new PrimaveraProjectCode();
			List<ProjectCodeAssignment> projectcodedetails = new ArrayList<ProjectCodeAssignment>();

			projectcodedetails = projectwsclient.checkAndCreateProjectCodes();
			logger.info(" Create Project Code Size" + projectcodedetails.size());
			List<List<ProjectCodeAssignment>> projectcodesublist = splitListToSubLists(
					projectcodedetails, 500);

			ListIterator<List<ProjectCodeAssignment>> projectcodesublistitr = projectcodesublist
					.listIterator();
			while (projectcodesublistitr.hasNext()) {
				List<ProjectCodeAssignment> projectcodecreate = projectcodesublistitr
						.next();
				recordlist = projectcodecreate;
				projectcodews.createProjectCodeAssignment(projectcodecreate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.CREATEPROJECTCODE
				 * , DTTConstants.PROJECTMASTER, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info("Project Code List Iterator" +
				 * projectcodecreate.size());
				 * //logger.info("  Projects Code created ");
				 */
			}

		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR038, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(
						DTTConstants.CREATEPROJECTCODE,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to Create Project Code ");

			}

		}

	}

	public void wsProjectCodeUpdate() throws Exception {
		// logger.info("  Projects Code Update Started ");
		List recordlist = new ArrayList();
		try {

			PrimaveraProjectCode projectcodeupdatews = new PrimaveraProjectCode();
			List<ProjectCodeAssignment> projectcodeupdatedetails = new ArrayList<ProjectCodeAssignment>();

			projectcodeupdatedetails = projectwsclient
					.checkAndUpdateProjectCodes();
			logger.info(" Update Project Code Size"
					+ projectcodeupdatedetails.size());
			List<List<ProjectCodeAssignment>> projectcodeupdatesublist = splitListToSubLists(
					projectcodeupdatedetails, 500);

			ListIterator<List<ProjectCodeAssignment>> projectcodeupdatesublistitr = projectcodeupdatesublist
					.listIterator();
			while (projectcodeupdatesublistitr.hasNext()) {
				List<ProjectCodeAssignment> projectcodeupdate = projectcodeupdatesublistitr
						.next();
				recordlist = projectcodeupdate;
				projectcodeupdatews
						.updateProjectCodeAssignment(projectcodeupdate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.UPDATEPROJECTCODE
				 * , DTTConstants.PROJECTMASTER, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info("Project Code Update List Iterator" +
				 * projectcodeupdate.size());
				 * //logger.info("  Projects Code Update done ");
				 */
			}
		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR039, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(
						DTTConstants.UPDATEPROJECTCODE,
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to Update Project Code ");

			}
		}
	}

	public void wsProjectUDFCreate() throws Exception {
		// logger.info("  Projects UDF Creation Started ");
		List recordlist = new ArrayList();
		try {

			List<UDFValue> projectudfdetails = new ArrayList<UDFValue>();

			projectudfdetails = projectwsclient.checkAndCreateProjectUDF();
			logger.info(" Create Project UDF Size" + projectudfdetails.size());
			List<List<UDFValue>> projectudfsublist = splitListToSubLists(
					projectudfdetails, 500);

			ListIterator<List<UDFValue>> projectudfsublistitr = projectudfsublist
					.listIterator();
			while (projectudfsublistitr.hasNext()) {
				List<UDFValue> projectudfcreate = projectudfsublistitr.next();
				recordlist = projectudfcreate;
				udfws.createUDFValue(projectudfcreate);
				/*
				 * processed.updateProcessedRecords("CreateProjectUDF",DTTConstants
				 * .PROJECTMASTER,recordlist,"Y",DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info("UDF List Iterator" + projectudfcreate.size());
				 * //logger.info("  Projects UDF Created");
				 */
			}
		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR040, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords("CreateProjectUDF",
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to Create Project UDF ");

			}
		}
	}

	public void wsProjectUDFUpdate() throws Exception {
		// logger.info("  Projects UDF update Started");
		List recordlist = new ArrayList();
		try {

			List<UDFValue> projectudfupdatedetails = new ArrayList<UDFValue>();

			projectudfupdatedetails = projectwsclient
					.checkAndUpdateProjectUDF();
			logger.info(" Update Project UDF Size"
					+ projectudfupdatedetails.size());
			List<List<UDFValue>> projectudfupdatesublist = splitListToSubLists(
					projectudfupdatedetails, 500);

			ListIterator<List<UDFValue>> projectudfupdatesublistitr = projectudfupdatesublist
					.listIterator();
			while (projectudfupdatesublistitr.hasNext()) {
				List<UDFValue> projectudfupdate = projectudfupdatesublistitr
						.next();
				recordlist = projectudfupdate;
				udfws.updateUDFValue(projectudfupdate);
				// processed.updateProcessedRecords("UpdateProjectUDF",DTTConstants.PROJECTMASTER,recordlist,"Y",DTTConstants.UPDATE);
				/*
				 * //logger.info("Update UDF List Iterator" +
				 * projectudfupdate.size());
				 * //logger.info("  Projects UDF updated ");
				 */
			}

		} catch (Exception e) {
			try {
				logger.error("Unable to Create project UDF", e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords("UpdateProjectUDF",
						DTTConstants.PROJECTMASTER, recordlist, DTTConstants.E,
						"Unable to Update Project UDF ");

			}
		}
	}

	public void wsWBSCreate() throws Exception {
		// logger.info("  WBS Creation Started ");
		List recordlist = new ArrayList();
		try {
			List<WBS> wbsdetails = new ArrayList<WBS>();

			wbsdetails = wbswsclient.checkAndCreateWBS();
			logger.info(" WBS Create Size" + wbsdetails.size());
			List<List<WBS>> wbssublist = splitListToSubLists(wbsdetails, 500);

			ListIterator<List<WBS>> wbssublistitr = wbssublist.listIterator();
			while (wbssublistitr.hasNext()) {
				List<WBS> wbscreate = wbssublistitr.next();
				recordlist = wbscreate;
				wbsws.createWBS(wbscreate);
				// logger.info("Create WBS SIZE  " + wbscreate.size());

				/*
				 * processed.updateProcessedRecords(DTTConstants.CREATEWBS,
				 * DTTConstants.ACTIVITY, recordlist, "Y", DTTConstants.UPDATE);
				 */
				// logger.info("WBS List Iterator" + wbscreate.size());
				// logger.info("  WBS Created ");

			}

		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR041, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(DTTConstants.CREATEWBS,
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Create WBS ");

			}
		}

	}

	public void wsWBSUpdate() throws Exception {
		// logger.info("  WBS Update Started ");
		List recordlist = new ArrayList();
		try {
			List<WBS> wbsupdatedetails = new ArrayList<WBS>();

			wbsupdatedetails = wbswsclient.checkAndUpdateWBS();
			logger.info(" WBS Update Size" + wbsupdatedetails.size());
			List<List<WBS>> wbsupdatesublist = splitListToSubLists(
					wbsupdatedetails, 500);

			ListIterator<List<WBS>> wbsupdatesublistitr = wbsupdatesublist
					.listIterator();
			while (wbsupdatesublistitr.hasNext()) {
				List<WBS> wbsupdate = wbsupdatesublistitr.next();
				recordlist = wbsupdate;
				wbsws.updateWBS(wbsupdate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.UPDATEWBS,
				 * DTTConstants.ACTIVITY, recordlist, "Y", DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info("Update WBS List Iterator" + wbsupdate.size());
				 */
				// logger.info("  WBS Update Done ");
			}
		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR042, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords(DTTConstants.UPDATEWBS,
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Update WBS ");

			}
		}

	}

	public void wsWBSUDFCreate() throws Exception {
		// logger.info("  WBS UDF Create Started");
		List recordlist = new ArrayList();
		try {
			List<UDFValue> wbsudfdetails = new ArrayList<UDFValue>();

			wbsudfdetails = wbswsclient.checkAndCreateWBSUDF();
			logger.info("Create WBS UDF Size" + wbsudfdetails.size());
			List<List<UDFValue>> wbsudfsublist = splitListToSubLists(
					wbsudfdetails, 500);

			ListIterator<List<UDFValue>> wbsudfsublistitr = wbsudfsublist
					.listIterator();
			while (wbsudfsublistitr.hasNext()) {
				List<UDFValue> wbsudfcreate = wbsudfsublistitr.next();
				recordlist = wbsudfcreate;
				// logger.info("  WBS UDF SIZE" + wbsudfcreate.size());
				List objIds = udfws.createUDFValue(wbsudfcreate);
				// logger.info("  WBS UDF created size" + objIds.size());
				// processed.updateProcessedRecords("CreateWBSUDF",DTTConstants.ACTIVITY,recordlist,"Y",DTTConstants.UPDATE);
				// logger.info("WBS List Iterator" + wbsudfcreate.size());
				// logger.info("  WBS UDF Created ");
			}

		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR043, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				processed.updateProcessedRecords("CreateWBSUDF",
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Create WBS UDF ");

			}
		}
	}

	public void wsWBSUDFUpdate() throws Exception {
		// logger.info("  WBS UDF Update Started");
		List recordlist = new ArrayList();
		try {

			List<UDFValue> wbsudfupdatedetails = new ArrayList<UDFValue>();

			wbsudfupdatedetails = wbswsclient.checkAndUpdateWBSUDF();
			logger.info("Update WBS UDF Size" + wbsudfupdatedetails.size());
			List<List<UDFValue>> wbsudfupdatesublist = splitListToSubLists(
					wbsudfupdatedetails, 500);

			ListIterator<List<UDFValue>> wbsudfupdatesublistitr = wbsudfupdatesublist
					.listIterator();
			while (wbsudfupdatesublistitr.hasNext()) {
				List<UDFValue> wbsudfupdate = wbsudfupdatesublistitr.next();
				recordlist = wbsudfupdate;
				udfws.updateUDFValue(wbsudfupdate);
				// processed.updateProcessedRecords("UpdateWBSUDF",DTTConstants.ACTIVITY,recordlist,"Y",DTTConstants.UPDATE);
				/*
				 * //logger.info("WBS UDF Update List Iterator" +
				 * wbsudfupdate.size()); //logger.info("  WBS UDF Updates ");
				 */}

		} catch (Exception e) {
			try {
				logger.error(DTTErrorConstants.ERR044, e);
			} catch (Exception ex) {

				e.printStackTrace();
				throw e;
			}

			finally {
				// processed.updateProcessedRecords("UpdateWBSUDF",DTTConstants.ACTIVITY,recordlist,DTTConstants.E,"Unable to Update WBS UDF ");

			}
		}
	}

	public void wsActivityCreate() throws Exception {
		// logger.info("  Activity Creation Started ");
		List recordlist = new ArrayList();
		try {

			List<Activity> activitydetails = new ArrayList<Activity>();
			activitydetails = activitywsclient.checkAndCreateActivity();

			List<List<Activity>> activitysublist = splitListToSubLists(
					activitydetails, 500);
			logger.info("Activity Creation Size" + activitydetails.size());
			ListIterator<List<Activity>> activitysublistitr = activitysublist
					.listIterator();
			while (activitysublistitr.hasNext()) {
				List<Activity> activitycreate = activitysublistitr.next();
				recordlist = activitycreate;
				activityws.createActivities(activitycreate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.CREATE_ACTIVITY,
				 * DTTConstants.ACTIVITY, recordlist, "Y", DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info(" Activity List Iterator" +
				 * activitycreate.size());
				 */
				// logger.info("Activity Creation done" );
			}
		} catch (Exception e) {
			try {
				logger.error(e);
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				processed.updateProcessedRecords(DTTConstants.CREATE_ACTIVITY,
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Create Activity ");

			}

		}

	}

	public void wsActivityUpdate() throws Exception {
		// logger.info("  Activity Update Started ");
		List recordlist = new ArrayList();
		try {
			List<Activity> activityupdatedetails = new ArrayList<Activity>();

			activityupdatedetails = activitywsclient.checkAndUpdateActivity();

			List<List<Activity>> activityupdatesublist = splitListToSubLists(
					activityupdatedetails, 500);

			ListIterator<List<Activity>> activityupdatesublistitr = activityupdatesublist
					.listIterator();
			while (activityupdatesublistitr.hasNext()) {
				List<Activity> activityupdate = activityupdatesublistitr.next();
				recordlist = activityupdate;
				activityws.updateActivities(activityupdate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.UPDATE_ACTIVITY,
				 * DTTConstants.ACTIVITY, recordlist, "Y", DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info(" Activity Update List Iterator" +
				 * activityupdate.size());
				 */

			}
		} catch (Exception e) {
			try {
				logger.error(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				processed.updateProcessedRecords(DTTConstants.UPDATE_ACTIVITY,
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Update Activity ");

			}

		}

	}

	public void wsActivityCodeCreate() throws Exception {
		// logger.info("  Activity Code Started ");
		List recordlist = new ArrayList();
		try {
			List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();

			activitycodedetails = activitywsclient
					.checkAndCreateActivityCodes();
			logger.info("Create Activity CODE Size"
					+ activitycodedetails.size());
			List<List<ActivityCodeAssignment>> activitycodesublist = splitListToSubLists(
					activitycodedetails, 500);

			ListIterator<List<ActivityCodeAssignment>> activitycodesublistitr = activitycodesublist
					.listIterator();
			while (activitycodesublistitr.hasNext()) {
				List<ActivityCodeAssignment> activitycodecreate = activitycodesublistitr
						.next();
				recordlist = activitycodecreate;
				activitycodews.createActivityCodeAssignment(activitycodecreate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.CREATE_ACTIVITYCODE
				 * , DTTConstants.ACTIVITY, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info("Activity Code List Iterator" +
				 * activitycodecreate.size());
				 * //logger.info("Create Activity Code Done" );
				 */
			}

		} catch (Exception e) {
			try {
				logger.error(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				processed.updateProcessedRecords(
						DTTConstants.CREATE_ACTIVITYCODE,
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Create Activity Code");

			}

		}

	}

	public void wsActivityCodeUpdate() throws Exception {
		List recordlist = new ArrayList();
		try {

			List<ActivityCodeAssignment> activitycodeupdatedetails = new ArrayList<ActivityCodeAssignment>();

			activitycodeupdatedetails = activitywsclient
					.checkAndUpdateActivityCodes();
			logger.info("Update Activity Code Size"
					+ activitycodeupdatedetails.size());
			List<List<ActivityCodeAssignment>> activitycodeupdatesublist = splitListToSubLists(
					activitycodeupdatedetails, 500);

			ListIterator<List<ActivityCodeAssignment>> activitycodeupdatesublistitr = activitycodeupdatesublist
					.listIterator();
			while (activitycodeupdatesublistitr.hasNext()) {
				List<ActivityCodeAssignment> activitycodeupdate = activitycodeupdatesublistitr
						.next();
				recordlist = activitycodeupdate;
				activitycodews.updateActivityCodeAssignment(activitycodeupdate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.UPDATE_ACTIVITYCODE
				 * , DTTConstants.ACTIVITY, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info("Activity Code Update List Iterator" +
				 * activitycodeupdate.size());
				 */

			}

		} catch (Exception e) {
			try {
				logger.error(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				processed.updateProcessedRecords(
						DTTConstants.UPDATE_ACTIVITYCODE,
						DTTConstants.ACTIVITY, recordlist, DTTConstants.E,
						"Unable to Update Activity Code");

			}

		}

	}

	public void wsActivityUDFCreate() throws Exception {
		List recordlist = new ArrayList();
		try {
			// logger.info("Create Activity UDF Started" );
			List<UDFValue> activityudfdetails = new ArrayList<UDFValue>();

			activityudfdetails = activitywsclient.checkAndCreateActivityUDF();
			logger.info("Create Activity UDF Size" + activityudfdetails.size());
			List<List<UDFValue>> activitytudfsublist = splitListToSubLists(
					activityudfdetails, 500);

			ListIterator<List<UDFValue>> activitytudfsublistitr = activitytudfsublist
					.listIterator();
			while (activitytudfsublistitr.hasNext()) {
				List<UDFValue> activityudfcreate = activitytudfsublistitr
						.next();
				recordlist = activityudfcreate;
				udfws.createUDFValue(activityudfcreate);
				processed.updateProcessedRecords(
						DTTConstants.CREATE_ACTIVITYUDF, DTTConstants.ACTIVITY,
						recordlist, "Y", DTTConstants.UPDATE);
				/*
				 * //logger.info("Activity UDF List Iterator" +
				 * activityudfcreate.size());
				 * //logger.info("Create Activity UDF Done" );
				 */}
		} catch (Exception e) {
			try {
				logger.error(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				processed.updateProcessedRecords(
						DTTConstants.CREATE_ACTIVITYUDF, DTTConstants.ACTIVITY,
						recordlist, DTTConstants.E,
						"Unable to Create Activity UDF");

			}

		}

	}

	public void wsActivityUDFUpdate() throws Exception {
		List recordlist = new ArrayList();
		try {
			List<UDFValue> activityudfupdatedetails = new ArrayList<UDFValue>();

			activityudfupdatedetails = activitywsclient
					.checkAndUpdateActivityUDF();
			logger.info("Update Activity UDF Size"
					+ activityudfupdatedetails.size());
			List<List<UDFValue>> activitytudfupdatesublist = splitListToSubLists(
					activityudfupdatedetails, 500);

			ListIterator<List<UDFValue>> activitytudfupdatesublistitr = activitytudfupdatesublist
					.listIterator();
			while (activitytudfupdatesublistitr.hasNext()) {
				List<UDFValue> activityudfupdate = activitytudfupdatesublistitr
						.next();
				recordlist = activityudfupdate;
				udfws.updateUDFValue(activityudfupdate);
				processed.updateProcessedRecords(
						DTTConstants.UPDATE_ACTIVITYUDF, DTTConstants.ACTIVITY,
						recordlist, "Y", DTTConstants.UPDATE);
				/*
				 * //logger.info("Activity UDF Update List Iterator" +
				 * activityudfupdate.size());
				 */

			}

		} catch (Exception e) {
			try {
				logger.error(e);
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				processed.updateProcessedRecords(
						DTTConstants.UPDATE_ACTIVITYUDF, DTTConstants.ACTIVITY,
						recordlist, DTTConstants.E,
						"Unable to Update Activity UDF");

			}

		}

	}

	public void wsAssignmentCreate() throws Exception {
		List recordlist = new ArrayList();
		try {

			List<ResourceAssignment> assignmentdetails = new ArrayList<ResourceAssignment>();
			assignmentdetails = assignmentwsclient.checkAndCreateAssignment();
			logger.info("Create Assignment  Size" + assignmentdetails.size());
			List<List<ResourceAssignment>> assignmentsublist = splitListToSubLists(
					assignmentdetails, 500);

			ListIterator<List<ResourceAssignment>> assignmentsublistitr = assignmentsublist
					.listIterator();
			while (assignmentsublistitr.hasNext()) {
				List<ResourceAssignment> assignmentcreate = assignmentsublistitr
						.next();
				recordlist = assignmentcreate;
				assignmentws.createRole(assignmentcreate);
				/*
				 * processed.updateProcessedRecords(DTTConstants.CREATE_ASSIGNMENT
				 * , DTTConstants.ASSIGNMENT, recordlist, "Y",
				 * DTTConstants.UPDATE);
				 */
				/*
				 * //logger.info(" Assignment List Iterator" +
				 * assignmentcreate.size());
				 */

			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			processed.updateProcessedRecords(DTTConstants.CREATE_ASSIGNMENT,
					DTTConstants.ASSIGNMENT, recordlist, DTTConstants.E,
					"Unable to Create Assignment");

		}

	}

	public void wsAssignmentUpdate() throws Exception {
		List recordlist = new ArrayList();
		try {
			List<ResourceAssignment> assignmentupdatedetails = new ArrayList<ResourceAssignment>();
			assignmentupdatedetails = assignmentwsclient
					.checkAndUpdateAssignment();

			List<List<ResourceAssignment>> assignmentupdatesublist = splitListToSubLists(
					assignmentupdatedetails, 500);

			ListIterator<List<ResourceAssignment>> assignmentupdatesublistitr = assignmentupdatesublist
					.listIterator();
			while (assignmentupdatesublistitr.hasNext()) {
				List<ResourceAssignment> assignmentupdate = assignmentupdatesublistitr
						.next();
				recordlist = assignmentupdate;
				assignmentws.updateRole(assignmentupdate);
				processed.updateProcessedRecords(
						DTTConstants.UPDATE_ASSIGNMENT,
						DTTConstants.ASSIGNMENT, recordlist, "Y",
						DTTConstants.UPDATE);
				/*
				 * //logger.info(" Assignment Update List Iterator" +
				 * assignmentupdate.size());
				 */

			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			processed.updateProcessedRecords(DTTConstants.UPDATE_ASSIGNMENT,
					DTTConstants.ASSIGNMENT, recordlist, DTTConstants.E,
					"Unable to Update Assignment");

		}

	}

	public void primaveraEPSwsclientcode() throws Exception {

		try {
			PrimaveraWSClient wsclient = new PrimaveraWSClient();
			ActivityMasterDAO getwbscount = new ActivityMasterDAO();
			ProjectMasterDAO getprojectcount = new ProjectMasterDAO();
			AssignmentMasterDAO getassignmentcount = new AssignmentMasterDAO();
			int projectcount = 0;
			int projecti = 0;
			int wbscount = 0;
			int wbsi = 0;
			int assignmentcount = 0;
			int assignmenti = 0;
			double dowhilewbsloopcount = 0;
			double dowhileactivityloopcount = 0;
			double dowhileprojectloopcount = 0;
			double dowhileassignmentloopcount = 0;
			int activitycount = 0;
			int activityi = 0;
			projectcount = getprojectcount.getProjectMasterCount();
			// logger.info("Total Projects to be processed"+projectcount);
			wbscount = getwbscount.getWBSMasterCount();
			// logger.info("Total WBS to be processed"+wbscount);

			activitycount = getwbscount.getActivityMasterCount();
			// logger.info("Total Activities to be processed"+activitycount);
			assignmentcount = getassignmentcount.getAssignmentMasterCount();
			if (projectcount != 0) {
				dowhileprojectloopcount = projectcount
						/ DTTConstants.MAXFETCHROWS;

				projecti = (int) round(dowhileprojectloopcount);

			}
			if (wbscount != 0) {
				dowhilewbsloopcount = wbscount / DTTConstants.MAXFETCHROWS;

				wbsi = (int) round(dowhilewbsloopcount);

			}
			if (activitycount != 0) {
				// logger.info("Activity activitycount"+activitycount);
				dowhileactivityloopcount = activitycount
						/ DTTConstants.MAXFETCHROWS;
				// logger.info("Activity dowhileactivityloopcount"+dowhileactivityloopcount);
				activityi = (int) round(dowhileactivityloopcount);
				// logger.info("Activity activityi"+activityi);

			}
			if (assignmentcount != 0) {
				logger.info("Assignment activitycount"
						+ dowhileassignmentloopcount);
				dowhileassignmentloopcount = assignmentcount
						/ DTTConstants.ASSIGNMENTMAXFETCHROWS;
				logger.info("Assignment dowhileassignmentloopcount"
						+ dowhileassignmentloopcount);
				assignmenti = (int) round(dowhileassignmentloopcount);
				logger.info("Assignment assignmenti" + assignmenti);

			}
			String isloadsites = prop.get("IS_LOAD_SITES").toString();
			String isloadprojects = prop.get("IS_LOAD_PROJECTS").toString();
			String isloadwbs = prop.get("IS_LOAD_WBS").toString();

			String isloadactivites = prop.get("IS_LOAD_ACTVITIES").toString();
			String isloadassignments = prop.get("IS_LOAD_ASSIGNMENTS")
					.toString();
			if (isloadsites.equalsIgnoreCase("Y")) {
				logger.info("Site Creation"
						+ java.util.Calendar.getInstance().getTime());

				wsclient.wsEPSCreate();
				logger.info("Site end"
						+ java.util.Calendar.getInstance().getTime());
			}
			if (isloadprojects.equalsIgnoreCase("Y")) {
				do {
					logger.info("OPP Project Create"
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsOppProjectCreate();
					logger.info("Opp Project end"
							+ java.util.Calendar.getInstance().getTime());
					// wsclient.wsOppProjectUpdate();
					logger.info("Project Create"
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsProjectCreate();
					logger.info("Project End "
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsProjectUpdate();
					logger.info("Project Code Create"
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsProjectCodeCreate();
					logger.info("Project Code end"
							+ java.util.Calendar.getInstance().getTime());
					// wsclient.wsProjectCodeUpdate();
					logger.info("Project UDF Create"
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsProjectUDFCreate();
					logger.info("Project UDF end"
							+ java.util.Calendar.getInstance().getTime());
					logger.info("Project UDF Update Create"
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsProjectUDFUpdate();
					logger.info("Project UDF Update End"
							+ java.util.Calendar.getInstance().getTime());
					logger.info("Project Iteration" + projecti);

					// logger.info("");
					projecti--;
				} while (projecti > 0);
				updatedao.updateLastRead(DTTConstants.PROJECTMASTER);
			}
			if (isloadwbs.equalsIgnoreCase("Y")) {
				do {
					logger.info("WBS Create "
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsWBSCreate();
					logger.info("WBS end "
							+ java.util.Calendar.getInstance().getTime());
					logger.info("WBS Update "
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsWBSUpdate();
					logger.info("WBS Update End"
							+ java.util.Calendar.getInstance().getTime());
					logger.info("WBS UDF Create "
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsWBSUDFCreate();
					logger.info("WBS UDF END "
							+ java.util.Calendar.getInstance().getTime());
					logger.info("WBS UDF Update Create "
							+ java.util.Calendar.getInstance().getTime());
					wsclient.wsWBSUDFUpdate();
					logger.info("WBS UDF Update END "
							+ java.util.Calendar.getInstance().getTime());

					

					// updatestatus.wbsUpdateAfterProcess();
					
					// inconsistantrecords.inconsistantRecords();

					// activityi=-1;
					logger.info("Activity Create"
							+ java.util.Calendar.getInstance().getTime());

					wsActivityCreate();
					logger.info("Activity end"
							+ java.util.Calendar.getInstance().getTime());
					// wsActivityUpdate();
					logger.info("Activity Code Create "
							+ java.util.Calendar.getInstance().getTime());
					wsActivityCodeCreate();
					// wsActivityCodeUpdate();
					logger.info("Activity Code end"
							+ java.util.Calendar.getInstance().getTime());
					logger.info("Activity UDF Create "
							+ java.util.Calendar.getInstance().getTime());
					wsActivityUDFCreate();

					logger.info("Activity UDF  end"
							+ java.util.Calendar.getInstance().getTime());
					logger.info("Activity UDF Update "
							+ java.util.Calendar.getInstance().getTime());
					wsActivityUDFUpdate();
					logger.info("Activity UDF  end"
							+ java.util.Calendar.getInstance().getTime());
					//logger.info("Activity Iteration" + activityi);
					logger.info("WBS Iteration" + wbsi);
					wbsi--;

				} while (wbsi > 0);
				updatedao.updateLastRead(DTTConstants.ACTIVITY);
			}
			 
			if (isloadassignments.equalsIgnoreCase("Y")) {
				//
				do {
					logger.info("Assignment Create "
							+ java.util.Calendar.getInstance().getTime());

					wsAssignmentCreate();
					logger.info("Assignment assignmenti" + assignmenti);

					logger.info("Assignment END "
							+ java.util.Calendar.getInstance().getTime());

					// wsAssignmentUpdate();
					assignmenti--;
				} while (assignmenti > 0);
				updatedao.updateLastRead(DTTConstants.ASSIGNMENT);

				// activitywsclient.checkAndGetActivityOutboundData();
			}
		}

		catch (Exception e) {

			e.printStackTrace();
			throw e;
		} finally {

		}

	}

	static int round(double d) {
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - (double) i;
		if (result < 0.5) {
			return d < 0 ? -i : i + 1;
		} else {
			return d < 0 ? -(i + 1) : i + 1;
		}
	}

	public static <T> List<List<T>> splitListToSubLists(List<T> parentList,
			int subListSize) {
		List<List<T>> subLists = new ArrayList<List<T>>();
		if (subListSize > parentList.size()) {
			subLists.add(parentList);
		} else {
			int remainingElements = parentList.size();
			int startIndex = 0;
			int endIndex = subListSize;
			do {
				List<T> subList = parentList.subList(startIndex, endIndex);
				subLists.add(subList);
				startIndex = endIndex;
				if (remainingElements - subListSize >= subListSize) {
					endIndex = startIndex + subListSize;
				} else {
					endIndex = startIndex + remainingElements - subList.size();
				}
				remainingElements -= subList.size();
			} while (remainingElements > 0);

		}
		return subLists;
	}

	public static void main(String[] args) throws Exception {

		try {
			PrimaveraWSClient primaverawsclient = new PrimaveraWSClient();
			primaverawsclient.primaveraEPSwsclientcode();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
