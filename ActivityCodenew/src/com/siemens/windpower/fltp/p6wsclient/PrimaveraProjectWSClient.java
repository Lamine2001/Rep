package com.siemens.windpower.fltp.p6wsclient;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.projectcode.ProjectCode;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignment;
import com.primavera.ws.p6.udfcode.UDFCode;
import com.primavera.ws.p6.udftype.UDFType;
import com.primavera.ws.p6.udfvalue.CreateUDFValuesResponse.ObjectId;
import com.primavera.ws.p6.udfvalue.ObjectFactory;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.fltp.dao.ProjectMasterDAO;
import com.siemens.windpower.fltp.dao.UpdateProcessRecordsDAO;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraProjectWSClient {
	Logger logger = null;
	ProjectMasterBean projectmasterbean = null;
	List<Map<String, Object>> projectmasterdata = null;

	public PrimaveraProjectWSClient() throws SQLException {
		logger = Logger.getLogger(PrimaveraProjectWSClient.class);

	}

	@SuppressWarnings({ "unchecked", "null" })
	public List<Project> checkAndCreateProject() {

		List<Project> epsoppurtunitydetails = new ArrayList<Project>();
		List<Project> epsmainprojectdetails = new ArrayList<Project>();
		// logger.info("Check and Create Project");
		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();
			List<Project> projectdata = new ArrayList<Project>();

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			PrimaveraProjectCode passingmentcode = new PrimaveraProjectCode();

			List<ProjectCode> readprojectcode = new ArrayList<ProjectCode>();
			PrimaveraProjectCode pcode = new PrimaveraProjectCode();
			readprojectcode = pcode.readProjectCode();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();

			if (projectmasterdata.size() != 0)
				/*
				 * //logger.info("projectmasterdata size " +
				 * projectmasterdata.size());
				 */
				for (Map<String, Object> map : projectmasterdata) {
					Project projectoppcreate = new Project();

					Project projectmaintenancecreate = new Project();
					String countryid = null;
					String cycles = null;
					String externalstatus = null;

					String sourcesystem = null;
					int worktype = 0;
					String locationshore = null;
					String locationshorecode = null;
					String siteid = null;
					Integer worktypestring = null;
					String projectnumber = null;
					String projectname = null;
					String sapparentobjectid = null;
					String contractstartdate = null;
					String contractenddate = null;
					String customerifa = null;
					String customername = null;
					String probability = null;
					String turbinequantity = null;

					for (Map.Entry<String, Object> entry : map.entrySet()) {
						ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
						UDFValue projectudfassign = new UDFValue();
						Integer countryobjectid = null;
						Integer locationshoreobjectid = null;
						Integer siteobjectid = 0;
						JAXBElement<Double> probabilityobject = null;
						JAXBElement<Double> turbinequantityobject = null;
						JAXBElement<Double> cyclesobject = null;

						JAXBElement<Integer> countryparentobjecctid = null;
						Integer opputunityobjectid = null;
						Integer projectobjectid = null;
						JAXBElement<Integer> projectobject = null;
						Integer opputunityparentobjectid = null;
						boolean opportunity = false;
						boolean project = false;
						boolean country = false;
						boolean location = false;
						boolean site = false;
						boolean worktypecheck = false;
						boolean externalstatuscheck = false;
						boolean locationshorecheck = false;
						boolean probabilitycheck = false;
						boolean turbinequantirycheck = false;
						boolean cyclescheck = false;
						boolean sapparentobjectidcheck = false;

						String key = entry.getKey();
						Object value = entry.getValue();
						if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
							countryid = (String) value;

							// System.out.println(countryid);

						}
						if (key.contains("Location_Shore")) {
							locationshore = (String) value;
							locationshorecode = (String) value;
							if (locationshorecode
									.contains(DTTConstants.OFFSHORE)) {
								locationshorecode = DTTConstants.OFFSHORECODE;
							}
							if (locationshorecode
									.contains(DTTConstants.ONSHORE)) {
								locationshorecode = DTTConstants.ONSHORECODE;
							}

							// System.out.println(locationshore);
						}
						if (key.contains("Project_Number")) {
							projectnumber = (String) value;

							// System.out.println(projectnumber);
						}
						if (key.contains(DTTConstants.PROJECTNAME)) {
							projectname = (String) value;

							// System.out.println(projectname);
						}

						if (key.contains("Source_System")) {
							sourcesystem = (String) value;

							// System.out.println(sourcesystem);
						}
						if (key.contains("Work_Type")) {
							worktype = Integer.valueOf((String) value);

							if (worktype == DTTConstants.WORKTYPE_1) {
								// worktypestring = "Prev.Main";
								worktypestring = DTTConstants.WORKTYPESTRING_1;

							}
							if (worktype == DTTConstants.WORKTYPE_2) {
								// worktypestring = "Retrofit";
								worktypestring = DTTConstants.WORKTYPESTRING_2;

							}
							if (worktype == DTTConstants.WORKTYPE_3) {
								// worktypestring = "Mods&up";
								worktypestring = DTTConstants.WORKTYPESTRING_3;
							}
							if (worktype == DTTConstants.WORKTYPE_4) {
								// worktypestring = "Repairs";
								worktypestring = DTTConstants.WORKTYPESTRING_4;
							}
							if (worktype == DTTConstants.WORKTYPE_5) {
								// worktypestring = "Service";
								worktypestring = DTTConstants.WORKTYPESTRING_5;
							}
							if (worktype == DTTConstants.WORKTYPE_6) {
								// worktypestring = "Service extension";
								worktypestring = DTTConstants.WORKTYPESTRING_6;
							}
							if (worktype == DTTConstants.WORKTYPE_7) {
								// worktypestring = "Mods&Up";
								worktypestring = DTTConstants.WORKTYPESTRING_3;
							}

							/* System.out.println(worktypestring); */
						}

						if (key.contains("SAP_Parent_Object_ID")) {
							sapparentobjectid = (String) (value);

							/* System.out.println(sapparentobjectid); */
						}
						if (key.contains(DTTConstants.CUSTOMER_IFA)) {
							customerifa = (String) (value);

							/* System.out.println(customerifa); */
						}
						if (key.contains(DTTConstants.CUSTOMER_NAME)) {
							customername = (String) (value);

							/* System.out.println(customername); */
						}
						if (key.contains(DTTConstants.PROBABILITY)) {
							probability = (String) (value);

							/* System.out.println(probability); */
						}

						if (key.contains(DTTConstants.CONTRACT_START)) {
							contractstartdate = (String) (value);

							/* System.out.println(contractstartdate); */
						}
						if (key.contains(DTTConstants.CONTRACT_END)) {
							contractenddate = (String) (value);

							/* System.out.println(contractenddate); */
						}
						if (key.contains("Site_Id")) {
							siteid = (String) (value);

							/* System.out.println(siteid); */
						}
						if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
							turbinequantity = (String) (value);

							/* System.out.println(siteid); */
						}

						if (key.contains(DTTConstants.CYCLES)) {
							cycles = (String) (value);

							/* System.out.println(siteid); */
						}

						if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
							externalstatus = (String) (value);
							if (!(externalstatus == null)) {
								if (externalstatus
										.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
									externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

								}
								if (externalstatus
										.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
									externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
								}
								if (externalstatus
										.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
									externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
								}
							}

							/* System.out.println(siteid); */
						}
						if (countryid != null && sourcesystem != null
								&& projectnumber != null && projectname != null) {

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

								epsss1 = readProjectEPS(countryobjectid);

							}

							// //logger.info("epsss1 size"+epsss1.size());

							if (epsss1 != null && sourcesystem != null) {
								if (sourcesystem.contains("SFDC")) {

									ListIterator<EPS> projectepsread = epsss1
											.listIterator();

									while (projectepsread.hasNext()) {
										EPS projectepsreadelement = projectepsread
												.next();
										String locationshorestring = DTTConstants.ATTACHOPP
												+ countryid;
										/*
										 * System.out.print(projectepsreadelement
										 * . getName() +
										 * " "+projectepsreadelement
										 * .getParentObjectId ().getValue() );
										 */
										if (projectepsreadelement
												.getName()
												.startsWith(
														DTTConstants.STARTS_WITH_OPPOR)) {
											if (projectepsreadelement
													.getId()
													.equalsIgnoreCase(
															locationshorestring)) {
												opportunity = true;
												// System.out.print(projectepsreadelement.getName());
												opputunityobjectid = projectepsreadelement
														.getObjectId();
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}
											/*
											 * if (projectepsreadelement
											 * .getName()
											 * .equals(locationshorestring)) {
											 * opportunity = true; //
											 * System.out.print
											 * (projectepsreadelement
											 * .getName()); opputunityobjectid =
											 * projectepsreadelement
											 * .getObjectId(); //
											 * opputunityparentobjectid=
											 * projectepsreadelement
											 * .getParentObjectId();
											 * 
											 * }
											 */
										}

									}
									List<Project> oppproject = new ArrayList<Project>();
									if (opputunityobjectid != 0) {
										oppproject = readProjectid(opputunityobjectid
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
											 * . getObjectId() +
											 * " "+projectidreadelement
											 * .getParentEPSObjectId());
											 */

											if (projectidreadelement.getId()
													.equals(projectnumber)) {
												project = true;
												/*
												 * System.out.print(
												 * projectidreadelement
												 * .getName());
												 */
												projectobjectid = projectidreadelement
														.getObjectId();
												System.out
														.print(projectobjectid);
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}
										}

										if (country) {
											if (opportunity) {
												if (project) {
													/*
													 * //logger.info(
													 * "Project already exists in primavera"
													 * + projectnumber);
													 */

												} else {
													if (!(oppcheck
															.contains(projectnumber))
															&& !(oppcheck
																	.contains(opputunityobjectid))) {

														oppcheck.add(opputunityobjectid
																.toString());
														oppcheck.add(projectnumber);
														projectoppcreate
																.setParentEPSObjectId(opputunityobjectid);
														projectoppcreate
																.setId(projectnumber);
														projectoppcreate
																.setName(projectname);

														/*
														 * System.out.println(
														 * opputunityobjectid +
														 * " " + projectnumber
														 * );
														 */

														epsoppurtunitydetails
																.add(projectoppcreate);
														/*
														 * System.out.println(
														 * epsoppurtunitydetails
														 * .size());
														 */
													}

												}
											}
										}

									}

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
										 * //logger.info("locationshore  " +
										 * locationshore);
										 */

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

											epsss2 = readProjectEPS(locationshoreobjectid);

										}
										// logger.info("epsss2  " +
										// epsss2.size());
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
												/*
												 * //logger.info("siteobjectid"
												 * + siteobjectid);
												 */
											}
										}

										List<Project> oppproject = new ArrayList<Project>();
										if (siteobjectid != 0) {
											oppproject = readProjectid(siteobjectid
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
													System.out
															.print(projectidreadelement
																	.getName());
													projectobjectid = projectidreadelement
															.getObjectId();
													// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

												}

											}
										}

										if (location) {
											if (site) {
												if (project) {
													/*
													 * //logger.info(
													 * "Project already exists in primavera"
													 * + projectnumber);
													 */
												} else {
													if (projectnumber != null
															&& projectname != null
															&& siteobjectid != null) {
														if (!(maintenancecheck
																.contains(projectnumber))
																&& !(maintenancecheck
																		.contains(siteobjectid))) {

															maintenancecheck
																	.add(siteobjectid
																			.toString());
															maintenancecheck
																	.add(projectnumber);
															projectmaintenancecreate
																	.setParentEPSObjectId(siteobjectid);
															projectmaintenancecreate
																	.setId(projectnumber);
															projectmaintenancecreate
																	.setName(projectname);
															/*
															 * //logger.info(
															 * "projectnumber" +
															 * projectnumber);
															 */
															/*
															 * System.out.println
															 * ( siteobjectid +
															 * " " +
															 * projectnumber );
															 */

															epsmainprojectdetails
																	.add(projectmaintenancecreate);
															// System.out.println(epsmainprojectdetails.size());

														}
													}
												}
											}
										}

									}
								}

								System.out
										.println("epsmainprojectdetails.size()"
												+ epsmainprojectdetails.size());
								if (epsmainprojectdetails != null) {

									ListIterator<Project> read = epsmainprojectdetails
											.listIterator();
									while (read.hasNext()) {

										Project projreadelement = read.next();

										System.out.print(epsmainprojectdetails
												.size());

										/*
										 * System.out.println(projreadelement.
										 * getName ());
										 */

									}

								}

							}
						}
					}
				}

			// logger.info("projectdetails size" +
			// epsmainprojectdetails.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return epsmainprojectdetails;

	}

	public List<Project> checkAndUpdateProject() {

		List<Project> epsoppurtunityupdatedetails = new ArrayList<Project>();
		List<Project> epsmainupdatedetails = new ArrayList<Project>();
		// logger.info("Check and Update Project");
		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();

			List<Project> projectdata = new ArrayList<Project>();

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			List<ProjectCode> readprojectcode = new ArrayList<ProjectCode>();
			PrimaveraProjectCode pcode = new PrimaveraProjectCode();
			readprojectcode = pcode.readProjectCode();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();

			for (Map<String, Object> map : projectmasterdata) {
				Project projectupdate = new Project();

				Project projectmaintenancecreate = new Project();
				String countryid = null;
				String cycles = null;
				String externalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				String siteid = null;
				Integer worktypestring = null;
				String projectnumber = null;
				String primaveraprojectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String contractstartdate = null;
				String contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;
				String turbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = null;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantirycheck = false;
					boolean cyclescheck = false;
					boolean sapparentobjectidcheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}

						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);

						if (externalstatus
								.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
							externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

						}
						if (externalstatus
								.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
							externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
						}
						if (externalstatus
								.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
							externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
						}

						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {
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

							epsss1 = readProjectEPS(countryobjectid);

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
									 * // System.out.print(element + " "); if
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

										epsss2 = readProjectEPS(locationshoreobjectid);

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
										oppproject = readProjectid(siteobjectid
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
												System.out
														.print(projectidreadelement
																.getName());
												primaveraprojectnumber = projectidreadelement
														.getId();
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}

										}
									}

									if (location) {
										if (site) {
											if (project) {
												if (!primaveraprojectnumber
														.contains(projectnumber)) {
													if (!(oppcheck
															.contains(projectnumber))
															&& !(oppcheck
																	.contains(opputunityobjectid))) {

														oppcheck.add(opputunityobjectid
																.toString());
														oppcheck.add(projectnumber);
														projectupdate
																.setParentEPSObjectId(opputunityobjectid);
														projectupdate
																.setId(projectnumber);
														projectupdate
																.setName(projectname);

														/*
														 * System.out.println(
														 * opputunityobjectid +
														 * " " + projectnumber
														 * );
														 */

														epsmainupdatedetails
																.add(projectupdate);
														/*
														 * System.out.println(
														 * epsoppurtunitydetails
														 * .size());
														 */
													}

												}

											}
										}
									}

								}
							}

							System.out.println("epsmainupdatedetails.size()"
									+ epsmainupdatedetails.size());
							if (epsmainupdatedetails != null) {

								ListIterator<Project> read = epsmainupdatedetails
										.listIterator();
								while (read.hasNext()) {

									Project projreadelement = read.next();

									System.out.print(epsmainupdatedetails
											.size());

									/*
									 * System.out.println(projreadelement.getName
									 * ());
									 */

								}

								System.out.println(projectdata);
							}

						}
					}
				}
			}
			// logger.info("Project update size()" +
			// epsmainupdatedetails.size());

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epsmainupdatedetails;

	}

	public List<Project> checkAndUpdateOppProject() {
		// logger.info("Check and Update Opp Project");

		List<Project> epsoppurtunityupdatedetails = new ArrayList<Project>();

		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();
			List<Project> projectdata = new ArrayList<Project>();

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			PrimaveraProjectCode passingmentcode = new PrimaveraProjectCode();

			List<ProjectCode> readprojectcode = new ArrayList<ProjectCode>();
			PrimaveraProjectCode pcode = new PrimaveraProjectCode();
			readprojectcode = pcode.readProjectCode();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();

			for (Map<String, Object> map : projectmasterdata) {
				Project projectoppupdate = new Project();

				Project projectmaintenancecreate = new Project();
				String countryid = null;
				String cycles = null;
				String externalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				String siteid = null;
				Integer worktypestring = null;
				String projectnumber = null;
				String primaveraprojectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String contractstartdate = null;
				String contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;
				String turbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = null;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantirycheck = false;
					boolean cyclescheck = false;
					boolean sapparentobjectidcheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}

						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);

						if (externalstatus
								.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
							externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

						}
						if (externalstatus
								.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
							externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
						}
						if (externalstatus
								.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
							externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
						}

						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {
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

							epsss1 = readProjectEPS(countryobjectid);

						}

						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains("SFDC")) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACHOPP
											+ countryid;
									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement
											.getName()
											.startsWith(
													DTTConstants.STARTS_WITH_OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											// System.out.print(projectepsreadelement.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}
								List<Project> oppproject = new ArrayList<Project>();
								if (opputunityobjectid != 0) {
									oppproject = readProjectid(opputunityobjectid
											.toString());
								}

								if (oppproject.size() != 0) {
									ListIterator<Project> projectidread = oppproject
											.listIterator();

									while (projectidread.hasNext()) {
										Project projectidreadelement = projectidread
												.next();

										/*
										 * System.out.print(projectidreadelement.
										 * getObjectId() +
										 * " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											/*
											 * System.out.print(projectidreadelement
											 * .getName());
											 */
											primaveraprojectnumber = projectidreadelement
													.getId();
											projectobjectid = projectidreadelement
													.getObjectId();
											System.out.print(projectobjectid);
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}
									}

									if (country) {
										if (opportunity) {
											if (project) {
												if (!primaveraprojectnumber
														.contains(projectnumber)) {
													if (!(oppcheck
															.contains(projectnumber))
															&& !(oppcheck
																	.contains(opputunityobjectid))) {

														oppcheck.add(opputunityobjectid
																.toString());
														oppcheck.add(projectnumber);
														projectoppupdate
																.setParentEPSObjectId(opputunityobjectid);
														projectoppupdate
																.setId(projectnumber);
														projectoppupdate
																.setName(projectname);

														/*
														 * System.out.println(
														 * opputunityobjectid +
														 * " " + projectnumber
														 * );
														 */

														epsoppurtunityupdatedetails
																.add(projectoppupdate);
														/*
														 * System.out.println(
														 * epsoppurtunitydetails
														 * .size());
														 */
													}

												}

											}
										}
									}

								}

							}

							System.out
									.println("epsoppurtunityupdatedetails.size()"
											+ epsoppurtunityupdatedetails
													.size());
							if (epsoppurtunityupdatedetails != null) {

								ListIterator<Project> read = epsoppurtunityupdatedetails
										.listIterator();
								while (read.hasNext()) {

									Project projreadelement = read.next();

									System.out
											.print(epsoppurtunityupdatedetails
													.size());

									/*
									 * System.out.println(projreadelement.getName
									 * ());
									 */

								}

								System.out.println(projectdata);
							}

						}
					}
				}
			}

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epsoppurtunityupdatedetails;
	}

	public List<EPS> readEPSforAll() throws Exception {
		PrimaveraEPS epsws = new PrimaveraEPS();
		List<EPS> epss = new ArrayList<EPS>();
		epss = epsws.readEPS();
		// epss=epsws.readProjectEPS();
		return epss;

	}

	public List<EPS> readProjectEPS(int objectid) throws Exception {
		PrimaveraEPS epsws = new PrimaveraEPS();
		List<EPS> epss = new ArrayList<EPS>();
		// epss = epsws.readEPS();
		epss = epsws.readWSProjectEPS(objectid);
		return epss;

	}

	public List<Project> readProjectid(String epsid) throws Exception {
		PrimaveraProject projws = new PrimaveraProject();
		List<Project> project = new ArrayList<Project>();
		// epss = epsws.readEPS();
		project = projws.opputunityReadProject(epsid);
		return project;

	}

	public List<UDFType> readProjectUDF() throws Exception {
		PrimaveraProjectUDF projudf = new PrimaveraProjectUDF();
		List<UDFType> udfvalues = new ArrayList<UDFType>();
		// epss = epsws.readEPS();
		udfvalues = projudf.readProjectUDF();
		return udfvalues;

	}

	public List<Project> checkAndCreateOppProject() throws SQLException {

		List<Project> epsoppurtunitydetails = new ArrayList<Project>();
		// logger.info("Check and Create Opp Project");
		ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
		projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

		projectmasterdata = new ArrayList<Map<String, Object>>();
		projectmasterdata = projectmasterbean.getProjectmasterdata();
		try {

			List epslist = new ArrayList();
			// epslist.add("Opp");

			List countrylist = new ArrayList();
			// epsmasterbean.getSfdccountrylist();

			List<EPS> projectcountryepsdata = new ArrayList<EPS>();

			EPS epscountrycreate = new EPS();
			List<Project> projectdata = new ArrayList<Project>();

			/*
			 * List<Project> epssitedetails = new ArrayList<Project>();
			 */

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();
			List<String> codecheck = new ArrayList<String>();
			List projectcountryList = new ArrayList();
			// logger.info("projectmasterdata" + projectmasterdata.size());
			for (Map<String, Object> map : projectmasterdata) {
				Project projectoppcreate = new Project();

				Project projectmaintenancecreate = new Project();
				String countryid = null;
				String cycles = null;
				String externalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				String siteid = null;
				Integer worktypestring = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String contractstartdate = null;
				String contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;
				String turbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = 0;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantirycheck = false;
					boolean cyclescheck = false;
					boolean sapparentobjectidcheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}

						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);
						if (externalstatus != null) {
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
							}
						}
						/* System.out.println(siteid); */
					}

					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {
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

							epsss1 = readProjectEPS(countryobjectid);

						}

						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains("SFDC")) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACHOPP
											+ countryid;

									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement
											.getName()
											.startsWith(
													DTTConstants.STARTS_WITH_OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											System.out
													.print(projectepsreadelement
															.getName());
											logger.info("projectepsreadelement"
													+ projectepsreadelement
															.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}
								List<Project> oppproject = new ArrayList<Project>();
								if (opputunityobjectid != 0) {
									System.out.println(opputunityobjectid);
									logger.info("opputunityobjectid"
											+ opputunityobjectid);
									oppproject = readProjectid(opputunityobjectid
											.toString());
								}
								if (oppproject.size() != 0) {
									ListIterator<Project> projectidread = oppproject
											.listIterator();

									while (projectidread.hasNext()) {
										Project projectidreadelement = projectidread
												.next();

										/*
										 * System.out.print(projectidreadelement.
										 * getObjectId() +
										 * " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											/*
											 * System.out.print(projectidreadelement
											 * .getName());
											 */
											projectobjectid = projectidreadelement
													.getObjectId();
											logger.info("projectobjectid"
													+ projectobjectid);
											System.out.print(projectobjectid);
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}
									}
								}

								if (country) {
									logger.info("opportunity" + opportunity);
									if (opportunity) {
										logger.info("opportunity" + opportunity);
										if (project) {
											logger.info("Project Already exists in Primavera");
										} else {
											if (!(oppcheck
													.contains(projectnumber))) {
												logger.info("projectnumber"
														+ projectnumber);
												logger.info("opputunityobjectid"
														+ opputunityobjectid);
												logger.info("projectname"
														+ projectname);
												oppcheck.add(opputunityobjectid
														.toString());
												oppcheck.add(projectnumber);
												projectoppcreate
														.setParentEPSObjectId(opputunityobjectid);
												projectoppcreate
														.setId(projectnumber);
												projectoppcreate
														.setName(projectname);

												/*
												 * System.out.println(
												 * opputunityobjectid + " " +
												 * projectnumber );
												 */

												epsoppurtunitydetails
														.add(projectoppcreate);
												/*
												 * System.out.println(
												 * epsoppurtunitydetails
												 * .size());
												 */
											}

										}
									}
								}

							}

							System.out.println("epsoppurtunitydetails"
									+ epsoppurtunitydetails.size());

							logger.info("epsoppurtunitydetails"
									+ epsoppurtunitydetails.size());
							if (epsoppurtunitydetails != null) {

								ListIterator<Project> read = epsoppurtunitydetails
										.listIterator();
								while (read.hasNext()) {

									Project readelement = read.next();

									// System.out.print(countryobjectid);

									System.out.println(readelement.getName());

								}

								// projectdata =

								System.out.println(projectdata);
							}

						}
					}
				}
			}

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epsoppurtunitydetails;

	}

	public List<ProjectCodeAssignment> checkAndCreateProjectCodes() {

		List<ProjectCodeAssignment> sfdcprojectcodedetails = new ArrayList<ProjectCodeAssignment>();
		// logger.info("Check and Create  Project Codes");
		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();
			List<Project> projectdata = new ArrayList<Project>();

			/*
			 * List<Project> epssitedetails = new ArrayList<Project>();
			 */

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			PrimaveraProjectCode passingmentcode = new PrimaveraProjectCode();

			List<ProjectCode> readprojectcode = new ArrayList<ProjectCode>();
			PrimaveraProjectCode pcode = new PrimaveraProjectCode();

			List<Project> epsoppurtunitydetails = new ArrayList<Project>();
			List<Project> epsmainprojectdetails = new ArrayList<Project>();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();

			// logger.info("projectmasterdata " + projectmasterdata.size());
			for (Map<String, Object> map : projectmasterdata) {
				Project projectoppcreate = new Project();
				List<ProjectCode> customernamecodecreatelist = new ArrayList<ProjectCode>();
				Project projectmaintenancecreate = new Project();

				String countryid = null;
				String cycles = null;
				String externalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				String siteid = null;
				Integer worktypestring = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String contractstartdate = null;
				String contractenddate = null;
				String customerifa = null;
				String customername = "null";
				String customernamesubstring = "null";
				List<String> codecheck = new ArrayList<String>();
				String probability = null;
				String turbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;
					String customernamecode = "null";
					String countrycode = "null";

					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = null;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantirycheck = false;
					boolean cyclescheck = false;
					boolean sapparentobjectidcheck = false;
					boolean customernamecheck = false;
					boolean customernamecodecheck = false;
					boolean countrycheck = false;
					boolean countrycodecheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}

						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);
						if (customername == null || customername.equals("")
								|| customername.equals(DTTConstants.NONE)
								|| customername.equals(DTTConstants.ZERO)) {
							customername = "null";
							// System.out.println("customername" +customername);

						}

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);
						if (!(externalstatus == null)) {
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
							}
						}

						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {

						// logger.info("sourcesystem" + sourcesystem);
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

							epsss1 = readProjectEPS(countryobjectid);

						}
						readprojectcode = pcode.readProjectCode();
						if (!customername.equals("null")) {
							// customernamesubstring=customername.substring(0,3);

							ListIterator<ProjectCode> projectcoderead = readprojectcode
									.listIterator();
							while (projectcoderead.hasNext()) {
								ProjectCode projectcodereadelement = projectcoderead
										.next();
								System.out.println(projectcodereadelement
										.getDescription());

								if (projectcodereadelement
										.getCodeTypeObjectId().equals(19)) {

									if (projectcodereadelement.getCodeValue()
											.equalsIgnoreCase(customerifa)) {

										customernamecodecheck = true;
									}

								}
								if (projectcodereadelement
										.getCodeTypeObjectId().equals(23)) {

									if (projectcodereadelement.getDescription()
											.equals(countryid)) {

										countrycodecheck = true;
									}

								}

								/*
								 * if(projectcodereadelement.getCodeValue().equals
								 * (customernamesubstring)){
								 * customernamesubstring
								 * =customername.substring(0,4); }
								 */

								/*
								 * logger.info(projectcodereadelement
								 * .getCodeTypeName() + "" +
								 * projectcodereadelement .getCodeTypeObjectId()
								 * + " " + projectcodereadelement
								 * .getParentObjectId().getValue() + " " +
								 * projectcodereadelement.getCodeValue() + " " +
								 * projectcodereadelement.getObjectId() + " " +
								 * projectcodereadelement .getDescription());
								 */

							}
							/*
							 * //logger.info("customernamecodecheck"+
							 * customernamecodecheck);
							 * //logger.info("customerifa"+customerifa);
							 * //logger.info("customername"+customername);
							 */
							if (customernamecodecheck) {
							} else {
								/*
								 * //logger.info("in else customername"+customername
								 * );
								 */
								if (!customerifa.equalsIgnoreCase("0")) {
									ProjectCode customernamecodecreate = new ProjectCode();
									customernamecodecreate
											.setCodeTypeObjectId(19);
									customernamecodecreate
											.setCodeValue(customerifa);
									customernamecodecreate
											.setDescription(customername);

									customernamecodecreatelist
											.add(customernamecodecreate);
									/*
									 * //logger.info("End of else customername"+
									 * customernamecodecreate
									 * .getCodeTypeObjectId());
									 */
								}
							}

						}
						/*
						 * //logger.info("customernamecodecreatelist "+
						 * customernamecodecreatelist.size());
						 */
						if (customernamecodecreatelist.size() != 0) {
							/*
							 * //logger.info("customernamecodecreatelist inside "
							 * + customernamecodecreatelist.size());
							 */
							pcode.createProjectCode(customernamecodecreatelist);
						}

						readprojectcode = pcode.readProjectCode();
						ListIterator<ProjectCode> customercoderead = readprojectcode
								.listIterator();
						while (customercoderead.hasNext()) {
							ProjectCode customercodereadelement = customercoderead
									.next();

							if (customercodereadelement.getDescription()
									.equals(customername)) {
								customernamecode = customercodereadelement
										.getObjectId().toString();
							}
							if (customercodereadelement.getDescription()
									.equals(countryid)) {
								countrycode = customercodereadelement
										.getObjectId().toString();
							}

						}

						// logger.info("epsss1" + epsss1.size());
						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains("SFDC")) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACHOPP
											+ countryid;
									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement
											.getName()
											.startsWith(
													DTTConstants.STARTS_WITH_OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											// System.out.print(projectepsreadelement.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}
								List<Project> oppproject = new ArrayList<Project>();
								if (opputunityobjectid != 0) {
									oppproject = readProjectid(opputunityobjectid
											.toString());
								}
								if (oppproject.size() != 0) {
									ListIterator<Project> projectidread = oppproject
											.listIterator();

									while (projectidread.hasNext()) {
										Project projectidreadelement = projectidread
												.next();

										/*
										 * System.out.print(projectidreadelement.
										 * getObjectId() +
										 * " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											/*
											 * System.out.print(projectidreadelement
											 * .getName());
											 */
											projectobjectid = projectidreadelement
													.getObjectId();
											System.out.print(projectobjectid);
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}
									}
									List<ProjectCodeAssignment> readprojectcodeassingment = new ArrayList<ProjectCodeAssignment>();
									readprojectcodeassingment = passingmentcode
											.readProjectCodeAssingment(projectobjectid
													.toString());

									ListIterator<ProjectCodeAssignment> projectcodeassingmentread = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentread.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelement = projectcodeassingmentread
												.next();
										if (projectcodereadassingmentelement
												.getProjectCodeObjectId()
												.equals(80)) {

											projectcodereadassingmentelement
													.getProjectObjectId();
										}

										/*
										 * System.out.println(
										 * projectcodereadassingmentelement
										 * .getProjectCodeTypeName() + "" +
										 * projectcodereadassingmentelement
										 * .getProjectCodeValue() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeObjectId() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectObjectId() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeTypeObjectId()
										 * .getValue());
										 */
									}
									if (country) {
										if (opportunity) {
											if (project) {

											} else {
												if (!(oppcheck
														.contains(projectnumber))
														&& !(oppcheck
																.contains(opputunityobjectid))) {

													oppcheck.add(opputunityobjectid
															.toString());
													oppcheck.add(projectnumber);
													projectoppcreate
															.setParentEPSObjectId(opputunityobjectid);
													projectoppcreate
															.setId(projectnumber);
													projectoppcreate
															.setName(projectname);

													/*
													 * System.out.println(
													 * opputunityobjectid + " "
													 * + projectnumber );
													 */

													epsoppurtunitydetails
															.add(projectoppcreate);
													/*
													 * System.out.println(
													 * epsoppurtunitydetails
													 * .size());
													 */
												}

											}
										}
									}
									ListIterator<ProjectCodeAssignment> projectcodeassingmentreadcheck = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentreadcheck
											.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelementcheck = projectcodeassingmentreadcheck
												.next();
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(23)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											countrycheck = true;
										}

										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(20)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											worktypecheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(26)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											projectcodereadassingmentelementcheck
													.getProjectCodeTypeObjectId();
											externalstatuscheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(19)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											customernamecheck = true;
										}
									}

									if (project) {

										if (worktypecheck) {

										} else {
											if (worktypestring != null
													|| worktypestring != 0) {
												if (!codecheck
														.contains(worktypestring
																.toString())) {
													codecheck
															.add(worktypestring
																	.toString());
													sfdcprojectcodecreate
															.setProjectObjectId(projectobjectid);
													sfdcprojectcodecreate
															.setProjectCodeObjectId(worktypestring);
													sfdcprojectcodecreate
															.setProjectId(projectnumber);
													sfdcprojectcodedetails
															.add(sfdcprojectcodecreate);
												}
											}
										}
										sfdcprojectcodecreate = new ProjectCodeAssignment();
										if (countrycheck) {

										} else {
											if (!countrycode.equals("null")) {
												if (!codecheck
														.contains(countryid
																.toString())) {
													codecheck.add(countryid
															.toString());
													sfdcprojectcodecreate
															.setProjectObjectId(projectobjectid);
													sfdcprojectcodecreate
															.setProjectCodeObjectId(Integer
																	.valueOf(countrycode));
													sfdcprojectcodecreate
															.setProjectId(projectnumber);
													sfdcprojectcodedetails
															.add(sfdcprojectcodecreate);
												}
											}
										}

										sfdcprojectcodecreate = new ProjectCodeAssignment();
										if (externalstatuscheck) {

										} else {
											if (externalstatus != null
													|| !(externalstatus
															.equals(""))) {
												if (!codecheck
														.contains(externalstatus
																.toString())) {
													codecheck
															.add(externalstatus
																	.toString());
													sfdcprojectcodecreate
															.setProjectObjectId(projectobjectid);
													sfdcprojectcodecreate
															.setProjectCodeObjectId(Integer
																	.valueOf(externalstatus));
													sfdcprojectcodecreate
															.setProjectId(projectnumber);
													sfdcprojectcodedetails
															.add(sfdcprojectcodecreate);
												}
											}
										}
										sfdcprojectcodecreate = new ProjectCodeAssignment();
										if (customernamecheck) {

										} else {
											System.out
													.println("customernamecode"
															+ customernamecode);
											if (!customernamecode
													.equals("null")) {
												if (!codecheck
														.contains(customernamecode
																.toString())) {
													codecheck
															.add(customernamecode
																	.toString());
													sfdcprojectcodecreate
															.setProjectObjectId(projectobjectid);
													sfdcprojectcodecreate
															.setProjectCodeObjectId(Integer
																	.valueOf(customernamecode));
													sfdcprojectcodecreate
															.setProjectId(projectnumber);
													sfdcprojectcodedetails
															.add(sfdcprojectcodecreate);
												}
											}
										}

										System.out
												.println(sfdcprojectcodedetails
														.size());

									}

								}

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
									// logger.info("Location" + locationshore);

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

										epsss2 = readProjectEPS(locationshoreobjectid);

									}
									// logger.info("epsss2" + epsss2.size());
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
										oppproject = readProjectid(siteobjectid
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
												System.out
														.print(projectidreadelement
																.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}

										}
									}
									List<ProjectCodeAssignment> readprojectcodeassingment = new ArrayList<ProjectCodeAssignment>();
									readprojectcodeassingment = passingmentcode
											.readProjectCodeAssingment(projectobjectid
													.toString());
									ListIterator<ProjectCodeAssignment> projectcodeassingmentreadcheck = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentreadcheck
											.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelementcheck = projectcodeassingmentreadcheck
												.next();
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(23)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											countrycheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(20)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											worktypecheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(21)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											locationshorecheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(19)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {

											customernamecheck = true;
										}
									}

									if (project) {

										/*
										 * //logger.info(
										 * "Project codes already exits in primavera"
										 * + projectnumber);
										 */
										if (worktypestring != null
												&& locationshorecode != null) {
											if (worktypecheck) {

											} else {
												if (worktypestring != null
														|| worktypestring != 0) {
													if (!codecheck
															.contains(worktypestring
																	.toString())) {
														/*
														 * //logger.info("Worktype"
														 * + worktype);
														 */
														codecheck
																.add(worktypestring
																		.toString());
														sfdcprojectcodecreate
																.setProjectObjectId(projectobjectid);
														sfdcprojectcodecreate
																.setProjectCodeObjectId(worktypestring);
														sfdcprojectcodecreate
																.setProjectId(projectnumber);
														sfdcprojectcodedetails
																.add(sfdcprojectcodecreate);
													}
												}
											}
											sfdcprojectcodecreate = new ProjectCodeAssignment();
											if (countrycheck) {

											} else {
												if (!countrycode.equals("null")) {
													if (!codecheck
															.contains(countryid
																	.toString())) {
														codecheck.add(countryid
																.toString());
														sfdcprojectcodecreate
																.setProjectObjectId(projectobjectid);
														sfdcprojectcodecreate
																.setProjectCodeObjectId(Integer
																		.valueOf(countrycode));
														sfdcprojectcodecreate
																.setProjectId(projectnumber);
														sfdcprojectcodedetails
																.add(sfdcprojectcodecreate);
													}
												}
											}

											sfdcprojectcodecreate = new ProjectCodeAssignment();
											if (locationshorecheck) {

											} else {
												if (locationshorecode != null
														|| locationshorecode != "") {
													if (!codecheck
															.contains(locationshorecode
																	.toString())) {
														codecheck
																.add(locationshorecode
																		.toString());
														sfdcprojectcodecreate
																.setProjectObjectId(projectobjectid);
														sfdcprojectcodecreate
																.setProjectCodeObjectId(Integer
																		.valueOf(locationshorecode));
														sfdcprojectcodecreate
																.setProjectId(projectnumber);
														sfdcprojectcodedetails
																.add(sfdcprojectcodecreate);
													}
												}
											}
											sfdcprojectcodecreate = new ProjectCodeAssignment();
											if (customernamecheck) {

											} else {
												/*
												 * //logger.info("customernamecode"
												 * + customernamecode);
												 */
												if (!customernamecode
														.equals("null")) {
													if (!codecheck
															.contains(customernamecode
																	.toString())) {
														codecheck
																.add(customernamecode
																		.toString());
														sfdcprojectcodecreate
																.setProjectObjectId(projectobjectid);
														sfdcprojectcodecreate
																.setProjectCodeObjectId(Integer
																		.valueOf(customernamecode));
														sfdcprojectcodecreate
																.setProjectId(projectnumber);
														sfdcprojectcodedetails
																.add(sfdcprojectcodecreate);
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
			if (sfdcprojectcodedetails != null) {

				ListIterator<ProjectCodeAssignment> read = sfdcprojectcodedetails
						.listIterator();
				while (read.hasNext()) {

					ProjectCodeAssignment projreadelement = read.next();

					// System.out.print(countryobjectid);

					System.out
							.println(projreadelement.getProjectCodeObjectId());

					System.out.println(projreadelement.getProjectObjectId());
					System.out.println(sfdcprojectcodedetails.size());

				}

				System.out.println(projectdata);
			}

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sfdcprojectcodedetails;

	}

	public List<ProjectCodeAssignment> checkAndUpdateProjectCodes()
			throws Exception {

		List<ProjectCodeAssignment> projectcodeupdatedetails = new ArrayList<ProjectCodeAssignment>();
		// logger.info("Check and Update  Project Codes");

		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();
			List<Project> projectdata = new ArrayList<Project>();

			/*
			 * List<Project> epssitedetails = new ArrayList<Project>();
			 */

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			PrimaveraProjectCode passingmentcode = new PrimaveraProjectCode();

			List<ProjectCode> readprojectcode = new ArrayList<ProjectCode>();
			PrimaveraProjectCode pcode = new PrimaveraProjectCode();
			readprojectcode = pcode.readProjectCode();

			ListIterator<ProjectCode> projectcoderead = readprojectcode
					.listIterator();
			while (projectcoderead.hasNext()) {

				ProjectCode projectcodereadelement = projectcoderead.next();

				/*
				 * System.out.println(projectcodereadelement.getCodeTypeName() +
				 * "" + projectcodereadelement.getCodeTypeObjectId() + " " +
				 * projectcodereadelement.getParentObjectId().getValue() + " " +
				 * projectcodereadelement.getCodeValue() + " " +
				 * projectcodereadelement.getObjectId());
				 */
			}

			List<Project> epsoppurtunitydetails = new ArrayList<Project>();
			List<Project> epsmainprojectdetails = new ArrayList<Project>();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();
			List<String> codecheck = new ArrayList<String>();

			for (Map<String, Object> map : projectmasterdata) {
				Project projectoppcreate = new Project();

				Project projectmaintenancecreate = new Project();
				String countryid = null;
				String cycles = null;
				String externalstatus = null;
				Integer primaveraexternalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				Integer primaveralocationshore = null;
				String siteid = null;
				Integer worktypestring = null;
				Integer primaveraworktype = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String contractstartdate = null;
				String contractenddate = null;
				String customerifa = null;
				String customername = null;
				Integer primaveracustomername = null;
				String probability = null;
				String turbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment projectcodeupdate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;
					String customernamecode = "null";
					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = null;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantirycheck = false;
					boolean cyclescheck = false;
					boolean sapparentobjectidcheck = false;
					boolean customernamecheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}
						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);
						if (customername == null || customername.equals("")
								|| customername.equals(DTTConstants.NONE)
								|| customername.equals(DTTConstants.ZERO)) {
							customername = "null";
							System.out.println("customername" + customername);

						}

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);
						if (!(externalstatus == null)) {
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
							}
						}
						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {
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

							epsss1 = readProjectEPS(countryobjectid);

						}
						readprojectcode = pcode.readProjectCode();
						ListIterator<ProjectCode> customercoderead = readprojectcode
								.listIterator();
						while (customercoderead.hasNext()) {
							ProjectCode customercodereadelement = customercoderead
									.next();

							if (customercodereadelement.getDescription()
									.equals(customername)) {
								customernamecode = customercodereadelement
										.getObjectId().toString();
							}
						}

						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains("SFDC")) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACHOPP
											+ countryid;
									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement
											.getName()
											.startsWith(
													DTTConstants.STARTS_WITH_OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											// System.out.print(projectepsreadelement.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}
								List<Project> oppproject = new ArrayList<Project>();
								if (opputunityobjectid != 0) {
									oppproject = readProjectid(opputunityobjectid
											.toString());
								}
								if (oppproject.size() != 0) {
									ListIterator<Project> projectidread = oppproject
											.listIterator();

									while (projectidread.hasNext()) {
										Project projectidreadelement = projectidread
												.next();

										/*
										 * System.out.print(projectidreadelement.
										 * getObjectId() +
										 * " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											/*
											 * System.out.print(projectidreadelement
											 * .getName());
											 */
											projectobjectid = projectidreadelement
													.getObjectId();
											/*
											 * System.out.print(projectobjectid);
											 */
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}
									}
									List<ProjectCodeAssignment> readprojectcodeassingment = new ArrayList<ProjectCodeAssignment>();
									readprojectcodeassingment = passingmentcode
											.readProjectCodeAssingment(projectobjectid
													.toString());
									ListIterator<ProjectCodeAssignment> projectcodeassingmentread = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentread.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelement = projectcodeassingmentread
												.next();
										if (projectcodereadassingmentelement
												.getProjectCodeObjectId()
												.equals(80)) {

											projectcodereadassingmentelement
													.getProjectObjectId();
										}

										/*
										 * System.out.println("Project Code " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeTypeName() + "" +
										 * projectcodereadassingmentelement
										 * .getProjectCodeValue() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeObjectId() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectObjectId() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeTypeObjectId()
										 * .getValue());
										 */
									}

									if (country) {
										if (opportunity) {
											if (project) {

											} else {
												if (!(oppcheck
														.contains(projectnumber))
														&& !(oppcheck
																.contains(opputunityobjectid))) {

													oppcheck.add(opputunityobjectid
															.toString());
													oppcheck.add(projectnumber);
													projectoppcreate
															.setParentEPSObjectId(opputunityobjectid);
													projectoppcreate
															.setId(projectnumber);
													projectoppcreate
															.setName(projectname);

													/*
													 * System.out.println(
													 * opputunityobjectid + " "
													 * + projectnumber );
													 */

													epsoppurtunitydetails
															.add(projectoppcreate);
													/*
													 * System.out.println(
													 * epsoppurtunitydetails
													 * .size());
													 */
												}

											}
										}
									}
									ListIterator<ProjectCodeAssignment> projectcodeassingmentreadcheck = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentreadcheck
											.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelementcheck = projectcodeassingmentreadcheck
												.next();
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(20)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											primaveraworktype = projectcodereadassingmentelementcheck
													.getProjectCodeObjectId();

											worktypecheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(26)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											projectcodereadassingmentelementcheck
													.getProjectCodeTypeObjectId();
											primaveraexternalstatus = projectcodereadassingmentelementcheck
													.getProjectCodeObjectId();
											externalstatuscheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(19)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											projectcodereadassingmentelementcheck
													.getProjectCodeTypeObjectId();
											primaveracustomername = projectcodereadassingmentelementcheck
													.getProjectCodeObjectId();
											customernamecheck = true;
										}
									}

									if (project) {

										if (worktypestring != null
												&& externalstatus != null) {
											if (worktypecheck) {
												if (!primaveraworktype
														.equals(worktypestring)) {

													if (worktypestring != null
															|| worktypestring != 0) {
														if (!codecheck
																.contains(worktypestring
																		.toString())) {

															projectcodeupdate
																	.setProjectObjectId(projectobjectid);
															projectcodeupdate
																	.setProjectCodeObjectId(worktypestring);
															projectcodeupdate
																	.setProjectId(projectnumber);
															projectcodeupdatedetails
																	.add(projectcodeupdate);
														}
													}

												}

											}

											projectcodeupdate = new ProjectCodeAssignment();
											if (customernamecheck) {
												if (!primaveracustomername
														.equals((Integer
																.valueOf(customernamecode)))) {
													if (!customername
															.equals("null")) {
														if (!codecheck
																.contains(customername
																		.toString())) {

															projectcodeupdate
																	.setProjectObjectId(projectobjectid);
															projectcodeupdate
																	.setProjectCodeObjectId(Integer
																			.valueOf(customernamecode));
															projectcodeupdate
																	.setProjectId(projectnumber);
															projectcodeupdatedetails
																	.add(projectcodeupdate);
														}
													}
												}

											}

											projectcodeupdate = new ProjectCodeAssignment();
											if (externalstatuscheck) {
												if (!primaveraexternalstatus
														.equals((Integer
																.valueOf(externalstatus)))) {
													if (externalstatus != null
															|| !(externalstatus
																	.equals(""))) {
														if (!codecheck
																.contains(externalstatus
																		.toString())) {

															projectcodeupdate
																	.setProjectObjectId(projectobjectid);
															projectcodeupdate
																	.setProjectCodeObjectId(Integer
																			.valueOf(externalstatus));
															projectcodeupdate
																	.setProjectId(projectnumber);
															projectcodeupdatedetails
																	.add(projectcodeupdate);
														}
													}
												}

											}

											System.out
													.println(projectcodeupdatedetails
															.size());

										}
									}

								}

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
									 * // System.out.print(element + " "); if
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

										epsss2 = readProjectEPS(locationshoreobjectid);

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
										oppproject = readProjectid(siteobjectid
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
												System.out
														.print(projectidreadelement
																.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}

										}
									}

									if (location) {
										if (site) {
											if (project) {

											} else {
												if (!(maintenancecheck
														.contains(projectnumber))
														&& !(maintenancecheck
																.contains(siteobjectid))) {

													maintenancecheck
															.add(siteobjectid
																	.toString());
													maintenancecheck
															.add(projectnumber);
													projectmaintenancecreate
															.setParentEPSObjectId(siteobjectid);
													projectmaintenancecreate
															.setId(projectnumber);
													projectmaintenancecreate
															.setName(projectname);

													/*
													 * System.out.println(
													 * siteobjectid + " " +
													 * projectnumber );
													 */

													epsmainprojectdetails
															.add(projectmaintenancecreate);
													// System.out.println(epsmainprojectdetails.size());

												}
											}
										}
									}
									List<ProjectCodeAssignment> readprojectcodeassingment = new ArrayList<ProjectCodeAssignment>();
									readprojectcodeassingment = passingmentcode
											.readProjectCodeAssingment(projectobjectid
													.toString());
									ListIterator<ProjectCodeAssignment> projectcodeassingmentread = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentread.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelement = projectcodeassingmentread
												.next();
										if (projectcodereadassingmentelement
												.getProjectCodeObjectId()
												.equals(80)) {

											projectcodereadassingmentelement
													.getProjectObjectId();
										}

										/*
										 * System.out.println("Project Code " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeTypeName() + "" +
										 * projectcodereadassingmentelement
										 * .getProjectCodeValue() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeObjectId() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectObjectId() + " " +
										 * projectcodereadassingmentelement
										 * .getProjectCodeTypeObjectId()
										 * .getValue());
										 */
									}
									ListIterator<ProjectCodeAssignment> projectcodeassingmentreadcheck = readprojectcodeassingment
											.listIterator();
									while (projectcodeassingmentreadcheck
											.hasNext()) {

										ProjectCodeAssignment projectcodereadassingmentelementcheck = projectcodeassingmentreadcheck
												.next();
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(20)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											primaveraworktype = projectcodereadassingmentelementcheck
													.getProjectCodeObjectId();

											worktypecheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(21)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											primaveralocationshore = projectcodereadassingmentelementcheck
													.getProjectCodeObjectId();
											locationshorecheck = true;
										}
										if (projectcodereadassingmentelementcheck
												.getProjectCodeTypeObjectId()
												.getValue().equals(19)
												&& projectcodereadassingmentelementcheck
														.getProjectObjectId()
														.equals(projectobjectid)) {
											projectcodereadassingmentelementcheck
													.getProjectCodeTypeObjectId();
											primaveracustomername = projectcodereadassingmentelementcheck
													.getProjectCodeObjectId();
											customernamecheck = true;
										}
									}

									if (project) {
										if (worktypestring != null
												&& locationshorecode != null) {
											if (worktypecheck) {
												if (!primaveraworktype
														.equals(worktypestring)) {

													if (worktypestring != null
															|| worktypestring != 0) {
														if (!codecheck
																.contains(worktypestring
																		.toString())) {

															projectcodeupdate
																	.setProjectObjectId(projectobjectid);
															projectcodeupdate
																	.setProjectCodeObjectId(worktypestring);
															projectcodeupdate
																	.setProjectId(projectnumber);
															projectcodeupdatedetails
																	.add(projectcodeupdate);
														}
													}

												}
											}

											projectcodeupdate = new ProjectCodeAssignment();
											if (locationshorecheck) {
												if (!primaveralocationshore
														.equals(Integer
																.valueOf(locationshorecode))) {
													if (locationshorecode != null
															|| locationshorecode != "") {
														if (!codecheck
																.contains(locationshorecode
																		.toString())) {

															projectcodeupdate
																	.setProjectObjectId(projectobjectid);
															projectcodeupdate
																	.setProjectCodeObjectId(Integer
																			.valueOf(locationshorecode));
															projectcodeupdate
																	.setProjectId(projectnumber);
															projectcodeupdatedetails
																	.add(projectcodeupdate);
														}
													}
												}

											}
											projectcodeupdate = new ProjectCodeAssignment();
											if (customernamecheck) {
												if (!primaveracustomername
														.equals((Integer
																.valueOf(customernamecode)))) {
													if (!customername
															.equals("null")) {
														if (!codecheck
																.contains(customername
																		.toString())) {

															projectcodeupdate
																	.setProjectObjectId(projectobjectid);
															projectcodeupdate
																	.setProjectCodeObjectId(Integer
																			.valueOf(customernamecode));
															projectcodeupdate
																	.setProjectId(projectnumber);
															projectcodeupdatedetails
																	.add(projectcodeupdate);
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
			if (projectcodeupdatedetails != null) {

				ListIterator<ProjectCodeAssignment> read = projectcodeupdatedetails
						.listIterator();
				while (read.hasNext()) {

					ProjectCodeAssignment projreadelement = read.next();

					// System.out.print(countryobjectid);

					System.out
							.println(projreadelement.getProjectCodeObjectId());

					System.out.println(projreadelement.getProjectObjectId());
					System.out.println(projectcodeupdatedetails.size());

				}

				System.out.println(projectdata);
			}

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return projectcodeupdatedetails;

	}

	public List<UDFValue> checkAndCreateProjectUDF() throws Exception {

		List<UDFValue> projectudfvaluedetails = new ArrayList<UDFValue>();
		// logger.info("Check and Create  Project UDF");
		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();
			PrimaveraProject projectworktype = new PrimaveraProject();

			PrimaveraProjectUDF projectudfvalue = new PrimaveraProjectUDF();

			List epslist = new ArrayList();
			// epslist.add("Opp");

			List countrylist = new ArrayList();
			// epsmasterbean.getSfdccountrylist();

			List<EPS> projectcountryepsdata = new ArrayList<EPS>();

			EPS epscountrycreate = new EPS();
			List<Project> projectdata = new ArrayList<Project>();

			/*
			 * List<Project> epssitedetails = new ArrayList<Project>();
			 */

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			/*
			 * List<UDFType> udfcode = new ArrayList<UDFType>(); udfcode =
			 * readProjectUDF(); ListIterator<UDFType> projectudfread =
			 * udfcode.listIterator(); while (projectudfread.hasNext()) {
			 * 
			 * UDFType udfreadelement = projectudfread.next();
			 * 
			 * System.out.println(udfreadelement.getDataType() + " " +
			 * udfreadelement.getSubjectArea() + " " + udfreadelement.getTitle()
			 * + " " + udfreadelement.getObjectId()); }
			 */
			List<Project> epsoppurtunitydetails = new ArrayList<Project>();
			List<Project> epsmainprojectdetails = new ArrayList<Project>();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();
			List<String> codecheck = new ArrayList<String>();
			List projectcountryList = new ArrayList();
			// logger.info("projectmasterdata" + projectmasterdata.size());
			for (Map<String, Object> map : projectmasterdata) {
				Project projectoppcreate = new Project();

				Project projectmaintenancecreate = new Project();
				String countryid = null;
				String cycles = null;

				String retrofitcampaignid = null;
				String externalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				String siteid = null;
				String siteaddress = null;
				Integer worktypestring = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String contractstartdate = "";
				String contractenddate = "";
				String customerifa = null;
				String customername = null;
				String probability = null;
				String turbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					ObjectFactory objectfactory = new ObjectFactory();
					JAXBElement<XMLGregorianCalendar> contractstartdateobject = null;
					JAXBElement<XMLGregorianCalendar> contractenddateobject = null;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = null;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantirycheck = false;
					boolean cyclescheck = false;
					boolean retrofitcampaignidcheck = false;
					boolean sapparentobjectidcheck = false;
					boolean contractstartdatecheck = false;
					boolean contractenddatecheck = false;
					boolean customerifacheck = false;
					boolean siteaddresscheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}

						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Site_Address")) {
						siteaddress = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains("Retrofit_Campaign_ID")) {
						retrofitcampaignid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);
						if (!(externalstatus == null)) {
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
							}
						}
						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {
						// logger.info("projectnumber" + projectnumber);
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

							epsss1 = readProjectEPS(countryobjectid);

						}
						List<UDFValue> readudf = new ArrayList<UDFValue>();
						PrimaveraProjectUDF readudfs = new PrimaveraProjectUDF();

						/*
						 * ListIterator<UDFValue> wbsudfidread = readudf
						 * .listIterator();
						 * 
						 * while (wbsudfidread.hasNext()) { UDFValue
						 * wbsudfidreadelement = wbsudfidread.next();
						 * 
						 * if (wbsudfidreadelement.getCodeValue() != null)
						 * 
						 * System.out.println("code " +
						 * wbsudfidreadelement.getCodeValue());
						 * 
						 * if (wbsudfidreadelement.getText() != null)
						 * 
						 * System.out.println("Text" +
						 * wbsudfidreadelement.getText());
						 * 
						 * if (wbsudfidreadelement.getDouble() != null)
						 * 
						 * System.out.println("Double" +
						 * wbsudfidreadelement.getDouble() .getValue());
						 * 
						 * if (wbsudfidreadelement .getUDFTypeObjectId().equals(
						 * 128)) {
						 * 
						 * turbinequantityobject = wbsudfidreadelement
						 * .getDouble(); } if
						 * (wbsudfidreadelement.getUDFTypeObjectId()
						 * .equals(129)) { probabilityobject =
						 * wbsudfidreadelement .getDouble();
						 * 
						 * } if (wbsudfidreadelement.getUDFTypeObjectId()
						 * .equals(132)) { cyclesobject =
						 * wbsudfidreadelement.getDouble();
						 * 
						 * } if (wbsudfidreadelement.getInteger() != null)
						 * 
						 * System.out.println("Integer" +
						 * wbsudfidreadelement.getInteger());
						 * 
						 * if (wbsudfidreadelement.getUDFTypeTitle() != null)
						 * 
						 * System.out .println("type title" +
						 * wbsudfidreadelement .getUDFTypeTitle());
						 * 
						 * if (wbsudfidreadelement .getForeignObjectId() !=
						 * null)
						 * 
						 * System.out.println("F Object" + wbsudfidreadelement
						 * .getForeignObjectId());
						 * 
						 * if (wbsudfidreadelement .getUDFTypeObjectId() !=
						 * null)
						 * 
						 * System.out.println("UDF type" + wbsudfidreadelement
						 * .getUDFTypeObjectId());
						 * 
						 * if (wbsudfidreadelement .getUDFCodeObjectId() !=
						 * null) System.out .println("UDF Code" +
						 * wbsudfidreadelement .getUDFTypeDataType());
						 * 
						 * }
						 */

						if (epsss1 != null && sourcesystem != null) {

							if (sourcesystem.contains("SFDC")) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACHOPP
											+ countryid;
									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement
											.getName()
											.startsWith(
													DTTConstants.STARTS_WITH_OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											// System.out.print(projectepsreadelement.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}
								List<Project> oppproject = new ArrayList<Project>();
								if (opputunityobjectid != 0) {
									oppproject = readProjectid(opputunityobjectid
											.toString());
								}
								if (oppproject.size() != 0) {
									ListIterator<Project> projectidread = oppproject
											.listIterator();

									while (projectidread.hasNext()) {
										Project projectidreadelement = projectidread
												.next();

										/*
										 * System.out.print(projectidreadelement.
										 * getObjectId() +
										 * " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											/*
											 * System.out.print(projectidreadelement
											 * .getName());
											 */
											projectobjectid = projectidreadelement
													.getObjectId();
											System.out.print(projectobjectid);
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}
									}

									if (project) {
										readudf = readudfs
												.readUDFValue(projectobjectid);
										ListIterator<UDFValue> wbsudfidreadcheck = readudf
												.listIterator();

										while (wbsudfidreadcheck.hasNext()) {
											UDFValue wbsudfidreadelementcheck = wbsudfidreadcheck
													.next();

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(129)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												probabilitycheck = true;
												probabilityobject = wbsudfidreadelementcheck
														.getDouble();
											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(128)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												turbinequantirycheck = true;
											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(132)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												cyclescheck = true;
											}

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(147)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												retrofitcampaignidcheck = true;
											}

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(171)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												siteaddresscheck = true;
											}

										}

										if (probabilitycheck) {

										} else {
											if (probability != null
													|| probability != " ") {

												/*
												 * probabilityobject
												 * .setValue(Double
												 * .valueOf(probability));
												 */
												probabilityobject = objectfactory
														.createUDFValueDouble(Double
																.valueOf(probability));
												projectudfassign
														.setDouble(probabilityobject);
												projectudfassign
														.setUDFTypeObjectId(129);
												projectudfassign
														.setForeignObjectId(projectobjectid);

												projectudfvaluedetails
														.add(projectudfassign);
												System.out
														.println(projectudfvaluedetails
																.size());

											}
										}
										projectudfassign = new UDFValue();
										if (turbinequantirycheck) {

										} else {
											if (turbinequantity != null) {
												if (!codecheck
														.contains(turbinequantity
																.toString())) {

													/*
													 * turbinequantityobject
													 * .setValue(Double
													 * .valueOf(turbinequantity
													 * .toString()));
													 */
													turbinequantityobject = objectfactory
															.createUDFValueDouble(Double
																	.valueOf(turbinequantity));
													projectudfassign
															.setDouble(turbinequantityobject);
													projectudfassign
															.setUDFTypeObjectId(128);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}
											}
										}

										projectudfassign = new UDFValue();
										if (cyclescheck) {

										} else {
											if (cycles != null) {
												if (!codecheck.contains(cycles
														.toString())) {

													/*
													 * cyclesobject
													 * .setValue(Double
													 * .valueOf(cycles
													 * .toString()));
													 */

													cyclesobject = objectfactory
															.createUDFValueDouble(Double
																	.valueOf(cycles));
													projectudfassign
															.setDouble(cyclesobject);
													projectudfassign
															.setUDFTypeObjectId(132);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}

											}
										}

										projectudfassign = new UDFValue();
										if (retrofitcampaignidcheck) {

										} else {
											if (retrofitcampaignid == null
													|| retrofitcampaignid
															.equals("")
													|| retrofitcampaignid
															.equals(DTTConstants.NONE)
													|| retrofitcampaignid
															.equals(DTTConstants.ZERO)) {
												retrofitcampaignid = "null";

											}

											if (!retrofitcampaignid
													.equals("null")) {

												projectudfassign
														.setText(retrofitcampaignid);

												projectudfassign
														.setUDFTypeObjectId(147);
												projectudfassign
														.setForeignObjectId(projectobjectid);
												projectudfvaluedetails
														.add(projectudfassign);
											}
										}
										projectudfassign = new UDFValue();
										if (siteaddresscheck) {

										} else {
											if (siteaddress == null
													|| siteaddress.equals("")
													|| siteaddress
															.equals(DTTConstants.NONE)
													|| siteaddress
															.equals(DTTConstants.ZERO)) {
												siteaddress = "null";

											}

											if (!siteaddress.equals("null")) {

												projectudfassign
														.setText(siteaddress);

												projectudfassign
														.setUDFTypeObjectId(171);
												projectudfassign
														.setForeignObjectId(projectobjectid);
												projectudfvaluedetails
														.add(projectudfassign);
											}
										}

									}

								}

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

										epsss2 = readProjectEPS(locationshoreobjectid);

									}
									if (epsss2 != null) {
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
									}
									List<Project> oppproject = new ArrayList<Project>();
									if (siteobjectid != 0) {
										oppproject = readProjectid(siteobjectid
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
												System.out
														.print(projectidreadelement
																.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}

										}
									}

									if (project) {
										readudf = readudfs
												.readUDFValue(projectobjectid);
										ListIterator<UDFValue> wbsudfidreadcheck = readudf
												.listIterator();
										while (wbsudfidreadcheck.hasNext()) {
											UDFValue wbsudfidreadelementcheck = wbsudfidreadcheck
													.next();

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(133)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												sapparentobjectidcheck = true;
												wbsudfidreadelementcheck
														.getUDFTypeObjectId();

											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(130)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												contractstartdatecheck = true;
												wbsudfidreadelementcheck
														.getUDFTypeObjectId();

											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(131)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												contractenddatecheck = true;
												wbsudfidreadelementcheck
														.getUDFTypeObjectId();

											}

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(147)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {

												retrofitcampaignidcheck = true;
												wbsudfidreadelementcheck
														.getUDFTypeObjectId();

											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(171)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {

												siteaddresscheck = true;
												wbsudfidreadelementcheck
														.getUDFTypeObjectId();

											}

										}
										if (!codecheck
												.contains(sapparentobjectid
														.toString())) {

											if (sapparentobjectidcheck) {
												/*
												 * //logger.info(
												 * "Project UDF already exists in Primavera"
												 * + sapparentobjectid);
												 */

											} else {

												if (sapparentobjectid != null
														|| sapparentobjectid != "") {

													codecheck
															.add(sapparentobjectid
																	.toString());
													projectudfassign
															.setText(sapparentobjectid);
													projectudfassign
															.setUDFTypeObjectId(133);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}

											}

											projectudfassign = new UDFValue();
											if (contractstartdatecheck) {

											} else {
												if (contractstartdate == null
														|| contractstartdate == ""
														|| contractstartdate
																.equals(DTTConstants.ZERO)) {
													contractstartdate = "null";

												}

												if (!contractstartdate
														.equals("null")) {
													contractstartdateobject = objectfactory
															.createUDFValueStartDate(toXMLGregorianCalendarWithoutTimeStamp(contractstartdate));
													/*
													 * projectudfassign
													 * .setText(
													 * contractstartdate);
													 */
													projectudfassign
															.setStartDate(contractstartdateobject);
													projectudfassign
															.setUDFTypeObjectId(130);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}
											}
											projectudfassign = new UDFValue();
											if (contractenddatecheck) {

											} else {
												if (contractenddate == null
														|| contractenddate == ""
														|| contractenddate
																.equals(DTTConstants.ZERO)) {
													contractenddate = "null";

												}

												if (!contractenddate
														.equals("null")) {

													/*
													 * projectudfassign
													 * .setText(
													 * contractenddate);
													 */
													contractenddateobject = objectfactory
															.createUDFValueFinishDate(toXMLGregorianCalendarWithoutTimeStamp(contractenddate));
													projectudfassign
															.setFinishDate(contractenddateobject);
													projectudfassign
															.setUDFTypeObjectId(131);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}
											}

											projectudfassign = new UDFValue();
											if (retrofitcampaignidcheck) {

											} else {
												if (retrofitcampaignid == null
														|| retrofitcampaignid
																.equals("")
														|| retrofitcampaignid
																.equals(DTTConstants.NONE)
														|| retrofitcampaignid
																.equals(DTTConstants.ZERO)) {
													retrofitcampaignid = "null";

												}

												if (!retrofitcampaignid
														.equals("null")) {

													projectudfassign
															.setText(retrofitcampaignid);

													projectudfassign
															.setUDFTypeObjectId(147);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}
											}
											projectudfassign = new UDFValue();
											if (siteaddresscheck) {

											} else {
												if (siteaddress == null
														|| siteaddress
																.equals("")
														|| siteaddress
																.equals(DTTConstants.NONE)
														|| siteaddress
																.equals(DTTConstants.ZERO)) {
													siteaddress = "null";

												}

												if (!siteaddress.equals("null")) {

													projectudfassign
															.setText(siteaddress);

													projectudfassign
															.setUDFTypeObjectId(171);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfvaluedetails
															.add(projectudfassign);
												}
											}

										}
									}
								}

							}

							System.out.println("projectudfvaluedetails.size()"
									+ projectudfvaluedetails.size());
							if (projectudfvaluedetails != null) {
								ListIterator<UDFValue> read = projectudfvaluedetails
										.listIterator();
								while (read.hasNext()) {

									UDFValue projreadelement = read.next();

									// System.out.print(countryobjectid);
									/*
									 * System.out.println(projreadelement.getDouble
									 * ().getValue());
									 */
									/*
									 * System.out.println(projreadelement
									 * .getText());
									 * System.out.println(projreadelement
									 * .getForeignObjectId());
									 */

								}

							}

						}
					}
				}
			}

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return projectudfvaluedetails;

	}

	public List<UDFValue> checkAndUpdateProjectUDF() throws Exception {

		List<UDFValue> projectudfupdatedetails = new ArrayList<UDFValue>();
		// logger.info("Check and Update  Project UDF");
		try {
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			projectmasterbean = ProjectMasterDAO.getProjectMasterDataDAO();

			projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = projectmasterbean.getProjectmasterdata();
			PrimaveraProject projectworktype = new PrimaveraProject();

			PrimaveraProjectUDF projectudfvalue = new PrimaveraProjectUDF();

			List epslist = new ArrayList();
			// epslist.add("Opp");

			List countrylist = new ArrayList();
			// epsmasterbean.getSfdccountrylist();

			List<EPS> projectcountryepsdata = new ArrayList<EPS>();

			EPS epscountrycreate = new EPS();
			List<Project> projectdata = new ArrayList<Project>();

			/*
			 * List<Project> epssitedetails = new ArrayList<Project>();
			 */

			List<EPS> epss = new ArrayList<EPS>();
			epss = readEPSforAll();

			/*
			 * List<UDFType> udfcode = new ArrayList<UDFType>(); udfcode =
			 * readProjectUDF(); ListIterator<UDFType> projectudfread =
			 * udfcode.listIterator(); while (projectudfread.hasNext()) {
			 * 
			 * UDFType udfreadelement = projectudfread.next();
			 * 
			 * 
			 * System.out.println(udfreadelement.getDataType() + " " +
			 * udfreadelement.getSubjectArea() + " " + udfreadelement.getTitle()
			 * + " " + udfreadelement.getObjectId()); }
			 */

			List<Project> epsoppurtunitydetails = new ArrayList<Project>();
			List<Project> epsmainprojectdetails = new ArrayList<Project>();

			List<String> oppcheck = new ArrayList<String>();

			List<String> maintenancecheck = new ArrayList<String>();
			List<String> codecheck = new ArrayList<String>();
			List projectcountryList = new ArrayList();
			List<String> recordlist = new ArrayList<String>();
			for (Map<String, Object> map : projectmasterdata) {
				Project projectoppcreate = new Project();

				Project projectmaintenancecreate = new Project();
				String countryid = null;
				String cycles = null;
				Double primaveracycles = null;
				String externalstatus = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String locationshorecode = null;
				String siteid = null;
				String siteaddress = null;
				Integer worktypestring = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				String primaverasapparentid = null;
				String contractstartdate = "";
				JAXBElement<XMLGregorianCalendar> primaveracontractstartdate = null;
				String contractenddate = "";
				JAXBElement<XMLGregorianCalendar> primaveracontractenddate = null;
				String customerifa = null;
				String primaveracustomerifa = null;
				String retrofitcampaignid = null;
				String primaveraretrofitcampaignid = null;
				String customername = null;
				String probability = null;
				Double primaveraprobability = null;
				String turbinequantity = null;
				Double primaveraturbinequantity = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					ProjectCodeAssignment sfdcprojectcodecreate = new ProjectCodeAssignment();
					UDFValue projectudfassign = new UDFValue();
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = 0;
					ObjectFactory objectfactory = new ObjectFactory();
					JAXBElement<XMLGregorianCalendar> contractstartdateobject = null;
					JAXBElement<XMLGregorianCalendar> contractenddateobject = null;
					JAXBElement<Double> probabilityobject = null;
					JAXBElement<Double> turbinequantityobject = null;
					JAXBElement<Double> cyclesobject = null;

					JAXBElement<Integer> countryparentobjecctid = null;
					Integer opputunityobjectid = null;
					Integer projectobjectid = null;
					JAXBElement<Integer> projectobject = null;
					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean worktypecheck = false;
					boolean externalstatuscheck = false;
					boolean locationshorecheck = false;
					boolean probabilitycheck = false;
					boolean turbinequantitycheck = false;
					boolean cyclescheck = false;
					boolean sapparentobjectidcheck = false;
					boolean contractstartdatecheck = false;
					boolean contractenddatecheck = false;
					boolean customerifacheck = false;
					boolean retrofitcampaignidcheck = false;

					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYIDCREATE)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;
						locationshorecode = (String) value;
						if (locationshorecode.contains(DTTConstants.OFFSHORE)) {
							locationshorecode = DTTConstants.OFFSHORECODE;
						}
						if (locationshorecode.contains(DTTConstants.ONSHORE)) {
							locationshorecode = DTTConstants.ONSHORECODE;
						}

						// System.out.println(locationshore);
					}
					if (key.contains("Project_Number")) {
						projectnumber = (String) value;
						recordlist.add(projectnumber);
						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNAME)) {
						projectname = (String) value;

						// System.out.println(projectname);
					}

					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains("Work_Type")) {
						worktype = Integer.valueOf((String) value);

						if (worktype == DTTConstants.WORKTYPE_1) {
							// worktypestring = "Prev.Main";
							worktypestring = DTTConstants.WORKTYPESTRING_1;

						}
						if (worktype == DTTConstants.WORKTYPE_2) {
							// worktypestring = "Retrofit";
							worktypestring = DTTConstants.WORKTYPESTRING_2;

						}
						if (worktype == DTTConstants.WORKTYPE_3) {
							// worktypestring = "Mods&up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}
						if (worktype == DTTConstants.WORKTYPE_4) {
							// worktypestring = "Repairs";
							worktypestring = DTTConstants.WORKTYPESTRING_4;
						}
						if (worktype == DTTConstants.WORKTYPE_5) {
							// worktypestring = "Service";
							worktypestring = DTTConstants.WORKTYPESTRING_5;
						}
						if (worktype == DTTConstants.WORKTYPE_6) {
							// worktypestring = "Service extension";
							worktypestring = DTTConstants.WORKTYPESTRING_6;
						}
						if (worktype == DTTConstants.WORKTYPE_7) {
							// worktypestring = "Mods&Up";
							worktypestring = DTTConstants.WORKTYPESTRING_3;
						}

						/* System.out.println(worktypestring); */
					}

					if (key.contains("SAP_Parent_Object_ID")) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.CUSTOMER_IFA)) {
						customerifa = (String) (value);

						/* System.out.println(customerifa); */
					}
					if (key.contains(DTTConstants.CUSTOMER_NAME)) {
						customername = (String) (value);

						/* System.out.println(customername); */
					}
					if (key.contains(DTTConstants.PROBABILITY)) {
						probability = (String) (value);

						/* System.out.println(probability); */
					}

					if (key.contains(DTTConstants.CONTRACT_START)) {
						contractstartdate = (String) (value);

						/* System.out.println(contractstartdate); */
					}
					if (key.contains(DTTConstants.CONTRACT_END)) {
						contractenddate = (String) (value);

						/* System.out.println(contractenddate); */
					}
					if (key.contains("Site_Id")) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains("Site_Address")) {
						siteaddress = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_QUANTITY)) {
						turbinequantity = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.CYCLES)) {
						cycles = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains("Retrofit_Campaign_ID")) {
						retrofitcampaignid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.EXTERNAL_STATUS)) {
						externalstatus = (String) (value);
						if (externalstatus != null) {
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_OPEN)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_OPENCODE;

							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_LOST)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_LOSTCODE;
							}
							if (externalstatus
									.contains(DTTConstants.EXTERNAL_STATUS_CTO)) {
								externalstatus = DTTConstants.EXTERNAL_STATUS_CTOCODE;
							}
						}

						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& projectnumber != null && projectname != null) {
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

							epsss1 = readProjectEPS(countryobjectid);

						}
						List<UDFValue> readudf = new ArrayList<UDFValue>();
						PrimaveraProjectUDF readudfs = new PrimaveraProjectUDF();

						/*
						 * ListIterator<UDFValue> wbsudfidread = readudf
						 * .listIterator();
						 * 
						 * while (wbsudfidread.hasNext()) { UDFValue
						 * wbsudfidreadelement = wbsudfidread.next();
						 * 
						 * if (wbsudfidreadelement.getCodeValue() != null)
						 * 
						 * System.out.println("code " +
						 * wbsudfidreadelement.getCodeValue());
						 * 
						 * if (wbsudfidreadelement.getText() != null)
						 * 
						 * System.out.println("Text" +
						 * wbsudfidreadelement.getText());
						 * 
						 * if (wbsudfidreadelement.getDouble() != null)
						 * 
						 * System.out.println("Double" +
						 * wbsudfidreadelement.getDouble() .getValue());
						 * 
						 * if (wbsudfidreadelement .getUDFTypeObjectId().equals(
						 * 128)) {
						 * 
						 * turbinequantityobject = wbsudfidreadelement
						 * .getDouble(); } if
						 * (wbsudfidreadelement.getUDFTypeObjectId()
						 * .equals(129)) { probabilityobject =
						 * wbsudfidreadelement .getDouble();
						 * 
						 * } if (wbsudfidreadelement.getUDFTypeObjectId()
						 * .equals(132)) { cyclesobject =
						 * wbsudfidreadelement.getDouble();
						 * 
						 * } if (wbsudfidreadelement.getInteger() != null)
						 * 
						 * System.out.println("Integer" +
						 * wbsudfidreadelement.getInteger());
						 * 
						 * if (wbsudfidreadelement.getUDFTypeTitle() != null)
						 * 
						 * System.out .println("type title" +
						 * wbsudfidreadelement .getUDFTypeTitle());
						 * 
						 * if (wbsudfidreadelement .getForeignObjectId() !=
						 * null)
						 * 
						 * System.out.println("F Object" + wbsudfidreadelement
						 * .getForeignObjectId());
						 * 
						 * if (wbsudfidreadelement .getUDFTypeObjectId() !=
						 * null)
						 * 
						 * System.out.println("UDF type" + wbsudfidreadelement
						 * .getUDFTypeObjectId());
						 * 
						 * if (wbsudfidreadelement .getUDFCodeObjectId() !=
						 * null) System.out .println("UDF Code" +
						 * wbsudfidreadelement .getUDFTypeDataType());
						 * 
						 * }
						 */

						if (epsss1 != null && sourcesystem != null) {

							if (sourcesystem.contains("SFDC")) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACHOPP
											+ countryid;
									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement
											.getName()
											.startsWith(
													DTTConstants.STARTS_WITH_OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											// System.out.print(projectepsreadelement.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}
								List<Project> oppproject = new ArrayList<Project>();
								if (opputunityobjectid != 0) {
									oppproject = readProjectid(siteobjectid
											.toString());
								}
								if (oppproject.size() != 0) {
									ListIterator<Project> projectidread = oppproject
											.listIterator();

									while (projectidread.hasNext()) {
										Project projectidreadelement = projectidread
												.next();

										/*
										 * System.out.print(projectidreadelement.
										 * getObjectId() +
										 * " "+projectidreadelement
										 * .getParentEPSObjectId());
										 */

										if (projectidreadelement.getId()
												.equals(projectnumber)) {
											project = true;
											/*
											 * System.out.print(projectidreadelement
											 * .getName());
											 */
											projectobjectid = projectidreadelement
													.getObjectId();
											System.out.print(projectobjectid);
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}
									}
									if (project) {
										readudf = readudfs
												.readUDFValue(projectobjectid);
										ListIterator<UDFValue> wbsudfidreadcheck = readudf
												.listIterator();

										while (wbsudfidreadcheck.hasNext()) {
											UDFValue wbsudfidreadelementcheck = wbsudfidreadcheck
													.next();

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(129)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												probabilitycheck = true;
												probabilityobject = wbsudfidreadelementcheck
														.getDouble();
												primaveraprobability = wbsudfidreadelementcheck
														.getDouble().getValue();
											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(128)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												turbinequantitycheck = true;
												turbinequantityobject = wbsudfidreadelementcheck
														.getDouble();
												primaveraturbinequantity = wbsudfidreadelementcheck
														.getDouble().getValue();
											}
											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(132)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												cyclescheck = true;
												cyclesobject = wbsudfidreadelementcheck
														.getDouble();
												primaveracycles = wbsudfidreadelementcheck
														.getDouble().getValue();
											}

											if (wbsudfidreadelementcheck
													.getUDFTypeObjectId()
													.equals(147)
													&& wbsudfidreadelementcheck
															.getForeignObjectId()
															.equals(projectobjectid)) {
												retrofitcampaignidcheck = true;
												wbsudfidreadelementcheck
														.getUDFTypeObjectId();
												primaveraretrofitcampaignid = wbsudfidreadelementcheck
														.getText();

											}

										}

										if (probability != null
												&& turbinequantity != null
												&& cycles != null) {
											if (probabilitycheck) {
												if (!primaveraprobability
														.equals(Double
																.valueOf(probability))) {
													if (probability != null
															|| probability != " ") {

														/*
														 * probabilityobject
														 * .setValue(Double
														 * .valueOf
														 * (probability));
														 */
														probabilityobject = objectfactory
																.createUDFValueDouble(Double
																		.valueOf(probability));
														projectudfassign
																.setDouble(probabilityobject);
														projectudfassign
																.setUDFTypeObjectId(129);
														projectudfassign
																.setForeignObjectId(projectobjectid);
														projectudfupdatedetails
																.add(projectudfassign);
														System.out
																.println(projectudfupdatedetails
																		.size());

													}
												}

											}

										}
										projectudfassign = new UDFValue();
										if (turbinequantitycheck) {
											if (!primaveraturbinequantity
													.equals(Double
															.valueOf(turbinequantity))) {
												if (!codecheck
														.contains(turbinequantity
																.toString())) {

													/*
													 * turbinequantityobject
													 * .setValue(Double
													 * .valueOf(turbinequantity
													 * .toString()));
													 */
													turbinequantityobject = objectfactory
															.createUDFValueDouble(Double
																	.valueOf(turbinequantity));
													projectudfassign
															.setDouble(turbinequantityobject);
													projectudfassign
															.setUDFTypeObjectId(128);
													projectudfassign
															.setForeignObjectId(projectobjectid);
													projectudfupdatedetails
															.add(projectudfassign);
												}

											}

											projectudfassign = new UDFValue();
											if (retrofitcampaignidcheck) {
												if (retrofitcampaignid == null
														|| retrofitcampaignid
																.equals("")
														|| retrofitcampaignid
																.equals(DTTConstants.NONE)
														|| retrofitcampaignid
																.equals(DTTConstants.ZERO)) {
													retrofitcampaignid = "null";

												}

												if (!retrofitcampaignid
														.equals("null")) {
													if (!primaveraretrofitcampaignid
															.contains(retrofitcampaignid)) {

														projectudfassign
																.setText(retrofitcampaignid);

														projectudfassign
																.setUDFTypeObjectId(147);
														projectudfassign
																.setForeignObjectId(projectobjectid);
														projectudfupdatedetails
																.add(projectudfassign);
													}

												}
											}
											projectudfassign = new UDFValue();
											if (cyclescheck) {
												if (!primaveracycles
														.equals(Double.valueOf(cycles
																.toString()))) {
													if (cycles != null) {
														if (!codecheck
																.contains(cycles
																		.toString())) {

															/*
															 * cyclesobject
															 * .setValue(Double
															 * .valueOf(cycles
															 * .toString()));
															 */
															cyclesobject = objectfactory
																	.createUDFValueDouble(Double
																			.valueOf(cycles));
															projectudfassign
																	.setDouble(cyclesobject);
															projectudfassign
																	.setUDFTypeObjectId(132);
															projectudfassign
																	.setForeignObjectId(projectobjectid);
															projectudfupdatedetails
																	.add(projectudfassign);
														}

													}
												}
											}
										}

									}

								}

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
									 * // System.out.print(element + " "); if
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

										epsss2 = readProjectEPS(locationshoreobjectid);

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
										oppproject = readProjectid(siteobjectid
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
												System.out
														.print(projectidreadelement
																.getName());
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

											}

										}
									}
									if (project) {
										if (projectobjectid != null) {
											readudf = readudfs
													.readUDFValue(projectobjectid);
										}
										if (readudf.size() != 0) {
											ListIterator<UDFValue> wbsudfidreadcheck = readudf
													.listIterator();
											while (wbsudfidreadcheck.hasNext()) {
												UDFValue wbsudfidreadelementcheck = wbsudfidreadcheck
														.next();

												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId()
														.equals(133)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(projectobjectid)) {
													sapparentobjectidcheck = true;
													wbsudfidreadelementcheck
															.getUDFTypeObjectId();
													primaverasapparentid = wbsudfidreadelementcheck
															.getText();

												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId()
														.equals(130)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(projectobjectid)) {
													contractstartdatecheck = true;
													wbsudfidreadelementcheck
															.getUDFTypeObjectId();
													primaveracontractstartdate = wbsudfidreadelementcheck
															.getStartDate();

												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId()
														.equals(131)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(projectobjectid)) {
													contractenddatecheck = true;
													wbsudfidreadelementcheck
															.getUDFTypeObjectId();
													primaveracontractenddate = wbsudfidreadelementcheck
															.getFinishDate();

												}

												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId()
														.equals(147)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(projectobjectid)) {
													retrofitcampaignidcheck = true;
													wbsudfidreadelementcheck
															.getUDFTypeObjectId();
													primaveraretrofitcampaignid = wbsudfidreadelementcheck
															.getText();

												}

											}
										}

										projectudfassign = new UDFValue();
										if (!codecheck
												.contains(sapparentobjectid
														.toString())) {
											if (sapparentobjectidcheck) {

												if (sapparentobjectid != null
														|| sapparentobjectid != "") {
													if (!primaverasapparentid
															.contains(sapparentobjectid)) {

														codecheck
																.add(sapparentobjectid
																		.toString());
														projectudfassign
																.setText(sapparentobjectid);
														projectudfassign
																.setUDFTypeObjectId(133);
														projectudfassign
																.setForeignObjectId(projectobjectid);
														projectudfupdatedetails
																.add(projectudfassign);
													}

												}
											}

											projectudfassign = new UDFValue();

											if (contractstartdatecheck) {
												if (contractstartdate == null
														|| contractstartdate == ""
														|| contractstartdate
																.equals(DTTConstants.ZERO)) {
													contractstartdate = "null";

												}

												if (!contractstartdate
														.equals("null")) {
													if (!primaveracontractstartdate
															.getValue()
															.equals(toXMLGregorianCalendarWithoutTimeStamp(contractstartdate))) {

														contractstartdateobject = objectfactory
																.createUDFValueStartDate(toXMLGregorianCalendarWithoutTimeStamp(contractstartdate));
														/*
														 * projectudfassign
														 * .setText
														 * (contractstartdate);
														 */
														projectudfassign
																.setStartDate(contractstartdateobject);

														projectudfassign
																.setUDFTypeObjectId(130);
														projectudfassign
																.setForeignObjectId(projectobjectid);
														projectudfupdatedetails
																.add(projectudfassign);
													}

												}
											}
											projectudfassign = new UDFValue();
											if (contractenddatecheck) {
												if (contractenddate == null
														|| contractenddate == ""
														|| contractenddate
																.equals(DTTConstants.ZERO)) {
													contractenddate = "null";

												}

												if (!contractenddate
														.equals("null")) {
													if (!primaveracontractenddate
															.getValue()
															.equals(toXMLGregorianCalendarWithoutTimeStamp(contractstartdate))) {
														contractenddateobject = objectfactory
																.createUDFValueFinishDate(toXMLGregorianCalendarWithoutTimeStamp(contractenddate));
														/*
														 * projectudfassign
														 * .setText
														 * (contractenddate);
														 */
														projectudfassign
																.setFinishDate(contractenddateobject);

														projectudfassign
																.setUDFTypeObjectId(131);
														projectudfassign
																.setForeignObjectId(projectobjectid);
														projectudfupdatedetails
																.add(projectudfassign);
													}

												}
											}

											projectudfassign = new UDFValue();
											if (retrofitcampaignidcheck) {
												if (retrofitcampaignid == null
														|| retrofitcampaignid
																.equals("")
														|| retrofitcampaignid
																.equals(DTTConstants.NONE)
														|| retrofitcampaignid
																.equals(DTTConstants.ZERO)) {
													retrofitcampaignid = "null";

												}

												if (!retrofitcampaignid
														.equals("null")) {
													if (!primaveraretrofitcampaignid
															.contains(retrofitcampaignid)) {

														projectudfassign
																.setText(retrofitcampaignid);

														projectudfassign
																.setUDFTypeObjectId(147);
														projectudfassign
																.setForeignObjectId(projectobjectid);
														projectudfupdatedetails
																.add(projectudfassign);
													}

												}
											}
											logger.info("Project Object ID"
													+ projectobjectid);
										}

									}
								}
							}

							System.out.println("projectudfvaluedetails.size()"
									+ projectudfupdatedetails.size());
							if (projectudfupdatedetails != null) {
								ListIterator<UDFValue> read = projectudfupdatedetails
										.listIterator();
								while (read.hasNext()) {

									UDFValue projreadelement = read.next();

									// System.out.print(countryobjectid);
									/*
									 * System.out.println(projreadelement.getDouble
									 * ().getValue());
									 */
									/*
									 * System.out.println(projreadelement
									 * .getText());
									 * System.out.println(projreadelement
									 * .getForeignObjectId());
									 */

								}

							}

						}
					}
				}
			}
			UpdateProcessRecordsDAO processed = new UpdateProcessRecordsDAO();
			processed.updaterecords(DTTConstants.PROJECTMASTER, "Y",
					recordlist, "Success", "Project_Number");

			System.out.println(projectdata);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return projectudfupdatedetails;

	}

	public static XMLGregorianCalendar toXMLGregorianCalendarWithoutTimeStamp(
			String date) {
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

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) throws Exception {
		PrimaveraProjectWSClient wssclient = new PrimaveraProjectWSClient();
		wssclient.checkAndCreateProjectUDF();
	}
}