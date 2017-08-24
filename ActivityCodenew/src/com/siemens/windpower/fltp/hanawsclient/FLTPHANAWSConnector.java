package com.siemens.windpower.fltp.hanawsclient;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;




//import javax.xml.bind.DatatypeConverter;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.io.BufferedReader;
//import javax.json.JsonObject;
//import java.util.ArrayList;
//import java.net.Proxy; Commented: used for Setting proxy to URLConnection
//import java.net.HttpURLConnection;
//import java.net.InetSocketAddress;
import java.security.Security;
import java.security.Provider;

import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.fltp.dao.ActivityMasterDAO;
import com.siemens.windpower.fltp.dao.AssignmentMasterDAO;
import com.siemens.windpower.fltp.dao.ProjectMasterDAO;
import com.siemens.windpower.fltp.dao.UpdateLastReadDAO;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraActivityWSClient;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;

public class FLTPHANAWSConnector {

	// Commented:Used for Setting proxy object to URL Connection if required
	// public static Proxy siemensproxyObj=null;
	// public static final String usernamePassword =
	// "WW007\\JAGTA00S:$happy123";

	// URL Connection objects for HANA webservices
	public static URLConnection projectMasterURLConnectionObj = null;
	public static URLConnection activityURLConnectionObj = null;
	public static URLConnection assignmentURLConnectionObj = null;

	static Map prop = null;
	static String projectmasterurl = null;
	static String activityurl = null;
	static String assignmenturl = null;
	static Logger logger = null;
UpdateLastReadDAO  lastread= null;
	// This class is used as a connector to connect to HANA Webservices

