package com.siemens.windpower.fltp.p6wsclient;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import com.primavera.ws.p6.eps.EPS;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.fltp.dao.ProjectMasterDAO;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraEPSWSClient {
	Logger logger = null;

	public PrimaveraEPSWSClient() {
		logger = Logger.getLogger(PrimaveraEPSWSClient.class);

	}

	public List<EPS> readAndCreateEPS() throws Exception {
		ProjectMasterBean epsmasterbean = null;
		List<EPS> epssitedetails = new ArrayList<EPS>();

		try {
			// logger.info("Read and Create EPS");
			ProjectMasterDAO ProjectMasterDAO = new ProjectMasterDAO();
			epsmasterbean = ProjectMasterDAO.getEPSMasterDataDAO();

			String locationshore = null;
			String countryid = null;
			String worktype = null;
			String siteid = null;
			String sitename = null;
			String sourcesystem = null;
			List epslist = new ArrayList();
			// epslist.add("Opp");

			List countrylist = new ArrayList();
			// epsmasterbean.getSfdccountrylist();
			List<EPS> epss = new ArrayList<EPS>();
			// logger.info("Call readEPSforAll");
			epss = readEPSforAll();
			// logger.info("Call readEPSforAll");

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> projectmasterdata = new ArrayList<Map<String, Object>>();
			projectmasterdata = epsmasterbean.getProjectmasterdata();
			// System.out.println(projectmasterdata);

			List<String> sitecheck = new ArrayList<String>();

			List<Integer> locationid = new ArrayList<Integer>();
			// logger.info("projectmasterdata" + projectmasterdata.size());
			for (Map<String, Object> map : projectmasterdata) {
				JAXBElement<Integer> countryparentobjecctid = null;
				countryid = null;
				worktype = null;
				siteid = null;
				sitename = null;
				locationshore = null;
				sourcesystem = null;
				EPS epssitecreate = new EPS();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					// System.out.println(key);

					if (key.contains("Country_Id")) {
						countryid = (String) entry.getValue();
						// System.out.println(entry.getValue());
					}
					if (key.contains("Work_Type")) {
						worktype = (String) entry.getValue();
						System.out.println(entry.getValue());
					}
					if (key.contains("Location_Shore")) {
						locationshore = (String) value;

						// System.out.println(locationshore);
					}
					if (key.contains("Site_id")) {
						siteid = (String) value;

						/* System.out.println(siteid); */
					}
					if (key.contains(DTTConstants.SITE_NAME)) {
						sitename = (String) value;

						// System.out.println(sitename);
					}
					if (key.contains("Source_System")) {
						sourcesystem = (String) value;

						// System.out.println(sitename);
					}
					if(sourcesystem!=null){
					if(!(sourcesystem.equalsIgnoreCase("SFDC"))){
					if (countryid != null) {
						if (locationshore != null) {

							Integer countryobjectid = null;
							String siteobjectid = null;
							Integer locationobjectid = null;
							JAXBElement<Integer> locationobject = null;
							boolean location = false;
							boolean country = false;
							boolean site = false;

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

							System.out.print(locationshore + " ");
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

							ListIterator<EPS> epsreaditr = epsss1
									.listIterator();
							while (epsreaditr.hasNext()) {
								EPS epsreadelement = epsreaditr.next();

								System.out.println(epsreadelement.getId()
										+ ""
										+ epsreadelement.getObjectId()
										+ ""
										+ epsreadelement.getParentObjectId()
												.getValue());

								if (epsreadelement.getId()
										.equals(locationshore)) {
									locationobjectid = epsreadelement
											.getObjectId();
									locationobject = epsreadelement
											.getParentObjectId();
									locationobject.setValue(locationobjectid);
									/*
									 * //logger.info("Location parent "+
									 * epsreadelement .getParentObjectId()
									 * .getValue()); logger.
									 * info("Location Object "+locationobjectid
									 * );
									 */
									// locationobject=locationobjectid;
									// epsreadelement.getParentObjectId();
									// System.out.println(locationobject.getValue()+
									// "");
									location = true;
									// System.out.println(locationshore+ "");
									// System.out.println(locationobjectid+ "");
								}

							}

							List<EPS> epsss2 = new ArrayList<EPS>();
							if (locationobjectid != null) {
								// System.out.println(countryobjectid);

								epsss2 = readProjectEPS(locationobjectid);
							}
							if (epsss2 != null) {
								ListIterator<EPS> epsreadsiteitr = epsss2
										.listIterator();

								while (epsreadsiteitr.hasNext()) {
									EPS epsreadsiteelement = epsreadsiteitr
											.next();
									System.out.println(epsreadsiteelement
											.getId() + "SiteID");
									if (epsreadsiteelement.getId().equals(
											siteid)) {
										siteobjectid = epsreadsiteelement
												.getObjectId().toString();
										site = true;
										System.out.println(epsreadsiteelement
												.getId() + "SiteID");
										/*
										 * //logger.info(epsreadsiteelement
										 * .getId() + "SiteID");
										 */

										/*
										 * System.out.println(siteid+ "");
										 * System.out.println(siteobjectid+ "");
										 */
									}

								}
							}
							if (country) {
								if (location) {

									// //logger.info(location);
									/*
									 * System.out.println(locationshore+ "");
									 * System.out.println(locationobjectid+ "");
									 */
									if (site) {

										/*
										 * //logger.info(
										 * "Site already exists in Primavera" +
										 * siteid);
										 */
										/*
										 * System.out.println(siteid+ "");
										 * System.out.println(siteobjectid+ "");
										 */

									} else {
										/*
										 * System.out.print(epsreadelement.
										 * getId() + ""); System.out.print(
										 * epsreadelement.getObjectId() + "");
										 */
										if (siteid != null) {
											if (!(sitecheck.contains(siteid))) {

												sitecheck.add(siteid);
												/*
												 * System.out.println(" "+
												 * siteid);
												 */

												// System.out.println(epssitecreate.getId()+" "+
												// sitename);
												/*
												 * JAXBElement<Integer>
												 * jaxbElement = new
												 * JAXBElement( new QName(EPS
												 * .class.getSimpleName()),
												 * EPS.class, locationobjectid);
												 */
												// jaxbElement.setValue(locationobjectid);
												epssitecreate
														.setParentObjectId(locationobject);
												// //logger.info("locationobject"+locationobject);
												/*
												 * System.out
												 * .println(locationobject
												 * .getValue() + " " +
												 * locationobjectid + " " +
												 * siteid);
												 */
												epssitecreate.setId(siteid
														.toString());
												epssitecreate.setName(sitename
														.toString());
												/*
												 * System.out.println(
												 * epssitecreate .getId()+" "+
												 * sitename);
												 */
												epssitedetails
														.add(epssitecreate);
												// System.out.println(epssitecreate+
												// "");
												locationid
														.add(locationobjectid);

												System.out
														.println(epssitedetails
																.size());
												/*
												 * epsws.CreateEPS(
												 * epssitedetails, locationid);
												 */
											}
											// System.out.println(sitename+
											// "");
											// epssitecreate.add(epsreaditr.previous());
											// epssitecreate.add(epsreadelement.getObjectId(),
											// epssitedetails);
											// System.out.println(epssitedetails);
										}

									}

								}
							}

						}
					}
				}
				}

			}
			ListIterator<EPS> read = epssitedetails.listIterator();
			System.out.println("epssitedetails.size()" + epssitedetails.size());
			while (read.hasNext()) {

				EPS readelement = read.next();

				// System.out.print(countryobjectid);

				System.out.println(readelement.getName());
				/* System.out.println(epsoppurtunitydetails.get(0).getId()); */
				// projectdata =
				// projectworktype.opputunityCreateProject(epsoppurtunitydetails);

			}
			// epsws.CreateEPS(epssitedetails, locationid);
		}
		}

		catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
		return epssitedetails;

	}

	public List<EPS> readEPSforAll() throws Exception {

		PrimaveraEPS epsws = new PrimaveraEPS();
		List<EPS> epss = new ArrayList<EPS>();
		try {
			// logger.info("Inside  readEPSforAll");
			epss = epsws.readEPS();
			// epss=epsws.readProjectEPS();
		} catch (Exception e) {

			logger.error(DTTErrorConstants.ERR015 + epss.size());
			throw e;
		}
		return epss;

	}

	public List<EPS> readProjectEPS(int objectid) throws Exception {
		List<EPS> epss = null;
		try {
			PrimaveraEPS epsws = new PrimaveraEPS();
			epss = new ArrayList<EPS>();

			epss = epsws.readWSProjectEPS(objectid);
		} catch (Exception e) {

			logger.error(DTTErrorConstants.ERR016 + e);
			throw e;
		}
		return epss;

	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		try {
			PrimaveraEPSWSClient primaverawsclient = new PrimaveraEPSWSClient();
			primaverawsclient.readAndCreateEPS();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
