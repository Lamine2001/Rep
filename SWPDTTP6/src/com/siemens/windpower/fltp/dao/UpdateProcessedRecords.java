package com.siemens.windpower.fltp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.naming.NamingException;

import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.common.DTTErrorConstants;

public class UpdateProcessedRecords {
	public void wbsUpdateAfterProcess() throws SQLException, NamingException,
			IOException {

		PreparedStatement preparedStmt = null;
		String query = null;
		int rowid = 0;
		String errormessage = null;
		Connection conn = DAOManagerForDTT.getConnection();
		query = "update Activity set Status_Flag=''";

		preparedStmt = conn.prepareStatement(query);
		System.out.println("query in update  " + query);
		java.sql.Timestamp date = new java.sql.Timestamp(
				new java.util.Date().getTime());
		System.out.println("Date in update  " + date);

		System.out.println("preparedStmt maxrows" + preparedStmt.getMaxRows());

		preparedStmt.execute();

	}

	public static void wbsUpdateProcess(List processedrecords)
			throws SQLException, NamingException, IOException {
		Connection conn = DAOManagerForDTT.getConnection();

		if (processedrecords.size() != 0) {

			String sql = "UPDATE Activity SET Status_Flag='Y' where Row_Id =?";

			conn.setAutoCommit(false);

			PreparedStatement ps = null;

			ps = conn.prepareStatement(sql);
			ListIterator rowread = processedrecords.listIterator();
			/* //logger.info("HQtechid"+hqtechid); */
			while (rowread.hasNext()) {
				Object readelement = rowread.next();
				ps.setInt(1, Integer.parseInt(readelement.toString()));
				ps.addBatch();
			}

			ps.executeBatch();

			conn.commit();
		}
	}

	public void updateprocessed(String tablename, Map map) throws SQLException,
			NamingException, IOException {

		PreparedStatement preparedStmt = null;
		String query = null;
		int rowid = 0;
		String errormessage = null;
		Connection conn = DAOManagerForDTT.getConnection();
		if (tablename == "Project_Master") {
			query = "update ProjectMaster set Status_Flag='NV' where Row_id=?";
		}
		if (tablename == "Activity") {
			query = "update Activity set Status_Flag='NV' where Row_id=?";
		}
		if (tablename == "Assignment") {
			query = "update Assignment set Status_Flag='NV' where Row_id=?";
		}
		preparedStmt = conn.prepareStatement(query);
		System.out.println("query in update  " + query);
		java.sql.Timestamp date = new java.sql.Timestamp(
				new java.util.Date().getTime());
		System.out.println("Date in update  " + date);
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = entry.getKey().toString();

			System.out.println("Key = " + key);
			if (key.equals("Row_id")) {
				rowid = (Integer) entry.getValue();
			}

			preparedStmt.setInt(1, rowid);
			preparedStmt.addBatch();
		}
		System.out.println("preparedStmt maxrows" + preparedStmt.getMaxRows());
		if (preparedStmt.getMaxRows() > 0) {
			preparedStmt.executeBatch();
		}

	}

}