	public FLTPHANAWSConnector() throws Exception {
		ReadProperties read = new ReadProperties();
		prop = read.getPropertiesMap();
		logger = Logger.getLogger(FLTPHANAWSConnector.class);
		lastread= new UpdateLastReadDAO();
		Calendar currentDate = Calendar.getInstance(); // Get the current date
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // format
																			// it
																			// as
																			// per
																			// your
												// requirement
		 String hanawsfromdate = formatter.format(currentDate.getTime());
		// Commented:Used for Setting proxy object to URL Connection if required
		// getProxyObject();
		// System.out.println("Proxy Object is Set: "+siemensproxyObj);
		setAuthenticator();
		// logger.info("Authenticator is Set: ");
		String isfullload = prop.get("IS_FULL_DATA_LOAD").toString();

		projectmasterurl = prop.get("PROJECTMASTERHANASERVICEURL").toString();

		activityurl = prop.get("ACTIVITYHANASERVICEURL").toString();

		assignmenturl = prop.get("ASSIGNMENTHANASERVICEURL").toString();
		//String hanafromdate = prop.get("HANA_WS_FROM_DATE").toString();
		String hanafromdate=lastread.selectLastReadDate();	
		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = (Date) parser.parse(hanafromdate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
        date = cal.getTime();
		
		hanafromdate=formatter.format(date);
		
		
		if (isfullload.equalsIgnoreCase("N")) {
			// https://demchdc95qx.dc4ca.siemens.de:4300/siemens/FLTP/xs/fltp_outbound/odata/dtt_project.xsodata/DTT_PROJECT?$format=json&$filter=(CREATED_DATE
			// gt '2015-03-11')
			if (hanafromdate != null) {
				hanawsfromdate = hanafromdate;
			}

			projectmasterurl = projectmasterurl
					+ "&$filter=(CREATED_DATE%20gt%20%27" + hanawsfromdate
					+ "%27)";
			activityurl = activityurl + "&$filter=(CREATED_DATE%20gt%20%27"
					+ hanawsfromdate + "%27)";
			assignmenturl = assignmenturl + "&$filter=(CREATED_DATE%20gt%20%27"
					+ hanawsfromdate + "%27)";
			/*
			 * projectmasterurl = projectmasterurl +
			 * "&$filter=(CREATED_DATE gt '" + hanawsfromdate + "')";
			 * activityurl = activityurl + "&$filter=(CREATED_DATE gt '" +
			 * hanawsfromdate + "')"; assignmenturl = assignmenturl +
			 * "&$filter=(CREATED_DATE gt '" + hanawsfromdate + "')";
			 */

		}
		/*
		 * projectmasterurl= URLEncoder.encode(projectmasterurl, "UTF-8");
		 * activityurl=URLEncoder.encode(activityurl, "UTF-8");
		 * assignmenturl=URLEncoder.encode(assignmenturl, "UTF-8");
		 */
		initializeURLConnections(projectmasterurl, activityurl, assignmenturl);

		logger.info("Project Master URL" + projectmasterurl);
		logger.info("Activity  URL" + activityurl);
		logger.info("Assignment  URL" + assignmenturl);
		// logger.info("URL Connections are initialized : ");

	}

	private void initializeURLConnections(String projectMasterURL,
			String activityURL, String assignmentURL)
			throws MalformedURLException, IOException {

		projectMasterURLConnectionObj = getURLConnectionObj(projectMasterURL);
		activityURLConnectionObj = getURLConnectionObj(activityURL);
		assignmentURLConnectionObj = getURLConnectionObj(assignmentURL);

	}

	public void releaseURLConnections() throws MalformedURLException,
			IOException {

		projectMasterURLConnectionObj = null;
		activityURLConnectionObj = null;
		assignmentURLConnectionObj = null;

	}

	private void setAuthenticator() {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				// return new PasswordAuthentication("mydomain\\PU_FLTP",
				// "PU_fltp123".toCharArray());
				String hanausername = prop.get("HANAWSUSERNAME").toString();
				String hanapassword = prop.get("HANAWSPASSWORD").toString();
				return new PasswordAuthentication(hanausername, hanapassword
						.toCharArray());
			}
		});

	}

	private static URLConnection getURLConnectionObj(String hanawsurlstring)
			throws MalformedURLException, IOException {
		String parseBase64Binarystr = null;
		String parseBase64Binarybytesstr = null;
		URL hanawsurlObj = null;
		URLConnection hanawsurlConnectionObj = null;
		enableSunJSSEBasedHttpsInsideweblogic();
		hanawsurlObj = new URL(hanawsurlstring);
		// Commented: Used for Setting proxy object to URL Connection if
		// required
		// hanawsurlConnectionObj =
		// hanawsurlObj.openConnection(siemensproxyObj);

		hanawsurlConnectionObj = hanawsurlObj.openConnection();
		// Commented: Used for Setting proxy object to URL Connection if
		// required
		// parseBase64Binarystr = new
		// String(DatatypeConverter.parseBase64Binary(usernamePassword));
		// parseBase64Binarybytesstr =
		// DatatypeConverter.printBase64Binary(parseBase64Binarystr.getBytes());
		// hanawsurlConnectionObj.setRequestProperty("Proxy-Authorization",
		// "Basic " +parseBase64Binarybytesstr);
		return hanawsurlConnectionObj;
	}

	/*
	 * Commented: Used for Setting proxy object to URL Connection if required
	 * private Proxy getProxyObject() { if(siemensproxyObj == null) {
	 * siemensproxyObj = new Proxy(Proxy.Type.HTTP, new
	 * InetSocketAddress("proxy.erlm.siemens.de", 81));
	 * 
	 * } return siemensproxyObj; }
	 */
	/*
	 * public static JsonArray getProjectMasterRecords(String hanawsurlstring)
	 * throws MalformedURLException, IOException {
	 * if(projectMasterURLConnectionObj == null) {
	 * projectMasterURLConnectionObj=getURLConnectionObj(hanawsurlstring); }
	 * projectMasterURLConnectionObj.connect(); InputStream inputStreamObj =
	 * projectMasterURLConnectionObj.getInputStream(); JsonReader rdr =
	 * Json.createReader(inputStreamObj);
	 * 
	 * JsonObject results=rdr.readObject();
	 * //System.out.println("results"+results.toString());
	 * results.getString("d");
	 * System.out.println("results single array"+results.getString("d"));
	 * JsonArray projectmasterRecords = rdr.readArray(); //JsonArray results =
	 * obj.getJsonArray("data"); inputStreamObj.close(); return
	 * projectmasterRecords; }
	 */

	public static JsonArray getProjectMasterRecords(String hanawsurlstring)
			throws MalformedURLException, IOException {
		if (projectMasterURLConnectionObj == null) {
			projectMasterURLConnectionObj = getURLConnectionObj(hanawsurlstring);
		}

		projectMasterURLConnectionObj.connect();
		InputStream inputStreamObj = projectMasterURLConnectionObj
				.getInputStream();
		JsonReader rdr = Json.createReader(inputStreamObj);
		/*
		 * JsonReader rdr = Json.createReader(new FileReader(
		 * "D:/Oracle/Middleware/user_projects/domains/SWPDTTP6/swpdttp6/config/PROJECT.json"
		 * ));
		 */
		// Get the Root JSON Object
		JsonObject results = rdr.readObject();
		// Get the Root JSON Value object
		JsonValue djsonvalueObject = results.get("d");
		JsonObject djsonobject = null;
		if (djsonvalueObject == null) {
			System.out
					.println("HANA Project WEBSERVICE OUPUT DOES NOT HAVE d key...");
		}

		if (djsonvalueObject.getValueType() == ValueType.OBJECT) {
			djsonobject = (JsonObject) djsonvalueObject;
		} else {
			System.out
					.println("HANA PROJECT WEBSERVICE OUPUT DOES NOT HAVE d key value as JSON Object instead it is ..."
							+ djsonvalueObject.getValueType());
		}

		// JsonArray projectmasterRecords = rdr.readArray();
		JsonArray projectmasterRecords = djsonobject.getJsonArray("results");
		// JsonArray results = obj.getJsonArray("data");
		// inputStreamObj.close();
		return projectmasterRecords;
		// return null;
	}

	private static void printRecords(JsonArray jsonArray, String tablename) {

		System.out.println("printRecords details..." + tablename);

		// logger.info("printRecords....." + jsonArray.size());
		/* //logger.info("printRecords....."+jsonArray.toString()); */
		for (int recordcount = 0; recordcount < jsonArray.size(); recordcount++) {
			/*
			 * for(int recordcount=0;recordcount<2;recordcount++) {
			 */
			/*
			 * //logger.info("printRecords details..."+jsonArray.getJsonObject(
			 * recordcount).toString());
			 * System.out.println("printRecords details..."
			 * +jsonArray.getJsonObject(recordcount).toString());
			 */
		}
		System.out.println("printRecords details..." + tablename);
		System.out.println("printRecords details..." + jsonArray.size());
	}

	/*
	 * public static JsonArray getActivityRecords(String hanawsurlstring) throws
	 * MalformedURLException, IOException { if(activityURLConnectionObj == null)
	 * { activityURLConnectionObj=getURLConnectionObj(hanawsurlstring); }
	 * activityURLConnectionObj.connect(); InputStream inputStreamObj =
	 * activityURLConnectionObj.getInputStream(); JsonReader rdr =
	 * Json.createReader(inputStreamObj); //JsonObject results=rdr.readObject();
	 * JsonArray activityRecords = rdr.readArray(); //JsonArray results =
	 * obj.getJsonArray("data"); inputStreamObj.close(); return activityRecords;
	 * }
	 */
	public static JsonArray getActivityRecords(String hanawsurlstring)
			throws MalformedURLException, IOException {
		if (activityURLConnectionObj == null) {
			activityURLConnectionObj = getURLConnectionObj(hanawsurlstring);
		}
		activityURLConnectionObj.connect();
		InputStream inputStreamObj = activityURLConnectionObj.getInputStream();
		JsonReader rdr = Json.createReader(inputStreamObj);
		/*
		 * JsonReader rdr = Json.createReader(new FileReader(
		 * "D:/Oracle/Middleware/user_projects/domains/SWPDTTP6/swpdttp6/config/ACTIVITY.json"
		 * ));
		 */
		// Get the Root JSON Object
		JsonObject results = rdr.readObject();
		// Get the Root JSON Value object
		JsonValue djsonvalueObject = results.get("d");
		JsonObject djsonobject = null;
		if (djsonvalueObject == null) {
			System.out
					.println("HANA ACTIVITY WEBSERVICE OUPUT DOES NOT HAVE d key...");
		}

		if (djsonvalueObject.getValueType() == ValueType.OBJECT) {
			djsonobject = (JsonObject) djsonvalueObject;
		} else {
			System.out
					.println("HANA ACTIVITY WEBSERVICE OUPUT DOES NOT HAVE d key value as JSON Object instead it is ..."
							+ djsonvalueObject.getValueType());
		}

		// JsonArray projectmasterRecords = rdr.readArray();
		JsonArray activityRecords = djsonobject.getJsonArray("results");
		// JsonArray results = obj.getJsonArray("data");
		// inputStreamObj.close();
		System.out.println("activityRecords" + activityRecords.size());
		return activityRecords;

	}

	/*
	 * public static JsonArray getAssignmentRecords(String hanawsurlstring)
	 * throws MalformedURLException, IOException { if(assignmentURLConnectionObj
	 * == null) {
	 * assignmentURLConnectionObj=getURLConnectionObj(hanawsurlstring); }
	 * assignmentURLConnectionObj.connect(); InputStream inputStreamObj =
	 * assignmentURLConnectionObj.getInputStream(); JsonReader rdr =
	 * Json.createReader(inputStreamObj); //JsonObject results=rdr.readObject();
	 * JsonArray assignmentRecords = rdr.readArray(); //JsonArray results =
	 * obj.getJsonArray("data"); inputStreamObj.close(); return
	 * assignmentRecords; }
	 */
	public static JsonArray getAssignmentRecords(String hanawsurlstring)
			throws MalformedURLException, IOException {
		if (assignmentURLConnectionObj == null) {
			assignmentURLConnectionObj = getURLConnectionObj(hanawsurlstring);
		}
		assignmentURLConnectionObj.connect();
		InputStream inputStreamObj = assignmentURLConnectionObj
				.getInputStream();
		JsonReader rdr = Json.createReader(inputStreamObj);
		/*
		 * JsonReader rdr = Json.createReader(new FileReader(
		 * "D:/Oracle/Middleware/user_projects/domains/SWPDTTP6/swpdttp6/config/ASSIGNMENT.json"
		 * ));
		 */
		// Get the Root JSON Object
		JsonObject results = rdr.readObject();
		// Get the Root JSON Value object
		JsonValue djsonvalueObject = results.get("d");
		JsonObject djsonobject = null;
		if (djsonvalueObject == null) {
			System.out
					.println("HANA ASSIGNMENT WEBSERVICE OUPUT DOES NOT HAVE d key...");
		}

		if (djsonvalueObject.getValueType() == ValueType.OBJECT) {
			djsonobject = (JsonObject) djsonvalueObject;
		} else {
			System.out
					.println("HANA ASSIGNMENT WEBSERVICE OUPUT DOES NOT HAVE d key value as JSON Object instead it is ..."
							+ djsonvalueObject.getValueType());
		}

		// JsonArray projectmasterRecords = rdr.readArray();
		JsonArray assignmentRecords = djsonobject.getJsonArray("results");
		// JsonArray results = obj.getJsonArray("data");
		// inputStreamObj.close();
		System.out.println("assignmentRecords" + assignmentRecords.size());
		return assignmentRecords;

	}

	public static void loadFLTPData() throws MalformedURLException,
			IOException, SQLException, NamingException, ParseException {

		logger.info("FLTPHANAWSConnector : STARTED getProjectMasterRecords");
		String inputfiledirectorypath = prop.get("INPUTFILEDIRECTORYPATH")
				.toString();
		String iswtiretofile=prop.get("IS_WRITE_TO_FILE").toString();
		JsonArray projectmasterRecords = getProjectMasterRecords(projectmasterurl);
		if(iswtiretofile.equalsIgnoreCase("Y")){
		writeInputToFile(projectmasterRecords, inputfiledirectorypath,
				DTTConstants.PROJECTMASTERINPUTFILENAME);
	}
		// PASS THE LIST TO DAO Class
		printRecords(projectmasterRecords, "Projectmaster");
		ProjectMasterDAO projectmasterDAO = new ProjectMasterDAO();
		int insertedrecordcount = projectmasterDAO
				.insertProjectMasterRecords(projectmasterRecords);
		logger.info("FLTPHANAWSConnector : insertedrecordcount of getProjectMasterRecords"
				+ insertedrecordcount);
		logger.info("FLTPHANAWSConnector : COMPLETED getProjectMasterRecords");

		logger.info("FLTPHANAWSConnector : STARTED getActivityRecords");
		JsonArray activityRecords = getActivityRecords(activityurl);
		if(iswtiretofile.equalsIgnoreCase("Y")){
		  writeInputToFile(activityRecords, inputfiledirectorypath,
		  DTTConstants.ACTIVITYINPUTFILENAME);
		}
		// PASS THE LIST TO DAO Class
		printRecords(activityRecords, "Activity");
		ActivityMasterDAO activityDAO = new ActivityMasterDAO();
		int insertedactivityrecordcount = activityDAO
				.insertActivityRecords(activityRecords);
		logger.info("FLTPHANAWSConnector : COMPLETED getActivityRecords"
				+ insertedactivityrecordcount);

		logger.info("FLTPHANAWSConnector : STARTED getAssignmentRecords");
		JsonArray assignmentRecords = getAssignmentRecords(assignmenturl);
		if(iswtiretofile.equalsIgnoreCase("Y")){
		  writeInputToFile(assignmentRecords, inputfiledirectorypath,
		  DTTConstants.ASSIGNMENTINPUTFILENAME);
		}
		// PASS THE LIST TO DAO Class
		printRecords(assignmentRecords, "Assignment");
		AssignmentMasterDAO assignmentDAO = new AssignmentMasterDAO();
		int insertedassignmentrecordcount = assignmentDAO
				.insertAssignmentRecords(assignmentRecords);
		logger.info("FLTPHANAWSConnector : COMPLETED getAssignmentRecords"
				+ insertedassignmentrecordcount);
		logger.info("FLTPHANAWSConnector : Perform Santiy Check");
		performHanaWSDataSanityCheckAndupdate();
	}

	public static void performHanaWSDataSanityCheckAndupdate()
			throws NamingException, SQLException, IOException {
		ProjectMasterDAO projectmasterDAO = new ProjectMasterDAO();
		ActivityMasterDAO activitymasterDAO = new ActivityMasterDAO();
		projectmasterDAO.performProjectMasterCheckForSanityAndUpdate();
		activitymasterDAO.performActivityCheckForSanityAndUpdate();
		AssignmentMasterDAO assignmentmasterDAO = new AssignmentMasterDAO();
		assignmentmasterDAO.performAssignmentCheckForSanityAndUpdate();
	}

	public static void loadPrimaveraData() throws Exception {
		PrimaveraActivityWSClient activitywsclient = new PrimaveraActivityWSClient();

		activitywsclient.insertActivityOutboundData();
	}

	private static void writeInputToFile(JsonArray jsonArray, String filepath,
			String filename) throws IOException {
		System.out.println("START: writeInputToFile....." + filename);

		DateFormat dateFormat = new SimpleDateFormat(
				DTTConstants.FILENAMEDATEFORMAT);
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
		filename = filepath + dateFormat.format(cal.getTime()) + "_" + filename;
		FileWriter file = null;
		try {
			int jsonarraysize = jsonArray.size();

			file = new FileWriter(filename);
			file.write(jsonArray.toString());
		} finally {
			file.flush();
			file.close();

		}
		System.out.println("END: Input is written to a file:" + filename);

	}

	public static void enableSunJSSEBasedHttpsInsideweblogic() {
		System.out.println("START:enableSunJSSEBasedHttpsInsideweblogic");
		try {
			System.out
					.println("enableSunJSSEBasedHttpsInsideweblogic  INSIDE TRY");
			System.setProperty("java.protocol.handler.pkgs",
					"com.sun.net.ssl.internal.www.protocol");
			System.out
					.println("enableSunJSSEBasedHttpsInsideweblogic PROPERTY SET");
			Class sunjsseproviderclass = Class
					.forName("com.sun.net.ssl.internal.www.protocol");

			System.out
					.println("enableSunJSSEBasedHttpsInsideweblogic  AFTER CLASS FORNAME"
							+ Security.getProvider("SunJSSE"));

			Security.addProvider((Provider) sunjsseproviderclass.newInstance());
			System.out
					.println("enableSunJSSEBasedHttpsInsideweblogic   PROVIDER ADDED");

		} catch (Exception ex) {
			System.out
					.println("enableSunJSSEBasedHttpsInsideweblogic  PROVIDER NOT ADDED");
		}

		System.out.println("END:enableSunJSSEBasedHttpsInsideweblogic");
	}

	public static void main(String[] args) throws Exception {
		FLTPHANAWSConnector obj = new FLTPHANAWSConnector();
		try {
			// logger.info("FLTPHANAWSConnector : STARTED");
			obj = new FLTPHANAWSConnector();
			FLTPHANAWSConnector.loadFLTPData();
			// logger.info("FLTPHANAWSConnector : DATA LOAD COMPLETED");

		} catch (Exception ex) {
			logger.error("Error...");
			ex.printStackTrace();
		} finally {
			obj.releaseURLConnections();
			// logger.info("releaseURLConnections :Completed ");
		}

	}

}
