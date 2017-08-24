package com.siemens.windpower.fltp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.siemens.windpower.fltp.beans.ActivityMasterBean;

import com.siemens.windpower.common.DTTConstants;

public class ActivityMasterDAO {
	ActivityMasterBean activitybean = new ActivityMasterBean();
	ActivityMasterBean wbsbean = new ActivityMasterBean();
	Logger logger = null;

	public ActivityMasterDAO() {
		logger = Logger.getLogger(ActivityMasterDAO.class);
	}

	public ActivityMasterBean getActivitytMasterDAO() throws Exception {
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			stmt.setMaxRows(50);
			rs = stmt
					.executeQuery("select distinct act.row_Id,act.Actual_Hours, act.HQ_TECH_ID_NUMBER,act.DTT_Activity_Object_ID,pms.Project_Number,pms.Country_Id,pms.Site_Id,pms.Location_Shore,act.Source_System,act.SAP_Parent_Object_ID,act.Start_Date,act.Maintenance_Plan_ID,act.MP_Call_Number,act.Activity_Name,act.Turbine_Name,act.Turbine_Type,act.Notification_ID,act.Notification_Status,act.Service_Order_ID,act.Order_Status,act.RU_TECH_ID_NUMBER,act.Actual_hours from Activity act,ProjectMaster pms,LastRead lr where act.SAP_Parent_Object_ID=pms.SAP_Parent_Object_ID and  lr.Table_Name='Activity' and  act.Created_Date>=lr.LastReadDate and act.status_flag not in ('Y','NV','E') and act.Activity_Name!=''");

			List<Map<String, Object>> activitymasterdata = new ArrayList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();
			
			while (rs.next()) {
				int activitynumber = 1;
				do {
					
					Map map = new HashMap();
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						String key = null;
						String value = null;
						key = meta.getColumnName(i);
						//logger.info("i"+i);
						if (key.contains("DTT_Activity_Object_ID")) {

							if (activitynumber == 0) {
								value = rs.getString(key);

							}
							if (activitynumber == 1) {
								value = rs.getString(key);
								value = value + DTTConstants.ATTACH_D;

							}
							
						} else if (key.contains("Activity_Name")) {

							if (activitynumber == 0) {
								value = rs.getString(key);
								value = value + DTTConstants.ATTACH_EFFORT;
								activitynumber = 2;
							}
							if (activitynumber == 1) {
								value = rs.getString(key);
								value = value + DTTConstants.ATTACH_DEMAND;
								activitynumber = 0;
							}

						} else {

							value = rs.getString(key);
						}

						map.put(key, value);
						// logger.info(key + " " + value);
					}
					activitymasterdata.add(map);
				} while (activitynumber == 0);
			}
			activitybean.setActivitymasterdata(activitymasterdata);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// conn.close();
		}
		return activitybean;

	}

	public ActivityMasterBean getWBStMasterDAO() throws Exception {
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			stmt.setMaxRows(50);
			rs = stmt
					.executeQuery("select distinct act.row_Id,act.Warranty_Start_Date,act.Warranty_End_Date,pms.work_type, pms.Project_Number,pms.Project_Name,pms.Country_Id,pms.Site_Id,pms.Location_Shore,act.Source_System,act.SAP_Parent_Object_ID,act.DTT_Activity_Object_ID,act.Start_Date,act.Maintenance_Plan_ID,act.MP_Call_Number,act.Activity_Name,act.Turbine_Name,act.Turbine_Type,act.Notification_ID,act.Notification_Status,act.Service_Order_ID,act.Order_Status,act.HQ_TECH_ID_NUMBER,act.Turbine_Funct_Loc_ID,act.RU_TECH_ID_NUMBER,act.Actual_hours from Activity act,ProjectMaster pms,LastRead lr where act.SAP_Parent_Object_ID=pms.SAP_Parent_Object_ID and lr.Table_Name='Activity' and  act.Created_Date>=lr.LastReadDate and act.status_flag not in ('Y','NV','E')");

			List<Map<String, Object>> wbsmasterdata = new ArrayList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();

			while (rs.next()) {
				Map map = new HashMap();

				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String key = meta.getColumnName(i);

					String value = rs.getString(key);
					map.put(key, value);

					/* System.out.println(key + " " + value); */
				}
				wbsmasterdata.add(map);
			}

			// UpdateProcessedRecords.wbsUpdateProcess(uniquelist);
			// //logger.info("WBS wbsmasterdata Size"+wbsmasterdata.size());
			wbsbean.setWbsmasterdata(wbsmasterdata);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// conn.close();
		}
		return wbsbean;

	}

	public void performActivityCheckForSanityAndUpdate()
			throws NamingException, SQLException, IOException {
		// logger.info("InSide performActivityCheckForSanityAndUpdate");
		// Read project master records
		// Check Sanity
		// Insert Error table for sanity failed records
		String sourcesystem = null;
		String sapparentobjectid = null;
		String dttactivityobjectid = null;
		String hqtechid = null;

		String turbinename = null;
		String activityname = null;
		String turbinetype = null;

		Date createdate = null;

		int rowid = 0;

		// Processing Flags for Error handling
		String errormessage = null;

		Map map = new HashMap();

		InsertErrorDAO errordao = new InsertErrorDAO();
		UpdateProcessedRecords updatedao = new UpdateProcessedRecords();

		Connection conn = null;

		conn = DAOManagerForDTT.getConnection();

		String sql = "select Row_Id,Created_Date,Source_System,Turbine_Type,Turbine_Name,Activity_Name,SAP_Parent_Object_ID,HQ_TECH_ID_NUMBER,DTT_Activity_Object_ID from  Activity where status_flag!='Y'";

		PreparedStatement preparedStmt = conn.prepareStatement(sql);
		ResultSet rs;
		preparedStmt = conn.prepareStatement(sql);
		rs = preparedStmt.executeQuery();

		while (rs.next()) {
			createdate = rs.getDate("CREATED_DATE");
			sourcesystem = rs.getString("SOURCE_SYSTEM");
			sapparentobjectid = rs.getString("SAP_PARENT_OBJECT_ID");
			turbinetype = rs.getString("Turbine_Type");
			turbinename = rs.getString("Turbine_Name");
			activityname = rs.getString("Activity_Name");
			hqtechid = rs.getString("HQ_TECH_ID_NUMBER");
			dttactivityobjectid = rs.getString("DTT_Activity_Object_ID");

			rowid = rs.getInt("Row_Id");
			errormessage = getMandatoryFieldErrorMessage(sourcesystem,
					sapparentobjectid, turbinetype, turbinename, activityname,
					hqtechid, createdate, dttactivityobjectid);
			if (errormessage != null) {
				map.put("ErrorMessage", errormessage);
				map.put("Row_id", rowid);

			}

		}

		errordao.insertSanityErrorRecords("Activity", map);

		updatedao.updateprocessed("Activity", map);
	}

	/*
	 * String sourcesystem,String sapparentobjectid,int turbinequantity,String
	 * opportunityid,int worktype,int customerIFA String projectnumber,int
	 * cycles,double probability,String locationshore,String customername,Date
	 * createdate, String opportunityname, String countryid,String siteid
	 */

	public String getMandatoryFieldErrorMessage(String sourcesystem,
			String sapparentobjectid, String turbinetype, String turbinename,
			String activityname, String hqtechid, Date createdate,
			String dttactivityobjectid) {
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

		if (turbinetype == null) {
			return "Missing Mandatory Field Value for Turbine_Type Or Value for "
					+ turbinetype + " is not valid.";
		}

		if (turbinename == null) {
			return "Missing Mandatory Field Value for Turbine_Name Or Value for "
					+ turbinename + " is not valid.";
		}
		if (activityname == null) {
			return "Missing Mandatory Field Value for Activity_Name Or Value for "
					+ activityname + " is not valid.";
		}
		if (hqtechid == null) {
			return "Missing Mandatory Field Value for HQ_TECH_ID_NUMBER Or Value for "
					+ hqtechid + " is not valid.";
		}
		if (dttactivityobjectid == null) {
			return "Missing Mandatory Field Value for DTT_ACTIVITY_OBJECT_ID Or Value for "
					+ dttactivityobjectid + " is not valid.";
		}
		if (dttactivityobjectid.length() > 20) {
			return " Mandatory Field Value for DTT_ACTIVITY_OBJECT_ID is there greather than 20 chars Or Value for "
					+ dttactivityobjectid + " is not valid.";
		}

		return null;

	}

	public int getWBSMasterCount() throws Exception {

		int count = 0;
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("select count(act.row_id) from Activity  act where  act.status_flag!='Y'");

			while (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// conn.close();
		}
		// logger.info("Count of WBS Records to be processed"+count);
		System.out.println("Count of WBS Records to be processed" + count);

		return count;
	}

	public int getActivityMasterCount() throws Exception {

		int count = 0;
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("select count(act.row_id) from Activity  act where  act.status_flag!='Y'");

			while (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// conn.close();
		}
		// logger.info("Count of Activity Records to be processed"+count);
		System.out.println("Count of Activity Records to be processed" + count);

		return count;
	}

	public int insertActivityRecords(JsonArray activityrecords)
			throws SQLException, NamingException, IOException, ParseException

	{

		System.out.println("Inside ActivityDAO: insertActivityRecords");
		int insertcount = 0;

		Connection conn = null;
		PreparedStatement preparedStmt = null;
		JsonObject activityRecord = null;
		String query = "INSERT INTO Activity(Created_Date,Source_system,SAP_Parent_Object_ID,DTT_Activity_Object_ID,Start_Date,Maintenance_Plan_ID,MP_Call_Number,Activity_Name,Turbine_Type,Turbine_Name,Notification_ID,Notification_Status,Service_Order_ID,Order_Status,HQ_TECH_ID_NUMBER,RU_TECH_ID_NUMBER,Actual_hours,Warranty_Start_Date,Warranty_End_Date,Notification_Desc,Service_Order_Desc,Turbine_Funct_Loc_ID,Status_Flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		conn = DAOManagerForDTT.getConnection();
		preparedStmt = conn.prepareStatement(query);
		// preparedStmt.setMaxRows(5000);
		// System.out.println(" ActivityDAO: maxrows set 5000");
		int BATCH_SIZE = 5000;
		List<Object> jsonObjects = new ArrayList<Object>();

		int outputIndex = 0;
		for (int recordcount = 0; recordcount < activityrecords.size(); recordcount++) {
			activityRecord = activityrecords.getJsonObject(recordcount);
			/*
			 * System.out.println("ActivityRecord: insertActivityRecords"+
			 * activityRecord.toString());
			 */
			jsonObjects.add(activityRecord);
			buildActivityPreparedStatement(preparedStmt, activityRecord);
			preparedStmt.addBatch();
			/*
			 * System.out.println(
			 * "Inside ActivityDAO: insertActivityRecords activityRecord"
			 * +activityRecord);
			 */
			// Flush if max objects per file has been reached.
			if (jsonObjects.size() == BATCH_SIZE) {
				int[] countarr = preparedStmt.executeBatch();
				insertcount = insertcount + countarr.length; // /Need to check
																// how to get
																// the exact
																// successful
																// insert count
				preparedStmt.clearBatch();
				System.out
						.println("Inside ActivityDAO: insertActivityRecords size count"
								+ jsonObjects.size());
				jsonObjects.clear();
				outputIndex++;
			}

		}
		int[] countarr = preparedStmt.executeBatch();
		insertcount = insertcount + countarr.length;
		System.out
				.println("Inside ActivityDAO: insertActivityRecords not matching batch size count"
						+ insertcount);

		conn.commit();

		System.out.println("End ActivityDAO: insertActivityRecords");
		return insertcount;
	}

	private PreparedStatement buildActivityPreparedStatement(
			PreparedStatement preparedStmt, JsonObject activityRecord)
			throws SQLException, ParseException {

		preparedStmt.setDate(1,
				getSqldateCreateDate(activityRecord.getString("CREATED_DATE")));
		preparedStmt.setString(2,
				(activityRecord.getString("SOURCE_SYSTEM", "")));
		preparedStmt.setString(3,
				(activityRecord.getString("SAP_PARENT_OBJECT_ID", "")));
		preparedStmt.setString(4,
				(activityRecord.getString("DTT_ACTIVITY_OBJECT_ID", "")));
		preparedStmt.setDate(5,
				getSqldate(activityRecord.getString("START_DATE", "")));
		preparedStmt.setString(6,
				(activityRecord.getString("MAINTENANCE_PLAN_ID", "")));
		preparedStmt.setString(7,
				(activityRecord.getString("MP_CALL_NUMBER", "")));
		preparedStmt.setString(8,
				(activityRecord.getString("ACTIVITY_NAME", "")));
		preparedStmt.setString(9,
				(activityRecord.getString("TURBINE_TYPE", "")));
		preparedStmt.setString(10,
				(activityRecord.getString("TURBINE_NAME", "")));
		preparedStmt.setString(11,
				(activityRecord.getString("NOTIFICATION_ID", "")));
		preparedStmt.setString(12,
				(activityRecord.getString("NOTIFICATION_STATUS", "")));
		preparedStmt.setString(13,
				(activityRecord.getString("SERVICE_ORDER_ID", "")));
		preparedStmt.setString(14,
				(activityRecord.getString("ORDER_STATUS", "")));
		preparedStmt.setString(15,
				(activityRecord.getString("HQ_TECH_ID_NUMBER", "")));
		preparedStmt.setString(16,
				(activityRecord.getString("RU_TECH_ID_NUMBER", "")));
		preparedStmt.setDouble(17, Double.parseDouble(activityRecord.getString(
				"ACTUAL_HOURS", "0.0")));
		preparedStmt
				.setDate(18, getSqldate(activityRecord.getString(
						"WARRANTY_START_DATE", "")));
		preparedStmt.setDate(19,
				getSqldate(activityRecord.getString("WARRANTY_END_DATE", "")));
		preparedStmt.setString(20,
				(activityRecord.getString("NOTIFICATION_DESC", "")));
		preparedStmt.setString(21,
				(activityRecord.getString("SERVICE_ORDER_DESC", "")));
		preparedStmt.setString(22,
				(activityRecord.getString("TURBINE_FUNC_LOC_ID", "")));
		preparedStmt.setString(23, "");
		return preparedStmt;
	}

	public Date getSqldate(String datestr) throws ParseException {
		java.sql.Date sqlStartDate = null;

		if (datestr.equalsIgnoreCase("null") || datestr.equalsIgnoreCase("")) {
			return sqlStartDate;
		} else {

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			/*
			 * DateFormat df = new SimpleDateFormat( "dd-MM-yyyy");
			 */

			/* DateFormat df = new SimpleDateFormat(); */
			java.util.Date date = df.parse(datestr);
			sqlStartDate = new java.sql.Date(date.getTime());
		}

		return sqlStartDate;

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

	public String getFiledValue(String jsonvalue) {

		if (jsonvalue.equalsIgnoreCase("null")) {
			jsonvalue = "";
		}

		return jsonvalue;

	}
}
