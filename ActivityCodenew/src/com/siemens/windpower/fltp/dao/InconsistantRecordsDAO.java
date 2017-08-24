package com.siemens.windpower.fltp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class InconsistantRecordsDAO {

	Logger logger = Logger.getLogger(InconsistantRecordsDAO.class);

	public void inconsistantRecords() throws Exception {
		try {
			String statusflag = DTTConstants.STATUSFLAG;
			String uniqueid = null;
			String errormessage = null;
			String tablename = null;
			java.sql.Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());
			System.out.println("Date  " + date);

			Connection conn = DAOManagerForDTT.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;

			String selectsql = "select pms.SAP_Parent_Object_ID,act.SAP_Parent_Object_ID as ACT_SAP_Parent_Object_ID from Activity act ,ProjectMaster pms,LastRead lr where pms.SAP_Parent_Object_ID!=act.SAP_Parent_Object_ID and lr.Table_Name='ProjectMaster' and  pms.Created_Date>lr.LastReadDate and  pms.Source_System like 'RBP%'";
			rs = stmt.executeQuery(selectsql);
			while (rs.next()) {
				uniqueid = rs.getString(DTTConstants.SAP_Parent_Object_ID);
				errormessage = "SAP patent object id does not exists in Activity Table";
				tablename = "ProjectMaster";
				if (uniqueid == null) {
					uniqueid = rs
							.getString(DTTConstants.ACT_SAP_Parent_Object_ID);
					errormessage = "SAP patent object id does not exists in Project Master Table";
					tablename = "Activity";
				}

				String query = "INSERT INTO ERROR(Created_Date,Source_Table,Source_Unique_ID,Error_Message,Status_Flag)"
						+ "VALUES (?,?,?,?,?)";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setTimestamp(1, date);
				preparedStmt.setString(2, tablename);
				preparedStmt.setString(3, uniqueid);
				preparedStmt.setString(4, errormessage);
				preparedStmt.setString(5, statusflag);

				preparedStmt.executeUpdate();

			}
		} catch (SQLException sqle) {
			logger.error(DTTErrorConstants.ERR009 + sqle);
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
