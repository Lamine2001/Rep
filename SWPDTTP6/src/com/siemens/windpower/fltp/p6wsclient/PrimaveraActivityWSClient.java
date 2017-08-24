package com.siemens.windpower.fltp.p6wsclient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.primavera.ws.p6.udfvalue.ObjectFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.activitycode.ActivityCode;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignment;
import com.primavera.ws.p6.baselineproject.BaselineProject;
import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.role.Role;
import com.primavera.ws.p6.udftype.UDFType;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.fltp.beans.ActivityMasterBean;
import com.siemens.windpower.fltp.dao.ActivityMasterDAO;
import com.siemens.windpower.fltp.dao.ActivityOutBoundDAO;
import com.siemens.windpower.fltp.dao.AssignmentMasterDAO;
import com.siemens.windpower.fltp.dao.UpdateLastReadDAO;
import com.siemens.windpower.fltp.dao.UpdateProcessRecordsDAO;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.common.ReadProperties;

public class PrimaveraActivityWSClient {
	Map prop = null;
	ActivityMasterBean activitymasterbean = null;
	ActivityMasterDAO activitymasterDAO = null;
	List<Map<String, Object>> activitymasterdata = null;
	Logger logger = null;

	public PrimaveraActivityWSClient() throws Exception {
		logger = Logger.getLogger(PrimaveraActivityWSClient.class);

		ReadProperties read = new ReadProperties();

		prop = read.readPropertiesFile();

	}

