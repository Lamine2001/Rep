package com.siemens.windpower.fltp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateLastReadDAO {
	public void updateLastRead(String tablename) throws Exception {
		Connection conn = null;
		try {
			conn = DAOManagerForDTT.getConnection();

			java.sql.Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());
			System.out.println("Date  " + date);

			String query = "update LastRead set LastReadDate=? where Table_Name= ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setTimestamp(1, date);
			preparedStmt.setString(2, tablename);

			preparedStmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

		}

	}
	
	public String selectLastReadDate() throws Exception {

		String date = null;
		Connection conn = null;
		try {

			conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("select LastReaddate from LastRead   where  Table_Name='ProjectMaster'");

			while (rs.next()) {
				date = rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// conn.close();
		}
		
		

		return date;
	}

	public static void main(String args[]) throws Exception {
		UpdateLastReadDAO updatedao = new UpdateLastReadDAO();
		updatedao.updateLastRead("Activity");
	}
}