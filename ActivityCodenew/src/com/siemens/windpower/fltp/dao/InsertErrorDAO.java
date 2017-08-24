package com.siemens.windpower.fltp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

public class InsertErrorDAO {

	public void insertError(String tablename, String message,
			List<String> errorlist) {
		try {
			String statusflag = "E";
			Connection conn = DAOManagerForDTT.getConnection();

			java.sql.Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());
			System.out.println("Date  " + date);

			String query = "INSERT INTO ERROR(Created_Date,Source_Table,Error_Message,Status_Flag,Source_Unique_ID) VALUES (?,?,?,?,?)";

			PreparedStatement preparedStmt = conn.prepareStatement(query);

			System.out.println("InsertSQL" + query);
			for (String uniqueid : errorlist) {

				System.out.println(" " + " " + " " + uniqueid + " ");

				preparedStmt.setTimestamp(1, date);
				preparedStmt.setString(2, tablename);

				preparedStmt.setString(3, message + " " + uniqueid);
				preparedStmt.setString(4, statusflag);

				preparedStmt.setString(5, uniqueid);
				preparedStmt.addBatch();
			}
			preparedStmt.executeBatch();
			conn.commit();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insertSanityErrorRecords(String tablename, Map map)
			throws SQLException, NamingException, IOException {

		String statusflag = "NV";
		Integer rowid = 0;
		String errormessage = null;
		Connection conn = DAOManagerForDTT.getConnection();

		java.sql.Timestamp date = new java.sql.Timestamp(
				new java.util.Date().getTime());
		System.out.println("Date  " + date);

		String query = "INSERT INTO ERROR(Created_Date,Source_Table,Error_Message,Status_Flag,Source_Unique_ID) VALUES (?,?,?,?,?)";

		PreparedStatement preparedStmt = conn.prepareStatement(query);

		System.out.println("InsertSQL" + query);
		System.out.println("Map Size" + map.size());
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = entry.getKey().toString();

			System.out.println("Key = " + key);
			if (key.equals("Row_id")) {
				rowid = (Integer) entry.getValue();
			}
			if (key.equals("ErrorMessage")) {
				errormessage = entry.getValue().toString();
			}

			if (rowid != 0 && errormessage != null) {
				preparedStmt.setTimestamp(1, date);
				preparedStmt.setString(2, tablename);

				preparedStmt.setString(3, errormessage);
				preparedStmt.setString(4, statusflag);
				/* System.out.println("InsertSQL" + query); */
				preparedStmt.setInt(5, rowid);
				preparedStmt.addBatch();
			}
		}
		preparedStmt.executeBatch();
		conn.commit();

	}

}
