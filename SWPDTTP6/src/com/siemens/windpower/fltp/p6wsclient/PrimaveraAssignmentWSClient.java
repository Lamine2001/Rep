package com.siemens.windpower.fltp.p6wsclient;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.projectcode.ProjectCode;
import com.primavera.ws.p6.resourceassignment.ObjectFactory;
import com.primavera.ws.p6.resourceassignment.ResourceAssignment;
import com.primavera.ws.p6.role.Role;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.fltp.beans.ActivityMasterBean;
import com.siemens.windpower.fltp.beans.AssignmentMasterBean;
import com.siemens.windpower.fltp.dao.ActivityMasterDAO;
import com.siemens.windpower.fltp.dao.AssignmentMasterDAO;
import com.siemens.windpower.fltp.dao.ProjectMasterDAO;
import com.siemens.windpower.fltp.dao.UpdateProcessRecordsDAO;

public class PrimaveraAssignmentWSClient {

	AssignmentMasterBean assignmentmasterbean = null;
	List<Map<String, Object>> assignmentmasterdata = null;
	AssignmentMasterDAO assignmentmasterDAO = null;
	Logger logger = null;

	PrimaveraAssignmentWSClient() throws SQLException {
		logger = Logger.getLogger(PrimaveraAssignmentWSClient.class);

	}

	@SuppressWarnings("unchecked")
	public List<ResourceAssignment> checkAndCreateAssignment() throws Exception {
		List<ResourceAssignment> createroledetails = new ArrayList<ResourceAssignment>();

		try {

			assignmentmasterDAO = new AssignmentMasterDAO();
			assignmentmasterbean = assignmentmasterDAO.getAssignmentMasterDAO();

			assignmentmasterdata = new ArrayList<Map<String, Object>>();
			assignmentmasterdata = assignmentmasterbean
					.getAssignmentmasterdata();
			PrimaveraEPS epsclient = new PrimaveraEPS();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			List rolecheck = new ArrayList();

			PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
			List<Role> readroledetails = new ArrayList<Role>();

			List<ResourceAssignment> readresourcedetails = new ArrayList<ResourceAssignment>();

			readroledetails = assignmenttype.readRole();

			List<String> recordlist = new ArrayList<String>();
			List<Role> readparentroledetails = new ArrayList<Role>();

			/*
			 * String roleidtemp="SWP-3.6 AT"; String rolenametemp=null;
			 * logger.info(roleidtemp);
			 * roleidtemp=roleidtemp.substring(4,roleidtemp.length()); String
			 * roleidsubtemp=roleidtemp.substring(0,3); String
			 * roleidtemp1=roleidtemp.substring(4,roleidtemp.length());
			 * logger.info("Role roleidsubtemp"+roleidsubtemp);
			 * logger.info("Role Substring"+roleidtemp);
			 * 
			 * 
			 * 
			 * logger.info("Role final"+roleidtemp1);
			 */
			ListIterator<Role> rolereadtem = readroledetails.listIterator();
			while (rolereadtem.hasNext()) {

				Role rolereadelement = rolereadtem.next();

				System.out.println("Role Name " + rolereadelement.getName()
						+ " role ID " + rolereadelement.getId()
						+ " role Object Id " + rolereadelement.getObjectId()
						+ " role parent object Id "
						+ rolereadelement.getParentObjectId().getValue());

				/*
				 * logger.info("Role Name " + rolereadelement .getName() +
				 * " role ID " + rolereadelement .getId() + " role Object Id " +
				 * rolereadelement .getObjectId()+" role parent object Id " +
				 * rolereadelement.getParentObjectId().getValue());
				 */
				/*
				 * if(rolereadelement.getId().contains(roleidsubtemp)){ String
				 * rolepreviousobject =rolereadelement.getObjectId().toString();
				 * logger.info("rolepreviousobject"+rolepreviousobject);
				 * readparentroledetails
				 * =assignmenttype.readParentRole(rolepreviousobject);
				 * ListIterator<Role> roleparentreadtem = readparentroledetails
				 * .listIterator(); while (roleparentreadtem.hasNext()) {
				 * 
				 * Role roleparentreadelement = roleparentreadtem.next();
				 * if(roleparentreadelement.getId().contains(roleidtemp1)){
				 * String
				 * roleparentobject=roleparentreadelement.getParentObjectId
				 * ().getValue().toString();
				 * logger.info("roleparentobject"+roleparentobject);
				 * if(roleparentobject.equalsIgnoreCase(rolepreviousobject)){
				 * logger
				 * .info("roleparentreadelement.getObjectId()"+roleparentreadelement
				 * .getObjectId());
				 * logger.info("roleparentreadelement.getName()"
				 * +roleparentreadelement.getName()); } } } }
				 */

			}

			// logger.info("activitymasterdata"+assignmentmasterdata.size());

			for (Map<String, Object> map : assignmentmasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String labordttid = null;

				String turbinename = null;
				String hqtechid = null;
				String roleid = null;
				String rolename = null;
				Double plannedhours = null;
				Integer rowid = null;
				String roleidfinal = null;
				String roleidsub = null;
				String roleidsub1 = null;
				String worktypestring = null;
				String activityname = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				Date contractstartdate = null;
				Date contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;
				Activity activitycreate = new Activity();

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					int wbsobjectid = 0;
					Integer roleobjectid = 0;
					Double nonlaborunits = 0.0;
					JAXBElement<Integer> wbsobject = null;

					JAXBElement<Double> plannedunitstimeid = null;
					ResourceAssignment rolecreate = new ResourceAssignment();
					JAXBElement<Integer> wbsjaxbElement = null;
					JAXBElement<Integer> activityobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;

					String activityobjectid = null;
					String activityobjectname = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean activity = false;
					boolean wbs = false;
					boolean role = false;
					boolean demandactivity = false;
					boolean resourcerole = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains("Country_Id")) {
						countryid = (String) value;

						// logger.info(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// logger.info(locationshore);
					}
					if (key.contains(DTTConstants.ACTIVITYNAME)) {
						activityname = (String) value;

						// logger.info(projectnumber);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// logger.info(projectnumber);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// logger.info(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == 1) {
							worktypestring = "Prev.Main";

						}
						if (worktype == 2) {
							worktypestring = "Retrofit";

						}
						if (worktype == 3) {
							worktypestring = "Mods&up";

						}
						if (worktype == 4) {
							worktypestring = "Repairs";

						}
						if (worktype == 5) {
							worktypestring = "Service";

						}
						if (worktype == 6) {
							worktypestring = "Service extension";

						}
						if (worktype == 7) {
							worktypestring = "Mods&Up";

						}

						/* logger.info(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* logger.info(sapparentobjectid); */
					}

					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains("DTT_Activity_Object_ID")) {
						dttactivityid = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains("Turbine_Name")) {
						turbinename = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains("HQ_TECH_ID_NUMBER")) {
						hqtechid = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains(DTTConstants.ROLEID)) {
						roleid = (String) (value);
						// logger.info(roleid);
						if (roleid != "" || roleid != null) {
							if (roleid.contains("WTCF_CS")) {
								roleid = roleid.substring(4, roleid.length());
								roleidsub = roleid.substring(0, 7);
								roleidfinal = roleid.substring(8,
										roleid.length());

								/*
								 * logger.info("roleid "+roleid);
								 * logger.info("roleidsub "+roleidsub);
								 * logger.info("Role sub1 "+roleidsub1);
								 * logger.info("roleidfinal "+roleidfinal);
								 */

							} else {
								roleid = roleid.substring(4, roleid.length());
								roleidsub = roleid.substring(0, 10);
								roleidfinal = roleid.substring(11,
										roleid.length());

								/*
								 * logger.info("roleid "+roleid);
								 * logger.info("roleidsub "+roleidsub);
								 * logger.info("Role sub1 "+roleidsub1);
								 * logger.info("roleidfinal "+roleidfinal);
								 */
							}
						}
						/*
						 * else{
						 * 
						 * logger.info(roleid);
						 * roleid=roleid.substring(4,roleid.length());
						 * roleidsub=roleid.substring(0,3);
						 * roleidfinal=roleid.substring(4,roleid.length());
						 * logger.info("Role roleidsub"+roleidsub);
						 * 
						 * 
						 * 
						 * logger.info("Role final"+roleidfinal);
						 * //logger.info("roleid"+roleid);
						 * //logger.info("roleidsub"+roleidsub);
						 * //logger.info("roleidfinal"+roleidfinal); }
						 */

					}
					if (key.contains(DTTConstants.PLANNED_HOURS)) {
						plannedhours = Double.valueOf(value.toString());

						/* logger.info(siteid); */
					}
					if (key.contains("Row_Id")) {
						rowid = Integer.valueOf(value.toString());
						if (rowid != 0 || rowid != null) {
							recordlist.add(rowid.toString());
						}

						/* logger.info(rowid); */
					}

