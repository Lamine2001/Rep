package com.siemens.windpower.fltp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class ProjectMasterDAO {

	ProjectMasterBean epsbean = new ProjectMasterBean();
	ProjectMasterBean projectbean = new ProjectMasterBean();
	Logger logger = null;

	public ProjectMasterDAO() {
		logger = Logger.getLogger(ProjectMasterDAO.class);
	}

	public ProjectMasterBean getProjectMasterDataDAO() throws SQLException {
		Connection conn = null;
		try {
			// logger.info(DTTErrorConstants.INF007);
			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			List sfdcsitelist = new ArrayList();
			List sppsitelist = new ArrayList();
			List sppcountrylist = new ArrayList();
			List sfdccountrylist = new ArrayList();
			List locationshorelist = new ArrayList();
			stmt.setMaxRows(50);
			rs = stmt
					.executeQuery("select Site_Address,Retrofit_Campaign_ID,Source_System,SAP_Parent_Object_ID,Cycles,Turbine_Quantity,Probability,Project_Number,Project_Name,Work_Type,Location_Shore,Country_Id as countryidcreate,Site_Id,Site_Name,Customer_IFA,Customer_Name,Contract_Start,Contract_End,External_Status from  ProjectMaster pms,LastRead lr where lr.Table_Name='ProjectMaster' and  pms.Created_Date>=lr.LastReadDate and pms.status_flag not in ('Y','NV','E')");

			List<Map<String, Object>> projectmasterdata = new ArrayList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String key = meta.getColumnName(i);

					String value = rs.getString(key);
					map.put(key, value);

					/* System.out.println(key + " " + value); */
				}
				projectmasterdata.add(map);
			}
			projectbean.setProjectmasterdata(projectmasterdata);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// conn.close();
		}

		return projectbean;

	}

	public ProjectMasterBean getEPSMasterDataDAO() throws Exception {
		Connection conn = null;
		List<Map<String, Object>> epsmasterdata = null;
		try {
			// logger.info(DTTErrorConstants.INF008);
			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			List sfdcsitelist = new ArrayList();
			List sppsitelist = new ArrayList();
			List sppcountrylist = new ArrayList();
			List sfdccountrylist = new ArrayList();
			List locationshorelist = new ArrayList();
			rs = stmt
					.executeQuery("select Source_System,Work_Type,Location_Shore,Country_Id,Site_id,Site_Name from  ProjectMaster pms,LastRead lr where lr.Table_Name='ProjectMaster' and  pms.Created_Date>lr.LastReadDate and pms.status_flag not in ('Y','NV','E') ");

			epsmasterdata = new ArrayList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String key = meta.getColumnName(i);

					String value = rs.getString(key);
					map.put(key, value);

					/* System.out.println(key +" "+ value); */
				}
				epsmasterdata.add(map);
			}

			/*
			 * while ( rs.next() ) { String Source_System =
			 * rs.getString("Source_System"); String sapprojectid =
			 * rs.getString("SAP_Project_ID"); int worktype
			 * =rs.getInt("Work_Type"); String Turbine_Quantity
			 * =rs.getString("Turbine_Quantity"); int locationshore
			 * =rs.getInt("Location_Shore"); String customerIFA
			 * =rs.getString("Customer_IFA"); String customername
			 * =rs.getString("Customer_Name"); String contactid
			 * =rs.getString("Contract_Id"); String countryid
			 * =rs.getString("Country_id"); String siteid
			 * =rs.getString("Site_id"); String probability
			 * =rs.getString("PROBABILITY"); String External_Status
			 * =rs.getString("External_Status"); Date contractstart
			 * =rs.getDate("Contract_Start"); Date contractend
			 * =rs.getDate("Contract_end"); epsbean.setCountryid(countryid);
			 * epsbean.setSiteid(siteid); epsbean.setSapprojectid(sapprojectid);
			 * 
			 * if((worktype==1) || (worktype==2) || worktype==3 || worktype==4){
			 * 
			 * 
			 * 
			 * sppsitelist.add(siteid); sppcountrylist.add(countryid); }
			 * if((worktype==5) || (worktype==6) || (worktype==7)){
			 * sfdcsitelist.add(siteid); sfdccountrylist.add(countryid);
			 * 
			 * } if(locationshore==1){ locationshorelist.add("OnShore"); }else
			 * if (locationshore==2){ locationshorelist.add("OffShore"); }
			 * 
			 * System.out.println(Source_System); System.out.println(worktype);
			 * System.out.println(sapprojectid);
			 * 
			 * }
			 */
			epsbean.setSppsitelist(sppsitelist);
			epsbean.setSppcountrylist(sppcountrylist);
			epsbean.setSfdcsitelist(sfdcsitelist);
			epsbean.setSfdccountrylist(sfdccountrylist);
			epsbean.setLocationshorelist(locationshorelist);
			epsbean.setProjectmasterdata(epsmasterdata);
			// System.out.println(epsbean.getProjectmasterdata());
			// logger.info(DTTErrorConstants.INF009 + epsmasterdata.size());
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR010);
			// logger.info(DTTErrorConstants.INF010 + epsmasterdata.size());
			throw e;

		} finally {
			// conn.close();
		}
		return epsbean;

	}

	public int insertProjectMasterRecords(JsonArray projectmasterRecords)
			throws SQLException, NamingException, IOException, ParseException

	{

		System.out
				.println("Inside ProjectMasterDAO: insertProjectMasterRecords");
		int insertcount = 0;

		Connection conn = null;
		PreparedStatement preparedStmt = null;
		JsonObject projectmasterRecord = null;
		String query = "INSERT INTO ProjectMaster(Created_Date,Source_System,SAP_Parent_Object_ID,Work_Type,Turbine_Quantity,Location_Shore,Customer_IFA,Customer_Name,Project_Number,Project_Name,Country_Id,Site_id,Site_Name,Site_Address,Probability,External_Status,Contract_Start,Contract_End,Cycles,Retrofit_Campaign_ID,Status_Flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		conn = DAOManagerForDTT.getConnection();
		preparedStmt = conn.prepareStatement(query);
		for (int recordcount = 0; recordcount < projectmasterRecords.size(); recordcount++) {

			projectmasterRecord = projectmasterRecords
					.getJsonObject(recordcount);
			buildProjectMasterPreparedStatement(preparedStmt,
					projectmasterRecord);
			preparedStmt.addBatch();
		}

		int[] countarr = preparedStmt.executeBatch();
		insertcount = countarr.length; // /Need to check how to get the exact
										// successful insert count
		conn.commit();

		System.out.println("End ProjectMasterDAO: insertProjectMasterRecords");
		return insertcount;
	}

	private PreparedStatement buildProjectMasterPreparedStatement(
			PreparedStatement preparedStmt, JsonObject projectmasterRecord)
			throws SQLException, ParseException {

		System.out.println(projectmasterRecord.getString("CREATED_DATE"));
		System.out.println(getSqldateCreateDate(projectmasterRecord
				.getString("CREATED_DATE")));
		preparedStmt.setDate(1, getSqldateCreateDate(projectmasterRecord
				.getString("CREATED_DATE")));
		preparedStmt.setString(2,
				(projectmasterRecord.getString("SOURCE_SYSTEM", "")));
		logger.info(projectmasterRecord.getString("SAP_PARENT_OBJECT_ID"));
		preparedStmt.setString(3,
				(projectmasterRecord.getString("SAP_PARENT_OBJECT_ID", "")));
		preparedStmt.setInt(4, (projectmasterRecord.getInt("WORK_TYPE", 0)));
		preparedStmt.setInt(5, Integer.parseInt(projectmasterRecord.getString(
				"TURBINE_QUANTITY", "0")));
		preparedStmt.setString(6, Integer.toString(projectmasterRecord.getInt(
				"LOCATION_SHORE", 0)));
		//logger.info(projectmasterRecord.getInt("CUSTOMER_IFA"));
		preparedStmt.setInt(7, (projectmasterRecord.getInt("CUSTOMER_IFA", 0)));

		preparedStmt.setString(8,
				(projectmasterRecord.getString("CUSTOMER_NAME", "")));
		/* logger.info(projectmasterRecord.getString("CUSTOMER_NAME")); */
		preparedStmt.setString(9,
				(projectmasterRecord.getString("PROJECT_NUMBER", "")));
		preparedStmt.setString(10,
				(projectmasterRecord.getString("PROJECT_NAME", "")));
		preparedStmt.setString(11,
				(projectmasterRecord.getString("COUNTRY_ID", "")));
		preparedStmt.setString(12,
				(projectmasterRecord.getString("SITE_ID", "")));
		preparedStmt.setString(13,
				(projectmasterRecord.getString("SITE_NAME", "")));
		preparedStmt.setString(14,
				(projectmasterRecord.getString("SITE_ADDRESS", "")));
		preparedStmt.setDouble(15, Double.parseDouble(projectmasterRecord
				.getString("PROBABILITY", "0")));
		preparedStmt.setString(16,
				(projectmasterRecord.getString("EXTERNAL_STATUS", "")));
		preparedStmt
				.setDate(17, getSqldate(projectmasterRecord.getString(
						"CONTRACT_START", "")));
		preparedStmt.setDate(18,
				getSqldate(projectmasterRecord.getString("CONTRACT_END", "")));
		preparedStmt.setInt(19, (projectmasterRecord.getInt("CYCLES", 0)));
		/* logger.info(projectmasterRecord.getString("RETROFIT_CAMPAIGN_ID")); */
		preparedStmt.setString(20,
				(projectmasterRecord.getString("RETROFIT_CAMPAIGN_ID", "")));
		preparedStmt.setString(21, "");
		return preparedStmt;

	}

	public void performProjectMasterCheckForSanityAndUpdate()
			throws NamingException, SQLException, IOException {

		// logger.info("InSide performProjectMasterCheckForSanityAndUpdate");
		// Read project master records
		// Check Sanity
		// Insert Error table for sanity failed records
		String sourcesystem = null;
		String sapparentobjectid = null;
		int turbinequantity = 0;
		String opportunityid = null;
		int worktype = 0;
		int customerIFA = 0;
		String projectnumber = null;
		int cycles = 0;

		double probability = 0;

		// Project Code fields
		String locationshore = null;
		String customername = null;

		Date createdate = null;

		// Project fields For SFDC
		String opportunityname = null;
		// Project fields For Spirodon

		// EPS fields
		String countryid = null;

		String siteid = null;
		int rowid = 0;

		// Processing Flags for Error handling
		String errormessage = null;
		String processedflag = null;
		Map map = new HashMap();

		InsertErrorDAO errordao = new InsertErrorDAO();
		UpdateProcessedRecords updatedao = new UpdateProcessedRecords();

		Connection conn = null;

		conn = DAOManagerForDTT.getConnection();

		String sql = "select pms.SAP_PARENT_OBJECT_ID,pms.CYCLES,pms.TURBINE_QUANTITY,pms.CUSTOMER_IFA,pms.CUSTOMER_NAME,pms.PROJECT_NUMBER,pms.PROBABILITY,pms.CREATED_DATE,pms.Row_Id,pms.Source_System,pms.Work_Type,pms.Location_Shore,pms.Country_Id,pms.Site_id from  ProjectMaster pms,LastRead lr where lr.Table_Name='ProjectMaster' and  pms.Created_Date>lr.LastReadDate and pms.status_flag!='Y'";

		PreparedStatement preparedStmt = conn.prepareStatement(sql);
		ResultSet rs;
		preparedStmt = conn.prepareStatement(sql);
		rs = preparedStmt.executeQuery();

		while (rs.next()) {
			createdate = rs.getDate("CREATED_DATE");
			sourcesystem = rs.getString("SOURCE_SYSTEM");
			sapparentobjectid = rs.getString("SAP_PARENT_OBJECT_ID");
			worktype = rs.getInt("WORK_TYPE");
			turbinequantity = rs.getInt("TURBINE_QUANTITY");
			locationshore = rs.getString("LOCATION_SHORE");
			customerIFA = rs.getInt("CUSTOMER_IFA");
			customername = rs.getString("CUSTOMER_NAME");
			projectnumber = rs.getString("PROJECT_NUMBER");

			countryid = rs.getString("COUNTRY_ID");
			siteid = rs.getString("SITE_ID");

			probability = rs.getDouble("PROBABILITY");

			cycles = rs.getInt("CYCLES");
			rowid = rs.getInt("Row_Id");
			errormessage = getMandatoryFieldErrorMessage(sourcesystem,
					sapparentobjectid, turbinequantity, opportunityid,
					worktype, customerIFA, projectnumber, cycles, probability,
					locationshore, customername, createdate, opportunityname,
					countryid, siteid);
			if (errormessage != null) {
				map.put("ErrorMessage", errormessage);
				map.put("Row_id", rowid);

			}

		}
		errordao.insertSanityErrorRecords("Project_Master", map);

		updatedao.updateprocessed("Project_Master", map);

	}

	/*
	 * String sourcesystem,String sapparentobjectid,int turbinequantity,String
	 * opportunityid,int worktype,int customerIFA String projectnumber,int
	 * cycles,double probability,String locationshore,String customername,Date
	 * createdate, String opportunityname, String countryid,String siteid
	 */

	public String getMandatoryFieldErrorMessage(String sourcesystem,
			String sapparentobjectid, int turbinequantity,
			String opportunityid, int worktype, int customerIFA,
			String projectnumber, int cycles, double probability,
			String locationshore, String customername, Date createdate,
			String opportunityname, String countryid, String siteid) {
		if (createdate == null) {
			return "Missing Mandatory Field Value for Create_Date Or Value for "
					+ createdate + " is not valid.";

		}
		if (sourcesystem == null) {
			return "Missing Mandatory Field Value for Source_System Or Value for "
					+ sourcesystem + " is not valid.";
		}
		if (sapparentobjectid == null) {
			return "Missing Mandatory Field Value for SAP_Parent_Object_Id Or Value for "
					+ sapparentobjectid + " is not valid.";
		}
		if (worktype == 0) {
			return "Missing Mandatory Field Value for Work_Type Or Value for "
					+ worktype + " is not valid.";

		}
		if (sourcesystem.equalsIgnoreCase("SFDC")) {
			if (turbinequantity == 0) {
				return "Missing Mandatory Field Value for Turbine_Quantity Or Value for "
						+ turbinequantity + " is not valid.";
			}
			if (probability == 0) {
				return "Missing Mandatory Field Value for Probability Or Value for "
						+ probability + " is not valid.";
			}
			if (cycles == 0) {
				return "Missing Mandatory Field Value for Cycles Or Value for "
						+ cycles + " is not valid.";
			}
		}
		if (locationshore == null) {
			return "Missing Mandatory Field Value for Location_shore Or Value for "
					+ locationshore + " is not valid.";
		}
		if (customerIFA == 0) {

			if (customername != null || customername.equalsIgnoreCase("")) {
				return "Missing Mandatory Field Value for Customer_IFA Or Value for "
						+ customerIFA + " is not valid.";
			}
		}
		if (customername == null) {
			if (customerIFA != 0) {
				return "Missing Mandatory Field Value for Customer_Name Or Value for "
						+ customername + " is not valid.";
			}
		}
		if (projectnumber == null) {
			return "Missing Mandatory Field Value for Project_Number Or Value for "
					+ projectnumber + " is not valid.";
		}
		if (countryid == null) {
			return "Missing Mandatory Field Value for Country_Id Or Value for "
					+ countryid + " is not valid.";
		}
		if (siteid == null) {
			return "Missing Mandatory Field Value for Site_Id Or Value for "
					+ siteid + " is not valid.";
		}

		return null;

	}

	public int getProjectMasterCount() throws Exception {

		int count = 0;
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("select count(pms.row_id) from ProjectMaster pms where pms.status_flag!='Y'");

			while (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// conn.close();
		}
		// logger.info("Count of Project Records to be processed" + count);
		System.out.println("Count of Project Records to be processed" + count);

		return count;
	}

	public Date getSqldateCreateDate(String datestr) throws ParseException {
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

	public Date getSqldate(String datestr) throws ParseException {
		java.sql.Date sqlStartDate = null;

		if (datestr.equalsIgnoreCase("null") || datestr.equalsIgnoreCase("")) {
			return sqlStartDate;
		} else {

			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

			/* DateFormat df = new SimpleDateFormat(); */
			java.util.Date date = df.parse(datestr);
			sqlStartDate = new java.sql.Date(date.getTime());
		}

		return sqlStartDate;

	}

	public String getFiledValue(String jsonvalue) {

		if (jsonvalue.equalsIgnoreCase("null")) {
			jsonvalue = "";
		}

		return jsonvalue;

	}

	public static void main(String args[]) throws ParseException {

		String startDate = "01/02/2013";
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = sdf1.parse(startDate);
		java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());

		System.out.println(sqlStartDate);

	}
}
