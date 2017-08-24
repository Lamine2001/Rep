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

import com.siemens.windpower.fltp.beans.ActivityMasterBean;
import com.siemens.windpower.fltp.beans.AssignmentMasterBean;

public class AssignmentMasterDAO {

	public AssignmentMasterBean getAssignmentMasterDAO() throws SQLException {

		AssignmentMasterBean assignmentbean = new AssignmentMasterBean();
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			List sfdcsitelist = new ArrayList();
			List sppsitelist = new ArrayList();
			List sppcountrylist = new ArrayList();
			List sfdccountrylist = new ArrayList();
			List locationshorelist = new ArrayList();
			stmt.setMaxRows(500);
			rs = stmt
					.executeQuery("select distinct asgm.Row_Id,asgm.DTT_Activity_Object_ID,asgm.Planned_Hours,asgm.Role_ID,act.HQ_TECH_ID_NUMBER,pms.Project_Number,pms.Country_Id,pms.Site_Id,pms.Location_Shore,act.Source_System,act.SAP_Parent_Object_ID,act.DTT_Activity_Object_ID,act.Start_Date,act.Maintenance_Plan_ID,act.MP_Call_Number,act.Activity_Name,act.Turbine_Name,act.Turbine_Type,act.Notification_ID,act.Notification_Status,act.Service_Order_ID,act.Order_Status,act.HQ_TECH_ID_NUMBER,act.RU_TECH_ID_NUMBER,act.Actual_hours  from Assignment asgm,ProjectMaster pms,Activity act,LastRead lr where asgm.DTT_Activity_Object_ID=act.DTT_Activity_Object_ID and  act.SAP_Parent_Object_ID=pms.SAP_Parent_Object_ID and lr.Table_Name='Assignment' and  asgm.Created_Date>=lr.LastReadDate and asgm.status_flag not in ('Y','NV','E')");

			List<Map<String, Object>> assignmentmasterdata = new ArrayList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String key = meta.getColumnName(i);

					String value = rs.getString(key);

					map.put(key, value);

					/* System.out.println(key + " " + value); */
				}
				assignmentmasterdata.add(map);
			}
			assignmentbean.setAssignmentmasterdata(assignmentmasterdata);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// conn.close();
		}
		return assignmentbean;

	}

	public int getAssignmentMasterCount() throws Exception {

		int count = 0;
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("select count(act.row_id) from Assignment  act where  act.status_flag!='Y'");

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

	public void performAssignmentCheckForSanityAndUpdate()
			throws NamingException, SQLException, IOException {
		System.out.println("InSide performAssignmentCheckForSanityAndUpdate");
		// Read project master records
		// Check Sanity
		// Insert Error table for sanity failed records
		String sourcesystem = null;

		String dttactivityobjectid = null;

		int plannedhours = 0;
		String roleid = null;

		Date createdate = null;

		int rowid = 0;

		// Processing Flags for Error handling
		String errormessage = null;
		String processedflag = null;
		Map map = new HashMap();

		InsertErrorDAO errordao = new InsertErrorDAO();
		UpdateProcessedRecords updatedao = new UpdateProcessedRecords();

		Connection conn = null;

		conn = DAOManagerForDTT.getConnection();

		String sql = "select Row_Id,Created_Date,Source_System,DTT_Activity_Object_ID,Role_ID,Planned_Hours from Assignment where status_flag!='Y'";

		PreparedStatement preparedStmt = conn.prepareStatement(sql);
		ResultSet rs;
		preparedStmt = conn.prepareStatement(sql);
		rs = preparedStmt.executeQuery();

		while (rs.next()) {
			createdate = rs.getDate("CREATED_DATE");
			sourcesystem = rs.getString("SOURCE_SYSTEM");

			roleid = rs.getString("Role_ID");
			plannedhours = rs.getInt("Planned_Hours");

			dttactivityobjectid = rs.getString("DTT_Activity_Object_ID");

			rowid = rs.getInt("Row_Id");
			errormessage = getMandatoryFieldErrorMessage(sourcesystem, roleid,
					plannedhours, createdate, dttactivityobjectid);
			if (errormessage != null) {
				map.put("ErrorMessage", errormessage);
				map.put("Row_id", rowid);

			}

		}
		errordao.insertSanityErrorRecords("Assignment", map);

		updatedao.updateprocessed("Assignment", map);

	}

	/*
	 * String sourcesystem,String sapparentobjectid,int turbinequantity,String
	 * opportunityid,int worktype,int customerIFA String projectnumber,int
	 * cycles,double probability,String locationshore,String customername,Date
	 * createdate, String opportunityname, String countryid,String siteid
	 */

	public String getMandatoryFieldErrorMessage(String sourcesystem,
			String roleid, int plannedhours, Date createdate,
			String dttactivityobjectid) {
		if (createdate == null) {
			return "Missing Mandatory Field Value for Create_Date Or Value for "
					+ createdate + " is not valid.";

		}
		if (sourcesystem == null) {
			return "Missing Mandatory Field Value for Source_System Or Value for "
					+ sourcesystem + " is not valid.";
		}
		if (roleid == null) {
			return "Missing Mandatory Field Value for Role_Id Or Value for "
					+ roleid + " is not valid.";
		}

		if (plannedhours == 0) {
			return "Missing Mandatory Field Value for Planned_Hours Or Value for "
					+ plannedhours + " is not valid.";
		}

		if (dttactivityobjectid == null) {
			return "Missing Mandatory Field Value for DTT_ACTIVITY_OBJECT_ID Or Value for "
					+ dttactivityobjectid + " is not valid.";
		}

		return null;

	}

	public List getNonLaborHoursDAO() throws Exception {

		AssignmentMasterBean assignmentbean = new AssignmentMasterBean();
		List<Map<String, Object>> nonlaborhoursdata = new ArrayList<Map<String, Object>>();
		try {

			Connection conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			List sfdcsitelist = new ArrayList();
			List sppsitelist = new ArrayList();
			List sppcountrylist = new ArrayList();
			List sfdccountrylist = new ArrayList();
			List locationshorelist = new ArrayList();
			rs = stmt
					.executeQuery("select SUM(Planned_Hours) as Non_Labor_Hours,DTT_Activity_Object_ID from Assignment asgm, LastRead lr where lr.Table_Name='Assignment' and  asgm.Created_Date>lr.LastReadDate group by DTT_Activity_Object_ID");

			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String key = meta.getColumnName(i);

					String value = rs.getString(key);

					map.put(key, value);

					/* System.out.println(key + " " + value); */
				}
				nonlaborhoursdata.add(map);
			}
			assignmentbean.setNonlaborhoursdata(nonlaborhoursdata);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return nonlaborhoursdata;

	}

	private PreparedStatement buildAssignmentPreparedStatement(
			PreparedStatement preparedStmt, JsonObject assignmentRecord)
			throws SQLException, ParseException {
		preparedStmt
				.setDate(1, getSqldateCreateDate(assignmentRecord
						.getString("CREATED_DATE")));
		preparedStmt.setString(2,
				(assignmentRecord.getString("SOURCE_SYSTEM", "")));
		preparedStmt.setString(3,
				(assignmentRecord.getString("DTT_ACTIVITY_OBJECT_ID", "")));
		preparedStmt.setString(4, (assignmentRecord.getString("ROLE_ID", "")));
		preparedStmt.setInt(
				5,
				Integer.parseInt(assignmentRecord.getString("PLANNED_HOURS",
						"0").toString()));
		preparedStmt.setString(6, "");
		return preparedStmt;
	}

	public int insertAssignmentRecords(JsonArray assignmentrecords)
			throws SQLException, NamingException, IOException, ParseException

	{

		System.out.println("Inside AssignmentDAO: insertAssignmentRecords");
		int insertcount = 0;

		Connection conn = null;
		PreparedStatement preparedStmt = null;
		JsonObject AssignmentmasterRecord = null;
		String query = "INSERT INTO Assignment(Created_Date,Source_System,DTT_Activity_Object_ID,Role_ID,Planned_Hours,Status_Flag) VALUES (?,?,?,?,?,?)";
		conn = DAOManagerForDTT.getConnection();
		int BATCH_SIZE = 5000;
		List<Object> jsonObjects = new ArrayList<Object>();
		preparedStmt = conn.prepareStatement(query);
		int outputIndex = 0;
		for (int recordcount = 0; recordcount < assignmentrecords.size(); recordcount++) {
			AssignmentmasterRecord = assignmentrecords
					.getJsonObject(recordcount);
			jsonObjects.add(AssignmentmasterRecord);
			buildAssignmentPreparedStatement(preparedStmt,
					AssignmentmasterRecord);
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
						.println("Inside AssingmentDAO: insertAssingmentRecords size count"
								+ jsonObjects.size());
				jsonObjects.clear();
				outputIndex++;
			}

		}
		int[] countarr = preparedStmt.executeBatch();
		insertcount = insertcount + countarr.length;
		System.out
				.println("Inside AssingmentDAO: insertAssingmentRecords not matching batch size count"
						+ insertcount);

		System.out.println("End AssingmentDAO: insertAssingmentRecords");
		return insertcount;

	}

	/*
	 * int[] countarr=preparedStmt.executeBatch(); insertcount=countarr.length;
	 * ///Need to check how to get the exact successful insert count
	 * conn.commit();
	 */

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