					if (countryid != null && sourcesystem != null
							&& activityname != null && projectnumber != null
							&& locationshore != null && dttactivityid != null
							&& roleid != null) {

						JAXBElement<Integer> roleparentobjectid = null;
						JAXBElement<Double> plannedunitsid = null;
						ListIterator<EPS> epsread = epss.listIterator();
						while (epsread.hasNext()) {

							EPS epsreadelement = epsread.next();
							if (epsreadelement.getId().equals(countryid)) {

								country = true;
								countryobjectid = epsreadelement.getObjectId();
								countryparentobjecctid = epsreadelement
										.getParentObjectId();

								// logger.info(countryobjectid);

							}
						}

						List<EPS> epsss1 = new ArrayList<EPS>();
						if (countryobjectid != null) {
							// logger.info(countryobjectid);

							epsss1 = epsclient
									.readWSProjectEPS(countryobjectid);

						}
						// logger.info("epsss1"+epsss1.size());
						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains("SFDC")) {
							} else {
								if (locationshore != ""
										|| locationshore != null) {
									if (locationshore
											.equals(DTTConstants.ONSHORE)) {
										locationshore = DTTConstants.ATTACH_ONS
												+ countryid;
									}
									if (locationshore
											.equals(DTTConstants.OFFSHORE)) {
										locationshore = DTTConstants.ATTACH_OFS
												+ countryid;
									}

								}
								// logger.info("locationshore "+
								// locationshore);
								if (siteid != null) {
									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();

										// logger.info(projectepsreadelement.getId()
										// +
										// " "+projectepsreadelement.getParentObjectId().getValue()
										// );
										if (projectepsreadelement.getId()
												.equals(locationshore)) {

											location = true;
											// logger.info(projectepsreadelement.getName());
											locationshoreobjectid = projectepsreadelement
													.getObjectId();

										}
									}

									List<EPS> epsss2 = new ArrayList<EPS>();
									if (locationshoreobjectid != null) {
										// logger.info(countryobjectid);

										epsss2 = epsclient
												.readWSProjectEPS(locationshoreobjectid);

									}
									ListIterator<EPS> prolocationepsread = epsss2
											.listIterator();
									while (prolocationepsread.hasNext()) {
										EPS projlocationepsreadelement = prolocationepsread
												.next();

										/*
										 * logger.info(
										 * projlocationepsreadelement.getId() +
										 * " "+projlocationepsreadelement.
										 * getParentObjectId().getValue() );
										 */
										if (projlocationepsreadelement.getId()
												.equals(siteid)) {

											site = true;
											// logger.info(projectepsreadelement.getName());
											siteobjectid = projlocationepsreadelement
													.getObjectId();

										}
									}
									List<Project> oppproject = new ArrayList<Project>();
									if (siteobjectid != null) {
										oppproject = projectref
												.opputunityReadProject(siteobjectid
														.toString());
									}
									if (oppproject.size() != 0) {
										ListIterator<Project> projectidread = oppproject
												.listIterator();

										while (projectidread.hasNext()) {
											Project projectidreadelement = projectidread
													.next();

											/*
											 * logger.info(projectidreadelement
											 * .getId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												// logger.info(projectidreadelement.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}

									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();

									if (projectobjectid != null) {
										readwbs = wbsreadele
												.readWBS(projectobjectid
														.toString());
									}
									if (readwbs.size() != 0) {
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										while (wbsidread.hasNext()) {
											WBS wbsidreadelement = wbsidread
													.next();

											if (wbsidreadelement.getName()
													.equals(turbinename)) {
												wbs = true;
												/*
												 * logger.info(projectidreadelement
												 * .getName());
												 */

												wbsobjectid = wbsidreadelement
														.getObjectId();

												/*
												 * wbsjaxbElement = new
												 * JAXBElement( new
												 * QName("WBSObjectId"
												 * ),Activity. class,
												 * wbsobjectid);
												 */

												/*
												 * wbsobject.setValue(Integer
												 * .valueOf(wbsobjectid));
												 */
												/*
												 * logger.info("InLoop" +
												 * wbsjaxbElement.getValue());
												 */
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}
										}
										List<Activity> readactivity = new ArrayList<Activity>();
										PrimaveraActivity activityreadele = new PrimaveraActivity();
										if (wbsobjectid != 0) {
											readactivity = activityreadele
													.readActivitieforAssignment(wbsobjectid);
										}
										if (readactivity.size() != 0) {
											ListIterator<Activity> Activityread = readactivity
													.listIterator();
											Activity activityidreadelement = new Activity();
											while (Activityread.hasNext()) {
												activityidreadelement = Activityread
														.next();

												/*
												 * logger.info(activityidreadelement
												 * .getId() + " " +
												 * activityidreadelement
												 * .getName());
												 */

												if (activityidreadelement
														.getId()
														.equals(dttactivityid
																+ DTTConstants.DTTACTIVITYID_ENDSWITH_D)) {
													activity = true;
													/*
													 * logger.info(
													 * projectidreadelement
													 * .getName());
													 */
													activityobjectname = activityidreadelement
															.getName();
													activityobjectid = activityidreadelement
															.getObjectId()
															.toString();
													activityobject = activityidreadelement
															.getWBSObjectId();
													/*
													 * logger.info(activityobjectid
													 * );
													 * 
													 * logger.info(activityobject
													 * .getValue());
													 */

												}
											}
										}

										// logger.info("Processing role");

										/*
										 * ListIterator<Role> rolereadmain =
										 * readroledetails .listIterator();
										 * while (rolereadmain.hasNext()) {
										 */
										ListIterator<Role> roleread = readroledetails
												.listIterator();
										while (roleread.hasNext()) {
											// logger.info("Processing inside roleidsub"+roleidsub);

											Role rolereadelement = roleread
													.next();
											// logger.info("Processing inside rolereadelement.getId()"+rolereadelement.getId());
											// logger.info("Processing outside role"+roleidsub);
											if (rolereadelement.getId()
													.contains(roleidsub)) {
												// logger.info("Processing inside role"+roleidsub);
												String rolepreviousobject = rolereadelement
														.getObjectId()
														.toString();

												readparentroledetails = assignmenttype
														.readParentRole(rolepreviousobject);
												ListIterator<Role> roleparentreadtem = readparentroledetails
														.listIterator();
												while (roleparentreadtem
														.hasNext()) {

													Role roleparentreadelement = roleparentreadtem
															.next();
													if (roleparentreadelement
															.getId()
															.equalsIgnoreCase(
																	roleidfinal)) {
														// logger.info("Processing inside roleidfinal"+roleidfinal);
														String roleparentobject = roleparentreadelement
																.getParentObjectId()
																.getValue()
																.toString();
														// logger.info("Processing outside roleparentobject"+roleparentobject);
														// logger.info("Processing outside roleparentobject"+rolepreviousobject);
														if (roleparentobject
																.equalsIgnoreCase(rolepreviousobject)) {
															// logger.info("Processing inside roleparentobject"+roleparentreadelement.getObjectId());
															role = true;
															roleobjectid = roleparentreadelement
																	.getObjectId();
															roleparentobjectid = assignmenttype
																	.constructRoleObject(roleobjectid);

															/*
															 * logger.info("role"
															 * +role);
															 * logger.info(
															 * "rolereadelement.getObjectId"
															 * +rolereadelement.
															 * getObjectId());
															 */
														}
													}
												}

											}
										}

										if (activityobjectid != null) {
											logger.info("Inside "
													+ activityobjectid);
											readresourcedetails = assignmenttype
													.readRoleAssign(activityobjectid
															.toString());

											if (readresourcedetails.size() != 0) {
												ListIterator<ResourceAssignment> resourceread = readresourcedetails
														.listIterator();
												while (resourceread.hasNext()) {

													ResourceAssignment resourcereadelement = resourceread
															.next();

													roleparentobjectid = resourcereadelement
															.getRoleObjectId();
													plannedunitsid = resourcereadelement
															.getPlannedUnits();
													/*
													 * plannedunitsid =
													 * resourcereadelement
													 * .getPlannedUnits();
													 */
													logger.info("roleid inside resource"
															+ roleid);

													if (resourcereadelement
															.getActivityObjectId()
															.equals(Integer
																	.valueOf(activityobjectid))) {
														logger.info("activityobjectid inside resource"
																+ activityobjectid);
														logger.info("getActivityObjectId inside resource"
																+ resourcereadelement
																		.getActivityObjectId());
														logger.info("resourcereadelement getRoleObjectId()"
																+ resourcereadelement
																		.getRoleObjectId()
																		.getValue());
														if (resourcereadelement
																.getRoleObjectId()
																.getValue()
																.equals(roleobjectid)) {
															/*
															 * if
															 * (resourcereadelement
															 * .getRoleId()
															 * .equals
															 * (roleidfinal)) {
															 */

															logger.info("resourcereadelement getRoleId()"
																	+ resourcereadelement
																			.getRoleId());
															logger.info("roleid inside "
																	+ roleid);
															resourcerole = true;
															roleparentobjectid = resourcereadelement
																	.getRoleObjectId();
															/*
															 * plannedunitsid =
															 * resourcereadelement
															 * .
															 * getPlannedUnits()
															 * ;
															 */

															/*
															 * logger.info(
															 * "Planned Units "
															 * + plannedunitsid
															 * .getValue() +
															 * " role ID " +
															 * plannedunitstimeid
															 * );
															 * 
															 * roleobjectid=
															 * 
															 * 
															 * resourcereadelement.
															 * getObjectId ();
															 */

														}
													}
												}

											}
										}
										/*
										 * else{ ListIterator<Role> roleread =
										 * readroledetails .listIterator();
										 * while (roleread.hasNext()) {
										 * //logger.
										 * info("Processing inside roleidsub"
										 * +roleidsub);
										 * 
										 * Role rolereadelement =
										 * roleread.next(); //logger.info(
										 * "Processing inside rolereadelement.getId()"
										 * +rolereadelement.getId());
										 * if(rolereadelement
										 * .getId().contains(roleidsub)){
										 * //logger
										 * .info("Processing inside role"
										 * +roleidsub); String
										 * rolepreviousobject
										 * =rolereadelement.getObjectId
										 * ().toString();
										 * 
										 * readparentroledetails=assignmenttype.
										 * readParentRole(rolepreviousobject);
										 * ListIterator<Role> roleparentreadtem
										 * = readparentroledetails
										 * .listIterator(); while
										 * (roleparentreadtem.hasNext()) {
										 * 
										 * Role roleparentreadelement =
										 * roleparentreadtem.next();
										 * if(roleparentreadelement
										 * .getId().equalsIgnoreCase
										 * (roleidfinal)){ //logger.info(
										 * "Processing inside roleidfinal"
										 * +roleidfinal); String
										 * roleparentobject
										 * =roleparentreadelement
										 * .getParentObjectId
										 * ().getValue().toString();
										 * 
										 * if(roleparentobject.equalsIgnoreCase(
										 * rolepreviousobject)){
										 * 
										 * role = true; roleobjectid =
										 * roleparentreadelement .getObjectId();
										 * roleparentobjectid
										 * .setValue(roleobjectid);
										 * //logger.info("role"+role);
										 * //logger.info
										 * ("rolereadelement.getObjectId"
										 * +rolereadelement.getObjectId());
										 * 
										 * 
										 * } } } } } }
										 */
										/*
										 * System.out .print("Role Name " +
										 * rolereadelement .getName() +
										 * " role ID " + rolereadelement
										 * .getId() + " role Object Id " +
										 * rolereadelement .getObjectId());
										 */

										/*
										 * roleparentobjectid=rolereadelement
										 * .getRoleObjectId();
										 */

										if (project) {
											if (wbs) {
												wbsobject = activityreadele
														.constructWBSObject(wbsobjectid);
												wbsobject.setValue(wbsobjectid);
												if (activity) {
													if (role) {
														if (resourcerole) {

														} else {

															if (roleid != null
																	&& activityobjectid != null
																	&& roleparentobjectid
																			.getValue() != null) {
																if (!(rolecheck
																		.contains(rowid
																				.toString()))) {

																	rolecheck
																			.add(rowid
																					.toString());
																	rolecheck
																			.add(roleid);

																	// activitycreate.setWBSName(turbinename);
																	/*
																	 * activitycreate
																	 * .
																	 * setProjectObjectId
																	 * (
																	 * projectobjectid
																	 * );
																	 */
																	// activitycreate.setWBSCode(hqtechid);

																	/*
																	 * plannedunitsid
																	 * .
																	 * setValue(
																	 * plannedhours
																	 * );
																	 */
																	rolecreate
																			.setActivityObjectId(Integer
																					.valueOf(activityobjectid));

																	logger.info("activityobject getValue()"
																			+ activityobject
																					.getValue());
																	System.out
																			.println(dttactivityid);

																	rolecreate
																			.setRoleName(rolename);
																	rolecreate
																			.setRoleId(roleid);

																	rolecreate
																			.setRoleObjectId(roleparentobjectid);
																	// rolecreate.setPlannedUnits(constructPlannedhoursObject(plannedhours));
																	// .setPlannedUnitsPerTime(plannedhours);
																	logger.info("roleid"
																			+ roleid);

																	createroledetails
																			.add(rolecreate);

																}

															}
														}
													}

												}
											}

										}
										logger.info("createroledetails size"
												+ createroledetails.size());
									}
								}

							}

						}

					}
				}
			}

			if (createroledetails != null) {

				ListIterator<ResourceAssignment> read = createroledetails
						.listIterator();
				while (read.hasNext()) {

					ResourceAssignment projreadelement = read.next();

					// logger.info(countryobjectid);

					/*
					 * logger.info(projreadelement.getActivityObjectId());
					 * logger.info(projreadelement.getRoleName());
					 * logger.info(projreadelement.getRoleId());
					 * logger.info(projreadelement
					 * .getRoleObjectId().getValue());
					 */

				}

				// logger.info(projectdata);
			}
			logger.info("createroledetails.size" + createroledetails.size());
			// assignmenttype.createRole(createroledetails);
			logger.info("Record List " + recordlist.size());
			UpdateProcessRecordsDAO processed = new UpdateProcessRecordsDAO();
			processed.updaterecords(DTTConstants.ASSIGNMENT, "Y", recordlist,
					"Success", "ROW_ID");

		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return createroledetails;
	}

	public List<ResourceAssignment> checkAndUpdateAssignment() throws Exception {
		List<ResourceAssignment> updateroledetails = new ArrayList<ResourceAssignment>();

		try {

			assignmentmasterDAO = new AssignmentMasterDAO();
			assignmentmasterbean = assignmentmasterDAO.getAssignmentMasterDAO();

			assignmentmasterdata = new ArrayList<Map<String, Object>>();
			assignmentmasterdata = assignmentmasterbean
					.getAssignmentmasterdata();
			PrimaveraEPS epsclient = new PrimaveraEPS();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			List activitycheck = new ArrayList();
			List rolecheck = new ArrayList();
			List plannedunitsrolecheck = new ArrayList();

			List<Activity> createactivity = new ArrayList<Activity>();

			PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
			List<Role> readroledetails = new ArrayList<Role>();

			List<ResourceAssignment> readresourcedetails = new ArrayList<ResourceAssignment>();
			List<Role> readparentroledetails = new ArrayList<Role>();
			readroledetails = assignmenttype.readRole();

			for (Map<String, Object> map : assignmentmasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String turbinename = null;
				String hqtechid = null;
				String roleid = null;
				String rolename = null;
				String primaverarolename = null;
				Double plannedhours = null;
				Integer rowid = null;
				String roleidfinal = null;
				String roleidsub = null;
				String roleidsub1 = null;
				String worktypestring = null;
				String activityname = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				Date contractstartdate = null;
				Date contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;
				Activity activitycreate = new Activity();

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					int wbsobjectid = 0;
					Integer roleobjectid = 0;

					JAXBElement<Integer> wbsobject = null;

					JAXBElement<Double> plannedunitstimeid = null;
					ResourceAssignment roleupdate = new ResourceAssignment();
					JAXBElement<Integer> wbsjaxbElement = null;
					JAXBElement<Integer> activityobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;

					String activityobjectid = null;
					Integer primaveraroleactivityid = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean activity = false;
					boolean wbs = false;
					boolean role = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains("Country_Id")) {
						countryid = (String) value;

						// logger.info(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// logger.info(locationshore);
					}
					if (key.contains(DTTConstants.ACTIVITYNAME)) {
						activityname = (String) value;

						// logger.info(projectnumber);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// logger.info(projectnumber);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// logger.info(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == 1) {
							worktypestring = "Prev.Main";

						}
						if (worktype == 2) {
							worktypestring = "Retrofit";

						}
						if (worktype == 3) {
							worktypestring = "Mods&up";

						}
						if (worktype == 4) {
							worktypestring = "Repairs";

						}
						if (worktype == 5) {
							worktypestring = "Service";

						}
						if (worktype == 6) {
							worktypestring = "Service extension";

						}
						if (worktype == 7) {
							worktypestring = "Mods&Up";

						}

						/* logger.info(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* logger.info(sapparentobjectid); */
					}

					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains("DTT_Activity_Object_ID")) {
						dttactivityid = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains("Turbine_Name")) {
						turbinename = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains("HQ_TECH_ID_NUMBER")) {
						hqtechid = (String) (value);

						/* logger.info(siteid); */
					}
					if (key.contains(DTTConstants.ROLEID)) {
						roleid = (String) (value);
						logger.info(roleid);
						if (roleid != "" || roleid != null) {
							if (roleid.contains("WTCF_CS")) {
								roleid = roleid.substring(4, roleid.length());
								roleidsub = roleid.substring(0, 7);
								roleidfinal = roleid.substring(8,
										roleid.length());

								logger.info("roleid " + roleid);
								logger.info("roleidsub " + roleidsub);
								logger.info("Role sub1 " + roleidsub1);
								logger.info("roleidfinal " + roleidfinal);

							} else {
								roleid = roleid.substring(4, roleid.length());
								roleidsub = roleid.substring(0, 10);
								roleidfinal = roleid.substring(11,
										roleid.length());

								logger.info("roleid " + roleid);
								logger.info("roleidsub " + roleidsub);
								logger.info("Role sub1 " + roleidsub1);
								logger.info("roleidfinal " + roleidfinal);
							}
						}

					}
					if (key.contains(DTTConstants.PLANNED_HOURS)) {
						plannedhours = Double.valueOf(value.toString());

						/* logger.info(siteid); */
					}
					if (key.contains("Row_Id")) {
						rowid = Integer.valueOf(value.toString());

						logger.info(rowid);
					}

					if (countryid != null && sourcesystem != null
							&& activityname != null && projectnumber != null
							&& locationshore != null && dttactivityid != null
							&& turbinename != null && hqtechid != null
							&& roleid != null) {
						JAXBElement<Integer> roleparentobjectid = null;
						JAXBElement<Double> plannedunitsid = null;
						JAXBElement<Double> primaveraplannedunits = null;
						ListIterator<EPS> epsread = epss.listIterator();
						while (epsread.hasNext()) {

							EPS epsreadelement = epsread.next();
							if (epsreadelement.getId().equals(countryid)) {

								country = true;
								countryobjectid = epsreadelement.getObjectId();
								countryparentobjecctid = epsreadelement
										.getParentObjectId();

								// logger.info(countryobjectid);

							}
						}

						List<EPS> epsss1 = new ArrayList<EPS>();
						if (countryobjectid != null) {
							// logger.info(countryobjectid);

							epsss1 = epsclient
									.readWSProjectEPS(countryobjectid);

						}
						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains("SFDC")) {
							} else {

								if (locationshore != ""
										|| locationshore != null) {
									if (locationshore
											.equals(DTTConstants.ONSHORE)) {
										locationshore = DTTConstants.ATTACH_ONS
												+ countryid;
									}
									if (locationshore
											.equals(DTTConstants.OFFSHORE)) {
										locationshore = DTTConstants.ATTACH_OFS
												+ countryid;
									}
									/*
									 * // logger.info(element + " "); if
									 * (countryid.equals("GB")) { countryid =
									 * "Great Britain"; if
									 * (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-GB"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-GB"; } } if
									 * (countryid.equals("IE")) { countryid =
									 * "Ireland";
									 * 
									 * if (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-IE"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-IE"; } }
									 * 
									 * if (countryid.equals("TR")) { countryid =
									 * "Great Britain"; if
									 * (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-TR"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-TR"; } } if
									 * (countryid.equals("DE")) { countryid =
									 * "Great Britain"; if
									 * (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-DE"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-DE"; } } if
									 * (countryid.equals("SE")) { countryid =
									 * "Great Britain"; if
									 * (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-SE"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-SE"; } } if
									 * (countryid.equals("PL")) { countryid =
									 * "Great Britain"; if
									 * (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-PL"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-PL"; } } if
									 * (countryid.equals("FR")) { countryid =
									 * "Great Britain"; if
									 * (locationshore.equals("on-shore")) {
									 * locationshore = "ONS-FR"; } if
									 * (locationshore.equals("off-shore")) {
									 * locationshore = "OFS-FR"; } }
									 */
								}
								// logger.info("locationshore "+
								// locationshore);
								if (siteid != null) {
									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();

										// logger.info(projectepsreadelement.getId()
										// +
										// " "+projectepsreadelement.getParentObjectId().getValue()
										// );
										if (projectepsreadelement.getId()
												.equals(locationshore)) {

											location = true;
											// logger.info(projectepsreadelement.getName());
											locationshoreobjectid = projectepsreadelement
													.getObjectId();

										}
									}

									List<EPS> epsss2 = new ArrayList<EPS>();
									if (locationshoreobjectid != null) {
										// logger.info(countryobjectid);

										epsss2 = epsclient
												.readWSProjectEPS(locationshoreobjectid);

									}
									ListIterator<EPS> prolocationepsread = epsss2
											.listIterator();
									while (prolocationepsread.hasNext()) {
										EPS projlocationepsreadelement = prolocationepsread
												.next();

										/*
										 * logger.info(
										 * projlocationepsreadelement.getId() +
										 * " "+projlocationepsreadelement.
										 * getParentObjectId().getValue() );
										 */
										if (projlocationepsreadelement.getId()
												.equals(siteid)) {

											site = true;
											// logger.info(projectepsreadelement.getName());
											siteobjectid = projlocationepsreadelement
													.getObjectId();

										}
									}
									List<Project> oppproject = new ArrayList<Project>();
									if (siteobjectid != null) {
										oppproject = projectref
												.opputunityReadProject(siteobjectid
														.toString());
									}
									if (oppproject.size() != 0) {
										ListIterator<Project> projectidread = oppproject
												.listIterator();

										while (projectidread.hasNext()) {
											Project projectidreadelement = projectidread
													.next();

											/*
											 * logger.info(projectidreadelement
											 * .getId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												// logger.info(projectidreadelement.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}

									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();
									if (projectobjectid != null) {
										readwbs = wbsreadele
												.readWBS(projectobjectid
														.toString());
									}
									if (readwbs.size() != 0) {
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										while (wbsidread.hasNext()) {
											WBS wbsidreadelement = wbsidread
													.next();

											if (wbsidreadelement.getName()
													.equals(turbinename)) {
												wbs = true;
												/*
												 * logger.info(projectidreadelement
												 * .getName());
												 */

												wbsobjectid = wbsidreadelement
														.getObjectId();

												/*
												 * wbsjaxbElement = new
												 * JAXBElement( new
												 * QName("WBSObjectId"
												 * ),Activity. class,
												 * wbsobjectid);
												 */

												/*
												 * wbsobject.setValue(Integer
												 * .valueOf(wbsobjectid));
												 */
												/*
												 * logger.info("InLoop" +
												 * wbsjaxbElement.getValue());
												 */
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}
										}
									}
									List<Activity> readactivity = new ArrayList<Activity>();
									PrimaveraActivity activityreadele = new PrimaveraActivity();
									if (wbsobjectid != 0) {
										readactivity = activityreadele
												.readActivitieforAssignment(wbsobjectid);
									}
									if (readactivity.size() != 0) {
										ListIterator<Activity> Activityread = readactivity
												.listIterator();
										Activity activityidreadelement = new Activity();
										while (Activityread.hasNext()) {
											activityidreadelement = Activityread
													.next();

											/*
											 * logger.info(activityidreadelement
											 * .getObjectId() +
											 * " "+activityidreadelement
											 * .getName());
											 */

											if (activityidreadelement
													.getId()
													.equals(dttactivityid
															+ DTTConstants.DTTACTIVITYID_ENDSWITH_D)) {
												activity = true;
												/*
												 * logger.info(projectidreadelement
												 * .getName());
												 */
												activityobjectid = activityidreadelement
														.getObjectId()
														.toString();
												activityobject = activityidreadelement
														.getWBSObjectId();
												logger.info(activityobjectid);

												logger.info(activityobject
														.getValue());

											}
										}
									}
									if (activityobjectid != null) {

										readresourcedetails = assignmenttype
												.readRoleAssign(activityobjectid
														.toString());
									}
									if (readresourcedetails.size() != 0) {
										ListIterator<ResourceAssignment> resourceread = readresourcedetails
												.listIterator();
										while (resourceread.hasNext()) {

											ResourceAssignment resourcereadelement = resourceread
													.next();

											roleparentobjectid = resourcereadelement
													.getRoleObjectId();
											plannedunitsid = resourcereadelement
													.getPlannedUnits();
											/*
											 * plannedunitsid =
											 * resourcereadelement
											 * .getPlannedUnits();
											 */
											logger.info("Role Name"
													+ resourcereadelement
															.getRoleName());
											if (resourcereadelement
													.getActivityObjectId()
													.equals(activityobjectid)) {
												primaveraroleactivityid = resourcereadelement
														.getActivityObjectId();
												primaverarolename = resourcereadelement
														.getRoleName();
												primaveraplannedunits = resourcereadelement
														.getPlannedUnits();

											}
											if (resourcereadelement.getRoleId()
													.equals(roleid)) {

												roleparentobjectid = resourcereadelement
														.getRoleObjectId();
												plannedunitsid = resourcereadelement
														.getPlannedUnits();

												logger.info("Planned Units "
														+ plannedunitsid
																.getValue()
														+ " role ID "
														+ plannedunitstimeid);
												/*
												 * roleobjectid=
												 * 
												 * 
												 * resourcereadelement.getObjectId
												 * ();
												 */

											}
										}

									}
									ListIterator<Role> roleread = readroledetails
											.listIterator();
									while (roleread.hasNext()) {
										// logger.info("Processing inside roleidsub"+roleidsub);

										Role rolereadelement = roleread.next();
										// logger.info("Processing inside rolereadelement.getId()"+rolereadelement.getId());
										if (rolereadelement.getId().contains(
												roleidsub)) {
											// logger.info("Processing inside role"+roleidsub);
											String rolepreviousobject = rolereadelement
													.getObjectId().toString();

											readparentroledetails = assignmenttype
													.readParentRole(rolepreviousobject);
											ListIterator<Role> roleparentreadtem = readparentroledetails
													.listIterator();
											while (roleparentreadtem.hasNext()) {

												Role roleparentreadelement = roleparentreadtem
														.next();
												if (roleparentreadelement
														.getId()
														.equalsIgnoreCase(
																roleidfinal)) {
													// logger.info("Processing inside roleidfinal"+roleidfinal);
													String roleparentobject = roleparentreadelement
															.getParentObjectId()
															.getValue()
															.toString();

													if (roleparentobject
															.equalsIgnoreCase(rolepreviousobject)) {

														role = true;
														roleobjectid = roleparentreadelement
																.getObjectId();
														roleparentobjectid
																.setValue(roleobjectid);
														// logger.info("role"+role);
														// logger.info("rolereadelement.getObjectId"+rolereadelement.getObjectId());

													}
												}
											}

										}
									}
									/*
									 * ListIterator<Role> roleread =
									 * readroledetails .listIterator(); while
									 * (roleread.hasNext()) {
									 * 
									 * Role rolereadelement = roleread.next();
									 * 
									 * 
									 * System.out .print("Role Name " +
									 * rolereadelement .getName() + " role ID "
									 * + rolereadelement .getId() +
									 * " role Object Id " + rolereadelement
									 * .getObjectId());
									 * 
									 * if (rolereadelement.getName().equals(
									 * rolename)) { role = true; roleobjectid =
									 * rolereadelement .getObjectId();
									 * roleparentobjectid
									 * .setValue(roleobjectid); System.out
									 * .println("(rolereadelement.getObjectId" +
									 * rolereadelement .getObjectId());
									 * 
									 * 
									 * roleparentobjectid=rolereadelement
									 * .getRoleObjectId();
									 * 
									 * }
									 * 
									 * }
									 */

									if (project) {
										if (wbs) {
											wbsobject = activityreadele
													.constructWBSObject(wbsobjectid);
											wbsobject.setValue(wbsobjectid);
											if (activity) {
												if (role) {
													if (roleid != null
															&& activityobjectid != null
															&& roleparentobjectid
																	.getValue() != null) {
														if (primaveraroleactivityid != null) {
															if (!primaverarolename
																	.contains(rolename)) {

																if (!(rolecheck
																		.contains(rowid
																				.toString()))) {

																	rolecheck
																			.add(rowid
																					.toString());
																	rolecheck
																			.add(roleid);

																	// activitycreate.setWBSName(turbinename);
																	/*
																	 * activitycreate
																	 * .
																	 * setProjectObjectId
																	 * (
																	 * projectobjectid
																	 * );
																	 */
																	// activitycreate.setWBSCode(hqtechid);

																	plannedunitsid
																			.setValue(plannedhours);
																	roleupdate
																			.setActivityObjectId(Integer
																					.valueOf(activityobjectid));

																	System.out
																			.println(activityobject
																					.getValue());
																	System.out
																			.println(dttactivityid);

																	roleupdate
																			.setRoleName(rolename);
																	roleupdate
																			.setRoleId(roleid);

																	roleupdate
																			.setRoleObjectId(roleparentobjectid);
																	roleupdate
																			.setPlannedUnits(plannedunitsid);

																	updateroledetails
																			.add(roleupdate);

																}
															}

															roleupdate = new ResourceAssignment();
															if (!primaveraplannedunits
																	.getValue()
																	.equals(plannedhours)) {
																if (!(plannedunitsrolecheck
																		.contains(rowid
																				.toString()))) {

																	plannedunitsrolecheck
																			.add(rowid
																					.toString());
																	plannedunitsrolecheck
																			.add(roleid);

																	// activitycreate.setWBSName(turbinename);
																	/*
																	 * activitycreate
																	 * .
																	 * setProjectObjectId
																	 * (
																	 * projectobjectid
																	 * );
																	 */
																	// activitycreate.setWBSCode(hqtechid);

																	plannedunitsid
																			.setValue(plannedhours);
																	roleupdate
																			.setActivityObjectId(Integer
																					.valueOf(activityobjectid));

																	/*
																	 * System.out
																	 * .println(
																	 * activityobject
																	 * .
																	 * getValue(
																	 * ));
																	 * System
																	 * .out
																	 * .println(
																	 * dttactivityid
																	 * );
																	 */

																	roleupdate
																			.setRoleObjectId(roleparentobjectid);
																	roleupdate
																			.setPlannedUnits(plannedunitsid);

																	updateroledetails
																			.add(roleupdate);

																}

															}
														}
													}
												}

											}

										}

									}

								}

							}

						}
					}
				}
			}
			if (updateroledetails != null) {

				ListIterator<ResourceAssignment> read = updateroledetails
						.listIterator();
				while (read.hasNext()) {

					ResourceAssignment projreadelement = read.next();

					// logger.info(countryobjectid);

					/*
					 * logger.info(projreadelement.getActivityObjectId());
					 * logger.info(projreadelement.getRoleName());
					 * logger.info(projreadelement.getRoleId());
					 * logger.info(projreadelement.getRoleObjectId()
					 * .getValue());
					 */

				}

				// logger.info(projectdata);
			}
			logger.info("updateroledetails.size" + updateroledetails.size());

		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return updateroledetails;
	}

	public JAXBElement<Double> constructPlannedhoursObject(double plannedhours) {
		ObjectFactory objectfactory = new ObjectFactory();
		return objectfactory.createResourceAssignmentPlannedUnits(plannedhours);
	}

	public static void main(String[] args) throws Exception {
		PrimaveraAssignmentWSClient roleclient = new PrimaveraAssignmentWSClient();

		roleclient.checkAndCreateAssignment();

	}

}
