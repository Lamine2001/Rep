package com.siemens.windpower.fltp.p6wsclient;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.udftype.UDFType;
import com.primavera.ws.p6.udfvalue.ObjectFactory;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.fltp.beans.ActivityMasterBean;
import com.siemens.windpower.fltp.dao.ActivityMasterDAO;
import com.siemens.windpower.fltp.dao.ProjectMasterDAO;
import com.siemens.windpower.fltp.dao.UpdateProcessRecordsDAO;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;


public class PrimaveraWBSWSClient {

	ActivityMasterBean activitymasterbean = null;
	Logger logger = null;
	List<Map<String, Object>> activitymasterdata =null;
	public PrimaveraWBSWSClient() throws Exception {
		logger = Logger.getLogger(PrimaveraWBSWSClient.class);
		
	}

	@SuppressWarnings("unchecked")
	public List<WBS> checkAndCreateWBS() throws Exception {
		List<WBS> createwbs = new ArrayList<WBS>();
		//logger.info("Check and Create  WBS");
		try {
			
			PrimaveraWBS wbstype = new PrimaveraWBS();
			ActivityMasterDAO activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getWBStMasterDAO();
			activitymasterdata= new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getWbsmasterdata();
			
			PrimaveraEPS epsclient = new PrimaveraEPS();
			PrimaveraProjectUDF projudf = new PrimaveraProjectUDF();
			List<UDFValue> projectudfvaluedetails = new ArrayList<UDFValue>();
			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();
			
			List wbscheck = new ArrayList();
			List ukwbscheck = new ArrayList();
			List ukwbshqcheck = new ArrayList();
			List ukwbsnewhqcheck = new ArrayList();
			List ukwbsprojectcheck = new ArrayList();
			List wbshqcheck = new ArrayList();
			List wbsnewhqcheck = new ArrayList();
			List wbsprojectcheck = new ArrayList();
			List wbsudfcheck = new ArrayList();
			//logger.info("activitymasterdata" + activitymasterdata.size());
			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String hqtechid = null;
				String rowid = null;

				String worktypestring = null;
				String turbinename = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				Date contractstartdate = null;
				Date contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					/* //logger.info("activitymasterdata map size"+map.size());*/
					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					WBS wbscreate = new WBS();
					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;
					String activityobjectid = null;
					Integer wbsobjectid = null;

					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean wbs = false;
					boolean wbsudf = false;
					UDFValue projectudfassign = new UDFValue();
					String key = entry.getKey();
					Object value = entry.getValue();

					if (key.contains(DTTConstants.COUNTRYID)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains(DTTConstants.LOCATIONSHORE)) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains(DTTConstants.TURBINE_NAME)) {
						turbinename = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNUMBER)) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains(DTTConstants.SOURCESYSTEM)) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains(DTTConstants.WORKTYPE)) {
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

						/* System.out.println(worktypestring); */
					}

					if (key.contains(DTTConstants.SAP_PARENT_OBJECT_ID)) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}

					if (key.contains(DTTConstants.SITEID)) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.DTT_ACTIVITY_OBJECT_ID)) {
						dttactivityid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.HQ_TECH_ID_NUMBER)) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.ROWID)) {
						rowid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& turbinename != null && locationshore != null
							&& dttactivityid != null && hqtechid != null
							&& rowid != null && projectnumber != null) {
						ListIterator<EPS> epsread = epss.listIterator();
						/* //logger.info("HQtechid"+hqtechid); */
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
							 System.out.println(countryobjectid);

							epsss1 = epsclient
									.readWSProjectEPS(countryobjectid);

						}
						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains(DTTConstants.SFDC)) {

								ListIterator<EPS> projectepsread = epsss1
										.listIterator();

								while (projectepsread.hasNext()) {
									EPS projectepsreadelement = projectepsread
											.next();
									String locationshorestring = DTTConstants.ATTACH_OPP
											+ countryid;
									/*
									 * System.out.print(projectepsreadelement.
									 * getName() +
									 * " "+projectepsreadelement.getParentObjectId
									 * ().getValue() );
									 */
									if (projectepsreadelement.getName()
											.startsWith(DTTConstants.OPPOR)) {
										if (projectepsreadelement.getId()
												.equals(locationshorestring)) {
											opportunity = true;
											// System.out.print(projectepsreadelement.getName());
											opputunityobjectid = projectepsreadelement
													.getObjectId().toString();
											// opputunityparentobjectid=projectepsreadelement.getParentObjectId();

										}

									}

								}List<Project> oppproject = new ArrayList<Project>();

								oppproject = projectref.opputunityReadProject(opputunityobjectid.toString());

								if (oppproject != null) {
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
								}

								List<WBS> readwbs = new ArrayList<WBS>();
								PrimaveraWBS wbsreadele = new PrimaveraWBS();
if(projectobjectid!=null){
								readwbs = wbsreadele.readWBS(projectobjectid
										.toString());
}
								ListIterator<WBS> wbsidread = readwbs
										.listIterator();
if(readwbs.size()!=0){
								while (wbsidread.hasNext()) {
									WBS wbsidreadelement = wbsidread.next();

									if (wbsidreadelement.getName().equals(
											turbinename)) {
										wbs = true;
										/*
										 * System.out.print(projectidreadelement
										 * .getName());
										 */

										wbsobjectid = wbsidreadelement
												.getObjectId();

										/*
										 * wbsjaxbElement = new JAXBElement( new
										 * QName("WBSObjectId"),Activity.class,
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

							} else {

								if (locationshore != ""
										|| locationshore != null) {
									if (locationshore.equals(DTTConstants.ONSHORE)) {
										locationshore = DTTConstants.ATTACH_ONS + countryid;
									}
									if (locationshore.equals(DTTConstants.OFFSHORE)) {
										locationshore = DTTConstants.ATTACH_OFS + countryid;
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
											/*
											 * //logger.info(
											 * "Site exsits in Primavera"+siteid
											 * );
											 */

										}
									}
									List<Project> oppproject = new ArrayList<Project>();
if(siteobjectid!=null){
									oppproject = projectref.opputunityReadProject(siteobjectid.toString());
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
												/*
												 * //logger.info(
												 * "Project exsits in Primavera"
												 * +projectidreadelement
												 * .getName());
												 */
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}

									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();
if(projectobjectid!=null){
									readwbs = wbsreadele
											.readWBS(projectobjectid.toString());
}
/*System.out.print("readwbs projectobjectid"+projectobjectid);
System.out.print("readwbs"+readwbs);
System.out.print("readwbs.size()"+readwbs.size());*/

									if (readwbs.size() != 0) {
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										while (wbsidread.hasNext()) {
											WBS wbsidreadelement = wbsidread
													.next();
											if (countryid
													.equalsIgnoreCase("GB")
													|| countryid
															.equalsIgnoreCase("IE")) {
												
												if (wbsidreadelement.getCode().equalsIgnoreCase(hqtechid)) {
													wbs = true;
													/*
													 * System.out.print(
													 * projectidreadelement
													 * .getName());
													 */

													wbsobjectid = wbsidreadelement
															.getObjectId();
													
												}
											}else{
													if (wbsidreadelement.getCode()
															.equalsIgnoreCase(dttactivityid)) {
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
														 * ),Activity.class,
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
									}
									if (project) {
										if (wbs) {
											/*//logger.info("WBS exists in primavera with "
													+ turbinename
													+ "for"
													+ projectnumber);*/
										} else {
											if(turbinename!=null){
												if (countryid
														.equalsIgnoreCase("GB")
														|| countryid
																.equalsIgnoreCase("IE")) {

													if (!(ukwbscheck
															.contains(hqtechid
																	.toString()))) {
														if(!ukwbsprojectcheck.contains(projectobjectid.toString())) {
															//ukwbsnewhqcheck=new ArrayList();
															
															ukwbsnewhqcheck.add(hqtechid.toString());
														ukwbshqcheck.add(hqtechid.toString());
														ukwbscheck.add(dttactivityid
																.toString());
														ukwbsprojectcheck.add(projectobjectid.toString());
														
														wbscreate
																.setProjectObjectId(projectobjectid);
														wbscreate.setName(turbinename);
														wbscreate.setCode(hqtechid
																.toString());
														logger.info(" Inside new Project dttactivityid"
																+ dttactivityid
																+ "ProjectNumber"
																+ projectnumber
																+"HQ"+hqtechid);
														createwbs.add(wbscreate);
														
																
														}else
														{
															if(ukwbsprojectcheck.contains(projectobjectid.toString())) {
																if (!ukwbshqcheck
																		.contains(hqtechid.toString())) {
																	 wbscreate = new WBS();
																	 ukwbshqcheck.add(hqtechid.toString());
																	 ukwbsnewhqcheck.add(hqtechid.toString());
																	wbscreate
																	.setProjectObjectId(projectobjectid);
															wbscreate.setName(turbinename);
															wbscreate.setCode(hqtechid
																	.toString());
															logger.info("Inside HQ dttactivityid"
																	+ dttactivityid
																	+ "ProjectNumber"
																	+ projectnumber
																	+"HQ"+hqtechid);
															createwbs.add(wbscreate);
																	
																}else{
																	if (!ukwbsnewhqcheck
																			.contains(hqtechid.toString())) {
																		
																	 wbscreate = new WBS();
																	 ukwbsnewhqcheck.add(hqtechid.toString());
																	 
																	wbscreate
																	.setProjectObjectId(projectobjectid);
															wbscreate.setName(turbinename);
															wbscreate.setCode(hqtechid
																	.toString());
															logger.info(" Inside new HQ dttactivityid"
																	+ dttactivityid
																	+ "ProjectNumber"
																	+ projectnumber
																	+"HQ"+hqtechid);
															createwbs.add(wbscreate);
																		}
																
																}
																
																
															}
														}
														
														
													
													}
												
												}else{
											if (!(wbscheck
													.contains(dttactivityid
															.toString()))) {
												if(!wbsprojectcheck.contains(projectobjectid.toString())) {
													wbsnewhqcheck=new ArrayList();
													
													wbsnewhqcheck.add(hqtechid.toString());
												wbshqcheck.add(hqtechid.toString());
												wbscheck.add(dttactivityid
														.toString());
												wbsprojectcheck.add(projectobjectid.toString());
												/*wbscheck.add(rowid.toString());
												wbscheck.add(projectnumber);*/
												/*//logger.info("turbinename"
														+ turbinename);*/
												wbscreate
														.setProjectObjectId(projectobjectid);
												wbscreate.setName(turbinename);
												wbscreate.setCode(dttactivityid
														.toString());
												logger.info(" Inside new Project dttactivityid"
														+ dttactivityid
														+ "ProjectNumber"
														+ projectnumber
														+"HQ"+hqtechid);
												createwbs.add(wbscreate);
												
														
												}else
												{
													if(wbsprojectcheck.contains(projectobjectid.toString())) {
														if (!wbshqcheck
																.contains(hqtechid.toString())) {
															 wbscreate = new WBS();
															 wbshqcheck.add(hqtechid.toString());
															 wbsnewhqcheck.add(hqtechid.toString());
															wbscreate
															.setProjectObjectId(projectobjectid);
													wbscreate.setName(turbinename);
													wbscreate.setCode(dttactivityid
															.toString());
													logger.info("Inside HQ dttactivityid"
															+ dttactivityid
															+ "ProjectNumber"
															+ projectnumber
															+"HQ"+hqtechid);
													createwbs.add(wbscreate);
															
														}else{
															if (!wbsnewhqcheck
																	.contains(hqtechid.toString())) {
																
															 wbscreate = new WBS();
															 wbsnewhqcheck.add(hqtechid.toString());
															 
															wbscreate
															.setProjectObjectId(projectobjectid);
													wbscreate.setName(turbinename);
													wbscreate.setCode(dttactivityid
															.toString());
													logger.info(" Inside new HQ dttactivityid"
															+ dttactivityid
															+ "ProjectNumber"
															+ projectnumber
															+"HQ"+hqtechid);
													createwbs.add(wbscreate);
																}
														
														}
														
														
													}
												}
												
												
											
											}
										}
											}
										}

										System.out.println("createwbs  "
												+ createwbs.size());

									}

								}

							}
						}

					}
				}
			}
			System.out.println("createwbs  " + createwbs.size());
			System.out.println("projectudfvaluedetails  "
					+ projectudfvaluedetails.size());
			//logger.info("create wbs  " + createwbs.size());

			// projudf.CreateUDFValue(projectudfvaluedetails);
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return createwbs;
	}

	public List<WBS> checkAndUpdateWBS() throws Exception {
		List<WBS> updatewbs = new ArrayList<WBS>();
		//logger.info("Check and Update  WBS");
		try {
			
			PrimaveraWBS wbstype = new PrimaveraWBS();
			ActivityMasterDAO activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getWBStMasterDAO();
			activitymasterdata= new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getWbsmasterdata();
			
			PrimaveraEPS epsclient = new PrimaveraEPS();
			PrimaveraProjectUDF projudf = new PrimaveraProjectUDF();
			List<UDFValue> projectudfvaluedetails = new ArrayList<UDFValue>();
			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();
			List<String> recordlist= new ArrayList<String>();
			List ukwbscheck = new ArrayList();
			List ukwbshqcheck = new ArrayList();
			List ukwbsnewhqcheck = new ArrayList();
			List ukwbsprojectcheck = new ArrayList();
			List wbscheck = new ArrayList();
			
			List wbsudfcheck = new ArrayList();

			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String hqtechid = null;
				String rowid = null;
				String primaverawbscode = null;
				Integer primaverawbsobjectid = null;

				String worktypestring = null;
				String turbinename = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				Date contractstartdate = null;
				Date contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					WBS wbsupdate = new WBS();
					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;
					String activityobjectid = null;
					Integer wbsobjectid = null;

					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean wbs = false;
					boolean wbsudf = false;
					UDFValue projectudfassign = new UDFValue();
					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYID)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains(DTTConstants.LOCATIONSHORE)) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains(DTTConstants.TURBINE_NAME)) {
						turbinename = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNUMBER)) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains(DTTConstants.SOURCESYSTEM)) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains(DTTConstants.WORKTYPE)) {
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

						/* System.out.println(worktypestring); */
					}

					if (key.contains(DTTConstants.SAP_PARENT_OBJECT_ID)) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}

					if (key.contains(DTTConstants.SITEID)) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.DTT_ACTIVITY_OBJECT_ID)) {
						dttactivityid = (String) (value);
						recordlist.add(dttactivityid);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.HQ_TECH_ID_NUMBER)) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.ROWID)) {
						rowid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (countryid != null && sourcesystem != null
							&& turbinename != null && locationshore != null
							&& dttactivityid != null && hqtechid != null
							&& rowid != null && projectnumber != null) {
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
							if (sourcesystem.contains(DTTConstants.SFDC)) {
							} else {

								if (locationshore != ""
										|| locationshore != null) {
									if (locationshore.equals(DTTConstants.ONSHORE)) {
										locationshore = DTTConstants.ATTACH_ONS + countryid;
									}
									if (locationshore.equals(DTTConstants.OFFSHORE)) {
										locationshore = DTTConstants.ATTACH_OFS + countryid;
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
									if(siteobjectid!=null){
									oppproject = projectref.opputunityReadProject(siteobjectid.toString());
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
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}
									logger.info("projectobjectid"+projectobjectid);
									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();
if(projectobjectid!=null){
									readwbs = wbsreadele
											.readWBS(projectobjectid.toString());
}
									if (readwbs.size() != 0) {
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										while (wbsidread.hasNext()) {
											WBS wbsidreadelement = wbsidread
													.next();
											if (countryid
													.equalsIgnoreCase("GB")
													|| countryid
															.equalsIgnoreCase("IE")) {
												if (wbsidreadelement.getCode()
														.equals(hqtechid)) {
													wbs = true;
													/*
													 * System.out.print(
													 * projectidreadelement
													 * .getName());
													 */

													wbsobjectid = wbsidreadelement
															.getObjectId();
													primaverawbscode = wbsidreadelement
															.getName();
													primaverawbsobjectid = wbsidreadelement
															.getObjectId();
													logger.info("primaverawbscode"+primaverawbscode);
													/*
													 * wbsjaxbElement = new
													 * JAXBElement( new
													 * QName("WBSObjectId"
													 * ),Activity.class,
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
											}else{

											if (wbsidreadelement.getCode()
													.equals(dttactivityid)) {
												wbs = true;
												/*
												 * System.out.print(
												 * projectidreadelement
												 * .getName());
												 */

												wbsobjectid = wbsidreadelement
														.getObjectId();
												primaverawbscode = wbsidreadelement
														.getName();
												primaverawbsobjectid = wbsidreadelement
														.getObjectId();
												/*
												 * wbsjaxbElement = new
												 * JAXBElement( new
												 * QName("WBSObjectId"
												 * ),Activity.class,
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
									}

									if (project) {
										if (wbs) {
											if (countryid
													.equalsIgnoreCase("GB")
													|| countryid
															.equalsIgnoreCase("IE")) {
												if (!primaverawbscode
														.contains(turbinename
																.toString())){
												if (!(ukwbscheck
														.contains(hqtechid
																.toString()))) {
													if(!ukwbsprojectcheck.contains(projectobjectid.toString())) {
														//ukwbsnewhqcheck=new ArrayList();
														
														ukwbsnewhqcheck.add(hqtechid.toString());
													ukwbshqcheck.add(hqtechid.toString());
													ukwbscheck.add(dttactivityid
															.toString());
													ukwbsprojectcheck.add(projectobjectid.toString());
													
													wbsupdate
													.setProjectObjectId(projectobjectid);
											wbsupdate
													.setName(turbinename);
											wbsupdate
													.setCode(hqtechid
															.toString());
											wbsupdate.setObjectId(primaverawbsobjectid);
													logger.info(" Inside new Project dttactivityid"
															+ dttactivityid
															+ "ProjectNumber"
															+ projectnumber
															+"HQ"+hqtechid);
													updatewbs.add(wbsupdate);
													
															
													}else
													{
														if(ukwbsprojectcheck.contains(projectobjectid.toString())) {
															if (!ukwbshqcheck
																	.contains(hqtechid.toString())) {
																wbsupdate = new WBS();
																 ukwbshqcheck.add(hqtechid.toString());
																 ukwbsnewhqcheck.add(hqtechid.toString());
																 wbsupdate
																	.setProjectObjectId(projectobjectid);
															wbsupdate
																	.setName(turbinename);
															wbsupdate
																	.setCode(hqtechid
																			.toString());
															wbsupdate.setObjectId(primaverawbsobjectid);
														logger.info("Inside HQ dttactivityid"
																+ dttactivityid
																+ "ProjectNumber"
																+ projectnumber
																+"HQ"+hqtechid);
														updatewbs.add(wbsupdate);
																
															}else{
																if (!ukwbsnewhqcheck
																		.contains(hqtechid.toString())) {
																	
																	wbsupdate = new WBS();
																 ukwbsnewhqcheck.add(hqtechid.toString());
																 
																 wbsupdate
																	.setProjectObjectId(projectobjectid);
															wbsupdate
																	.setName(turbinename);
															wbsupdate
																	.setCode(hqtechid
																			.toString());
															wbsupdate.setObjectId(primaverawbsobjectid);
															updatewbs.add(wbsupdate);
														logger.info(" Inside new HQ dttactivityid"
																+ dttactivityid
																+ "ProjectNumber"
																+ projectnumber
																+"HQ"+hqtechid);
														
																	}
															
															}
															
															
														}
													}
													
													
												
												}
											/*	if (!primaverawbscode
														.contains(turbinename
																.toString())) {*/
												
													/*if (!(ukwbscheck
															.contains(turbinename
																	.toString()))) {*/

														/*ukwbscheck.add(turbinename
																.toString());
														wbscheck.add(hqtechid
																.toString());
														wbscheck.add(projectnumber);
	logger.info("dttactivityid "+dttactivityid);
	logger.info("primaverawbscode "+primaverawbscode);
	logger.info("projectobjectid "+projectobjectid);
	logger.info("primaverawbsobjectid "+primaverawbsobjectid);
														wbsupdate
																.setProjectObjectId(projectobjectid);
														wbsupdate
																.setName(turbinename);
														wbsupdate
																.setCode(hqtechid
																		.toString());
														wbsupdate.setObjectId(primaverawbsobjectid);
														updatewbs.add(wbsupdate);*/

													/*}*/
													
												}
												
											}else{
											if (!primaverawbscode
													.contains(turbinename
															.toString())) {
												if (!(wbscheck
														.contains(dttactivityid
																.toString()))) {

													wbscheck.add(dttactivityid
															.toString());
													wbscheck.add(hqtechid
															.toString());
													wbscheck.add(projectnumber);
logger.info("dttactivityid "+dttactivityid);
logger.info("primaverawbscode "+primaverawbscode);
/*logger.info("projectobjectid "+projectobjectid);
logger.info("primaverawbsobjectid "+primaverawbsobjectid);*/
													wbsupdate
															.setProjectObjectId(projectobjectid);
													wbsupdate
															.setName(turbinename);
													wbsupdate
															.setCode(dttactivityid
																	.toString());
													wbsupdate.setObjectId(primaverawbsobjectid);
													updatewbs.add(wbsupdate);

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
			System.out.println("updatewbs  " + updatewbs.size());
			//logger.info("update wbs Size" + updatewbs.size());
			// projudf.CreateUDFValue(projectudfvaluedetails);
			/*UpdateProcessRecordsDAO processed=new UpdateProcessRecordsDAO();
			processed.updaterecords(DTTConstants.ACTIVITY, "Y", recordlist,"Success", "DTT_ACTIVITY_OBJECT_ID");*/
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return updatewbs;
	}

	public List<UDFValue> checkAndCreateWBSUDF() throws Exception {
		List<UDFValue> wbsudfvaluedetails = new ArrayList<UDFValue>();
		//logger.info("Check and Create  WBS UDF");
		try {
			
			PrimaveraWBS wbstype = new PrimaveraWBS();

			ActivityMasterDAO activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getWBStMasterDAO();
			activitymasterdata= new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getWbsmasterdata();
			PrimaveraEPS epsclient = new PrimaveraEPS();
			PrimaveraProjectUDF projudf = new PrimaveraProjectUDF();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();
			
			ObjectFactory objectfactory=new ObjectFactory();
			
			List wbscheck = new ArrayList();
			List wbsudfcheck = new ArrayList();
			List turbinefunctionalidlist = new ArrayList();
			List turbinetypelist = new ArrayList();
			List warrantystartdatlist = new ArrayList();
			List warrantyenddatlist = new ArrayList();
			JAXBElement<XMLGregorianCalendar> wbswarrantystartdate = null;
			JAXBElement<XMLGregorianCalendar> wbswarrantyenddate = null;

			List<WBS> createwbs = new ArrayList<WBS>();
			/*List<UDFType> udfcode = new ArrayList<UDFType>();
			udfcode = projudf.readProjectUDF();
			ListIterator<UDFType> projectudfread = udfcode.listIterator();
			while (projectudfread.hasNext()) {

				UDFType udfreadelement = projectudfread.next();
				
				 * //logger.info(udfreadelement.getDataType() + " " +
				 * udfreadelement.getSubjectArea() + " " +
				 * udfreadelement.getTitle() + " " +
				 * udfreadelement.getObjectId());
				 
				System.out.println(udfreadelement.getDataType() + " "
						+ udfreadelement.getSubjectArea() + " "
						+ udfreadelement.getTitle() + " "
						+ udfreadelement.getObjectId());
			}*/
			List<UDFValue> readudf = new ArrayList<UDFValue>();
			PrimaveraProjectUDF readudfs = new PrimaveraProjectUDF();

/*			readudf = readudfs.readUDFValue();
			ListIterator<UDFValue> wbsudfidread = readudf.listIterator();

			while (wbsudfidread.hasNext()) {
				UDFValue wbsudfidreadelement = wbsudfidread.next();

				if (wbsudfidreadelement.getCodeValue() != null)
					
					 * System.out.println("code " +
					 * wbsudfidreadelement.getCodeValue());
					 
					if (wbsudfidreadelement.getText() != null)
						
						 * System.out.println("Text" +
						 * wbsudfidreadelement.getText());
						 
						if (wbsudfidreadelement.getDouble() != null)
							System.out.println("Double"
									+ wbsudfidreadelement.getDouble()
											.getValue());
				if (wbsudfidreadelement.getInteger() != null)
					
					 * System.out.println("Integer" +
					 * wbsudfidreadelement.getInteger());
					 
					if (wbsudfidreadelement.getUDFTypeTitle() != null)
						
						 * System.out.println("type title" +
						 * wbsudfidreadelement.getUDFTypeTitle());
						 
						if (wbsudfidreadelement.getForeignObjectId() != null)
							
							 * System.out.println("F Object" +
							 * wbsudfidreadelement.getForeignObjectId());
							 
							if (wbsudfidreadelement.getUDFTypeObjectId() != null)
								
								 * System.out.println("UDF type" +
								 * wbsudfidreadelement.getUDFTypeObjectId());
								 
								if (wbsudfidreadelement.getUDFCodeObjectId() != null)
									System.out.println("UDF Code"
											+ wbsudfidreadelement
													.getUDFTypeDataType());

			}*/
			//logger.info("activitymasterdata"+activitymasterdata.size());

			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String turbinefunctionalid = null;
				String dttactivityid = null;
				String hqtechid = null;
				String rowid = null;
				String turbinetype = null;

				String worktypestring = null;
				String turbinename = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				Date contractstartdate = null;
				Date contractenddate = null;
				JAXBElement<XMLGregorianCalendar> warrantystartdate = null;
				JAXBElement<XMLGregorianCalendar> warrantyenddate = null;
				String warrantystartdat = null;
				String warrantyenddat = null;
				String customerifa = null;
				String customername = null;
				String probability = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					WBS wbscreate = new WBS();
					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;
					String activityobjectid = null;
					Integer wbsobjectid = null;

					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean wbs = false;
					boolean wbsudf = false;
					boolean wbsturbinefunctionalcheck = false;
					boolean warrantystartdatecheck = false;
					boolean warrantyenddatecheck = false;
					boolean turbinetypecheck = false;
					UDFValue projectudfassign = new UDFValue();
					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYID)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains(DTTConstants.LOCATIONSHORE)) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains(DTTConstants.TURBINE_NAME)) {
						turbinename = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNUMBER)) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains(DTTConstants.SOURCESYSTEM)) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains(DTTConstants.WORKTYPE)) {
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

						/* System.out.println(worktypestring); */
					}

					if (key.contains(DTTConstants.SAP_PARENT_OBJECT_ID)) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.WARRANTY_START_DATE)) {
						warrantystartdat = (String) (value);
						// //logger.info("warrantystartdat"+warrantystartdat);
						// warrantystartdate =
						// toXMLGregorianCalendarWithoutTimeStamp(warrantystartdat);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.WARRANTY_END_DATE)) {
						warrantyenddat = (String) (value);
						// //logger.info("warrantyenddat"+warrantyenddat);
						// warrantyenddate =
						// toXMLGregorianCalendarWithoutTimeStamp(warrantyenddat);
						/* System.out.println(sapparentobjectid); */
					}

					if (key.contains(DTTConstants.SITEID)) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_FUNCT_LOC_ID)) {
						turbinefunctionalid = (String) (value);
						/*if(turbinefunctionalid=="")
						{
							turbinefunctionalid="null";
						}*/
						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.DTT_ACTIVITY_OBJECT_ID)) {
						dttactivityid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.HQ_TECH_ID_NUMBER)) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.ROWID)) {
						rowid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.TURBINE_TYPE)) {
						turbinetype = (String) (value);

						/* System.out.println(siteid); */
					}

					if (countryid != null && sourcesystem != null
							&& turbinename != null && locationshore != null
							&& dttactivityid != null && hqtechid != null
							&& projectnumber != null) {

						ListIterator<EPS> epsread = epss.listIterator();
						/* //logger.info("Got values "+countryid+sourcesystem+turbinename+locationshore+dttactivityid+hqtechid+projectnumber);*/
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
							/* //logger.info("countryobjectid"+countryobjectid);*/

							epsss1 = epsclient
									.readWSProjectEPS(countryobjectid);

						}
						if (epsss1 != null && sourcesystem != null) {
							if (sourcesystem.contains(DTTConstants.SFDC)) {
							} else {

								if (locationshore != ""
										|| locationshore != null) {

									if (locationshore.equals(DTTConstants.ONSHORE)) {
										locationshore = DTTConstants.ATTACH_ONS + countryid;
									}
									if (locationshore.equals(DTTConstants.OFFSHORE)) {
										locationshore = DTTConstants.ATTACH_OFS + countryid;
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
if(siteobjectid!=null){
									oppproject = projectref.opputunityReadProject(siteobjectid.toString());
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
												/*//logger.info(projectidreadelement
																.getName());*/
												projectobjectid = projectidreadelement
														.getObjectId();
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}

									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();
if(projectobjectid!=null){
									readwbs = wbsreadele
											.readWBS(projectobjectid.toString());
}
									////logger.info("readwbs"+readwbs.size());
									if (readwbs.size() != 0) {
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										while (wbsidread.hasNext()) {
											WBS wbsidreadelement = wbsidread
													.next();

											if (wbsidreadelement.getName()
													.equals(turbinename
															.toString())) {
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
												 * ),Activity.class,
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
									
									if (project) {
										if (wbs) {
											readudf = readudfs.readUDFValue(wbsobjectid);
											ListIterator<UDFValue> wbsudfidreadcheck = readudf
													.listIterator();

											while (wbsudfidreadcheck.hasNext()) {
												UDFValue wbsudfidreadelementcheck = wbsudfidreadcheck
														.next();
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																161)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													wbsudf = true;
												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																172)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													wbsturbinefunctionalcheck = true;
												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																163)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													
													 wbswarrantystartdate =
													 wbsudfidreadelementcheck
													 .getStartDate();
													 
													/*warrantystartdat = wbsudfidreadelementcheck
															.getText();*/
													warrantystartdatecheck = true;
												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																164)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													
													  wbswarrantyenddate =
													  wbsudfidreadelementcheck
													  .getFinishDate();
													 
													/*warrantyenddat = wbsudfidreadelementcheck
															.getText();*/
													warrantyenddatecheck = true;
												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																142)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													
													turbinetype = wbsudfidreadelementcheck
															.getText();
													turbinetypecheck = true;
												}
												/*
												 * if (wbsudfidreadelementcheck
												 * .getUDFTypeObjectId().equals( 163)) {
												 * wbswarrantystartdate =
												 * wbsudfidreadelementcheck
												 * .getStartDate(); } if
												 * (wbsudfidreadelementcheck
												 * .getUDFTypeObjectId().equals( 164)) {
												 * wbswarrantyenddate =
												 * wbsudfidreadelementcheck
												 * .getFinishDate(); }
												 */
											}


											if (wbsudf) {
												//logger.info("hqtechid"+hqtechid);
											} else {
												if (hqtechid != null) {

													if (!wbsudfcheck
															.contains(wbsobjectid)) {
														wbsudfcheck
																.add(wbsobjectid);
														projectudfassign
																.setUDFTypeObjectId(161);
														projectudfassign
																.setForeignObjectId(wbsobjectid);
														projectudfassign
																.setText(hqtechid
																		.toString());
														wbsudfvaluedetails
																.add(projectudfassign);
													//	//logger.info("hqtechid"+hqtechid);

													}
												}
											}
											projectudfassign = new UDFValue();
											if (wbsturbinefunctionalcheck) {

											} else {
												//logger.info("turbinefunctionalid"+turbinefunctionalid);
												if (turbinefunctionalid != null ||turbinefunctionalid!="") {
													if (!(turbinefunctionalidlist
															.contains(wbsobjectid
																	.toString()))) {
														turbinefunctionalidlist
																.add(wbsobjectid
																		.toString());
														 //logger.info("turbinefunctionalid"+turbinefunctionalid);
														
														projectudfassign
																.setUDFTypeObjectId(172);
														projectudfassign
																.setForeignObjectId(wbsobjectid);
														projectudfassign
																.setText(turbinefunctionalid
																		);
														wbsudfvaluedetails
																.add(projectudfassign);
														////logger.info("turbinefunctionalid"+turbinefunctionalid);
													}
												}
											}
											projectudfassign = new UDFValue();
											if (turbinetypecheck) {

											} else {

												if (turbinetype != null) {
													if (!(turbinetypelist
															.contains(wbsobjectid
																	.toString()))) {
														turbinetypelist
																.add(wbsobjectid
																		.toString());
														
														projectudfassign
																.setUDFTypeObjectId(142);
														projectudfassign
																.setForeignObjectId(wbsobjectid);
														projectudfassign
																.setText(turbinetype
																		.toString());
														wbsudfvaluedetails
																.add(projectudfassign);
													}
												}
											}
											projectudfassign = new UDFValue();
											if (warrantystartdatecheck) {

											} else {
												if (warrantystartdat != null) {
													if (!warrantystartdatlist
															.contains(wbsobjectid)) {
														warrantystartdatlist
																.add(wbsobjectid);
														
														warrantystartdate=objectfactory.createUDFValueStartDate(toXMLGregorianCalendarWithoutTimeStamp(warrantystartdat));
														/*
														 * wbswarrantystartdate
														 * .setValue(
														 * warrantystartdate);
														 */
														projectudfassign
																.setUDFTypeObjectId(163);
														projectudfassign
																.setForeignObjectId(wbsobjectid);
														projectudfassign
																.setStartDate(warrantystartdate);
														wbsudfvaluedetails
																.add(projectudfassign);
													}
												}
											}
											projectudfassign = new UDFValue();
											if (warrantyenddatecheck) {

											} else {
												if (warrantyenddat != null) {
													if (!warrantyenddatlist
															.contains(wbsobjectid)) {
														warrantyenddatlist
																.add(wbsobjectid);
														
														warrantyenddate=objectfactory.createUDFValueFinishDate(toXMLGregorianCalendarWithoutTimeStamp(warrantyenddat));
														projectudfassign
																.setUDFTypeObjectId(164);
														projectudfassign
																.setForeignObjectId(wbsobjectid);
														projectudfassign
																.setFinishDate(warrantyenddate);
														wbsudfvaluedetails
																.add(projectudfassign);
													}
												}

											}
											/*
											 * //logger.info("UDF SIZE  " +
											 * wbsudfvaluedetails.size());
											 */
											logger.info("UDF SIZE  "
															+ wbsudfvaluedetails
																	.size());
											if (wbsudfvaluedetails != null) {
												ListIterator<UDFValue> read = wbsudfvaluedetails
														.listIterator();
												while (read.hasNext()) {

													UDFValue projreadelement = read
															.next();

													// System.out.print(countryobjectid);
													/*
													 * System.out.println(
													 * projreadelement.getDouble
													 * ().getValue());
													 */
													/*
													 * //logger.info(projreadelement
													 * .getText());
													 * //logger.info(projreadelement
													 * .getForeignObjectId());
													 */

												}

											}

										}

									}

								}
							}

						}
					}
				}
				System.out.println("createwbs  " + createwbs.size());
				System.out.println("wbsudfvaluedetails  "
						+ wbsudfvaluedetails.size());
				/* //logger.info("create wbs udf Size"+wbsudfvaluedetails.size()); */

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return wbsudfvaluedetails;
	}

	public List<UDFValue> checkAndUpdateWBSUDF() throws Exception {
		List<UDFValue> wbsudfupdatedetails = new ArrayList<UDFValue>();
		//logger.info("Check and Update  WBS UDF");
		try {
			
			PrimaveraWBS wbstype = new PrimaveraWBS();

			ActivityMasterDAO activitymasterDAO = new ActivityMasterDAO();
			activitymasterbean = activitymasterDAO.getWBStMasterDAO();
			activitymasterdata= new ArrayList<Map<String, Object>>();
			activitymasterdata = activitymasterbean.getWbsmasterdata();
			PrimaveraEPS epsclient = new PrimaveraEPS();
			PrimaveraProjectUDF projudf = new PrimaveraProjectUDF();

			PrimaveraProject projectref = new PrimaveraProject();
			List<EPS> epss = new ArrayList<EPS>();
			epss = epsclient.readEPS();
			List<String> recordlist= new ArrayList<String>();
			ObjectFactory objectfactory=new ObjectFactory();
			List wbscheck = new ArrayList();
			List wbsudfcheck = new ArrayList();
			JAXBElement<XMLGregorianCalendar> wbswarrantystartdate = null;
			JAXBElement<XMLGregorianCalendar> wbswarrantyenddate = null;
			List<WBS> createwbs = new ArrayList<WBS>();
			List<UDFType> udfcode = new ArrayList<UDFType>();
			udfcode = projudf.readProjectUDF();
			ListIterator<UDFType> projectudfread = udfcode.listIterator();
			while (projectudfread.hasNext()) {

				UDFType udfreadelement = projectudfread.next();

				System.out.println(udfreadelement.getDataType() + " "
						+ udfreadelement.getSubjectArea() + " "
						+ udfreadelement.getTitle() + " "
						+ udfreadelement.getObjectId());
			}
			List<UDFValue> readudf = new ArrayList<UDFValue>();
			PrimaveraProjectUDF readudfs = new PrimaveraProjectUDF();

			/*readudf = readudfs.readUDFValue();
			ListIterator<UDFValue> wbsudfidread = readudf.listIterator();

			while (wbsudfidread.hasNext()) {
				UDFValue wbsudfidreadelement = wbsudfidread.next();

				if (wbsudfidreadelement.getCodeValue() != null)
					
					 * System.out.println("code " +
					 * wbsudfidreadelement.getCodeValue());
					 
					if (wbsudfidreadelement.getText() != null)
						
						 * System.out.println("Text" +
						 * wbsudfidreadelement.getText());
						 
						if (wbsudfidreadelement.getDouble() != null)
							System.out.println("Double"
									+ wbsudfidreadelement.getDouble()
											.getValue());
				if (wbsudfidreadelement.getInteger() != null)
					
					 * System.out.println("Integer" +
					 * wbsudfidreadelement.getInteger());
					 
					if (wbsudfidreadelement.getUDFTypeTitle() != null)
						
						 * System.out.println("type title" +
						 * wbsudfidreadelement.getUDFTypeTitle());
						 
						if (wbsudfidreadelement.getForeignObjectId() != null)
							
							 * System.out.println("F Object" +
							 * wbsudfidreadelement.getForeignObjectId());
							 
							if (wbsudfidreadelement.getUDFTypeObjectId() != null)
								
								 * System.out.println("UDF type" +
								 * wbsudfidreadelement.getUDFTypeObjectId());
								 
								if (wbsudfidreadelement.getUDFCodeObjectId() != null)
									System.out.println("UDF Code"
											+ wbsudfidreadelement
													.getUDFTypeDataType());

			}
*/
			for (Map<String, Object> map : activitymasterdata) {

				String countryid = null;

				String sourcesystem = null;
				int worktype = 0;
				String locationshore = null;
				String siteid = null;
				String dttactivityid = null;
				String hqtechid = null;
				String primaverawbshqtechid = null;
				String rowid = null;

				String worktypestring = null;
				String turbinename = null;
				String projectnumber = null;
				String projectname = null;
				String sapparentobjectid = null;
				Date contractstartdate = null;
				Date contractenddate = null;
				String customerifa = null;
				String customername = null;
				String probability = null;
				/*
				 * JAXBElement<XMLGregorianCalendar> primaverawarrantystartdate
				 * = null; JAXBElement<XMLGregorianCalendar>
				 * primaverawarrantyenddate = null;
				 */
				JAXBElement<XMLGregorianCalendar> primaverawarrantystartdate = null;
				JAXBElement<XMLGregorianCalendar> primaverawarrantyenddate = null;
				JAXBElement<XMLGregorianCalendar> warrantystartdate = null;
				JAXBElement<XMLGregorianCalendar> warrantyenddate = null;
				String warrantystartdat = null;
				String warrantyenddat = null;
				String turbinefunctionalid = null;

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					Integer countryobjectid = null;
					Integer locationshoreobjectid = null;
					Integer siteobjectid = null;
					WBS wbsupdate = new WBS();
					JAXBElement<Integer> countryparentobjecctid = null;
					String opputunityobjectid = null;
					Integer projectobjectid = null;
					String activityobjectid = null;
					Integer wbsobjectid = null;

					Integer opputunityparentobjectid = null;
					boolean opportunity = false;
					boolean project = false;
					boolean country = false;
					boolean location = false;
					boolean site = false;
					boolean wbs = false;
					boolean wbsudf = false;
					boolean warrantystartdatecheck = false;
					boolean warrantyenddatecheck = false;
					boolean wbsturbinefunctionalcheck = false;
					
					UDFValue projectudfassign = new UDFValue();
					String key = entry.getKey();
					Object value = entry.getValue();
					if (key.contains(DTTConstants.COUNTRYID)) {
						countryid = (String) value;

						// System.out.println(countryid);

					}
					if (key.contains(DTTConstants.LOCATIONSHORE)) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains(DTTConstants.TURBINE_NAME)) {
						turbinename = (String) value;

						// System.out.println(projectnumber);
					}
					if (key.contains(DTTConstants.PROJECTNUMBER)) {
						projectnumber = (String) value;

						// System.out.println(projectnumber);
					}

					if (key.contains(DTTConstants.SOURCESYSTEM)) {
						sourcesystem = (String) value;

						// System.out.println(sourcesystem);
					}
					if (key.contains(DTTConstants.WORKTYPE)) {
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

						/* System.out.println(worktypestring); */
					}

					if (key.contains(DTTConstants.SAP_PARENT_OBJECT_ID)) {
						sapparentobjectid = (String) (value);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.WARRANTY_START_DATE)) {
						warrantystartdat = (String) (value);
						// warrantystartdate =
						// toXMLGregorianCalendarWithoutTimeStamp(warrantystartdat);

						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.WARRANTY_END_DATE)) {
						warrantyenddat = (String) (value);
						// warrantyenddate =
						// toXMLGregorianCalendarWithoutTimeStamp(warrantyenddat);
						/* System.out.println(sapparentobjectid); */
					}
					if (key.contains(DTTConstants.TURBINE_FUNCT_LOC_ID)) {
						turbinefunctionalid = (String) (value);
						if(turbinefunctionalid=="")
						{
							turbinefunctionalid="null";
						}
						/* System.out.println(siteid); */
					}

					if (key.contains(DTTConstants.SITEID)) {
						siteid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.DTT_ACTIVITY_OBJECT_ID)) {
						dttactivityid = (String) (value);
						
						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.HQ_TECH_ID_NUMBER)) {
						hqtechid = (String) (value);

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.ROWID)) {
						rowid = (String) (value);

						/* System.out.println(siteid); */
					}

					if (countryid != null && sourcesystem != null
							&& turbinename != null && locationshore != null
							&& dttactivityid != null && hqtechid != null
							&& projectnumber != null) {
						if (dttactivityid != null && !(countryid.equalsIgnoreCase("GB"))) {
							recordlist.add(dttactivityid);
						}
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
							if (sourcesystem.contains(DTTConstants.SFDC)) {
							} else {

								if (locationshore != ""
										|| locationshore != null) {
									if (locationshore.equals(DTTConstants.ONSHORE)) {
										locationshore = DTTConstants.ATTACH_ONS + countryid;
									}
									if (locationshore.equals(DTTConstants.OFFSHORE)) {
										locationshore = DTTConstants.ATTACH_OFS + countryid;
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
if(siteobjectid!=null){
									oppproject = projectref.opputunityReadProject(siteobjectid.toString());
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
												// opputunityparentobjectid=projectidreadelement.getParentObjectId();

											}

										}
									}

									List<WBS> readwbs = new ArrayList<WBS>();
									PrimaveraWBS wbsreadele = new PrimaveraWBS();
if(projectobjectid!=null){
									readwbs = wbsreadele
											.readWBS(projectobjectid.toString());
}
									if (readwbs.size() != 0) {
										ListIterator<WBS> wbsidread = readwbs
												.listIterator();

										while (wbsidread.hasNext()) {
											WBS wbsidreadelement = wbsidread
													.next();

											if (wbsidreadelement.getName()
													.equals(turbinename
															)) {
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
												 * ),Activity.class,
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
									
									if (project) {
										if (wbs) {
											readudf = readudfs.readUDFValue(wbsobjectid);
											ListIterator<UDFValue> wbsudfidreadcheck = readudf
													.listIterator();

											while (wbsudfidreadcheck.hasNext()) {
												UDFValue wbsudfidreadelementcheck = wbsudfidreadcheck
														.next();
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																161)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													primaverawbshqtechid = wbsudfidreadelementcheck
															.getText();
													wbsudf = true;
												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																163)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													
													  primaverawarrantystartdate =
													  wbsudfidreadelementcheck
													  .getStartDate();
													 
													/*primaverawarrantystartdate = wbsudfidreadelementcheck
															.getText();*/
													
													  wbswarrantystartdate =
													  wbsudfidreadelementcheck
													  .getStartDate();
													 
													/*warrantystartdat = wbsudfidreadelementcheck
															.getText();*/
													warrantystartdatecheck = true;
												}
												if (wbsudfidreadelementcheck
														.getUDFTypeObjectId().equals(
																164)
														&& wbsudfidreadelementcheck
																.getForeignObjectId()
																.equals(wbsobjectid)) {
													
													  primaverawarrantyenddate =
													  wbsudfidreadelementcheck.getFinishDate();
													 
													/*primaverawarrantyenddate = wbsudfidreadelementcheck
															.getText();*/
													/*
													 * wbswarrantyenddate =
													 * wbsudfidreadelementcheck
													 * .getFinishDate();
													 */
													/*warrantyenddat = wbsudfidreadelementcheck
															.getText();*/
													warrantyenddatecheck = true;
												}
											}


											/*
											 * if(wbsudf){
											 * if(!primaverawbshqtechid
											 * .contains(hqtechid.toString())){
											 * if
											 * (!(wbsudfcheck.contains(dttactivityid
											 * .toString()))) {
											 * wbsudfcheck.add(dttactivityid
											 * .toString()); projectudfassign
											 * .setUDFTypeObjectId(161);
											 * projectudfassign
											 * .setForeignObjectId(wbsobjectid);
											 * projectudfassign
											 * .setText(hqtechid.toString());
											 * wbsudfupdatedetails
											 * .add(projectudfassign); } }
											 * 
											 * 
											 * }
											 */
											
											/*projectudfassign = new UDFValue();
											if (wbsturbinefunctionalcheck) {

											} else {
												////logger.info("turbinefunctionalid"+turbinefunctionalid);
												if (turbinefunctionalid != null ||turbinefunctionalid!="null") {
													 {
														
														// //logger.info("turbinefunctionalid"+turbinefunctionalid);
														projectudfassign
																.setUDFTypeObjectId(172);
														projectudfassign
																.setForeignObjectId(wbsobjectid);
														projectudfassign
																.setText(turbinefunctionalid
																		.toString());
														wbsudfupdatedetails
																.add(projectudfassign);
														////logger.info("turbinefunctionalid"+turbinefunctionalid);
													}
												}
											}*/
											////logger.info("warrantystartdate before "+warrantystartdate.getValue());
											 projectudfassign = new UDFValue();
											if (warrantystartdatecheck) {
												if (warrantystartdat != null) {
													if(!(primaverawarrantystartdate==null)){
													if (!primaverawarrantystartdate.getValue()
															.equals(toXMLGregorianCalendarWithoutTimeStamp(warrantystartdat))){
														
														
													warrantystartdate=objectfactory.createUDFValueStartDate(toXMLGregorianCalendarWithoutTimeStamp(warrantystartdat));
													
													//logger.info("warrantystartdate"+warrantystartdate.getValue());
													projectudfassign
															.setUDFTypeObjectId(163);
													projectudfassign
															.setForeignObjectId(wbsobjectid);
													projectudfassign
															.setStartDate(warrantystartdate);
													wbsudfupdatedetails
															.add(projectudfassign);
													} 
												}
												}

											}
											 projectudfassign = new UDFValue();

											if (warrantyenddatecheck) {
												if (warrantyenddat != null) {
													if(!(primaverawarrantyenddate==null)){
													if (!primaverawarrantyenddate.getValue()
															.equals(toXMLGregorianCalendarWithoutTimeStamp(warrantyenddat))){
													warrantyenddate=objectfactory.createUDFValueFinishDate(toXMLGregorianCalendarWithoutTimeStamp(warrantyenddat));
													projectudfassign
															.setUDFTypeObjectId(164);
													projectudfassign
															.setForeignObjectId(wbsobjectid);
													projectudfassign
															.setFinishDate(warrantyenddate);
													wbsudfupdatedetails
															.add(projectudfassign);
													 } 
												}
												}

											}

										}
										logger.info("UDF Update SIZE  "
												+ wbsudfupdatedetails.size());

									}

								}

							}
						}

					}
				}
			}

			System.out.println("wbsudfupdatedetails update  "
					+ wbsudfupdatedetails.size());
			UpdateProcessRecordsDAO processed=new UpdateProcessRecordsDAO();
			processed.updaterecords(DTTConstants.ACTIVITY, "Y", recordlist,"Success", "DTT_ACTIVITY_OBJECT_ID");
			//logger.info("Update  WBS UDF Size" + wbsudfupdatedetails.size());
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return wbsudfupdatedetails;
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

	public static void main(String[] args) throws Exception {
		PrimaveraWBSWSClient wbsclient = new PrimaveraWBSWSClient();

		wbsclient.checkAndCreateWBSUDF();

	}
}
