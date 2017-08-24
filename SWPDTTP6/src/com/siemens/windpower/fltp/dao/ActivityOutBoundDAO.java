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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.siemens.windpower.fltp.beans.AssignmentMasterBean;

public class ActivityOutBoundDAO {
	Logger logger = null;
	Connection conn = null;

	public ActivityOutBoundDAO() {
		logger = Logger.getLogger(UpdateProcessRecordsDAO.class);

	}

	public void insertActivityOutboundDataDAO(
			List<Map<String, Object>> outboundlist) throws NamingException,
			SQLException, IOException, ParseException {
		conn = DAOManagerForDTT.getConnection();
		java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
		// logger.info("Date  " + date);
		// date='2015-';
		String query = "INSERT INTO Activity_Outbound(Target_System,Change_Date,Maintenance_Plan_Call_Number,Maintenance_Plan_Number,Notification_Number,Service_Order_Number,New_Start_Date,Status,Created_Date) VALUES (?,?,?,?,?,?,?,?,?)";
		conn.setAutoCommit(false);
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		for (Map<String, Object> map : outboundlist) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {

				String key = entry.getKey();
				Object value = entry.getValue();
				if (value == null || value == "null") {
					value = "";
				}
				// logger.info("KEY  " +key);
				// logger.info("VALUE  " +value);

				if (key.equalsIgnoreCase("sapparentobjectid")) {
					preparedStmt.setString(1, value.toString());
				}
				if (key.equalsIgnoreCase("changedate")) {
					preparedStmt.setDate(2, getSqldate(value.toString()));
				}

				if (key.equalsIgnoreCase("mpcallnumber")) {
					preparedStmt.setString(3, value.toString());
				}
				if (key.equalsIgnoreCase("maintainanceplanid")) {
					preparedStmt.setString(4, value.toString());
				}
				if (key.equalsIgnoreCase("notificationnumber")) {
					preparedStmt.setString(5, value.toString());
				}
				if (key.equalsIgnoreCase("serviceordernumber")) {
					preparedStmt.setString(6, value.toString());
				}
				if (key.equalsIgnoreCase("startdate")) {
					preparedStmt.setDate(7, getSqldate(value.toString()));
				}

			}
			preparedStmt.setString(8, "N");
			preparedStmt.setDate(9, date);
			preparedStmt.addBatch();

			// preparedStmt.executeQuery();
		}

		 logger.info("InsertSQL" + query);

