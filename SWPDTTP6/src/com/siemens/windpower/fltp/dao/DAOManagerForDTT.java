package com.siemens.windpower.fltp.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class DAOManagerForDTT {

	static Logger logger = null;
	private static Connection connection = null;

	public DAOManagerForDTT() {

	}

	public static Connection getConnection() throws NamingException,
			SQLException, IOException {

		logger = Logger.getLogger(DAOManagerForDTT.class);
		Connection result = null;
		// //logger.info("Getting Connection from Connection Pool of DTT");

		try {
			if (connection != null) {
				// logger.info(DTTErrorConstants.INF005);
				return connection;

			}
			ReadProperties read = new ReadProperties();
			Map prop = null;
			prop = read.readPropertiesFile();
			String JNDI = prop.get(DTTConstants.JNDINAME).toString();
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup(JNDI);
			result = dataSource.getConnection();
			// result =
			// DriverManager.getConnection("jdbc:sqlserver://DKBDKB7TST014A;databaseName=FLTPDTTDB",
			// "fltp_app_user", "1.fltpapp");
			// logger.info(DTTErrorConstants.INF006);
		} catch (SQLException e) {
			logger.error(DTTErrorConstants.ERR008, e);
			e.printStackTrace();
			throw e;
		}
		connection = result;
		return result;
	}

	private static void log(Object aObject) {
		System.out.println(aObject);
	}

	public static void closeDBResources() throws Exception {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}

	}

	public static void main(String args[]) throws IllegalAccessException,
			ClassNotFoundException, Exception {
		DAOManagerForDTT ConnectionPoolManager = new DAOManagerForDTT();
		ConnectionPoolManager.getConnection();

	}

}
