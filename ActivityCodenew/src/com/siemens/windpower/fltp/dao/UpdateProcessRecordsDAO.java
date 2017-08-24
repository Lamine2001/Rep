package com.siemens.windpower.fltp.dao;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class UpdateProcessRecordsDAO {
	Logger logger = null;

	public UpdateProcessRecordsDAO() {
		logger = Logger.getLogger(UpdateProcessRecordsDAO.class);
	}

	public void updaterecords(String tablename, String flag,
			List<String> processedrecords, String message, String columnname)
			throws SQLException, NamingException, IOException {
		Connection conn = null;
		try {
			conn = DAOManagerForDTT.getConnection();
			// logger.info(DTTErrorConstants.INF011);
			Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());
			System.out.println("Date  " + date);

			if (tablename.equalsIgnoreCase("ProjectMaster")) {
				if (flag.equalsIgnoreCase("Y")) {
					// logger.info(columnname);
					if (processedrecords.size() != 0) {
						// logger.info(DTTErrorConstants.INF012);
						List<Integer> rowidlist = getProjectRow(tablename,
								processedrecords, columnname);
						String qin = generateQsForIn(rowidlist.size());
						String sql = "UPDATE ProjectMaster SET Status_Flag=? where Row_Id IN("
								+ qin + ")";
						PreparedStatement ps = null;
						int j = 2;
						ps = conn.prepareStatement(sql);

						ps.setObject(1, flag);
						Integer[] tempArr = new Integer[rowidlist.size()];
						tempArr = rowidlist.toArray(tempArr);
						for (int i = 0; i < tempArr.length; i++) {

							ps.setObject(j + i, tempArr[i]);

							/*
							 * System.out.println(j + i + " " + " " + " " +
							 * tempArr[i] + " ");
							 */
						}

						int result = ps.executeUpdate();

						if (result == PreparedStatement.EXECUTE_FAILED) {
							throw new SQLException(
									String.format(
											"Entry %d failed to execute in the batch insert with a return code of %d.",
											result));
						}
					}
					conn.commit();
				}
				if (flag.equalsIgnoreCase("E")) {
					/*
					 * //logger.info(DTTErrorConstants.INF013 +
					 * processedrecords.size());
					 */
					if (processedrecords.size() != 0) {
						List<Integer> rowidlist = getProjectRow(tablename,
								processedrecords, columnname);
						String qin = generateQsForIn(rowidlist.size());
						String sql = "UPDATE ProjectMaster SET Status_Flag=?,Error_Date=?,Error_Message=? where Row_Id IN("
								+ qin + ")";
						conn.setAutoCommit(false);

						PreparedStatement ps = null;

						ps = conn.prepareStatement(sql);
						int j = 4;
						// System.out.println("SQL" + sql);
						// logger.info(DTTErrorConstants.INF014 + sql);
						ps.setObject(1, flag);
						ps.setTimestamp(2, date);
						ps.setObject(3, message);
						Integer[] tempArr = new Integer[rowidlist.size()];
						tempArr = rowidlist.toArray(tempArr);
						for (int i = 0; i < tempArr.length; i++) {

							ps.setObject(j + i, tempArr[i]);

							/*
							 * System.out.println(j + i + " " + " " + " " +
							 * tempArr[i] + " ");
							 */
						}

						int result = ps.executeUpdate();

						if (result == PreparedStatement.EXECUTE_FAILED) {
							throw new SQLException(
									String.format(
											"Entry %d failed to execute in the batch insert with a return code of %d.",
											result));
						}

						conn.commit();

						String query = "INSERT INTO ERROR(Created_Date,Source_Table,Error_Message,Status_Flag,Source_Unique_ID) VALUES (?,?,?,?,?)";
						conn.setAutoCommit(false);
						PreparedStatement preparedStmt = conn
								.prepareStatement(query);

						// //logger.info(DTTErrorConstants.INF015 + query);
						for (String uniqueid : processedrecords) {

							// logger.info(" " + " " + " " + uniqueid + " ");
							// logger.info(" " + " " + " " + flag + " ");
							// logger.info(" " + " " + " " + tablename + " ");

							preparedStmt.setTimestamp(1, date);
							preparedStmt.setString(2, tablename);

							preparedStmt.setString(3, message + " " + uniqueid);
							preparedStmt.setString(4, flag);

							preparedStmt.setString(5, uniqueid);
							preparedStmt.addBatch();
						}
						preparedStmt.executeBatch();
						conn.commit();
						// logger.info(DTTErrorConstants.INF016);

					}
				}

			}
			if (tablename.contains("Activity")) {
				if (processedrecords.size() != 0) {
					if (flag.equalsIgnoreCase("Y")) {
						// logger.info(columnname);
						List<Integer> rowidlist = getActivityRow(tablename,
								processedrecords, columnname);
						String qin = generateQsForIn(rowidlist.size());
						logger.info("rowidlist.size "+rowidlist.size());
						String sql = "UPDATE Activity SET Status_Flag=? where Row_Id IN("
								+ qin + ")";
						PreparedStatement ps = null;
						int j = 2;
						ps = conn.prepareStatement(sql);
						System.out.println("SQL" + sql);
						ps.setObject(1, flag);
						Integer[] tempArr = new Integer[rowidlist.size()];
						tempArr = rowidlist.toArray(tempArr);
						for (int i = 0; i < tempArr.length; i++) {

							ps.setObject(j + i, tempArr[i]);

							/*
							 * System.out.println(j + i + " " + " " + " " +
							 * tempArr[i] + " ");
							 */
						}

						int result = ps.executeUpdate();
						
						logger.info("RESULT "+result);

						if (result == PreparedStatement.EXECUTE_FAILED) {
							throw new SQLException(
									String.format(
											"Entry %d failed to execute in the batch insert with a return code of %d.",
											result));
						}
					}
					conn.commit();
				}
				if (flag.equalsIgnoreCase("E")) {
					if (processedrecords.size() != 0) {
						List<Integer> rowidlist = getActivityRow(tablename,
								processedrecords, columnname);
						String qin = generateQsForIn(rowidlist.size());
						String sql = "UPDATE Activity SET Status_Flag=?,Error_Date=?,Error_Message=? where Row_Id IN("
								+ qin + ")";
						conn.setAutoCommit(false);

						PreparedStatement ps = null;
						int j = 4;
						ps = conn.prepareStatement(sql);
						System.out.println("SQL" + sql);
						// logger.info("SQL" + sql);
						ps.setObject(1, flag);
						ps.setTimestamp(2, date);
						ps.setObject(3, message);
						Integer[] tempArr = new Integer[rowidlist.size()];
						tempArr = rowidlist.toArray(tempArr);
						for (int i = 0; i < tempArr.length; i++) {

							ps.setObject(j + i, tempArr[i]);
							// logger.info(tempArr[i]);
							/*
							 * System.out.println(j + i + " " + " " + " " +
							 * tempArr[i] + " ");
							 */
						}

						int result = ps.executeUpdate();

						if (result == PreparedStatement.EXECUTE_FAILED) {
							throw new SQLException(
									String.format(
											"Entry %d failed to execute in the batch insert with a return code of %d.",
											result));
						}

						conn.commit();

						String query = "INSERT INTO ERROR(Created_Date,Source_Table,Error_Message,Status_Flag,Source_Unique_ID) VALUES (?,?,?,?,?)";

						PreparedStatement preparedStmt = conn
								.prepareStatement(query);

						// System.out.println("InsertSQL" + query);
						for (String uniqueid : processedrecords) {

							/*
							 * System.out .println(" " + " " + " " + uniqueid +
							 * " ");
							 */

							preparedStmt.setTimestamp(1, date);
							preparedStmt.setString(2, tablename);

							preparedStmt.setString(3, message + " " + uniqueid);
							preparedStmt.setString(4, flag);

							preparedStmt.setString(5, uniqueid);
							preparedStmt.addBatch();
						}
						preparedStmt.executeBatch();
						conn.commit();

					}
				}
			}
			if (tablename.contains("Assignment")) {
				if (flag.equalsIgnoreCase("Y")) {
					if (processedrecords.size() != 0) {
						List<Integer> rowidlist = getAssignmentRow(tablename,
								processedrecords, columnname);
						String qin = generateQsForIn(rowidlist.size());
						String sql = "UPDATE Assignment SET Status_Flag=? where Row_Id IN("
								+ qin + ")";
						PreparedStatement ps = null;
						int j = 2;
						ps = conn.prepareStatement(sql);
						System.out.println("SQL" + sql);
						ps.setObject(1, flag);
						Integer[] tempArr = new Integer[rowidlist.size()];
						tempArr = rowidlist.toArray(tempArr);
						for (int i = 0; i < tempArr.length; i++) {

							ps.setObject(j + i, tempArr[i]);

							System.out.println(j + i + " " + " " + " "
									+ tempArr[i] + " ");
						}

						int result = ps.executeUpdate();

						if (result == PreparedStatement.EXECUTE_FAILED) {
							throw new SQLException(
									String.format(
											"Entry %d failed to execute in the batch insert with a return code of %d.",
											result));
						}

						conn.commit();
					}
				}
				if (flag.equalsIgnoreCase("E")) {
					if (processedrecords.size() != 0) {
						List<Integer> rowidlist = getAssignmentRow(tablename,
								processedrecords, columnname);
						String qin = generateQsForIn(rowidlist.size());
						String sql = "UPDATE Assignment SET Status_Flag=?,Error_Date=?,Error_Message=? where Row_Id IN("
								+ qin + ")";
						conn.setAutoCommit(false);

						PreparedStatement ps = null;
						int j = 4;
						ps = conn.prepareStatement(sql);
						System.out.println("SQL" + sql);
						ps.setObject(1, flag);
						ps.setTimestamp(2, date);
						ps.setObject(3, message);
						Integer[] tempArr = new Integer[rowidlist.size()];
						tempArr = rowidlist.toArray(tempArr);
						for (int i = 0; i < tempArr.length; i++) {

							ps.setObject(j + i, tempArr[i]);

							System.out.println(j + i + " " + " " + " "
									+ tempArr[i] + " ");
						}

						int result = ps.executeUpdate();

						if (result == PreparedStatement.EXECUTE_FAILED) {
							throw new SQLException(
									String.format(
											"Entry %d failed to execute in the batch insert with a return code of %d.",
											result));
						}

						conn.commit();

						String query = "INSERT INTO ERROR(Created_Date,Source_Table,Error_Message,Status_Flag,Source_Unique_ID) VALUES (?,?,?,?,?)";

						PreparedStatement preparedStmt = conn
								.prepareStatement(query);

						System.out.println("InsertSQL" + query);
						for (String uniqueid : processedrecords) {

							System.out
									.println(" " + " " + " " + uniqueid + " ");

							preparedStmt.setTimestamp(1, date);
							preparedStmt.setString(2, tablename);

							preparedStmt.setString(3, message + " " + uniqueid);
							preparedStmt.setString(4, flag);

							preparedStmt.setString(5, uniqueid);
							preparedStmt.addBatch();
						}
						preparedStmt.executeBatch();
						conn.commit();

					}

				}
			}
		} catch (SQLException e) {
			logger.error(DTTErrorConstants.ERR011 + e);
			conn.rollback();
			throw new RuntimeException(e);
		} finally {
			// conn.close();
		}

	}

	public List getProjectRow(String tablename, List<String> processedrecords,
			String columnname) throws SQLException, NamingException,
			IOException {
		Connection conn = null;
		ResultSet rsset;
		List rowidlist = new ArrayList();
		try {
			conn = DAOManagerForDTT.getConnection();
			String sql = null;

			int rowid;

			String qin = generateQsForIn(processedrecords.size());
			if (columnname.contains("Site_Id")) {
				sql = "SELECT Row_Id FROM ProjectMaster WHERE Site_Id IN("
						+ qin + ")";
			}
			if (columnname.contains("Project_Number")) {
				sql = "SELECT Row_Id FROM ProjectMaster WHERE Project_Number IN("
						+ qin + ")";
			}

			PreparedStatement ps = conn.prepareStatement(sql);
			int j = 1;
			// logger.info(columnname);
			// logger.info(sql);
			// ps.setString(1, tablename);
			// ps.setObject(1, columnname);
			String[] tempArr = new String[processedrecords.size()];
			tempArr = processedrecords.toArray(tempArr);

			for (int i = 0; i < tempArr.length; i++) {

				ps.setObject(j + i, tempArr[i]);
				// logger.info(DTTErrorConstants.INF015 + columnname + " " +
				// tempArr[i]);
				// System.out.println(j + i + " " + " " + " " + tempArr[i] +
				// " ");
			}

			rsset = ps.executeQuery();

			while (rsset.next()) {
				rowid = rsset.getInt("Row_Id");
				rowidlist.add(rowid);
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		} finally {
			// conn.close();
		}
		// //logger.info("row size"+rowidlist.size());
		return rowidlist;
	}

	public List getActivityRow(String tablename, List<String> processedrecords,
			String columnname) throws SQLException, NamingException,
			IOException {
		Connection conn = null;
		ResultSet rsset;
		List rowidlist = new ArrayList();
		try {
			conn = DAOManagerForDTT.getConnection();

			int rowid;

			String qin = generateQsForIn(processedrecords.size());

			String sql = "SELECT Row_Id FROM Activity WHERE DTT_Activity_Object_ID IN("
					+ qin + ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			int j = 1;
			// ps.setString(1, tablename);
			// ps.setObject(1, columnname);

			// logger.info(sql);
			String[] tempArr = new String[processedrecords.size()];
			tempArr = processedrecords.toArray(tempArr);

			for (int i = 0; i < tempArr.length; i++) {

				ps.setObject(j + i, tempArr[i]);
				 logger.info(tempArr[i]);
				// System.out.println(j + i + " " + " " + " " + tempArr[i] +
				// " ");
			}

			rsset = ps.executeQuery();

			while (rsset.next()) {
				rowid = rsset.getInt("Row_Id");
				rowidlist.add(rowid);
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		} finally {
			// conn.close();
		}
		return rowidlist;
	}

	public List getAssignmentRow(String tablename,
			List<String> processedrecords, String columnname)
			throws SQLException, NamingException, IOException {
		Connection conn = null;
		ResultSet rsset;
		List rowidlist = new ArrayList();
		try {
			conn = DAOManagerForDTT.getConnection();

			int rowid;

			String qin = generateQsForIn(processedrecords.size());
			String sql = "SELECT Row_Id FROM Assignment WHERE Row_Id IN(" + qin
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			int j = 1;
			// ps.setString(1, tablename);
			// ps.setObject(1, columnname);
			String[] tempArr = new String[processedrecords.size()];
			tempArr = processedrecords.toArray(tempArr);

			for (int i = 0; i < tempArr.length; i++) {

				ps.setObject(j + i, tempArr[i]);

				// System.out.println(j + i + " " + " " + " " + tempArr[i] +
				// " ");
			}

			rsset = ps.executeQuery();

			while (rsset.next()) {
				rowid = rsset.getInt("Row_Id");
				rowidlist.add(rowid);
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		} finally {
			// conn.close();
		}
		return rowidlist;
	}

	private String generateQsForIn(int numQs) {
		String items = "";
		for (int i = 0; i < numQs; i++) {
			if (i != 0)
				items += ",";
			items += "?";
		}
		return items;
	}
}