		preparedStmt.executeBatch();
		conn.commit();

	}

	public void insertActivityOutboundInterDataDAO(
			List<Map<String, Object>> outboundlist) throws NamingException,
			SQLException, IOException, ParseException {
		conn = DAOManagerForDTT.getConnection();
		java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
		// logger.info("Date  " + date);
		String query = "INSERT INTO ACTIVITY_OUTBOUND_INT(Change_Date,Target_System,Maintenance_Plan_Call_Number,Maintenance_Plan_Number,Notification_Number,Service_Order_Number,New_Start_Date,Status,Created_Date) VALUES(?,?,?,?,?,?,?,?,?)";
		conn.setAutoCommit(false);
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		for (Map<String, Object> map : outboundlist) {

			for (Map.Entry<String, Object> entry : map.entrySet()) {

				String key = entry.getKey();
				Object value = entry.getValue();
				if (value == null || value == "null") {
					value = "";
				}
				// logger.info("KEY  " +key);
				// logger.info("VALUE  " +value);

				if (key.equalsIgnoreCase("changedate")) {
					// preparedStmt.setObject(1, "CONVERT(datetime,'"+
					// getSqldate(value.toString()) + "',103),");
					preparedStmt.setDate(1, getSqldate(value.toString()));
					// logger.info("changedate  " +
					// getSqldate(value.toString()));
				}

				if (key.equalsIgnoreCase("sapparentobjectid")) {
					preparedStmt.setString(2, value.toString());
					// logger.info("sapparentobjectid  " + value.toString());
				}

				if (key.equalsIgnoreCase("mpcallnumber")) {
					preparedStmt.setString(3, value.toString());
					// logger.info("mpcallnumber  " + value.toString());
				}
				if (key.equalsIgnoreCase("maintainanceplanid")) {
					preparedStmt.setString(4, value.toString());
					// logger.info("maintainanceplanid  " + value.toString());
				}
				if (key.equalsIgnoreCase("notificationnumber")) {
					preparedStmt.setString(5, value.toString());
					// logger.info("notificationnumber  " + value.toString());
				}
				if (key.equalsIgnoreCase("serviceordernumber")) {
					preparedStmt.setString(6, value.toString());
					// logger.info("serviceordernumber  " + value.toString());
				}
				if (key.equalsIgnoreCase("startdate")) {
					preparedStmt.setDate(7, getSqldate(value.toString()));
					// logger.info("startdate  " +
					// getSqldate(value.toString()));
				}

			}
			preparedStmt.setString(8, "N");
			// logger.info("Date  " + date);
			preparedStmt.setDate(9, date);
			preparedStmt.addBatch();
			// logger.info("SQL  " + preparedStmt.toString());

			// preparedStmt.executeQuery();
		}

		// logger.info("InsertSQL" + query);

		preparedStmt.executeBatch();
		conn.commit();

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

	public List<Map<String, Object>> selectActivityOutboundDataDAO()
			throws NamingException, SQLException, IOException {
		List<Map<String, Object>> activityoutbounddata = null;
		Connection conn = null;

		conn = DAOManagerForDTT.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs;

		List sfdcsitelist = new ArrayList();
		List sppsitelist = new ArrayList();
		List sppcountrylist = new ArrayList();
		List sfdccountrylist = new ArrayList();
		List locationshorelist = new ArrayList();

		rs = stmt
				.executeQuery("select Target_System,Change_Date,Maintenance_Plan_Call_Number,Maintenance_Plan_Number,Notification_Number,Service_Order_Number,New_Start_Date,Status,Created_Date from ACTIVITY_OUTBOUND where status='New'");

		activityoutbounddata = new ArrayList<Map<String, Object>>();
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			Map map = new HashMap();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String key = meta.getColumnName(i);

				String value = rs.getString(key);

				map.put(key, value);

				// logger.info(key + " " + value);
			}
			activityoutbounddata.add(map);
		}

		return activityoutbounddata;

	}

	public int updateActivityOutboundRecords(JCoTable spiridonTableObj)
			throws SQLException, NamingException, IOException

	{
		// logger.info("START: ActivityOutboundDAO: updateActivityOutboundRecords");
		int updatecount = 0;

		// UNCOMMENT THIS CODE
		Connection conn = null;
		PreparedStatement preparedStmtMP = null;
		PreparedStatement preparedStmNotification = null;
		// String query =
		// "UPDATE ACTIVITY_OUTBOUND SET STATUS = ? ,Spiridon_Error_Msg = ? WHERE Maintenance_Plan_Number = ? AND  Maintenance_Plan_Call_Number = ? AND Notification_Number = ?";

		String updateMPQuery = "UPDATE ACTIVITY_OUTBOUND SET STATUS = ? ,Spiridon_Error_Msg = ? WHERE Maintenance_Plan_Number = ? AND  Maintenance_Plan_Call_Number = ?";

		String updateNotificationQuery = "UPDATE ACTIVITY_OUTBOUND SET STATUS = ? ,Spiridon_Error_Msg = ? WHERE Notification_Number = ?";

		conn = DAOManagerForDTT.getConnection();

		preparedStmtMP = conn.prepareStatement(updateMPQuery);
		preparedStmNotification = conn
				.prepareStatement(updateNotificationQuery);
		// logger.info("START: ActivityOutboundDAO: updateActivityOutboundRecords getNumRows"+spiridonTableObj.getNumRows());
		for (int recordcount = 0; recordcount < spiridonTableObj.getNumRows(); recordcount++) {
			spiridonTableObj.setRow(recordcount);

			if (spiridonTableObj.getString("QMNUM") != null
					|| !(spiridonTableObj.getString("QMNUM")
							.equalsIgnoreCase(""))) {

				preparedStmNotification = buildNotificationActivityOutboundPreparedStatement(
						preparedStmNotification, spiridonTableObj);

				/* preparedStmNotification.execute(); */
			}
			if (spiridonTableObj.getString("WARPL") != null
					|| !(spiridonTableObj.getString("WARPL")
							.equalsIgnoreCase(""))) {
				preparedStmtMP = buildMPActivityOutboundPreparedStatement(
						preparedStmtMP, spiridonTableObj);
				/* preparedStmtMP.execute(); */
			}

			preparedStmtMP.addBatch();
			preparedStmNotification.addBatch();

		}

		int[] countarrMP = preparedStmtMP.executeBatch();
		int[] countarrNotification = preparedStmNotification.executeBatch();
		updatecount = countarrMP.length + countarrNotification.length; // /Need
																		// to
																		// check
																		// how
																		// to
																		// get
																		// the
																		// exact
																		// successful
																		// update
																		// count
		conn.commit();

		// logger.info("END: ActivityOutboundDAO: updateActivityOutboundRecords");

		return updatecount;
	}

	private PreparedStatement buildMPActivityOutboundPreparedStatement(
			PreparedStatement preparedStmt, JCoTable spiridonTableObj)
			throws SQLException {

		// UPDATE ACTIVITY_OUTBOUND SET STATUS = ? ,Spiridon_Error_Msg = ? WHERE
		// Maintenance_Plan_Number = ? AND Maintenance_Plan_Call_Number = ?";
		// update the outbound table records
		preparedStmt.setString(1, (String) spiridonTableObj.getString("STATS"));
		preparedStmt.setString(2, (String) spiridonTableObj.getString("ERRLG")); // Error
																					// Message
		preparedStmt.setString(3, (String) spiridonTableObj.getString("WARPL")); // Maintenance
																					// plan
																					// no
		preparedStmt.setString(4, (String) spiridonTableObj.getString("ABNUM")); // //call
																					// number
		return preparedStmt;
	}

	private PreparedStatement buildNotificationActivityOutboundPreparedStatement(
			PreparedStatement preparedStmt, JCoTable spiridonTableObj)
			throws SQLException {

		// update the outbound table records
		// String
		// updateNotificationQuery="UPDATE ACTIVITY_OUTBOUND SET STATUS = ? ,Spiridon_Error_Msg = ? WHERE Notification_Number = ?";
		preparedStmt.setString(1, (String) spiridonTableObj.getString("STATS"));
		preparedStmt.setString(2, (String) spiridonTableObj.getString("ERRLG")); // Error
																					// Message
		preparedStmt.setString(3, (String) spiridonTableObj.getString("QMNUM")); // notification
																					// num
		return preparedStmt;
	}

	public JCoTable selectActivityOutboundDataDAO(JCoTable spiridonTableObj)
			throws NamingException, SQLException, IOException {

		// logger.info("START: ActivityOutboundDAO: selectActivityOutboundDataDAO");
		// UNCOMMENT THIS CODE
		Connection conn = null;
		conn = DAOManagerForDTT.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs;
		rs = stmt
				.executeQuery("select Target_System,Change_Date,Maintenance_Plan_Call_Number,Maintenance_Plan_Number,Notification_Number,Service_Order_Number,New_Start_Date,Status,Created_Date from ACTIVITY_OUTBOUND where status in ('N')");
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {

			spiridonTableObj.appendRow();
			// POPULATE THE Following values with the ActivityOutbound objects
			// value.
			spiridonTableObj.setValue("SYSTEM", "SPQ310"); // SYSTEM  DTT can
															// use to describe
															// the Spiridon
															// system (SPQ =
															// SPQ310 and SPP =
															// SPP310)
			spiridonTableObj.setValue("CHGDA", ""); // CHGDA  Date on which the
													// record is being changed
													// in DTT. Expected date
													// format to be YYYYMMDD.
			spiridonTableObj.setValue("WARPL",
					rs.getString("Maintenance_Plan_Number")); // Maintenance
																// Plan number
			spiridonTableObj.setValue("ABNUM",
					rs.getString("Maintenance_Plan_Call_Number")); // Maintenance
																	// Plan call
																	// number
			spiridonTableObj.setValue("QMNUM",
					rs.getString("Notification_Number")); // Notification number
			spiridonTableObj.setValue("EQUNR", ""); // FUTURE DEVELOPMENT
			spiridonTableObj.setValue("NEWDT",
					getNewStartDateString(rs.getString("New_Start_Date"))); // New
																			// start
																			// date
			// spiridonTableObj.setValue("STATS",
			// rs.getString("Status"));//STATS  Return status from Spiridon (E
			// = Error , S = Success). This can be given full description
			// instead of just 1 character. Please confirm the requirement from
			// your end.
			spiridonTableObj.setValue("STATS", "");// STATS  Return status from
													// Spiridon (E = Error , S =
													// Success). This can be
													// given full description
													// instead of just 1
													// character. Please confirm
													// the requirement from your
													// end.
			spiridonTableObj.setValue("LOGDT", "");// LOGDT  Date on which the
													// record is processed in
													// Spiridon."Change date"
													// column of DTT
			spiridonTableObj.setValue("LOGTM", "");// LOGTM  Time at which the
													// record is processed in
													// Spiridon."Time Stamp of"
													// column of DTT
			spiridonTableObj.setValue("ERRLG", "");// ERRLG  The details of the
													// success / error log.

		}

		/*
		 * //DUMMY RECORDS for (int i=0;i<5;i++) {
		 * 
		 * spiridonTableObj.appendRow(); //POPULATE THE Following values with
		 * the ActivityOutbound objects value.
		 * spiridonTableObj.setValue("SYSTEM", "SPQ310"); //SYSTEM  DTT can use
		 * to describe the Spiridon system (SPQ = SPQ310 and SPP = SPP310)
		 * spiridonTableObj.setValue("CHGDA", ""); //CHGDA  Date on which the
		 * record is being changed in DTT. Expected date format to be YYYYMMDD.
		 * spiridonTableObj.setValue("WARPL", "215073"); // Maintenance Plan
		 * number spiridonTableObj.setValue("ABNUM", 33+i); // Maintenance Plan
		 * call number spiridonTableObj.setValue("QMNUM", ""); // Notification
		 * number spiridonTableObj.setValue("EQUNR", ""); // FUTURE DEVELOPMENT
		 * spiridonTableObj.setValue("NEWDT", "20171125"); // New start date
		 * spiridonTableObj.setValue("STATS", "");//STATS  Return status from
		 * Spiridon (E = Error , S = Success). This can be given full
		 * description instead of just 1 character. Please confirm the
		 * requirement from your end. spiridonTableObj.setValue("LOGDT",
		 * "");//LOGDT  Date on which the record is processed in
		 * Spiridon."Change date" column of DTT
		 * spiridonTableObj.setValue("LOGTM", "" );//LOGTM  Time at which the
		 * record is processed in Spiridon."Time Stamp of" column of DTT
		 * spiridonTableObj.setValue("ERRLG", "");//ERRLG  The details of the
		 * success / error log.
		 * 
		 * }
		 */

		// logger.info("END: ActivityOutboundDAO: selectActivityOutboundDataDAO");

		return spiridonTableObj;

	}

	private String getNewStartDateString(String datestr) {
		/* String datestr="2015-04-22"; */

		int firsttoken = datestr.indexOf("-");

		int secondtoken = datestr.lastIndexOf("-");

		String yearstr = datestr.substring(0, firsttoken);

		String monthstr = datestr.substring(firsttoken + 1, secondtoken);

		String daystr = datestr.substring(secondtoken + 1);

		String responseString = yearstr.concat(monthstr).concat(daystr);

		// logger.info("year string..."+yearstr);

		// logger.info("monthstr string..."+monthstr);

		// logger.info("daystr string..."+daystr);

		// logger.info("responseString string..."+responseString);
		return responseString;

	}

}