	@SuppressWarnings("unchecked")
	public List<Activity> checkAndCreateActivity() throws Exception {
		List<Activity> createactivity = new ArrayList<Activity>();

		activitymasterDAO = new ActivityMasterDAO();
		activitymasterbean = activitymasterDAO.getActivitytMasterDAO();
		activitymasterdata = new ArrayList<Map<String, Object>>();
		activitymasterdata = activitymasterbean.getActivitymasterdata();

	//	PrimaveraActivity activitytype = new PrimaveraActivity();

		AssignmentMasterDAO assignmentmasterDAO = new AssignmentMasterDAO();
		List<Map<String, Object>> nonlaborhoursdata = assignmentmasterDAO
				.getNonLaborHoursDAO();
		PrimaveraEPS epsclient = new PrimaveraEPS();

		//PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();
		//List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();

		PrimaveraProject projectref = new PrimaveraProject();
		List<EPS> epss = new ArrayList<EPS>();
		epss = epsclient.readEPS();
		List activitycheck = new ArrayList();
		//List activitycodecheck = new ArrayList();
		//List activityudfcheck = new ArrayList();
		//List serviceudfcheck = new ArrayList();
		//List notificationudfcheck = new ArrayList();
		List laborcheck = new ArrayList();

		 logger.info("activitymasterdata"+activitymasterdata.size());
		for (Map<String, Object> map : activitymasterdata) {

			String countryid = null;

			String sourcesystem = null;
			//int worktype = 0;
			String locationshore = null;
			String siteid = null;
			String dttactivityid = null;
			String turbinename = null;
			//String turbinetype = null;
			//String turbinetypename = null;

			String hqtechid = null;
			//String rutechid = null;
			//String notificationid = "";
			//String serviceorderid = "";
			//String startdate = null;

			//String worktypestring = null;
			String activityname = null;
			String durationtype = null;
			String type = null;
			String projectnumber = null;
			//String projectname = null;
			//String sapparentobjectid = null;
			/*
			 * Date contractstartdate = null; Date contractenddate = null;
			 */
			//String customerifa = null;
			//String customername = null;
			//String probability = null;
			Activity activitycreate = new Activity();

			//ActivityCodeAssignment acitivitycodecreate = new ActivityCodeAssignment();

			for (Map.Entry<String, Object> entry : map.entrySet()) {

				//UDFValue activityudfassign = new UDFValue();
				Integer countryobjectid = null;
				Integer locationshoreobjectid = null;
				Integer siteobjectid = 0;
				int wbsobjectid = 0;
				JAXBElement<Integer> wbsobject = null;

				//JAXBElement<Integer> wbsjaxbElement = null;
				JAXBElement<Integer> activityobject = null;
				// JAXBElement<Integer> activityparentobject = null;
				//String mpcallnumber = null;

				JAXBElement<Integer> countryparentobjecctid = null;
				String opputunityobjectid = null;
				Integer projectobjectid = 0;
				//Integer activitycodeobjectid = null;
				Integer activityobjectid = null;
				//Integer opputunityparentobjectid = null;
				String labordttid = null;
				double nonlaborunits = 0.0;
				//boolean opportunity = false;
				boolean project = false;
				boolean country = false;
				boolean location = false;
				boolean site = false;
				boolean activity = false;
				boolean wbs = false;
				//boolean dttactivityidcheck = false;
				//boolean mpcallnumbercheck = false;
				//boolean hqtechidcheck = false;
				//boolean rutechidcheck = false;
				//boolean notificationidcheck = false;
				//boolean serviceorderidcheck = false;
				//boolean turbinetypecheck = false;

				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.contains("Country_Id")) {
					countryid = (String) value;

					// System.out.println(countryid);

				}
				if (key.contains("Location_Shore")) {
					locationshore = (String) value;

					// System.out.println(locationshore);
				}
				if (key.contains("Activity_Name")) {
					activityname = (String) value;

					if (activityname.contains(DTTConstants.DEMAND)) {

						durationtype = DTTConstants.DEMAND_DURATION;
						type = DTTConstants.DEMAND_TYPE;

					}
					if (activityname.contains(DTTConstants.EFFORT)) {
						durationtype = DTTConstants.EFFORT_DURATION;
						type = DTTConstants.EFFORT_TYPE;

					}
					// System.out.println(projectnumber);
				}
				if (key.contains("Project_Number")) {
					projectnumber = (String) value;

					// System.out.println(projectnumber);
				}

				if (key.contains("Source_System")) {
					sourcesystem = (String) value;

					// System.out.println(sourcesystem);
				}
				/*if (key.contains("Work_Type")) {
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

					 System.out.println(worktypestring); 
				}*/

				/*if (key.contains("SAP_Parent_Object_ID")) {
					sapparentobjectid = (String) (value);

					 System.out.println(sapparentobjectid); 
				}*/

				if (key.contains("Site_Id")) {
					siteid = (String) (value);

					/* System.out.println(siteid); */
				}
				if (key.contains("DTT_Activity_Object_ID")) {
					dttactivityid = (String) (value);

					/* System.out.println(siteid); */
				}
				if (key.contains("Turbine_Name")) {
					turbinename = (String) (value);

					/* System.out.println(siteid); */
				}
				/*if (key.contains("Turbine_Type")) {
					turbinetypename = (String) (value);
					turbinetype = turbinetypename.substring(4,
							turbinetypename.length());
					System.out.println(turbinetype);
				}*/
				if (key.contains("HQ_TECH_ID_NUMBER")) {
					hqtechid = (String) (value);

					/* System.out.println(siteid); */
				}

				/*if (key.contains(DTTConstants.STARTDATE)) {
					startdate = (String) (value);

				}

				if (key.contains(DTTConstants.MP_CALL_NUMBER)) {
					mpcallnumber = (String) (value);

					 System.out.println(siteid); 
				}
				if (key.contains(DTTConstants.NOTIFICATION_ID)) {
					notificationid = (String) (value);

					 System.out.println(siteid); 
				}
				if (key.contains(DTTConstants.SERVICE_ORDER_ID)) {
					serviceorderid = (String) (value);

					 System.out.println(siteid); 
				}
				if (key.contains(DTTConstants.RU_TECH_ID_NUMBER)) {
					rutechid = (String) (value);

					 System.out.println(siteid); 
				}*/

				if (countryid != null && sourcesystem != null
						&& activityname != null && projectnumber != null
						&& locationshore != null && dttactivityid != null
						&& turbinename != null && hqtechid != null && siteid!=null) {
					ListIterator<EPS> epsread = epss.listIterator();
					while (epsread.hasNext()) {

						EPS epsreadelement = epsread.next();
						if (epsreadelement.getId().equals(countryid)) {

							country = true;
							countryobjectid = epsreadelement.getObjectId();
							countryparentobjecctid = epsreadelement
									.getParentObjectId();

							// System.out.print(countryobjectid);

						}
					}

					List<EPS> epsss1 = new ArrayList<EPS>();
					if (countryobjectid != null) {
						// System.out.println(countryobjectid);

						epsss1 = epsclient.readWSProjectEPS(countryobjectid);

					}
					if (epsss1 != null && sourcesystem != null) {
						if (sourcesystem.contains("SFDC")) {
						} else {

							if (locationshore != "" || locationshore != null) {
								if (locationshore.equals(DTTConstants.ONSHORE)) {
									locationshore = DTTConstants.ATTACH_ONS
											+ countryid;
								}
								if (locationshore.equals(DTTConstants.OFFSHORE)) {
									locationshore = DTTConstants.ATTACH_OFS
											+ countryid;
								}

							}
							// System.out.println("locationshore "+
							// locationshore);
							if (siteid != null) {
								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();

									// System.out.print(projectepsreadelement.getId()
									// +
									// " "+projectepsreadelement.getParentObjectId().getValue()
									// );
									if (projectepsreadelement.getId().equals(
											locationshore)) {

										location = true;
										// System.out.print(projectepsreadelement.getName());
										locationshoreobjectid = projectepsreadelement
												.getObjectId();

									}
								}

								List<EPS> epsss2 = new ArrayList<EPS>();
								if (locationshoreobjectid != null) {
									// System.out.println(countryobjectid);

									epsss2 = epsclient
											.readWSProjectEPS(locationshoreobjectid);

								}
								ListIterator<EPS> prolocationepsread = epsss2
										.listIterator();
								while (prolocationepsread.hasNext()) {
									EPS projlocationepsreadelement = prolocationepsread
											.next();

									/*
									 * System.out.print(
									 * projlocationepsreadelement.getId() +
									 * " "+projlocationepsreadelement.
									 * getParentObjectId().getValue() );
									 */
									if (projlocationepsreadelement.getId()
											.equals(siteid)) {

										site = true;
										// System.out.print(projectepsreadelement.getName());
										siteobjectid = projlocationepsreadelement
												.getObjectId();

									}
								}

								List<Project> oppproject = new ArrayList<Project>();
								if (siteobjectid != 0) {
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
										 * System.out.print(projectidreadelement
										 * .getId() + " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											// System.out.print(projectidreadelement.getName());
											projectobjectid = projectidreadelement
													.getObjectId();
											// opputunityparentobjectid=projectidreadelement.getParentObjectId();

										}

									}
								}
								List<WBS> readwbs = new ArrayList<WBS>();
								PrimaveraWBS wbsreadele = new PrimaveraWBS();
								if (projectobjectid != 0) {
									readwbs = wbsreadele
											.readWBS(projectobjectid.toString());
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
												 * System.out.print(
												 * projectidreadelement
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
												 * System.out.print("InLoop" +
												 * wbsjaxbElement.getValue());
												 */
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}
										}
										List<Activity> readactivity = new ArrayList<Activity>();
										PrimaveraActivity activityreadele = new PrimaveraActivity();
										if (wbsobjectid != 0) {
											readactivity = activityreadele
													.readActivities(wbsobjectid);
										}
										if (readactivity.size() != 0) {
											ListIterator<Activity> Activityread = readactivity
													.listIterator();
											Activity activityidreadelement = new Activity();
											while (Activityread.hasNext()) {
												activityidreadelement = Activityread
														.next();

												/*
												 * System.out.print(
												 * activityidreadelement
												 * .getDurationType() +
												 * " "+activityidreadelement
												 * .getType
												 * ()+" "+activityidreadelement
												 * .getPlannedLaborUnits());
												 */

												if (activityidreadelement
														.getId().equals(
																dttactivityid)
														|| activityidreadelement
																.getId()
																.equals(dttactivityid
																		.substring(
																				0,
																				dttactivityid
																						.length() - 1))) {

													activity = true;
													/*
													 * System.out.print(
													 * projectidreadelement
													 * .getName());
													 */

													activityobjectid = activityidreadelement
															.getObjectId();
													activityobject = activityidreadelement
															.getWBSObjectId();
													/*
													 * System.out.print(
													 * activityobjectid);
													 * 
													 * System.out.print(
													 * activityobject
													 * .getValue());
													 */

												}

											}
										}

										/*
										 * PrimaveraAssignment assignmenttype =
										 * new PrimaveraAssignment(); List<Role>
										 * readroledetails = new
										 * ArrayList<Role>();
										 * 
										 * readroledetails =
										 * assignmenttype.readRole();
										 * ListIterator<Role> roleread =
										 * readroledetails .listIterator();
										 * while (roleread.hasNext()) {
										 * 
										 * Role rolereadelement =
										 * roleread.next();
										 * 
										 * System.out.print(rolereadelement
										 * .getName() + "" +
										 * rolereadelement.getId() + "" +
										 * rolereadelement .getParentObjectId()
										 * .getValue()); activityparentobject =
										 * rolereadelement .getParentObjectId();
										 * 
										 * }
										 */
										if (project) {
											if (wbs) {
												wbsobject = activityreadele
														.constructWBSObject(wbsobjectid);
												wbsobject.setValue(wbsobjectid);
												if (activity) {

												} else {

													if (wbsobject.getValue() != null) {

														if (!(activitycheck
																.contains(dttactivityid
																		.toString()))) {

															activitycheck
																	.add(dttactivityid
																			.toString());
															activitycheck
																	.add(activityname);
															logger.info("activityname"
																	+ activityname);
															// activitycreate.setWBSName(turbinename);
															/*
															 * activitycreate
															 * .setProjectObjectId
															 * (
															 * projectobjectid);
															 */
															// activitycreate.setWBSCode(hqtechid);
															activitycreate
																	.setWBSObjectId(wbsobject);

															/*
															 * System.out
															 * .println
															 * (wbsobject
															 * .getValue());
															 * System.out
															 * .println
															 * (dttactivityid);
															 */
															// logger.info("activityname"+activityname);
															activitycreate
																	.setName(activityname);
															activitycreate
																	.setDurationType(durationtype);
															activitycreate
																	.setType(type);

															activitycreate
																	.setId(dttactivityid);
															for (Map<String, Object> laborhoursmap : nonlaborhoursdata) {
																for (Map.Entry<String, Object> entrylabor : laborhoursmap
																		.entrySet()) {
																	String entrylaborkey = entrylabor
																			.getKey();
																	Object entrylaborvalue = entrylabor
																			.getValue();

																	if (entrylaborkey
																			.contains("DTT_Activity_Object_ID")) {
																		labordttid = (String) entrylaborvalue;
																	}

																	if (labordttid
																			.contains(dttactivityid)) {
																		if (entrylaborkey
																				.contains(DTTConstants.Non_LABOR_HOURS)) {
																			nonlaborunits = Double
																					.valueOf(entrylaborvalue
																							.toString());

																			System.out
																					.println("nonlaborunits   "
																							+ entrylaborvalue);
																		}
																		if (!(laborcheck
																				.contains(dttactivityid))
																				&& nonlaborunits != 0.0) {
																			laborcheck
																					.add(dttactivityid);
																			activitycreate
																					.setPlannedNonLaborUnits(nonlaborunits);
																			/*
																			 * System
																			 * .
																			 * out
																			 * .
																			 * println
																			 * (
																			 * "nonlaborunits   "
																			 * +
																			 * nonlaborunits
																			 * )
																			 * ;
																			 * System
																			 * .
																			 * out
																			 * .
																			 * println
																			 * (
																			 * "nonlaborunits   "
																			 * +
																			 * laborcheck
																			 * .
																			 * size
																			 * (
																			 * )
																			 * )
																			 * ;
																			 */
																		}
																	}
																}
															}

															createactivity
																	.add(activitycreate);
															System.out
																	.println("createactivity.size"
																			+ createactivity
																					.size());

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
		if (createactivity != null) {

			ListIterator<Activity> read = createactivity.listIterator();
			while (read.hasNext()) {

				Activity projreadelement = read.next();

				// System.out.print(countryobjectid);

				/*
				 * System.out.println(projreadelement.getWBSObjectId()
				 * .getValue());
				 */
				System.out.println(projreadelement.getName());
				System.out.println(projreadelement.getId());

			}

			// System.out.println(projectdata);
		}

		// logger.info("createactivity.size" + createactivity.size());

		// activitytype.createActivities(createactivity);
		// activitycodetype.createActivityCodeAssignment(activitycodedetails);
		// udfs.CreateUDFValue(activityudfdetails);

		return createactivity;
	}

	@SuppressWarnings("unchecked")
	public List<Activity> checkAndUpdateActivity() throws Exception {

		List<Activity> updateactivity = new ArrayList<Activity>();

		try {
			activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getActivitytMasterDAO();
			activitymasterdata = new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getActivitymasterdata();

			//PrimaveraActivity activitytype = new PrimaveraActivity();

			PrimaveraEPS epsclient = new PrimaveraEPS();

		//	PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();
			//List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			List activitycheck = new ArrayList();
			//List activitycodecheck = new ArrayList();
			//List activityudfcheck = new ArrayList();
			//List serviceudfcheck = new ArrayList();
			//List notificationudfcheck = new ArrayList();

			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				//int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String primaveraactivityid = null;
				String turbinename = null;
				//String turbinetype = null;
				//String turbinetypename = null;

				String hqtechid = null;
				//String rutechid = null;
				//String notificationid = "";
				//String serviceorderid = "";
				//String startdate = null;

				//String worktypestring = null;
				String activityname = null;
				String projectnumber = null;
				//String projectname = null;
				//String sapparentobjectid = null;
				/*
				 * Date contractstartdate = null; Date contractenddate = null;
				 */
				//String customerifa = null;
				//String customername = null;
				//String probability = null;

				//ActivityCodeAssignment acitivitycodecreate = new ActivityCodeAssignment();

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					Activity activityupdate = new Activity();

					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					int wbsobjectid = 0;
					JAXBElement<Integer> wbsobject = null;

					JAXBElement<Integer> activityobject = null;
					JAXBElement<Integer> activityparentobject = null;
					//String mpcallnumber = null;

					JAXBElement<Integer> countryparentobjecctid = null;
				//	String opputunityobjectid = null;
					Integer projectobjectid = null;
					//Integer activitycodeobjectid = null;
					Integer activityobjectid = null;
					//Integer opputunityparentobjectid = null;
					//boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean activity = false;
					boolean wbs = false;
					//boolean dttactivityidcheck = false;
					/*boolean mpcallnumbercheck = false;
					boolean hqtechidcheck = false;
					boolean rutechidcheck = false;
					boolean notificationidcheck = false;
					boolean serviceorderidcheck = false;
					boolean turbinetypecheck = false;
*/
					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains("Country_Id")) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains("Activity_Name")) {
						activityname = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					/*if (key.contains("Work_Type")) {
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

						 System.out.println(worktypestring); 
					}*/

					/*if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						 System.out.println(sapparentobjectid); 
					}*/

					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("DTT_Activity_Object_ID")) {
						dttactivityid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Turbine_Name")) {
						turbinename = (String) (value);

						/* System.out.println(siteid); */
					}
					/*if (key.contains("Turbine_Type")) {
						turbinetypename = (String) (value);
						turbinetype = turbinetypename.substring(4,
								turbinetypename.length());
						System.out.println(turbinetype);
					}*/
					if (key.contains("HQ_TECH_ID_NUMBER")) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}
					/*
					 * if (key.contains(DTTConstants.STARTDATE)) { startdate =
					 * (XMLGregorianCalendar) (value);
					 * 
					 * System.out.println(siteid); }
					 */
					/*if (key.contains(DTTConstants.MP_CALL_NUMBER)) {
						mpcallnumber = (String) (value);

						 System.out.println(siteid); 
					}
					if (key.contains(DTTConstants.NOTIFICATION_ID)) {
						notificationid = (String) (value);

						 System.out.println(siteid); 
					}
					if (key.contains(DTTConstants.SERVICE_ORDER_ID)) {
						serviceorderid = (String) (value);

						 System.out.println(siteid); 
					}
					if (key.contains(DTTConstants.RU_TECH_ID_NUMBER)) {
						rutechid = (String) (value);

						 System.out.println(siteid); 
					}
*/
					if (countryid != null && sourcesystem != null
							&& activityname != null && projectnumber != null
							&& locationshore != null && dttactivityid != null
							&& turbinename != null && hqtechid != null  && siteid!=null) {
						ListIterator<EPS> epsread = epss.listIterator();
						while (epsread.hasNext()) {

							EPS epsreadelement = epsread.next();
							if (epsreadelement.getId().equals(countryid)) {

								country = true;
								countryobjectid = epsreadelement.getObjectId();
								countryparentobjecctid = epsreadelement
										.getParentObjectId();

								// System.out.print(countryobjectid);

							}
						}

						List<EPS> epsss1 = new ArrayList<EPS>();
						if (countryobjectid != null) {
							// System.out.println(countryobjectid);

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
									
								}
								// System.out.println("locationshore "+
								// locationshore);
								if (siteid != null) {
									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();

										// System.out.print(projectepsreadelement.getId()
										// +
										// " "+projectepsreadelement.getParentObjectId().getValue()
										// );
										if (projectepsreadelement.getId()
												.equals(locationshore)) {

											location = true;
											// System.out.print(projectepsreadelement.getName());
											locationshoreobjectid = projectepsreadelement
													.getObjectId();

										}
									}

									List<EPS> epsss2 = new ArrayList<EPS>();
									if (locationshoreobjectid != null) {
										// System.out.println(countryobjectid);

										epsss2 = epsclient
												.readWSProjectEPS(locationshoreobjectid);

									}
									ListIterator<EPS> prolocationepsread = epsss2
											.listIterator();
									while (prolocationepsread.hasNext()) {
										EPS projlocationepsreadelement = prolocationepsread
												.next();

										/*
										 * System.out.print(
										 * projlocationepsreadelement.getId() +
										 * " "+projlocationepsreadelement.
										 * getParentObjectId().getValue() );
										 */
										if (projlocationepsreadelement.getId()
												.equals(siteid)) {

											site = true;
											// System.out.print(projectepsreadelement.getName());
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
											 * System.out.print(projectidreadelement
											 * .getId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												// System.out.print(projectidreadelement.getName());
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
												 * System.out.print(
												 * projectidreadelement
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
												 * System.out.print("InLoop" +
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
												.readActivities(wbsobjectid);
									}
									if (readactivity.size() != 0) {
										ListIterator<Activity> Activityread = readactivity
												.listIterator();
										Activity activityidreadelement = new Activity();
										while (Activityread.hasNext()) {
											activityidreadelement = Activityread
													.next();

											/*
											 * System.out.print(
											 * activityidreadelement
											 * .getObjectId() +
											 * " "+activityidreadelement
											 * .getName());
											 */

											if (activityidreadelement.getId()
													.equals(dttactivityid)
													|| activityidreadelement
															.getId()
															.equals(dttactivityid
																	.substring(
																			0,
																			dttactivityid
																					.length() - 1))) {
												activity = true;
												/*
												 * System.out.print(
												 * projectidreadelement
												 * .getName());
												 */

												activityobjectid = activityidreadelement
														.getObjectId();
												activityobject = activityidreadelement
														.getWBSObjectId();
												primaveraactivityid = activityidreadelement
														.getId();
												System.out
														.print(activityobjectid);

												System.out.print(activityobject
														.getValue());

											}
										}

									}

									PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
									List<Role> readroledetails = new ArrayList<Role>();

									readroledetails = assignmenttype.readRole();
									ListIterator<Role> roleread = readroledetails
											.listIterator();
									while (roleread.hasNext()) {

										Role rolereadelement = roleread.next();

										System.out.print(rolereadelement
												.getName()
												+ ""
												+ rolereadelement.getId()
												+ ""
												+ rolereadelement
														.getParentObjectId()
														.getValue());
										activityparentobject = rolereadelement
												.getParentObjectId();

									}

									if (project) {
										if (wbs) {
											wbsobject = activityreadele
													.constructWBSObject(wbsobjectid);
											wbsobject.setValue(wbsobjectid);
											if (activity) {
												if (!primaveraactivityid
														.contains(dttactivityid
																.toString())) {
													if (wbsobject.getValue() != null) {
														if (!(activitycheck
																.contains(dttactivityid
																		.toString()))) {

															activitycheck
																	.add(dttactivityid
																			.toString());
															activitycheck
																	.add(activityname);

															activityupdate
																	.setWBSObjectId(wbsobject);

															/*
															 * System.out
															 * .println
															 * (wbsobject
															 * .getValue());
															 * System.out
															 * .println
															 * (dttactivityid);
															 */

															activityupdate
																	.setName(activityname);
															activityupdate
																	.setId(dttactivityid);
															updateactivity
																	.add(activityupdate);

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
			if (updateactivity != null) {

				ListIterator<Activity> read = updateactivity.listIterator();
				while (read.hasNext()) {

					Activity projreadelement = read.next();

					// System.out.print(countryobjectid);

					/*
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 */
					System.out.println(projreadelement.getName());
					System.out.println(projreadelement.getId());

				}

				// System.out.println(projectdata);
			}

			System.out.println("updateactivity.size" + updateactivity.size());

			// activitytype.createActivities(createactivity);
			// activitycodetype.createActivityCodeAssignment(activitycodedetails);
			// udfs.CreateUDFValue(activityudfdetails);
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return updateactivity;
	}

	@SuppressWarnings("unchecked")
	public List<ActivityCodeAssignment> checkAndCreateActivityCodes()
			throws Exception {
		List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();
		try {
			activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getActivitytMasterDAO();
			activitymasterdata = new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getActivitymasterdata();

			PrimaveraEPS epsclient = new PrimaveraEPS();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			PrimaveraActivityCode activitycode = new PrimaveraActivityCode();

			/*
			 * istIterator<ActivityCode> activitycoderead = readactivitycode
			 * .listIterator(); while (activitycoderead.hasNext()) {
			 * 
			 * ActivityCode activitycodereadelement = activitycoderead.next();
			 * 
			 * //logger.info(activitycodereadelement.getCodeTypeName() + "" +
			 * activitycodereadelement.getCodeTypeObjectId() + " " +
			 * activitycodereadelement.getParentObjectId() + " " +
			 * activitycodereadelement.getCodeValue() + " " +
			 * activitycodereadelement.getObjectId()); }
			 */
			List<String> recordlist = new ArrayList<String>();

			List activitycodecheck = new ArrayList();
			//List activityudfcheck = new ArrayList();
			//List serviceudfcheck = new ArrayList();
			//List notificationudfcheck = new ArrayList();

			List<Activity> createactivity = new ArrayList<Activity>();
			// logger.info("activitymasterdata"+activitymasterdata.size());
			if (activitymasterdata.size() != 0) {
				for (Map<String, Object> map : activitymasterdata) {

					String countryid = null;

					String sourcesystem = null;
					//int worktype = 0;
					String locationshore = null;
					String siteid = null;
					String dttactivityid = null;
					String turbinename = null;
					String turbinetype = null;
					String turbinetypename = null;

					String hqtechid = null;
					//String rutechid = null;
					//String notificationid = "";
					//String serviceorderid = "";
					//XMLGregorianCalendar startdate = null;

					//String worktypestring = null;
					String activityname = null;
					String projectnumber = null;
					//String projectname = null;
					//String sapparentobjectid = null;
					/*
					 * Date contractstartdate = null; Date contractenddate =
					 * null;
					 */
					//String customerifa = null;
					//String customername = null;
					//String probability = null;
					//Activity activitycreate = new Activity();

					ActivityCodeAssignment acitivitycodecreate = new ActivityCodeAssignment();

					for (Map.Entry<String, Object> entry : map.entrySet()) {

					//	UDFValue activityudfassign = new UDFValue();
						Integer countryobjectid = null;
						Integer locationshoreobjectid = null;
						Integer siteobjectid = null;
						int wbsobjectid = 0;
						JAXBElement<Integer> wbsobject = null;

						JAXBElement<Integer> wbsjaxbElement = null;
						JAXBElement<Integer> activityobject = null;
						JAXBElement<Integer> activityparentobject = null;
						//String mpcallnumber = null;

						JAXBElement<Integer> countryparentobjecctid = null;
						//String opputunityobjectid = null;
						Integer projectobjectid = null;
						Integer activitycodeobjectid = null;
						Integer activityobjectid = null;
						//Integer opputunityparentobjectid = null;
						//boolean opportunity = false;
						boolean project = false;
						boolean country = false;
						boolean location = false;
						boolean site = false;
						boolean activity = false;
						boolean wbs = false;
					/*	boolean dttactivityidcheck = false;
						boolean mpcallnumbercheck = false;
						boolean hqtechidcheck = false;
						boolean rutechidcheck = false;
						boolean notificationidcheck = false;
						boolean serviceorderidcheck = false;*/
						boolean turbinetypecheck = false;
						boolean turbinetypecodecheck = false;
						List<ActivityCode> turbinetypecodecreatelist = new ArrayList<ActivityCode>();
						String key = entry.getKey();
						Object value = entry.getValue();
						if (key.contains("Country_Id")) {
							countryid = (String) value;

							// System.out.println(countryid);

						}
						if (key.contains("Location_Shore")) {
							locationshore = (String) value;

							// System.out.println(locationshore);
						}
						if (key.contains("Activity_Name")) {
							activityname = (String) value;

							// System.out.println(projectnumber);
						}
						if (key.contains("Project_Number")) {
							projectnumber = (String) value;

							// System.out.println(projectnumber);
						}

						if (key.contains("Source_System")) {
							sourcesystem = (String) value;

							// System.out.println(sourcesystem);
						}
						/*if (key.contains("Work_Type")) {
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

							 System.out.println(worktypestring); 
						}*/

						/*if (key.contains("SAP_Parent_Object_ID")) {
							sapparentobjectid = (String) (value);

							 System.out.println(sapparentobjectid); 
						}*/

						if (key.contains("Site_Id")) {
							siteid = (String) (value);

							/* System.out.println(siteid); */
						}
						if (key.contains("DTT_Activity_Object_ID")) {
							dttactivityid = (String) (value);
							recordlist.add(dttactivityid);
							/* System.out.println(siteid); */
						}
						if (key.contains("Turbine_Name")) {
							turbinename = (String) (value);

							/* System.out.println(siteid); */
						}
						if (key.contains("Turbine_Type")) {
							turbinetypename = (String) (value);
							// turbinetype = turbinetypename.substring(4, 11);
							turbinetype = turbinetypename;
							System.out.println(turbinetype);
						}
						if (key.contains("HQ_TECH_ID_NUMBER")) {
							hqtechid = (String) (value);

							/* System.out.println(siteid); */
						}
						/*
						 * if (key.contains(DTTConstants.STARTDATE)) { startdate
						 * = (XMLGregorianCalendar) (value);
						 * 
						 * System.out.println(siteid); }
						 */
						/*if (key.contains(DTTConstants.MP_CALL_NUMBER)) {
							mpcallnumber = (String) (value);

							 System.out.println(siteid); 
						}
						if (key.contains(DTTConstants.NOTIFICATION_ID)) {
							notificationid = (String) (value);

							 System.out.println(siteid); 
						}
						if (key.contains(DTTConstants.SERVICE_ORDER_ID)) {
							serviceorderid = (String) (value);

							 System.out.println(siteid); 
						}
						if (key.contains(DTTConstants.RU_TECH_ID_NUMBER)) {
							rutechid = (String) (value);

							 System.out.println(siteid); 
						}*/

						if (countryid != null && sourcesystem != null
								&& activityname != null
								&& projectnumber != null
								&& locationshore != null
								&& dttactivityid != null && turbinename != null
								&& hqtechid != null  && siteid!=null  && turbinetype!=null) {
							ListIterator<EPS> epsread = epss.listIterator();
							while (epsread.hasNext()) {

								EPS epsreadelement = epsread.next();
								if (epsreadelement.getId().equals(countryid)) {

									country = true;
									countryobjectid = epsreadelement
											.getObjectId();
									countryparentobjecctid = epsreadelement
											.getParentObjectId();

									// System.out.print(countryobjectid);

								}
							}

							List<EPS> epsss1 = new ArrayList<EPS>();
							if (countryobjectid != null) {
								// System.out.println(countryobjectid);

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

									}
									// System.out.println("locationshore "+
									// locationshore);
									if (siteid != null) {
										ListIterator<EPS> projectepsread = epsss1
												.listIterator();

										while (projectepsread.hasNext()) {
											EPS projectepsreadelement = projectepsread
													.next();

											// System.out.print(projectepsreadelement.getId()
											// +
											// " "+projectepsreadelement.getParentObjectId().getValue()
											// );
											if (projectepsreadelement.getId()
													.equals(locationshore)) {

												location = true;
												// System.out.print(projectepsreadelement.getName());
												locationshoreobjectid = projectepsreadelement
														.getObjectId();

											}
										}

										List<EPS> epsss2 = new ArrayList<EPS>();
										if (locationshoreobjectid != null) {
											// System.out.println(countryobjectid);

											epsss2 = epsclient
													.readWSProjectEPS(locationshoreobjectid);

										}
										ListIterator<EPS> prolocationepsread = epsss2
												.listIterator();
										while (prolocationepsread.hasNext()) {
											EPS projlocationepsreadelement = prolocationepsread
													.next();

											/*
											 * System.out.print(
											 * projlocationepsreadelement
											 * .getId() +
											 * " "+projlocationepsreadelement.
											 * getParentObjectId().getValue() );
											 */
											if (projlocationepsreadelement
													.getId().equals(siteid)) {

												site = true;
												// System.out.print(projectepsreadelement.getName());
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
												 * System.out.print(
												 * projectidreadelement .getId()
												 * + " "+projectidreadelement
												 * .getParentEPSObjectId());
												 */

												if (projectidreadelement
														.getId().equals(
																projectnumber)) {
													project = true;
													// System.out.print(projectidreadelement.getName());
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
													 * System.out.print(
													 * projectidreadelement
													 * .getName());
													 */

													wbsobjectid = wbsidreadelement
															.getObjectId();

												}
											}
										}
										List<Activity> readactivity = new ArrayList<Activity>();
										PrimaveraActivity activityreadele = new PrimaveraActivity();
										if (wbsobjectid != 0) {
											readactivity = activityreadele
													.readActivities(wbsobjectid);
										}
										if (readactivity.size() != 0) {
											ListIterator<Activity> Activityread = readactivity
													.listIterator();
											Activity activityidreadelement = new Activity();
											while (Activityread.hasNext()) {
												activityidreadelement = Activityread
														.next();

												/*
												 * System.out.print(
												 * activityidreadelement
												 * .getObjectId() +
												 * " "+activityidreadelement
												 * .getName());
												 */

												if (activityidreadelement
														.getId().equals(
																dttactivityid)
														|| activityidreadelement
																.getId()
																.equals(dttactivityid
																		.substring(
																				0,
																				dttactivityid
																						.length() - 1))) {
													activity = true;
													/*
													 * System.out.print(
													 * projectidreadelement
													 * .getName());
													 */

													activityobjectid = activityidreadelement
															.getObjectId();
													activityobject = activityidreadelement
															.getWBSObjectId();
													System.out
															.print(activityobjectid);

													System.out
															.print(activityobject
																	.getValue());

												}
											}
										}
										List<ActivityCode> readactivitycode = new ArrayList<ActivityCode>();

										readactivitycode = activitycode
												.readActivityCode();

										if (readactivitycode.size() != 0) {
											if (!turbinetype.equals("null")) {
												// customernamesubstring=customername.substring(0,3);

												ListIterator<ActivityCode> activitycodereadid = readactivitycode
														.listIterator();
												while (activitycodereadid
														.hasNext()) {
													ActivityCode activitycodereadelement = activitycodereadid
															.next();

													if (activitycodereadelement
															.getCodeTypeObjectId()
															.equals(205)) {
														/*
														 * //logger.info(
														 * "activitycodereadelement getCodeTypeName()"
														 * +
														 * activitycodereadelement
														 * .getCodeTypeName());
														 * //logger.info(
														 * "activitycodereadelement getCodeValue()"
														 * +
														 * activitycodereadelement
														 * .getCodeValue());
														 */
														if (activitycodereadelement
																.getDescription()
																.equals(turbinetype)) {

															turbinetypecodecheck = true;
															/*
															 * //logger.info(
															 * "turbinetypecodecheck "
															 * +
															 * turbinetypecodecheck
															 * );
															 */
														}

													}

												}

												/*
												 * if(projectcodereadelement.
												 * getCodeValue().equals
												 * (customernamesubstring)){
												 * customernamesubstring
												 * =customername.substring(0,4);
												 * }
												 */
												/*
												 * //logger.info(
												 * activitycodereadelement
												 * .getCodeTypeName() + "" +
												 * activitycodereadelement
												 * .getCodeTypeObjectId() + " "
												 * + activitycodereadelement
												 * .getParentObjectId
												 * ().getValue() + " " +
												 * activitycodereadelement
												 * .getCodeValue() + " " +
												 * activitycodereadelement
												 * .getObjectId() + " " +
												 * activitycodereadelement
												 * .getDescription());
												 */
											}
											/*
											 * //logger.info("customernamecodecheck"
											 * +customernamecodecheck);
											 * //logger.
											 * info("customerifa"+customerifa);
											 * //logger.info("customername"+
											 * customername);
											 */
											if (turbinetypecodecheck) {
											} else {
												// logger.info("in else turbine type"+turbinetypecodecheck);
												ActivityCode turbinetypecodecreate = new ActivityCode();
												turbinetypecodecreate
														.setCodeTypeObjectId(205);
												turbinetypecodecreate
														.setCodeValue(hqtechid);
												turbinetypecodecreate
														.setDescription(turbinetype);

												turbinetypecodecreatelist
														.add(turbinetypecodecreate);
												/*
												 * //logger.info(
												 * "End of else customername"
												 * +customernamecodecreate
												 * .getCodeTypeObjectId());
												 */

											}

										}

										/*
										 * //logger.info(
										 * "customernamecodecreatelist "
										 * +customernamecodecreatelist.size());
										 */
										if (turbinetypecodecreatelist.size() != 0) {
											/*
											 * //logger.info(
											 * "customernamecodecreatelist inside "
											 * +
											 * customernamecodecreatelist.size()
											 * );
											 */
											activitycode
													.createActivityCode(turbinetypecodecreatelist);
										}

										PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
										List<Role> readroledetails = new ArrayList<Role>();

										readroledetails = assignmenttype
												.readRole();
										ListIterator<Role> roleread = readroledetails
												.listIterator();
										while (roleread.hasNext()) {

											Role rolereadelement = roleread
													.next();

											System.out
													.print(rolereadelement
															.getName()
															+ ""
															+ rolereadelement
																	.getId()
															+ ""
															+ rolereadelement
																	.getParentObjectId()
																	.getValue());
											activityparentobject = rolereadelement
													.getParentObjectId();

										}

										if (project) {
											if (wbs) {
												wbsobject = activityreadele
														.constructWBSObject(wbsobjectid);
												wbsobject.setValue(wbsobjectid);
												if (!dttactivityid
														.endsWith("D")) {

													ListIterator<ActivityCode> activitycodereadcheck = readactivitycode
															.listIterator();
													while (activitycodereadcheck
															.hasNext()) {

														ActivityCode activitycodereadelement = activitycodereadcheck
																.next();

														if (activitycodereadelement
																.getDescription()
																.equals(turbinetype)) {
															activitycodeobjectid = activitycodereadelement
																	.getObjectId();
															System.out
																	.println("activitycodeobjectid"
																			+ activitycodeobjectid);

														}

													}
													List<ActivityCodeAssignment> readactivitycodeassignment = new ArrayList<ActivityCodeAssignment>();
													if (activityobjectid != null) {
														readactivitycodeassignment = activitycode
																.readActivityCodeAssignment(activityobjectid
																		.toString());
													}
													if (readactivitycodeassignment
															.size() != 0) {
														ListIterator<ActivityCodeAssignment> activitycodeassignmentread = readactivitycodeassignment
																.listIterator();
														while (activitycodeassignmentread
																.hasNext()) {

															ActivityCodeAssignment activitycodeassignmentreadelement = activitycodeassignmentread
																	.next();

															if (activitycodeassignmentreadelement
																	.getActivityCodeObjectId()
																	.equals(activitycodeobjectid)
																	&& activitycodeassignmentreadelement
																			.getActivityObjectId()
																			.equals(activityobjectid)) {
																turbinetypecheck = true;
															}
														}
													}

													if (activity) {
														if (turbinetypecheck) {

														} else {
															if (turbinetype != null
																	|| turbinetype != "") {
																if (!activitycodecheck
																		.contains(dttactivityid
																				.toString())) {
																	activitycodecheck
																			.add(dttactivityid
																					.toString());

																	acitivitycodecreate
																			.setActivityCodeObjectId(activitycodeobjectid);

																	acitivitycodecreate
																			.setActivityObjectId(activityobjectid);
																	activitycodedetails
																			.add(acitivitycodecreate);
																}
															}
														}

													}

												}

											}
										}
										logger.info("activitycodedetails size"
												+ activitycodedetails.size());
									}

								}

							}

						}

					}
				}
			}

			if (activitycodedetails != null) {

				ListIterator<ActivityCodeAssignment> read = activitycodedetails
						.listIterator();
				while (read.hasNext()) {

					ActivityCodeAssignment projreadelement = read.next();

					// System.out.print(countryobjectid);

					/*
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 */
					/*
					 * System.out.println(projreadelement.getText());
					 * System.out.println(projreadelement.getUDFTypeObjectId());
					 * System.out.println(projreadelement.getForeignObjectId());
					 */

				}

				// System.out.println(projectdata);
			}

			System.out.println("activitycodedetails.size"
					+ activitycodedetails.size());

			// activitytype.createActivities(createactivity);
			// activitycodetype.createActivityCodeAssignment(activitycodedetails);
			// udfs.CreateUDFValue(activityudfdetails);
			/*
			 * UpdateProcessRecordsDAO processed=new UpdateProcessRecordsDAO();
			 * processed.updaterecords(DTTConstants.ACTIVITY, "Y",
			 * recordlist,"Success", "DTT_ACTIVITY_OBJECT_ID");
			 */
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return activitycodedetails;
	}

	public List<ActivityCodeAssignment> checkAndUpdateActivityCodes()
			throws Exception {

		List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();
		try {

			activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getActivitytMasterDAO();
			activitymasterdata = new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getActivitymasterdata();

			PrimaveraActivity activitytype = new PrimaveraActivity();

			PrimaveraEPS epsclient = new PrimaveraEPS();

			PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			List<ActivityCodeAssignment> readactivitycodeassignment = new ArrayList<ActivityCodeAssignment>();
			PrimaveraActivityCode activitycode = new PrimaveraActivityCode();

			/*
			 * List<ActivityCode> readactivitycode = new
			 * ArrayList<ActivityCode>(); readactivitycode =
			 * activitycode.readActivityCode();
			 * 
			 * ListIterator<ActivityCode> activitycoderead = readactivitycode
			 * .listIterator(); while (activitycoderead.hasNext()) {
			 * 
			 * ActivityCode activitycodereadelement = activitycoderead.next();
			 * 
			 * System.out.println(activitycodereadelement.getCodeTypeName() + ""
			 * + activitycodereadelement.getCodeTypeObjectId() + " " +
			 * activitycodereadelement.getParentObjectId() + " " +
			 * activitycodereadelement.getCodeValue() + " " +
			 * activitycodereadelement.getObjectId()); }
			 */

			List activitycheck = new ArrayList();
			List activitycodecheck = new ArrayList();
			List activityudfcheck = new ArrayList();
			List serviceudfcheck = new ArrayList();
			List notificationudfcheck = new ArrayList();

			List<Activity> createactivity = new ArrayList<Activity>();

			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String turbinename = null;
				String turbinetype = null;
				String turbinetypename = null;

				String hqtechid = null;
				String rutechid = null;
				String notificationid = "";
				String serviceorderid = "";
				XMLGregorianCalendar startdate = null;

				String worktypestring = null;
				String activityname = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				/*
				 * Date contractstartdate = null; Date contractenddate = null;
				 */
				String customerifa = null;
				String customername = null;
				String probability = null;
				Activity activitycreate = new Activity();

				ActivityCodeAssignment acitivitycodecreate = new ActivityCodeAssignment();

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					UDFValue activityudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					int wbsobjectid = 0;
					JAXBElement<Integer> wbsobject = null;

					JAXBElement<Integer> wbsjaxbElement = null;
					JAXBElement<Integer> activityobject = null;
					JAXBElement<Integer> activityparentobject = null;
					String mpcallnumber = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;
					Integer activitycodeobjectid = null;
					Integer activityobjectid = null;
					Integer primaveraturbinetype = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean activity = false;
					boolean wbs = false;
					boolean dttactivityidcheck = false;
					boolean mpcallnumbercheck = false;
					boolean hqtechidcheck = false;
					boolean rutechidcheck = false;
					boolean notificationidcheck = false;
					boolean serviceorderidcheck = false;
					boolean turbinetypecheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains("Country_Id")) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains("Activity_Name")) {
						activityname = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					/*if (key.contains("Work_Type")) {
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

						 System.out.println(worktypestring); 
					}*/

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}

					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("DTT_Activity_Object_ID")) {
						dttactivityid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Turbine_Name")) {
						turbinename = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Turbine_Type")) {
						turbinetypename = (String) (value);
						turbinetype = turbinetypename.substring(4,
								turbinetypename.length());
						System.out.println(turbinetype);
					}
					if (key.contains("HQ_TECH_ID_NUMBER")) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}
					/*
					 * if (key.contains(DTTConstants.STARTDATE)) { startdate =
					 * (XMLGregorianCalendar) (value);
					 * 
					 * System.out.println(siteid); }
					 */
					/*if (key.contains(DTTConstants.MP_CALL_NUMBER)) {
						mpcallnumber = (String) (value);

						 System.out.println(siteid); 
					}
					if (key.contains(DTTConstants.NOTIFICATION_ID)) {
						notificationid = (String) (value);

						 System.out.println(siteid); 
					}
					if (key.contains(DTTConstants.SERVICE_ORDER_ID)) {
						serviceorderid = (String) (value);

						 System.out.println(siteid); 
					}
					if (key.contains(DTTConstants.RU_TECH_ID_NUMBER)) {
						rutechid = (String) (value);

						 System.out.println(siteid); 
					}*/

					if (countryid != null && sourcesystem != null
							&& activityname != null && projectnumber != null
							&& locationshore != null && dttactivityid != null
							&& turbinename != null && hqtechid != null) {
						ListIterator<EPS> epsread = epss.listIterator();
						while (epsread.hasNext()) {

							EPS epsreadelement = epsread.next();
							if (epsreadelement.getId().equals(countryid)) {

								country = true;
								countryobjectid = epsreadelement.getObjectId();
								countryparentobjecctid = epsreadelement
										.getParentObjectId();

								// System.out.print(countryobjectid);

							}
						}

						List<EPS> epsss1 = new ArrayList<EPS>();
						if (countryobjectid != null) {
							// System.out.println(countryobjectid);

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

								}
								// System.out.println("locationshore "+
								// locationshore);
								if (siteid != null) {
									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();

										// System.out.print(projectepsreadelement.getId()
										// +
										// " "+projectepsreadelement.getParentObjectId().getValue()
										// );
										if (projectepsreadelement.getId()
												.equals(locationshore)) {

											location = true;
											// System.out.print(projectepsreadelement.getName());
											locationshoreobjectid = projectepsreadelement
													.getObjectId();

										}
									}

									List<EPS> epsss2 = new ArrayList<EPS>();
									if (locationshoreobjectid != null) {
										// System.out.println(countryobjectid);

										epsss2 = epsclient
												.readWSProjectEPS(locationshoreobjectid);

									}
									ListIterator<EPS> prolocationepsread = epsss2
											.listIterator();
									while (prolocationepsread.hasNext()) {
										EPS projlocationepsreadelement = prolocationepsread
												.next();

										/*
										 * System.out.print(
										 * projlocationepsreadelement.getId() +
										 * " "+projlocationepsreadelement.
										 * getParentObjectId().getValue() );
										 */
										if (projlocationepsreadelement.getId()
												.equals(siteid)) {

											site = true;
											// System.out.print(projectepsreadelement.getName());
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
											 * System.out.print(projectidreadelement
											 * .getId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												// System.out.print(projectidreadelement.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}
									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();
									if (projectobjectid != 0) {
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
												 * System.out.print(
												 * projectidreadelement
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
												 * System.out.print("InLoop" +
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
												.readActivities(wbsobjectid);
									}
									if (readactivity.size() != 0) {
										ListIterator<Activity> Activityread = readactivity
												.listIterator();
										Activity activityidreadelement = new Activity();
										while (Activityread.hasNext()) {
											activityidreadelement = Activityread
													.next();

											/*
											 * System.out.print(
											 * activityidreadelement
											 * .getObjectId() +
											 * " "+activityidreadelement
											 * .getName());
											 */

											if (activityidreadelement.getId()
													.equals(dttactivityid)
													|| activityidreadelement
															.getId()
															.equals(dttactivityid
																	.substring(
																			0,
																			dttactivityid
																					.length() - 1))) {
												activity = true;
												/*
												 * System.out.print(
												 * projectidreadelement
												 * .getName());
												 */

												activityobjectid = activityidreadelement
														.getObjectId();
												activityobject = activityidreadelement
														.getWBSObjectId();
												System.out
														.print(activityobjectid);

												System.out.print(activityobject
														.getValue());

											}
										}
									}

									PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
									List<Role> readroledetails = new ArrayList<Role>();

									readroledetails = assignmenttype.readRole();
									ListIterator<Role> roleread = readroledetails
											.listIterator();
									while (roleread.hasNext()) {

										Role rolereadelement = roleread.next();

										System.out.print(rolereadelement
												.getName()
												+ ""
												+ rolereadelement.getId()
												+ ""
												+ rolereadelement
														.getParentObjectId()
														.getValue());
										activityparentobject = rolereadelement
												.getParentObjectId();

									}
									List<ActivityCode> readactivitycode = new ArrayList<ActivityCode>();

									readactivitycode = activitycode
											.readActivityCode();

									if (project) {
										if (wbs) {
											wbsobject = activityreadele
													.constructWBSObject(wbsobjectid);
											wbsobject.setValue(wbsobjectid);
											if (!dttactivityid.endsWith("D")) {

												ListIterator<ActivityCode> activitycodereadcheck = readactivitycode
														.listIterator();
												while (activitycodereadcheck
														.hasNext()) {

													ActivityCode activitycodereadelement = activitycodereadcheck
															.next();

													if (activitycodereadelement
															.getDescription()
															.equals(turbinetype)) {
														activitycodeobjectid = activitycodereadelement
																.getObjectId();
														System.out
																.println("activitycodeobjectid"
																		+ activitycodeobjectid);

													}

												}
												if (activityobjectid != null) {
													readactivitycodeassignment = activitycode
															.readActivityCodeAssignment(activityobjectid
																	.toString());
												}
												if (readactivitycodeassignment
														.size() != 0) {
													ListIterator<ActivityCodeAssignment> activitycodeassignmentread = readactivitycodeassignment
															.listIterator();
													while (activitycodeassignmentread
															.hasNext()) {

														ActivityCodeAssignment activitycodeassignmentreadelement = activitycodeassignmentread
																.next();

														if (activitycodeassignmentreadelement
																.getActivityObjectId()
																.equals(activityobjectid)) {
															primaveraturbinetype = activitycodeassignmentreadelement
																	.getActivityCodeObjectId();

															turbinetypecheck = true;
														}
													}
												}

												if (activity) {
													if (turbinetypecheck) {
														if (!primaveraturbinetype
																.equals(activitycodeobjectid)) {
															if (turbinetype != null
																	|| turbinetype != "") {
																if (!activitycodecheck
																		.contains(dttactivityid
																				.toString())) {
																	activitycodecheck
																			.add(dttactivityid
																					.toString());

																	acitivitycodecreate
																			.setActivityCodeObjectId(activitycodeobjectid);

																	acitivitycodecreate
																			.setActivityObjectId(activityobjectid);
																	activitycodedetails
																			.add(acitivitycodecreate);
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

			if (activitycodedetails != null) {

				ListIterator<ActivityCodeAssignment> read = activitycodedetails
						.listIterator();
				while (read.hasNext()) {

					ActivityCodeAssignment projreadelement = read.next();

					// System.out.print(countryobjectid);

					/*
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 */
					/*
					 * System.out.println(projreadelement.getText());
					 * System.out.println(projreadelement.getUDFTypeObjectId());
					 * System.out.println(projreadelement.getForeignObjectId());
					 */

				}

				// System.out.println(projectdata);
			}

			System.out.println("activitycodedetails.size"
					+ activitycodedetails.size());

			// activitytype.createActivities(createactivity);
			// activitycodetype.createActivityCodeAssignment(activitycodedetails);
			// udfs.CreateUDFValue(activityudfdetails);
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return activitycodedetails;
	}

	@SuppressWarnings("unchecked")
	public List<UDFValue> checkAndCreateActivityUDF() throws Exception {
		List<UDFValue> activityudfdetails = new ArrayList<UDFValue>();
		try {
			activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getActivitytMasterDAO();
			activitymasterdata = new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getActivitymasterdata();

			PrimaveraActivity activitytype = new PrimaveraActivity();

			PrimaveraEPS epsclient = new PrimaveraEPS();

			PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();
			List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			List<String> recordlist = new ArrayList<String>();

			//List activitycheck = new ArrayList();
			//List activitycodecheck = new ArrayList();
			List activityudfcheck = new ArrayList();
			List serviceudfcheck = new ArrayList();
			List mpcalludfcheck = new ArrayList();
			List notificationudfcheck = new ArrayList();
			List notificationstatusudfcheck = new ArrayList();
			List orderstatusudfcheck = new ArrayList();
			JAXBElement<XMLGregorianCalendar> activitystartdate = null;
			JAXBElement<Double> actualhoursdouble = null;
			List<Activity> createactivity = new ArrayList<Activity>();

			List<UDFValue> readudf = new ArrayList<UDFValue>();
			PrimaveraProjectUDF udfs = new PrimaveraProjectUDF();

			/*
			 * readudf = udfs.readUDFValueUDF(); ListIterator<UDFValue>
			 * wbsudfidread = readudf.listIterator();
			 * 
			 * 
			 * while (wbsudfidread.hasNext()) { UDFValue wbsudfidreadelement =
			 * wbsudfidread.next();
			 * //logger.info("getUDFTypeObjectId"+wbsudfidreadelement
			 * .getUDFTypeObjectId());
			 * //logger.info("getCodeValue"+wbsudfidreadelement
			 * .getUDFTypeSubjectArea());
			 * //logger.info("getUDFTypeTitle"+wbsudfidreadelement
			 * .getUDFTypeTitle());
			 * //logger.info("getCodeValue"+wbsudfidreadelement
			 * .getDescription()); if
			 * (wbsudfidreadelement.getUDFTypeObjectId().equals(159)) {
			 * System.out.println("UDF Code" +
			 * wbsudfidreadelement.getStartDate()); activitystartdate =
			 * wbsudfidreadelement.getStartDate();
			 * 
			 * }
			 * 
			 * }
			 */
			List<UDFType> readudftype = new ArrayList<UDFType>();
			readudftype = udfs.readProjectUDF();
			//ListIterator<UDFType> wbsudftypeidread = readudftype.listIterator();

			/*while (wbsudftypeidread.hasNext()) {
				UDFType wbsudftypeidreadelement = wbsudftypeidread.next();
				System.out.println(wbsudftypeidreadelement.getDataType() + " "
						+ wbsudftypeidreadelement.getObjectId() + " "
						+ wbsudftypeidreadelement.getSubjectArea() + " "
						+ wbsudftypeidreadelement.getTitle());
				// logger.info(wbsudftypeidreadelement.getDataType()+" "+wbsudftypeidreadelement.getObjectId()+" "+wbsudftypeidreadelement.getSubjectArea()+" "+wbsudftypeidreadelement.getTitle());

			}*/

			logger.info("activitymasterdata" + activitymasterdata.size());
			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				//int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String turbinename = null;
				//String turbinetype = null;
				//String turbinetypename = null;
				String actualhours = null;
				String hqtechid = null;
				String rutechid = null;
				String notificationid = "";
				String notificationstatus = "";
				String serviceorderid = "";
				String orderstatus = "";
				String startdat = null;
				XMLGregorianCalendar startdate = null;
				ObjectFactory objectfactory = new ObjectFactory();
				//String worktypestring = null;
				String activityname = null;
				String projectnumber = null;
				//String projectname = null;
				//String sapparentobjectid = null;
				/*
				 * Date contractstartdate = null; Date contractenddate = null;
				 */
				//String customerifa = null;
				//String customername = null;
				//String probability = null;
				//Activity activitycreate = new Activity();
				String mainplanid = null;
			//	ActivityCodeAssignment acitivitycodecreate = new ActivityCodeAssignment();

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					UDFValue activityudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					int wbsobjectid = 0;
					JAXBElement<Integer> wbsobject = null;

					JAXBElement<Integer> wbsjaxbElement = null;
					JAXBElement<Integer> activityobject = null;
					JAXBElement<Integer> activityparentobject = null;
					String mpcallnumber = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					//String opputunityobjectid = null;
					Integer projectobjectid = null;
					//Integer activitycodeobjectid = null;
					Integer activityobjectid = null;
					//Integer opputunityparentobjectid = null;
					//boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean activity = false;
					boolean wbs = false;
					boolean dttactivityidcheck = false;
					boolean mpcallnumbercheck = false;
					boolean mainplanidcheck = false;
					boolean hqtechidcheck = false;
					boolean actualhourscheck = false;
					boolean rutechidcheck = false;
					boolean notificationidcheck = false;
					boolean notificationstatuscheck = false;
					boolean serviceorderidcheck = false;
					boolean orderstatuscheck = false;
					//boolean turbinetypecheck = false;
					boolean startdatecheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains("Country_Id")) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Actual_Hours")) {
						actualhours = (String) value;
						// logger.info("Actual Hours From DTT "+actualhours);
						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains("Activity_Name")) {
						activityname = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					/*if (key.contains("Work_Type")) {
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

						 System.out.println(worktypestring); 
					}*/

					/*if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						 System.out.println(sapparentobjectid); 
					}*/

					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("DTT_Activity_Object_ID")) {
						dttactivityid = (String) (value);
						if (!(dttactivityid.endsWith("D"))) {
							recordlist.add(dttactivityid);
						}
						/* System.out.println(siteid); */
					}
					if (key.contains("Turbine_Name")) {
						turbinename = (String) (value);

						/* System.out.println(siteid); */
					}
					/*if (key.contains("Turbine_Type")) {
						turbinetypename = (String) (value);
						turbinetype = turbinetypename.substring(4,
								turbinetypename.length());
						System.out.println(turbinetype);
					}*/
					if (key.contains("HQ_TECH_ID_NUMBER")) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.STARTDATE)) {
						startdat = (String) (value);
						{
							if (startdat != null)
								startdate = toXMLGregorianCalendarWithoutTimeStamp(startdat);

						}
						System.out.println(startdate);
					}

					if (key.contains(DTTConstants.MP_CALL_NUMBER)) {
						mpcallnumber = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Maintenance_Plan_ID")) {
						mainplanid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.NOTIFICATION_ID)) {
						notificationid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.NOTIFICATION_STATUS)) {
						notificationstatus = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.SERVICE_ORDER_ID)) {
						serviceorderid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.ORDER_STATUS)) {
						orderstatus = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.RU_TECH_ID_NUMBER)) {
						rutechid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (countryid != null && sourcesystem != null
							&& activityname != null && projectnumber != null
							&& locationshore != null && dttactivityid != null
							&& turbinename != null && hqtechid != null && siteid!=null) {

						ListIterator<EPS> epsread = epss.listIterator();
						while (epsread.hasNext()) {

							EPS epsreadelement = epsread.next();
							if (epsreadelement.getId().equals(countryid)) {

								country = true;
								countryobjectid = epsreadelement.getObjectId();
								countryparentobjecctid = epsreadelement
										.getParentObjectId();

								// System.out.print(countryobjectid);

							}
						}

						List<EPS> epsss1 = new ArrayList<EPS>();
						if (countryobjectid != null) {
							// System.out.println(countryobjectid);

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
								// System.out.println("locationshore "+
								// locationshore);
								if (siteid != null) {
									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();

										// System.out.print(projectepsreadelement.getId()
										// +
										// " "+projectepsreadelement.getParentObjectId().getValue()
										// );
										if (projectepsreadelement.getId()
												.equals(locationshore)) {

											location = true;
											// System.out.print(projectepsreadelement.getName());
											locationshoreobjectid = projectepsreadelement
													.getObjectId();

										}
									}

									List<EPS> epsss2 = new ArrayList<EPS>();
									if (locationshoreobjectid != null) {
										// System.out.println(countryobjectid);

										epsss2 = epsclient
												.readWSProjectEPS(locationshoreobjectid);

									}
									ListIterator<EPS> prolocationepsread = epsss2
											.listIterator();
									while (prolocationepsread.hasNext()) {
										EPS projlocationepsreadelement = prolocationepsread
												.next();

										/*
										 * System.out.print(
										 * projlocationepsreadelement.getId() +
										 * " "+projlocationepsreadelement.
										 * getParentObjectId().getValue() );
										 */
										if (projlocationepsreadelement.getId()
												.equals(siteid)) {

											site = true;
											// System.out.print(projectepsreadelement.getName());
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
											 * System.out.print(projectidreadelement
											 * .getId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												// System.out.print(projectidreadelement.getName());
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
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										if (readwbs.size() != 0)
											while (wbsidread.hasNext()) {
												WBS wbsidreadelement = wbsidread
														.next();

												if (wbsidreadelement.getName()
														.equals(turbinename)) {
													wbs = true;
													/*
													 * System.out.print(
													 * projectidreadelement
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
													 * System.out.print("InLoop"
													 * +
													 * wbsjaxbElement.getValue(
													 * ));
													 */
													// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

												}
											}
										List<Activity> readactivity = new ArrayList<Activity>();
										PrimaveraActivity activityreadele = new PrimaveraActivity();
										if (wbsobjectid != 0) {
											readactivity = activityreadele
													.readActivities(wbsobjectid);
										}
										if (readactivity.size() != 0) {
											ListIterator<Activity> Activityread = readactivity
													.listIterator();
											Activity activityidreadelement = new Activity();
											while (Activityread.hasNext()) {
												activityidreadelement = Activityread
														.next();

												/*
												 * System.out.print(
												 * activityidreadelement
												 * .getObjectId() +
												 * " "+activityidreadelement
												 * .getName());
												 */

												if (activityidreadelement
														.getId().equals(
																dttactivityid)
														|| activityidreadelement
																.getId()
																.equals(dttactivityid
																		.substring(
																				0,
																				dttactivityid
																						.length() - 1))) {
													activity = true;
													/*
													 * System.out.print(
													 * projectidreadelement
													 * .getName());
													 */

													activityobjectid = activityidreadelement
															.getObjectId();
													activityobject = activityidreadelement
															.getWBSObjectId();
													System.out
															.print(activityobjectid);

													System.out
															.print(activityobject
																	.getValue());

												}
											}
										}

										PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
										List<Role> readroledetails = new ArrayList<Role>();

										readroledetails = assignmenttype
												.readRole();
										ListIterator<Role> roleread = readroledetails
												.listIterator();
										while (roleread.hasNext()) {

											Role rolereadelement = roleread
													.next();

											System.out
													.print(rolereadelement
															.getName()
															+ ""
															+ rolereadelement
																	.getId()
															+ ""
															+ rolereadelement
																	.getParentObjectId()
																	.getValue());
											activityparentobject = rolereadelement
													.getParentObjectId();

										}

										if (project) {
											if (wbs) {
												wbsobject = activityreadele
														.constructWBSObject(wbsobjectid);
												wbsobject.setValue(wbsobjectid);
												if (!dttactivityid
														.endsWith("D")) {
													if (activity) {

														// logger.info("activity"+activity);
														if (activityobjectid != null) {
															readudf = udfs
																	.readUDFValue(activityobjectid);
														}
														if (readudf.size() != 0) {
															ListIterator<UDFValue> wbsudfidreadchcek = readudf
																	.listIterator();

															while (wbsudfidreadchcek
																	.hasNext()) {
																UDFValue wbsudfidreadelement = wbsudfidreadchcek
																		.next();
																if (wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(159)) {
																	/*
																	 * System.out
																	 * .println(
																	 * "UDF Code"
																	 * +
																	 * wbsudfidreadelement
																	 * .
																	 * getStartDate
																	 * ());
																	 */
																	activitystartdate = wbsudfidreadelement
																			.getStartDate();

																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(135)) {
																	dttactivityidcheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(173)) {
																	startdatecheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(140)) {
																	hqtechidcheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(141)) {
																	rutechidcheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(139)) {
																	mpcallnumbercheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(165)) {
																	mainplanidcheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(137)) {
																	notificationidcheck = true;
																}
																/*
																 * if (
																 * wbsudfidreadelement
																 * .
																 * getForeignObjectId
																 * () .equals(
																 * activityobjectid
																 * ) &&
																 * wbsudfidreadelement
																 * .
																 * getUDFTypeObjectId
																 * ()
																 * .equals(183))
																 * {
																 * notificationstatuscheck
																 * = true; }
																 */

																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(247)) {
																	notificationstatuscheck = true;
																}
																/*
																 * if (
																 * wbsudfidreadelement
																 * .
																 * getForeignObjectId
																 * () .equals(
																 * activityobjectid
																 * ) &&
																 * wbsudfidreadelement
																 * .
																 * getUDFTypeObjectId
																 * ()
																 * .equals(184))
																 * {
																 * orderstatuscheck
																 * = true; }
																 */
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(248)) {
																	orderstatuscheck = true;
																}
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(138)) {
																	serviceorderidcheck = true;
																}
																/*
																 * if (
																 * wbsudfidreadelement
																 * .
																 * getForeignObjectId
																 * () .equals(
																 * activityobjectid
																 * ) &&
																 * wbsudfidreadelement
																 * .
																 * getUDFTypeObjectId
																 * ()
																 * .equals(185))
																 * {
																 * actualhourscheck
																 * = true; }
																 */
																if (wbsudfidreadelement
																		.getForeignObjectId()
																		.equals(activityobjectid)
																		&& wbsudfidreadelement
																				.getUDFTypeObjectId()
																				.equals(249)) {
																	actualhourscheck = true;
																}
															}

														}
														// DTT Activity ID

														if (!activityudfcheck
																.contains(dttactivityid
																		.toString())) {
															activityudfcheck
																	.add(dttactivityid
																			.toString());
															if (dttactivityidcheck) {

															} else {
																if (dttactivityid != null) {
																	activityudfassign
																			.setText(dttactivityid);
																	activityudfassign
																			.setUDFTypeObjectId(135);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

															activityudfassign = new UDFValue();
															if (hqtechidcheck) {

															} else {
																if (hqtechid == null
																		|| hqtechid == ""
																		|| hqtechid
																				.equals("0")) {
																	hqtechid = "";

																}
																if (!hqtechid
																		.equals("")) {
																	activityudfassign
																			.setText(hqtechid);
																	activityudfassign
																			.setUDFTypeObjectId(140);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

															activityudfassign = new UDFValue();
															if (actualhourscheck) {

															} else {
																if (actualhours == null
																		|| actualhours == ""
																		|| actualhours
																				.equals("0")) {
																	actualhours = "0";

																	// logger.info("Outside Actual Hours"+actualhours);
																}
																
																	// logger.info("Inside Actual Hours"+actualhours);
																	actualhoursdouble = objectfactory
																			.createUDFValueDouble(Double
																					.parseDouble(actualhours));
																	activityudfassign
																			.setDouble(actualhoursdouble);
																	activityudfassign
																			.setUDFTypeObjectId(249);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																
															}
															activityudfassign = new UDFValue();
															if (rutechidcheck) {

															} else {
																if (rutechid == null
																		|| rutechid == ""
																		|| rutechid
																				.equals("0")) {
																	rutechid = "";

																}
																if (!rutechid
																		.equals("")) {

																	activityudfassign
																			.setText(rutechid);
																	activityudfassign
																			.setUDFTypeObjectId(141);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}
															activityudfassign = new UDFValue();
															if (startdatecheck) {

															} else {
																if (startdat != null)

																{

																	activitystartdate = objectfactory
																			.createUDFValueStartDate(toXMLGregorianCalendarWithoutTimeStamp(startdat));
																	activityudfassign
																			.setStartDate(activitystartdate);
																	activityudfassign
																			.setUDFTypeObjectId(173);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

															activityudfassign = new UDFValue();
															if (mainplanidcheck) {

															} else {
																if (mainplanid == null
																		|| mainplanid == ""
																		|| mainplanid
																				.equals("0")) {
																	mainplanid = "";

																}
																logger.info("mainplanid first "+mainplanid);
																if (!mainplanid
																		.equals("")) {
																	activityudfassign
																			.setText(mainplanid.toString());
																	activityudfassign
																			.setUDFTypeObjectId(165);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																	
																	//logger.info("mainplanid"+mainplanid);
																	
																}
															}

														}

														activityudfassign = new UDFValue();
														if (mpcallnumbercheck) {

														} else {

															if (mpcallnumber == null
																	|| mpcallnumber == ""
																	|| mpcallnumber
																			.equals("0")) {
																mpcallnumber = "";

															}
															logger.info("MP Call first "+mpcallnumber);
															if (!mpcallnumber
																	.equals("")) {
																if (!mpcalludfcheck
																		.contains(activityobjectid)) {
																mpcalludfcheck.add(activityobjectid);
																
																activityudfassign
																		.setText(mpcallnumber);
																activityudfassign
																		.setUDFTypeObjectId(139);
																activityudfassign
																		.setForeignObjectId(activityobjectid);
																activityudfdetails
																		.add(activityudfassign);
															}
														}
															}

														if (notificationidcheck) {

														} else {

															activityudfassign = new UDFValue();
															if (notificationid == null
																	|| notificationid == ""
																	|| notificationid
																			.equals("0")) {
																notificationid = "";

															}

															if (!notificationudfcheck
																	.contains(dttactivityid)) {

																if (!notificationid
																		.equals("")) {

																	notificationudfcheck
																			.add(notificationid);
																	notificationudfcheck
																			.add(dttactivityid);
																	activityudfassign
																			.setText(notificationid);
																	activityudfassign
																			.setUDFTypeObjectId(137);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}
														}

														if (notificationstatuscheck) {

														} else {

															activityudfassign = new UDFValue();
															if (notificationstatus == null
																	|| notificationstatus == ""
																	|| notificationstatus
																			.equals("")) {
																notificationstatus = "";

															}

															if (!notificationstatusudfcheck
																	.contains(dttactivityid)) {

																if (!notificationstatus
																		.equals("")) {

																	notificationstatusudfcheck
																			.add(notificationstatus);
																	notificationstatusudfcheck
																			.add(dttactivityid);
																	activityudfassign
																			.setText(notificationstatus);
																	activityudfassign
																			.setUDFTypeObjectId(247);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}
														}
														if (orderstatuscheck) {

														} else {

															activityudfassign = new UDFValue();
															if (orderstatus == null
																	|| orderstatus == ""
																	|| orderstatus
																			.equals("")) {
																orderstatus = "";

															}

															if (!orderstatusudfcheck
																	.contains(dttactivityid)) {

																if (!orderstatus
																		.equals("")) {

																	orderstatusudfcheck
																			.add(orderstatus);
																	orderstatusudfcheck
																			.add(dttactivityid);
																	activityudfassign
																			.setText(orderstatus);
																	activityudfassign
																			.setUDFTypeObjectId(248);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}
														}

														activityudfassign = new UDFValue();
														if (serviceorderidcheck) {

														} else {
															if (serviceorderid == null
																	|| serviceorderid == ""
																	|| serviceorderid
																			.equals("0")) {
																serviceorderid = "";
																

															}
															if (!serviceudfcheck
																	.contains(dttactivityid)) {
																if (!serviceorderid
																		.equals("")) {
																	serviceudfcheck
																			.add(serviceorderid);
																	serviceudfcheck
																			.add(dttactivityid);

																	activityudfassign
																			.setText(serviceorderid);
																	activityudfassign
																			.setUDFTypeObjectId(138);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}
														}
													}

												}
											}
										}
									}
									logger.info("activityudfdetails size"
											+ activityudfdetails.size());
								}

							}

						}

					}
				}

			}
			if (createactivity != null) {

				ListIterator<Activity> read = createactivity.listIterator();
				while (read.hasNext()) {

					Activity projreadelement = read.next();

					// System.out.print(countryobjectid);

					/*
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 */
					System.out.println(projreadelement.getName());
					System.out.println(projreadelement.getId());

				}

				// System.out.println(projectdata);
			}

			if (activityudfdetails != null) {

				ListIterator<UDFValue> read = activityudfdetails.listIterator();
				while (read.hasNext()) {

					UDFValue projreadelement = read.next();

					// System.out.print(countryobjectid);

					/*
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 */
					/*
					 * System.out.println(projreadelement.getStartDate());
					 * System.out.println(projreadelement.getUDFTypeObjectId());
					 * System.out.println(projreadelement.getForeignObjectId());
					 */

				}

				// System.out.println(projectdata);
			}
			System.out.println("createactivity.size" + createactivity.size());
			System.out.println("activitycodedetails.size"
					+ activitycodedetails.size());
			System.out.println("activityudfdetails.size"
					+ activityudfdetails.size());
			// activitytype.createActivities(createactivity);
			// activitycodetype.createActivityCodeAssignment(activitycodedetails);
			// udfs.createUDFValue(activityudfdetails);
			logger.info("recordlist" + recordlist.size());

			
			  /*UpdateProcessRecordsDAO processed=new UpdateProcessRecordsDAO();
			  processed.updaterecords(DTTConstants.ACTIVITY, "Y",
			  recordlist,"Success", "DTT_ACTIVITY_OBJECT_ID");*/
			 
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return activityudfdetails;
	}

	public List<UDFValue> checkAndUpdateActivityUDF() throws Exception {
		List<UDFValue> activityudfdetails = new ArrayList<UDFValue>();
		try {
			activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getActivitytMasterDAO();
			activitymasterdata = new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getActivitymasterdata();

			PrimaveraActivity activitytype = new PrimaveraActivity();

			PrimaveraEPS epsclient = new PrimaveraEPS();

			PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();
			List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();

			List<String> recordlist = new ArrayList<String>();

			List activitycheck = new ArrayList();
			List activitycodecheck = new ArrayList();
			List activityudfcheck = new ArrayList();
			List serviceudfcheck = new ArrayList();
			List notificationudfcheck = new ArrayList();
			List orderstatusudfcheck = new ArrayList();
			List notificationstatusudfcheck = new ArrayList();
			List<Activity> createactivity = new ArrayList<Activity>();

			List<UDFValue> readudf = new ArrayList<UDFValue>();
			PrimaveraProjectUDF udfs = new PrimaveraProjectUDF();

			// readudf = udfs.readUDFValue();

			JAXBElement<XMLGregorianCalendar> activitystartdate = null;
			JAXBElement<Double> actualhoursdouble = null;
logger.info("activitymasterdata "+activitymasterdata.size());
			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;
				String actualhours = null;

				String sourcesystem = null;
				//int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String primaveradttactivity = null;
				String turbinename = null;
				String turbinetype = null;
				String turbinetypename = null;

				String hqtechid = null;
				String primaverahqtechid = null;
				String rutechid = null;
				String primaverarutechid = null;
				XMLGregorianCalendar startdate = null;
				String startdat = null;
				JAXBElement<XMLGregorianCalendar> primaverastartdate = null;
				JAXBElement<Double> primaveraactualhours = null;

				String notificationid = "";
				String primaveranotificationid = "";
				String serviceorderid = "";
				String primaveraserviceorderid = "";

				String notificationstatus = "";
				String primaveranotificationstatus = "";
				String orderstatus = "";
				String primaveraorderstatus = "";

				//String worktypestring = null;
				String activityname = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				/*
				 * Date contractstartdate = null; Date contractenddate = null;
				 */
			//	String customerifa = null;
				//String customername = null;
				//String probability = null;
				//Activity activitycreate = new Activity();

			//	ActivityCodeAssignment acitivitycodecreate = new ActivityCodeAssignment();

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					UDFValue activityudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					int wbsobjectid = 0;
					JAXBElement<Integer> wbsobject = null;

					JAXBElement<Integer> wbsjaxbElement = null;
					JAXBElement<Integer> activityobject = null;
					JAXBElement<Integer> activityparentobject = null;
					String mpcallnumber = null;
					String mainplanid = null;
					String primaveramainplanid = null;
					String primaverampcallnumber = null;
					ObjectFactory objectfactory = new ObjectFactory();
					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;
					Integer activitycodeobjectid = null;
					Integer activityobjectid = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean activity = false;
					boolean wbs = false;
					boolean dttactivityidcheck = false;
					boolean mpcallnumbercheck = false;
					boolean mainplanidcheck = false;
					boolean hqtechidcheck = false;
					boolean rutechidcheck = false;
					boolean notificationidcheck = false;
					boolean notificationstatuscheck = false;
					boolean serviceorderidcheck = false;
					boolean orderstatuscheck = false;
					//boolean turbinetypecheck = false;
					boolean startdatecheck = false;
					boolean actualhourscheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains("Country_Id")) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Actual_Hours")) {
						actualhours = (String) value;
						// logger.info(" Actual Hours"+actualhours);
						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains("Activity_Name")) {
						activityname = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					/*if (key.contains("Work_Type")) {
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

						 System.out.println(worktypestring); 
					}*/

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}

					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("DTT_Activity_Object_ID")) {
						dttactivityid = (String) (value);
						//logger.info("dttactivityid "+dttactivityid);
						if (!(dttactivityid.endsWith("D"))) {
							recordlist.add(dttactivityid);
							//logger.info("Inside dttactivityid "+dttactivityid);
						}

						/* System.out.println(siteid); */
					}
					if (key.contains("Turbine_Name")) {
						turbinename = (String) (value);

						/* System.out.println(siteid); */
					}
					/*if (key.contains("Turbine_Type")) {
						turbinetypename = (String) (value);
						turbinetype = turbinetypename.substring(4,
								turbinetypename.length());
						System.out.println(turbinetype);
					}*/
					if (key.contains("HQ_TECH_ID_NUMBER")) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.STARTDATE)) {
						startdat = (String) (value);
						System.out.println("Start Date" + startdat);
						if (startdat != null || startdat != ""
								|| startdat != "null") {
							startdate = toXMLGregorianCalendarWithoutTimeStamp(startdat);
						}
					}

					if (key.contains(DTTConstants.MP_CALL_NUMBER)) {
						mpcallnumber = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Maintenance_Plan_ID")) {
						mainplanid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.NOTIFICATION_ID)) {
						notificationid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.NOTIFICATION_STATUS)) {
						notificationstatus = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.SERVICE_ORDER_ID)) {
						serviceorderid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.ORDER_STATUS)) {
						orderstatus = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.RU_TECH_ID_NUMBER)) {
						rutechid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (countryid != null && sourcesystem != null
							&& activityname != null && projectnumber != null
							&& locationshore != null && dttactivityid != null
							&& turbinename != null && hqtechid != null && siteid!=null) {
						ListIterator<EPS> epsread = epss.listIterator();
						while (epsread.hasNext()) {

							EPS epsreadelement = epsread.next();
							if (epsreadelement.getId().equals(countryid)) {

								country = true;
								countryobjectid = epsreadelement.getObjectId();
								countryparentobjecctid = epsreadelement
										.getParentObjectId();

								// System.out.print(countryobjectid);

							}
						}

						List<EPS> epsss1 = new ArrayList<EPS>();
						if (countryobjectid != null) {
							// System.out.println(countryobjectid);

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
								}
								// System.out.println("locationshore "+
								// locationshore);
								if (siteid != null) {
									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();

										// System.out.print(projectepsreadelement.getId()
										// +
										// " "+projectepsreadelement.getParentObjectId().getValue()
										// );
										if (projectepsreadelement.getId()
												.equals(locationshore)) {

											location = true;
											// System.out.print(projectepsreadelement.getName());
											locationshoreobjectid = projectepsreadelement
													.getObjectId();

										}
									}

									List<EPS> epsss2 = new ArrayList<EPS>();
									if (locationshoreobjectid != null) {
										// System.out.println(countryobjectid);

										epsss2 = epsclient
												.readWSProjectEPS(locationshoreobjectid);

									}
									ListIterator<EPS> prolocationepsread = epsss2
											.listIterator();
									while (prolocationepsread.hasNext()) {
										EPS projlocationepsreadelement = prolocationepsread
												.next();

										/*
										 * System.out.print(
										 * projlocationepsreadelement.getId() +
										 * " "+projlocationepsreadelement.
										 * getParentObjectId().getValue() );
										 */
										if (projlocationepsreadelement.getId()
												.equals(siteid)) {

											site = true;
											// System.out.print(projectepsreadelement.getName());
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
											 * System.out.print(projectidreadelement
											 * .getId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												// System.out.print(projectidreadelement.getName());
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
												 * System.out.print(
												 * projectidreadelement
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
												 * System.out.print("InLoop" +
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
											 * System.out.print(
											 * activityidreadelement
											 * .getObjectId() +
											 * " "+activityidreadelement
											 * .getName());
											 */

											if (activityidreadelement.getId()
													.equals(dttactivityid)
													|| activityidreadelement
															.getId()
															.equals(dttactivityid
																	.substring(
																			0,
																			dttactivityid
																					.length() - 1))) {
												activity = true;
												/*
												 * System.out.print(
												 * projectidreadelement
												 * .getName());
												 */

												activityobjectid = activityidreadelement
														.getObjectId();
												activityobject = activityidreadelement
														.getWBSObjectId();
												System.out
														.print(activityobjectid);

												System.out.print(activityobject
														.getValue());

											}
										}
									}

									PrimaveraAssignment assignmenttype = new PrimaveraAssignment();
									List<Role> readroledetails = new ArrayList<Role>();

									readroledetails = assignmenttype.readRole();
									ListIterator<Role> roleread = readroledetails
											.listIterator();
									while (roleread.hasNext()) {

										Role rolereadelement = roleread.next();

										System.out.print(rolereadelement
												.getName()
												+ ""
												+ rolereadelement.getId()
												+ ""
												+ rolereadelement
														.getParentObjectId()
														.getValue());
										activityparentobject = rolereadelement
												.getParentObjectId();

									}

									if (project) {
										if (wbs) {
											wbsobject = activityreadele
													.constructWBSObject(wbsobjectid);
											wbsobject.setValue(wbsobjectid);
											if (!dttactivityid.endsWith("D")) {
												if (activityobjectid != null) {
													readudf = udfs
															.readUDFValue(activityobjectid);
												}
												if (readudf.size() != 0) {
													ListIterator<UDFValue> wbsudfidreadchcek = readudf
															.listIterator();

													while (wbsudfidreadchcek
															.hasNext()) {
														UDFValue wbsudfidreadelement = wbsudfidreadchcek
																.next();
														if (wbsudfidreadelement
																.getUDFTypeObjectId()
																.equals(159)) {
															/*
															 * System.out.println
															 * ("UDF Code" +
															 * wbsudfidreadelement
															 * .getStartDate());
															 */
															activitystartdate = wbsudfidreadelement
																	.getStartDate();

														}

														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(135)) {
															dttactivityidcheck = true;
															primaveradttactivity = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(173)) {
															startdatecheck = true;
															primaverastartdate = wbsudfidreadelement
																	.getStartDate();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(140)) {
															hqtechidcheck = true;
															primaverahqtechid = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(141)) {
															rutechidcheck = true;
															primaverarutechid = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(139)) {
															mpcallnumbercheck = true;
															primaverampcallnumber = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(165)) {
															mainplanidcheck = true;
															primaveramainplanid = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(137)) {
															notificationidcheck = true;
															primaveranotificationid = wbsudfidreadelement
																	.getText();
														}

														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(247)) {
															notificationstatuscheck = true;
															/*
															 * logger.info(
															 * "Notifications  status outside in"
															 * +
															 * notificationstatus
															 * );
															 */
															primaveranotificationstatus = wbsudfidreadelement
																	.getText();
															/*
															 * logger.info(
															 * "primaveranotificationstatus"
															 * +
															 * primaveranotificationstatus
															 * );
															 */
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(138)) {
															serviceorderidcheck = true;
															primaveraserviceorderid = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(248)) {
															orderstatuscheck = true;
															primaveraorderstatus = wbsudfidreadelement
																	.getText();
														}
														if (wbsudfidreadelement
																.getForeignObjectId()
																.equals(activityobjectid)
																&& wbsudfidreadelement
																		.getUDFTypeObjectId()
																		.equals(249)) {
															actualhourscheck = true;

															primaveraactualhours = wbsudfidreadelement
																	.getDouble();
															 logger.info("primaveraactualhours"+primaveraactualhours.getValue());
														}
													}
												}

												if (activity) {

													// DTT Activity ID

													if (!activityudfcheck
															.contains(dttactivityid
																	.toString())) {
														activityudfcheck
																.add(dttactivityid
																		.toString());
														if (dttactivityidcheck) {
															if (dttactivityid != null) {
																if (!primaveradttactivity
																		.contains(dttactivityid)) {

																	activityudfassign
																			.setText(dttactivityid);
																	activityudfassign
																			.setUDFTypeObjectId(135);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

														}
														activityudfassign = new UDFValue();
														 logger.info("Inside Actual Hours "+actualhours);
														if (actualhourscheck) {
															if (actualhours == null
																	|| actualhours == ""
																	|| actualhours
																			.equals("0")) {
																actualhours = "0";

															}

															if (!actualhours
																	.equals("0")) {
																 logger.info("Insideactualhourscheck "+actualhourscheck);
																if (!primaveraactualhours
																		.getValue()
																		.equals(Double
																				.parseDouble(actualhours))) {

																	 logger.info("Inside Actual Hours"+actualhours);
																	actualhoursdouble = objectfactory
																			.createUDFValueDouble(Double
																					.parseDouble(actualhours));
																	activityudfassign
																			.setDouble(actualhoursdouble);
																	activityudfassign
																			.setUDFTypeObjectId(249);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}

															}
														}

														activityudfassign = new UDFValue();
														if (hqtechidcheck) {

															if (hqtechid != null) {
																if (!primaverahqtechid
																		.contains(hqtechid)) {
																	activityudfassign
																			.setText(hqtechid);
																	activityudfassign
																			.setUDFTypeObjectId(140);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

														}
														activityudfassign = new UDFValue();
														if (rutechidcheck) {
															if (rutechid != null) {
																if (!primaverarutechid
																		.contains(rutechid)) {

																	activityudfassign
																			.setText(rutechid);
																	activityudfassign
																			.setUDFTypeObjectId(141);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

														}
														activityudfassign = new UDFValue();
														// Start Date
														if (startdatecheck) {

															if (startdat != null)

															{
																if (!(primaverastartdate == null)) {
																	if (!primaverastartdate
																			.getValue()
																			.equals(toXMLGregorianCalendarWithoutTimeStamp(startdat))) {

																		activitystartdate = objectfactory
																				.createUDFValueFinishDate(toXMLGregorianCalendarWithoutTimeStamp(startdat));

																		activityudfassign
																				.setStartDate(activitystartdate);
																		activityudfassign
																				.setUDFTypeObjectId(173);
																		activityudfassign
																				.setForeignObjectId(activityobjectid);
																		activityudfdetails
																				.add(activityudfassign);
																	}

																}
															}

														}
													}
													activityudfassign = new UDFValue();
													if (mpcallnumbercheck) {
														if (mpcallnumber != null) {
															if (!primaverampcallnumber
																	.contains(mpcallnumber)) {

																activityudfassign
																		.setText(mpcallnumber);
																activityudfassign
																		.setUDFTypeObjectId(139);
																activityudfassign
																		.setForeignObjectId(activityobjectid);
																activityudfdetails
																		.add(activityudfassign);
															}
														}

													}
													activityudfassign = new UDFValue();
													if (mainplanidcheck) {
														if (mainplanid != null) {
															if (!primaveramainplanid
																	.contains(mainplanid)) {

																activityudfassign
																		.setText(mainplanid);
																activityudfassign
																		.setUDFTypeObjectId(165);
																activityudfassign
																		.setForeignObjectId(activityobjectid);
																activityudfdetails
																		.add(activityudfassign);
															}
														}

													}
													activityudfassign = new UDFValue();
													if (notificationidcheck) {

														activityudfassign = new UDFValue();
														if (notificationid == null
																|| notificationid == ""
																|| notificationid
																		.equals("0")) {
															notificationid = "null";

														}

														if (!notificationudfcheck
																.contains(dttactivityid)) {

															if (!notificationid
																	.equals("null")) {

																if (!primaveranotificationid
																		.contains(notificationid)) {
																	notificationudfcheck
																			.add(notificationid);
																	notificationudfcheck
																			.add(dttactivityid);
																	activityudfassign
																			.setText(notificationid);
																	activityudfassign
																			.setUDFTypeObjectId(137);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

														}

													}
													activityudfassign = new UDFValue();
													if (notificationstatuscheck) {
														
														  logger.info(
														  "Notifications  status outside in"
														  +notificationstatus);
														 
														activityudfassign = new UDFValue();
														if (notificationstatus == null
																|| notificationstatus == ""
																|| notificationstatus
																		.equals("0")) {
															notificationstatus = "null";

														}

														if (!notificationstatusudfcheck
																.contains(dttactivityid)) {
															/*
															 * logger.info(
															 * "Notifications  status outside"
															 * +
															 * notificationstatus
															 * );
															 */
															if (!notificationstatus
																	.equals("null")) {
																
																 logger.info(
																 "primaveranotificationstatus"
																 +
																 primaveranotificationstatus
																 );
																
																if (!primaveranotificationstatus
																		.contains(notificationstatus)) {
																	/*
																	 * logger.info
																	 * (
																	 * "notificationstatus"
																	 * +
																	 * notificationstatus
																	 * );
																	 */
																	notificationstatusudfcheck
																			.add(notificationstatus);
																	notificationstatusudfcheck
																			.add(dttactivityid);
																	activityudfassign
																			.setText(notificationstatus);
																	activityudfassign
																			.setUDFTypeObjectId(247);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);

																	
																	 logger.info
																	 (
																	  "Notifications  status"
																	  +
																	  notificationstatus
																	  );
																	 
																	activityudfdetails
																			.add(activityudfassign);
																}
															}

														}

													}
													if (orderstatuscheck) {

														activityudfassign = new UDFValue();
														if (orderstatus == null
																|| orderstatus == ""
																|| orderstatus
																		.equals("0")) {
															orderstatus = "null";

														}
														logger.info
														 (
														  "Order  status outside "
														  +
														  orderstatus
														  );
														logger.info
														 (
														  "primaveraorderstatus  status"
														  +
														  primaveraorderstatus
														  );
														if (!orderstatusudfcheck
																.contains(dttactivityid)) {

															if (!orderstatus
																	.equals("null")) {
																if (!primaveraorderstatus
																		.contains(orderstatus)) {
																	orderstatusudfcheck
																			.add(orderstatus);
																	orderstatusudfcheck
																			.add(dttactivityid);
																	activityudfassign
																			.setText(orderstatus);
																	activityudfassign
																			.setUDFTypeObjectId(248);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
																	logger.info
																	 (
																	  "Order  status"
																	  +
																	  orderstatus
																	  );
																}
															}

														}

													}
													activityudfassign = new UDFValue();
													if (serviceorderidcheck) {

														if (serviceorderid == null
																|| serviceorderid == ""
																|| serviceorderid
																		.equals("0")) {
															serviceorderid = "null";

														}
														if (!serviceudfcheck
																.contains(dttactivityid)) {
															if (!serviceorderid
																	.equals("null")) {
																if (!primaveraserviceorderid
																		.contains(serviceorderid)) {
																	serviceudfcheck
																			.add(serviceorderid);
																	serviceudfcheck
																			.add(dttactivityid);

																	activityudfassign
																			.setText(serviceorderid);
																	activityudfassign
																			.setUDFTypeObjectId(138);
																	activityudfassign
																			.setForeignObjectId(activityobjectid);
																	activityudfdetails
																			.add(activityudfassign);
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

			if (activityudfdetails != null) {

				ListIterator<UDFValue> read = activityudfdetails.listIterator();
				while (read.hasNext()) {

					UDFValue projreadelement = read.next();

					// System.out.print(countryobjectid);

					/*
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 */
					System.out.println(projreadelement.getText());
					System.out.println(projreadelement.getUDFTypeObjectId());
					System.out.println(projreadelement.getForeignObjectId());

				}

				// System.out.println(projectdata);
			}

			System.out.println("activityudfupdatedetails.size"
					+ activityudfdetails.size());
			// activitytype.createActivities(createactivity);
			// activitycodetype.createActivityCodeAssignment(activitycodedetails);
			logger.info("recordlist"+recordlist.size());
			UpdateProcessRecordsDAO processed = new UpdateProcessRecordsDAO();
			processed.updaterecords(DTTConstants.ACTIVITY, "Y", recordlist,
					"Success", "DTT_ACTIVITY_OBJECT_ID");
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return activityudfdetails;
	}

	public List checkAndGetActivityOutboundData() throws Exception {
		List<Map<String, Object>> activityoutbound = new ArrayList<Map<String, Object>>();

		Integer udftype = null;
		String mpcallnumber = "";
		String maintainanceplanid = "";
		String startdate = null;
		String finishdate = null;
		String baselineupdateddate = null;
		String sapparentobjectid = null;
		Integer projectobjectid = null;
		String notificationnumber = "";
		String serviceordernumber = "";
		Map ouboundmap = null;
		UpdateLastReadDAO  lastread= lastread= new UpdateLastReadDAO();
		int shorttermhorizon = 0;
		String multiregion = prop.get(DTTConstants.IS_MULTI_REGION_ENABLED)
				.toString();

		if (multiregion.equalsIgnoreCase(DTTConstants.Y)) {
			shorttermhorizon = Integer.parseInt(prop.get(
					DTTConstants.ALL_SHORTTERM_HORIZON).toString());
		} else {
			shorttermhorizon = Integer.parseInt(prop.get(
					DTTConstants.UK_SHORTTERM_HORIZON).toString());
		}

		System.out.println("shorttermhorizon" + shorttermhorizon);
		// logger.info("short term horizon "+shorttermhorizon);
		List<BaselineProject> readoutboundbaselineprojects = new ArrayList<BaselineProject>();
		Calendar currentDate = Calendar.getInstance(); // Get the current date
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // format
																					// it
																					// as
																					// per
																					// your
																					// requirement
		
		//String todaysdate = prop.get("HANA_WS_FROM_DATE").toString();
		String todaysdate=lastread.selectLastReadDate();	
		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = (Date) parser.parse(todaysdate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
        date = cal.getTime();
		
        todaysdate=formatter.format(date);
     // String todaysdate = formatter.format(currentDate.getTime());
      //String todaysdate = prop.get("HANA_WS_FROM_DATE").toString();
		//todaysdate = todaysdate + " 02:00:00";
		 todaysdate="2015-05-30 00:00:01";
		logger.info("Now the date is :=>  " + todaysdate);

		PrimaveraActivity pactivity = new PrimaveraActivity();
		List<Activity> readoutboundactivity = new ArrayList<Activity>();
		readoutboundbaselineprojects = pactivity
				.readBaselineProjectsOutbound(todaysdate);
		// logger.info(readoutboundbaselineprojects.size());
		System.out.println("readoutboundbaselineprojects"
				+ readoutboundbaselineprojects.size());
		ListIterator<BaselineProject> baselineprojectsread = readoutboundbaselineprojects
				.listIterator();
		System.out.println();
		while (baselineprojectsread.hasNext()) {

			BaselineProject baselineprojectelement = baselineprojectsread
					.next();
			baselineupdateddate = baselineprojectelement
					.getLastBaselineUpdateDate().getValue().toString();
			projectobjectid = baselineprojectelement
					.getOriginalProjectObjectId().getValue();
			String baselinetype = baselineprojectelement.getBaselineTypeName();
			sapparentobjectid = baselineprojectelement.getId();
			logger.info("Baseline Type"
					+ baselineprojectelement.getBaselineTypeName());
			if (baselinetype.equalsIgnoreCase("interface")) {

				/*
				 * System.out.println("baselineprojectelement.getWBSObjectId()"+
				 * baselineprojectelement.getWBSObjectId().getValue());
				 * System.out
				 * .println("baselineprojectelement.getBaselineTypeName()"
				 * +baselineprojectelement.getBaselineTypeName().getBytes());
				 * System
				 * .out.println("baselineprojectelement.getBaselineTypeObjectId()"
				 * +
				 * baselineprojectelement.getBaselineTypeObjectId().getValue());
				 * System
				 * .out.println("baselineprojectelement.getLastBaselineUpdateDate()"
				 * +
				 * baselineprojectelement.getLastBaselineUpdateDate().getValue()
				 * ); System.out.println("baselineprojectelement.getName()"+
				 * baselineprojectelement.getName()); System.out.println(
				 * "baselineprojectelement.getOriginalProjectObjectId()"
				 * +baselineprojectelement
				 * .getOriginalProjectObjectId().getValue());
				 * System.out.println(
				 * "baselineprojectelement.getObjectId()"+baselineprojectelement
				 * .getObjectId().intValue());
				 */// logger.info("changedate"+ baselineupdateddate);
				System.out.println("changedate" + baselineupdateddate);

				Integer wbsobjectid = baselineprojectelement
						.getOriginalProjectObjectId().getValue();

				readoutboundactivity = pactivity.readActivitiesOutbound(
						wbsobjectid, todaysdate);
				System.out.println("readoutboundactivity "
						+ readoutboundactivity.size());
				// logger.info("readoutboundactivity"+
				// readoutboundactivity.size());
				// System.out.println("readoutboundactivity"+
				// readoutboundactivity.size());
				ListIterator<Activity> activiityread = readoutboundactivity
						.listIterator();
				List<UDFValue> readudf = new ArrayList<UDFValue>();
				List<UDFValue> readprojectudf = new ArrayList<UDFValue>();
				PrimaveraProjectUDF udfs = new PrimaveraProjectUDF();
				while (activiityread.hasNext()) {

					mpcallnumber = "";
					maintainanceplanid = "";
					notificationnumber = "";
					serviceordernumber = "";

					Activity activityelement = activiityread.next();
					String typename = activityelement.getType();
					if (typename.equalsIgnoreCase("Task Dependent")) {
						startdate = activityelement.getStartDate().toString();
						finishdate = activityelement.getBaselineStartDate()
								.getValue().toString();

						boolean isshorttermhorizoncheckvalid = isShorttermHorizonValid(
								getSqldate(todaysdate), getSqldate(startdate),
								shorttermhorizon);
						System.out.println("startdate" + startdate);
						System.out.println("todaysdate" + todaysdate);
						// logger.info("TYPE"+activityelement.getType());
						System.out.println("isshorttermhorizoncheckvalid"
								+ isshorttermhorizoncheckvalid);
						if (isshorttermhorizoncheckvalid) {

							System.out.println("startdate" + startdate);
							Integer activityobjectid = activityelement
									.getObjectId();
							/*
							 * System.out.println("activityelement.getObjectId()"
							 * +activityelement.getObjectId());
							 * System.out.println
							 * ("activityelement.getName()"+activityelement
							 * .getName()); System.out.println(
							 * "activityelement.getBaselineStartDate()"
							 * +activityelement
							 * .getBaselineStartDate().getValue());
							 * System.out.println
							 * ("activityelement.getBaselineFinishDate()"
							 * +activityelement
							 * .getBaselineFinishDate().getValue());
							 * System.out.println
							 * ("activityelement.getStartDate()"
							 * +activityelement.getStartDate());
							 */

							// logger.info("activityobjectid"+activityobjectid);
							/*
							 * System.out.println("sapparentobjectid"+
							 * sapparentobjectid);
							 */
							readudf = udfs
									.readOutbounfUDFValue(activityobjectid);
							ListIterator<UDFValue> udfreadactivityvalues = readudf
									.listIterator();
							while (udfreadactivityvalues.hasNext()) {
								ouboundmap = new HashMap();
								ouboundmap.put("changedate",
										baselineupdateddate);
								ouboundmap.put("startdate", startdate);
								UDFValue udfelement = udfreadactivityvalues
										.next();
								udftype = udfelement.getUDFTypeObjectId();
								// logger.info("udftype"+ udftype);
								if (udftype == 139) {
									mpcallnumber = udfelement.getText();
									ouboundmap
											.put("mpcallnumber", mpcallnumber);
								}

								// if(mpcallnumber==null ||mpcallnumber==""){
								ouboundmap.put("mpcallnumber", mpcallnumber);
								// }
								if (udftype == 165) {
									maintainanceplanid = udfelement.getText();
									ouboundmap.put("maintainanceplanid",
											maintainanceplanid);
								}
								// if(maintainanceplanid==null
								// ||maintainanceplanid==""){
								ouboundmap.put("maintainanceplanid",
										maintainanceplanid);
								// }
								System.out.println("maintainanceplanid"
										+ maintainanceplanid);
								if (udftype == 137) {
									notificationnumber = udfelement.getText();
									ouboundmap.put("notificationnumber",
											notificationnumber);
								}
								// if(notificationnumber==null){
								ouboundmap.put("notificationnumber",
										notificationnumber);
								// }
								System.out.println("notificationnumber"
										+ notificationnumber);
								if (udftype == 138) {
									serviceordernumber = udfelement.getText();
									ouboundmap.put("serviceordernumber",
											serviceordernumber);
								}
								// if(serviceordernumber==null){
								ouboundmap.put("serviceordernumber",
										serviceordernumber);
								// }

								System.out.println("serviceordernumber"
										+ serviceordernumber);
								ouboundmap.put("sapparentobjectid",
										sapparentobjectid);
								/*
								 * System.out.println(
								 * "udfelement.getForeignObjectId()"
								 * +udfelement.getForeignObjectId());
								 * System.out.
								 * println("udfelement.getText()"+udfelement
								 * .getText()); System.out.println(
								 * "udfelement.getUDFTypeObjectId()"
								 * +udfelement.getUDFTypeObjectId());
								 * 
								 * readprojectudf =
								 * udfs.readProjectOutbounfUDFValue
								 * (projectobjectid); ListIterator<UDFValue>
								 * udfreadprojectvalues =
								 * readprojectudf.listIterator();
								 * //logger.info("udfreadprojectvalues"
								 * +readprojectudf.size()); while
								 * (udfreadprojectvalues.hasNext()) { UDFValue
								 * udfprojectelement =
								 * udfreadprojectvalues.next();
								 * System.out.println
								 * ("udfprojectelement"+udfprojectelement
								 * .getUDFTypeObjectId());
								 * udftype=udfprojectelement
								 * .getUDFTypeObjectId(); if(udftype==133){
								 * sapparentobjectid
								 * =udfprojectelement.getText();
								 * ouboundmap.put("sapparentobjectid",
								 * sapparentobjectid); }
								 * if(sapparentobjectid==null){
								 * ouboundmap.put("sapparentobjectid",
								 * sapparentobjectid); } }
								 */

							}
							activityoutbound.add(ouboundmap);

						}

					}

				}
			}
		}

		return activityoutbound;

	}

	public void insertActivityOutboundData() throws Exception {

		List<Map<String, Object>> activityoutbound = new ArrayList<Map<String, Object>>();
		activityoutbound = checkAndGetActivityOutboundData();
		ActivityOutBoundDAO activityoutbounddao = new ActivityOutBoundDAO();
		activityoutbounddao
				.insertActivityOutboundInterDataDAO(activityoutbound);
		activityoutbounddao.insertActivityOutboundDataDAO(activityoutbound);

	}

	public static XMLGregorianCalendar toXMLGregorianCalendarWithoutTimeStamp(
			String date) throws DatatypeConfigurationException, ParseException {
		Date mDate = null;
		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar xmlGregorianCalendar;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			mDate = df.parse(date);
			cal.setTime(mDate);
			xmlGregorianCalendar = DatatypeFactory.newInstance()
					.newXMLGregorianCalendarDate(cal.get(Calendar.YEAR),
							cal.get(Calendar.MONTH) + 1,
							cal.get(Calendar.DAY_OF_MONTH),
							DatatypeConstants.FIELD_UNDEFINED);
			return xmlGregorianCalendar;

		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public static boolean isShorttermHorizonValid(Date oldDate, Date newDate,
			Integer shorttermhorizon) {
		// how to check if date1 is equal to date2
		boolean isshorttermhorizonvalid = false;

		int days = DifferenceInDays(oldDate, newDate);
		System.out.println(days);
		if (days >= shorttermhorizon) {

			isshorttermhorizonvalid = true;
		}

		return isshorttermhorizonvalid;
	}

	public final static long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

	public static int DifferenceInDays(Date from, Date to) {
		return (int) ((to.getTime() - from.getTime()) / MILLISECONDS_IN_DAY);
	}

	public Date getSqldate(String datestr) throws ParseException {
		java.sql.Date sqlStartDate = null;

		if (datestr.equalsIgnoreCase("null") || datestr.equalsIgnoreCase("")) {
			return sqlStartDate;
		} else {

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			/* DateFormat df = new SimpleDateFormat(); */
			java.util.Date date = df.parse(datestr);
			sqlStartDate = new java.sql.Date(date.getTime());
		}

		return sqlStartDate;

	}

	public static void main(String[] args) throws Exception {
		PrimaveraActivityWSClient activityclient = new PrimaveraActivityWSClient();

		boolean flag = activityclient.isShorttermHorizonValid(
				activityclient.getSqldate("2015-03-18"),
				activityclient.getSqldate("2016-03-20"), 90);
		System.out.print("flag" + flag);
	}

}
