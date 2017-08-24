package com.siemens.windpower.common;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.activitycode.ActivityCode;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignment;
import com.primavera.ws.p6.activitycodetype.ActivityCodeType;
import com.primavera.ws.p6.activitycodetype.CreateActivityCodeTypes;
import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.role.Role;
import com.primavera.ws.p6.activitycodetype.ObjectFactory;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.fltp.dao.ActivityMasterDAO;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraActivity;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraActivityCode;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraAssignment;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraEPS;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraProject;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWBS;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;

public class ActivityCodeCreate {
	
	Logger	logger = Logger.getLogger(ActivityCodeCreate.class);

	@SuppressWarnings("null")
	public void wsActivityCodeCreate() throws Exception 
	{
		// logger.info("  Activity Code Started ");
		List recordlist = new ArrayList();
		try 
		{
			List<ActivityCode> activitycodedetails = new ArrayList<ActivityCode>();

			ActivityCodeCreate activitywsclient = new ActivityCodeCreate();
			
			System.out.println("Create Activity CODE ");
			activitycodedetails = activitywsclient.checkAndCreateActivityCodes();
			System.out.println("Create Activity CODE Size"+ activitycodedetails.size());
			
			List<List<ActivityCode>> activitycodesublist = splitListToSubLists(activitycodedetails, 500);

			ListIterator<List<ActivityCode>> activitycodesublistitr = activitycodesublist.listIterator();
			
			while (activitycodesublistitr.hasNext()) 
			{
				List<ActivityCode> activitycodecreate = activitycodesublistitr.next();
				recordlist = activitycodecreate;
				
				PrimaveraActivityCode activitycodews = new PrimaveraActivityCode();
				activitycodews.createActivityCode(activitycodecreate); // It will return the objectId, but where are we using it?
				
				
				/*
				 * AVR31
				 * 
					recordlist.add(activitycodews.createActivityCodeAssignment(activitycodecreate));
				 *
				*/
				
				
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
				logger.error( e);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				
			}

		}

	}
	@SuppressWarnings({ "null", "unchecked" })
	public List<ActivityCode> checkAndCreateActivityCodes()throws Exception 
	{List<ActivityCode> activitytypecodecreatelist = new ArrayList<ActivityCode>();
		//List<ActivityCodeAssignment> activitycodedetails = new ArrayList<ActivityCodeAssignment>();
		try 
		{
			ExcelFileReader excelfiledata= new ExcelFileReader();
			PrimaveraEPS epsclient = new PrimaveraEPS();
			logger.info("ExcelFileReader");
			
			String activitycodedescription=null;
			String activitycodevalue=null;
			String projectid = null;
			Integer activitytypeid=null;
			JAXBElement<Integer> epsid=null;
			ObjectFactory objectfactory = new ObjectFactory();
			Integer epssid = null;
			List activytycodes= new ArrayList();
			
			PrimaveraActivityCode activitycode = new PrimaveraActivityCode();
			
			
			List<List<String>> datalist=excelfiledata.excelRead();
			
			for(List<String> csv : datalist)
	    	{
	    		//dumb logic to place the commas correctly
	    		if(!csv.isEmpty())
	    		{
	    			 activitytypeid=null;
	    			boolean iscodevalueFound = false;
	    			/*logger.info(csv.get(1));
	    			logger.info(csv.get(0));
	    			logger.info(csv.get(2));*/
	    			
	    			for(int i=1; i < csv.size(); i++)
	    			{
	    				System.out.print("," + csv.get(i));
	    			}
	    			System.out.println("From excel Activiy Code"+csv.get(0));
	    			// System.out.println("epsid"+epsid.getValue());
	    			if(csv.get(0).equalsIgnoreCase("A"))
	    			{
	    				boolean isEpsFound = false;
	    				String epsname=csv.get(1);
	    				 System.out.println("From excel Activiy Code Name "+csv.get(1));
	    			
	    				List<EPS> epss = new ArrayList<EPS>();
	    				epss = epsclient.readEPS();
	    				 ListIterator<EPS> epsread = epss.listIterator();
							while (epsread.hasNext()) {

								EPS epsreadelement = epsread.next();
								if (epsreadelement.getId().equalsIgnoreCase(epsname)) {
									//epsid=epsreadelement.getObjectId();
									epssid = epsreadelement.getObjectId();
									epsid=objectfactory.createActivityCodeTypeEPSObjectId(epssid);
									
									 System.out.print(epsid);
									 System.out.print(epssid);
									
									isEpsFound=true;
									

								}
							}
								PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();
			    				
			    				List<ActivityCodeType> readactivitycodetype = new ArrayList<ActivityCodeType>();

			    				readactivitycodetype = activitycodetype.readActivityCodeType(); // It will return ObjectIds
			    				
			    				boolean isTypeFound = false;
			    				
			    				if (readactivitycodetype.size() != 0) 
			    				{
			    					ListIterator<ActivityCodeType> activitycodetypereadid = readactivitycodetype.listIterator();
			    						
			    					while (activitycodetypereadid.hasNext()) 
			    						{
			    							ActivityCodeType activitycodetypereadelement = activitycodetypereadid.next();
			    							
			    						
			    							
			    							// System.out.println("epsid"+epsid.getValue());
			    							if (activitycodetypereadelement.getName().equals(csv.get(2))) 
			    							{	
			    								System.out.println(
					    								 "activitycodetypereadelement getName()"
					    								  +
					    								  activitycodetypereadelement
					    								  .getName());
					    								  System.out.println(
					    								 "activitycodetypereadelement getObjectId()"
					    								  +
					    								  activitycodetypereadelement
					    								  .getObjectId());
			    								 
			    								 activitytypeid=activitycodetypereadelement.getObjectId();
			    								// epsid=activitycodetypereadelement.getEPSObjectId();
			    								 isTypeFound = true;
			    							}	
			    						}			
			    				}
			    			
			    				if(!isTypeFound && isEpsFound)
			    				{
			    					 System.out.print(epssid);
			    					// epsid.setValue(epssid);
			    					List<ActivityCodeType> newelement = new ArrayList<ActivityCodeType>();
			    					ActivityCodeType ob = new ActivityCodeType();
			    					ob.setName(csv.get(2));
			    					ob.setScope("EPS");
			    					ob.setEPSObjectId(epsid);
			    					 System.out.print(epsid.getName());
			    					newelement.add(ob);
		    					
			    					List<Integer> objids = activitycodetype.createActivityCodeType(newelement);
			    					System.out.println("objids"+objids);
			    					/*if (objids.size() != 0) 
			    					{
			    						ListIterator<Integer> newelementdid = objids.listIterator();
			    						
			    						while (newelementdid.hasNext()) 
			    							{
			    								activitytypeid = newelementdid.next();
			    								

			    								System.out.println("activitytypeid"+activitytypeid);
			    								break;
			    							}
			    				}*/
			    			}
	    				
	    			}
	    			
	    			else
	    			{
	    				PrimaveraActivityCode activitycodetype = new PrimaveraActivityCode();
	    				
	    				List<ActivityCodeType> readactivitycodetype = new ArrayList<ActivityCodeType>();

	    				readactivitycodetype = activitycodetype.readActivityCodeType(); // It will return ObjectIds
	    				
	    				
	    				
	    				if (readactivitycodetype.size() != 0) 
	    				{
	    					ListIterator<ActivityCodeType> activitycodetypereadid = readactivitycodetype.listIterator();
	    						
	    					while (activitycodetypereadid.hasNext()) 
	    						{
	    							ActivityCodeType activitycodetypereadelement = activitycodetypereadid.next();
	    							
	    						
	    							
	    							// System.out.println("epsid"+epsid.getValue());
	    							if (activitycodetypereadelement.getName().equals(csv.get(0))) 
	    							{	
	    								System.out.println(
			    								 "activitycodetypereadelement getName()"
			    								  +
			    								  activitycodetypereadelement
			    								  .getName());
			    								  System.out.println(
			    								 "activitycodetypereadelement getObjectId()"
			    								  +
			    								  activitycodetypereadelement
			    								  .getObjectId());
	    								 
	    								 activitytypeid=activitycodetypereadelement.getObjectId();
	    								// epsid=activitycodetypereadelement.getEPSObjectId();
	    								 
	    							}	
	    						}			
	    				}
	    				
	    				//ActivityCode activitytypecoderead = new ActivityCode();
	    				List<ActivityCode> readactivitycode = new ArrayList<ActivityCode>();

	    				readactivitycode = activitycode.readActivityCode(); // It will return ObjectIds
	    				
	    				 iscodevalueFound = false;
	    				
	    				if (readactivitycode.size() != 0) 
	    				{
	    					ListIterator<ActivityCode> activitycodereadid = readactivitycode.listIterator();
	    						
	    					while (activitycodereadid.hasNext()) 
	    						{
	    							ActivityCode activitycodereadelement = activitycodereadid.next();
	    							
	    						
	    							
	    							// System.out.println("epsid"+epsid.getValue());
	    							if (activitycodereadelement.getCodeValue().equalsIgnoreCase(csv.get(1))) 
	    							{	
	    								/*System.out.println(
			    								 "activitycodetypereadelement getName()"
			    								  +
			    								  activitycodereadelement
			    								  .getCodeValue());
			    								  System.out.println(
			    								 "activitycodetypereadelement getObjectId()"
			    								  +
			    								  activitycodereadelement
			    								  .getObjectId());*/
	    								 
	    								// activitytypeid=activitycodereadelement.getObjectId();
	    								// epsid=activitycodetypereadelement.getEPSObjectId();
	    								 iscodevalueFound = true;
	    							}	
	    						}			
	    				}
	    				if(!iscodevalueFound){
						activitycodedescription=csv.get(2).toString();
						activitycodevalue=csv.get(1).toString();
						
						ActivityCode activitytypecodecreate = new ActivityCode();
						
						activitytypecodecreate
								.setCodeTypeObjectId(activitytypeid);
						activitytypecodecreate
								.setCodeValue(activitycodevalue);
						activitytypecodecreate
								.setDescription(activitycodedescription);
						
						System.out.println("activitytypecodecreate.getCodeValue"+activitytypecodecreate.getCodeValue());
						System.out.println("activitytypecodecreate.getCodeTypeObjectId"+activitytypecodecreate.getCodeTypeObjectId());
						
						activitytypecodecreatelist
								.add(activitytypecodecreate);
	    				}
						
					}
	    		}
	    		System.out.print("\n");
	    	}
			
			System.out.println("activitytypecodecreatelist.size"+activitytypecodecreatelist.size());
			/*if (activitytypecodecreatelist.size() != 0) {
				
				 * //logger.info(
				 * "customernamecodecreatelist inside "
				 * +
				 * customernamecodecreatelist.size()
				 * );
				 
				 activytycodes=activitycode.createActivityCode(activitytypecodecreatelist);
			}
			activytycodes.size();
			System.out.println("activytycodes created .size"+activytycodes.size());*/
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
			 
			List<String> recordlist = new ArrayList<String>();

			List activitycodecheck = new ArrayList();
			

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
														 
														if (activitycodereadelement
																.getDescription()
																.equals(turbinetype)) {

															turbinetypecodecheck = true;
															
															 * //logger.info(
															 * "turbinetypecodecheck "
															 * +
															 * turbinetypecodecheck
															 * );
															 
														}

													}

												}

												
												 * if(projectcodereadelement.
												 * getCodeValue().equals
												 * (customernamesubstring)){
												 * customernamesubstring
												 * =customername.substring(0,4);
												 * }
												 
												
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
												 
											}
											
											 * //logger.info("customernamecodecheck"
											 * +customernamecodecheck);
											 * //logger.
											 * info("customerifa"+customerifa);
											 * //logger.info("customername"+
											 * customername);
											 
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
												
												 * //logger.info(
												 * "End of else customername"
												 * +customernamecodecreate
												 * .getCodeTypeObjectId());
												 

											}

										}

										
										 * //logger.info(
										 * "customernamecodecreatelist "
										 * +customernamecodecreatelist.size());
										 
										if (turbinetypecodecreatelist.size() != 0) {
											
											 * //logger.info(
											 * "customernamecodecreatelist inside "
											 * +
											 * customernamecodecreatelist.size()
											 * );
											 
											activitycode
													.createActivityCode(turbinetypecodecreatelist);
										}


										
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

					
					 * System.out.println(projreadelement.getWBSObjectId()
					 * .getValue());
					 
					
					 * System.out.println(projreadelement.getText());
					 * System.out.println(projreadelement.getUDFTypeObjectId());
					 * System.out.println(projreadelement.getForeignObjectId());
					 

				}

				// System.out.println(projectdata);
			}

			System.out.println("activitycodedetails.size"
					+ activitycodedetails.size());

			// activitytype.createActivities(createactivity);
			// activitycodetype.createActivityCodeAssignment(activitycodedetails);
			// udfs.CreateUDFValue(activityudfdetails);
			
			 * UpdateProcessRecordsDAO processed=new UpdateProcessRecordsDAO();
			 * processed.updaterecords(DTTConstants.ACTIVITY, "Y",
			 * recordlist,"Success", "DTT_ACTIVITY_OBJECT_ID");
			 
		}
*/
		}	catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return activitytypecodecreatelist;
	}
	
	public static <T> List<List<T>> splitListToSubLists(List<T> parentList,int subListSize) 
	{
		List<List<T>> subLists = new ArrayList<List<T>>();
		if (subListSize > parentList.size()) {
			subLists.add(parentList);
		}
		else 
		{
			int remainingElements = parentList.size(); //Let parent size 700
			int startIndex = 0;
			int endIndex = subListSize; //500
			do 
			{
				List<T> subList = parentList.subList(startIndex, endIndex);//0,500//500,700
				subLists.add(subList);
				startIndex = endIndex;//500 //700
				if (remainingElements - subListSize >= subListSize) // 700-500>=500// 200-500>=500
				{
					endIndex = startIndex + subListSize;
				} 
				else 
				{
					endIndex = startIndex + remainingElements - subList.size();// 500+700-500 , why it can't be endIndex = remainingElements
				}                                                              // 700+200-700
 				remainingElements -= subList.size(); //200
			} 
			while (remainingElements > 0);
		}
		return subLists;
	}
}