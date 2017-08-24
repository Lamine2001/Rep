package com.siemens.windpower.fltp.saprfcclient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.fltp.dao.ActivityOutBoundDAO;

public class SpiridonRFCConnector {
	public Logger logger = null;

	private static SpridonDestinationDataProvider provider = null;

	public void pushActivityOutboundRecordsToSpiridon() throws JCoException,
			SQLException, NamingException, IOException {
		System.out
				.println("SAPRFCConnector : START:pushActivityOutboundRecordsToSpiridon");
		initializeSpriridonConnection();
		getSAPRFCConnectionUsingPool();
		loadDataToSpiridon();
		releaseSAPRFCResources();
		System.out
				.println("SAPRFCConnector : END:pushActivityOutboundRecordsToSpiridon");
	}

	private static void releaseSAPRFCResources() {

		System.out.println("SAPRFCConnector : START:releaseSAPRFCResources:"
				+ provider);
		if (com.sap.conn.jco.ext.Environment
				.isDestinationDataProviderRegistered()) {
			// com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(provider);
		}
		System.out.println("SAPRFCConnector : END:releaseSAPRFCResources:"
				+ provider);
	}

	private static void initializeSpriridonConnection() throws IOException {
		System.out
				.println("SAPRFCConnector : START:initializeSpriridonConnection");
		// FLTP.getSpiridon properties object assign to jcoProperties variable
		Properties jcoProperties = new Properties();
		jcoProperties = getSpiridonConnectionProperties(jcoProperties);
		SpridonDestinationDataProvider provider = new SpridonDestinationDataProvider();
		// provider.addDestination( ABAP_AS, jcoProperties );
		provider.addDestination(DTTConstants.ABAP_AS_POOLED, jcoProperties);
		if (!com.sap.conn.jco.ext.Environment
				.isDestinationDataProviderRegistered()) {
			com.sap.conn.jco.ext.Environment
					.registerDestinationDataProvider(provider);
		}
		// The destination name must match the name you use for looking up a
		// destination.
		// JCoDestination destination = JCoDestinationManager.getDestination(
		// "A01" );
		System.out
				.println("SAPRFCConnector : END:initializeSpriridonConnection");

	}

	private static void loadDataToSpiridon() throws JCoException, SQLException,
			NamingException, IOException {
		System.out.println("SAPRFCConnector : START:loadDataToSpiridon");
		getSAPRFCConnectionUsingPool();
		invokeSpriridonRFCProgram();
		System.out.println("SAPRFCConnector : END:loadDataToSpiridon");

	}

	private static void getSAPRFCConnectionUsingPool() throws JCoException {
		System.out
				.println("SAPRFCConnector : START:getSAPRFCConnectionUsingPool");
		JCoDestination destination = JCoDestinationManager
				.getDestination(DTTConstants.ABAP_AS_POOLED);
		System.out.println("PING START");
		destination.ping(); // UNCOMMENT THIS WHILE TESTING WITH RFC
		System.out.println("PING END,CONNECTION SUCCESSFUL");
		System.out
				.println("SAPRFCConnector : END:getSAPRFCConnectionUsingPool");
	}

	private static JCoTable getActivityOutboundTableObj(
			JCoTable jcoActivityOutboundTable) throws NamingException,
			SQLException, IOException {

		// Get the Records from Activity Outbound table
		ActivityOutBoundDAO activityOutboundDAO = new ActivityOutBoundDAO();
		jcoActivityOutboundTable = activityOutboundDAO
				.selectActivityOutboundDataDAO(jcoActivityOutboundTable);
		System.out
				.println("SAPRFCConnector : getActivityOutboundTableObj of Activity Outbound DTT Table is: "
						+ jcoActivityOutboundTable.getNumRows());
		return jcoActivityOutboundTable;
	}

	public static JCoTable buildRFCInput(JCoDestination destination,
			JCoFunction function, JCoTable jcoActivityOutboundTable)
			throws NamingException, SQLException, IOException {

		// Get the Records from Activity Outbound table
		// jcoActivityOutboundTable.appendRows(dttOutboundRecordCount);
		jcoActivityOutboundTable = getActivityOutboundTableObj(jcoActivityOutboundTable);
		return jcoActivityOutboundTable;

	}

	public static JCoTable executeRFC(JCoDestination destination,
			JCoFunction function, JCoTable jcoActivityOutboundTable)
			throws JCoException {
		System.out.println("STARTED: executeRFC" + function);
		System.out.println("Function::" + function);
		System.out.println("INPUT TABLE"
				+ function.getTableParameterList().getTable(
						DTTConstants.RFC_TABLE_NAME));
		function.execute(destination);
		System.out.println("OUTPUT TABLE"
				+ function.getTableParameterList().getTable(
						DTTConstants.RFC_TABLE_NAME));
		System.out.println("END:executeRFC Function invoke with table:");
		JCoTable spiridonTableObj = function.getTableParameterList().getTable(
				DTTConstants.RFC_TABLE_NAME);
		return spiridonTableObj;
	}

	private static void printRFCOutput(JCoTable spiridonTableObj) {

		for (int i = 0; i < spiridonTableObj.getNumRows(); i++) {
			spiridonTableObj.setRow(i);
			System.out.println("PLAN#" + spiridonTableObj.getValue("WARPL"));
			System.out.println("PLAN Call Number"
					+ spiridonTableObj.getValue("ABNUM"));
			System.out.println("Notofication Number"
					+ spiridonTableObj.getValue("QMNUM"));
			System.out.println("STATUS" + spiridonTableObj.getValue("STATS"));
			System.out.println("ERROR" + spiridonTableObj.getValue("ERRLG"));
			System.out.println("ERROR DATE"
					+ spiridonTableObj.getValue("LOGDT"));
			System.out.println("ERROR TIME"
					+ spiridonTableObj.getValue("LOGTM"));
		}
	}

	private static void invokeSpriridonRFCProgram() throws JCoException,
			SQLException, NamingException, IOException {
		System.out.println("SAPRFCConnector : START:invokeSpriridonRFCProgram");

		// Get RFC Function handle
		JCoDestination destination = null;
		JCoFunction function = null;
		destination = JCoDestinationManager
				.getDestination(DTTConstants.ABAP_AS_POOLED);
		function = destination.getRepository().getFunction(
				DTTConstants.RFC_FUNCTION_NAME);
		if (function == null)
			throw new RuntimeException(DTTErrorConstants.RFC_NOT_FOUND);
		System.out.println("Function Name Before sapRFCInput..." + function);

		// BUILD THE RFC INPUT
		JCoTable jcoActivityOutboundTable = function.getTableParameterList()
				.getTable(DTTConstants.RFC_TABLE_NAME);
		jcoActivityOutboundTable = buildRFCInput(destination, function,
				jcoActivityOutboundTable);
		if (jcoActivityOutboundTable.getNumRows() > 0) {
			// Set the RFC Input to the Function
			function.getTableParameterList().setValue(
					DTTConstants.RFC_TABLE_NAME, jcoActivityOutboundTable);

			// Execute RFC
			jcoActivityOutboundTable = executeRFC(destination, function,
					jcoActivityOutboundTable);

			// Print RFC Output
			printRFCOutput(jcoActivityOutboundTable);

			// UPDATE THE RESPONSE TO THE ACTIVITY_OUTBOUND DTT TABLE
			int updatedRecordcount = updateActivityOutboundRecords(jcoActivityOutboundTable);
			System.out
					.println("SAPRFCConnector : Updatedrecordcount of Activity Outbound DTT Table is: "
							+ updatedRecordcount);
		}

		else {
			System.out
					.println("There are No Records Available To Send To Spiridon System");
		}

	}

	private static int updateActivityOutboundRecords(JCoTable table)
			throws SQLException, NamingException, IOException {
		System.out.println("SAPRFCConnector : updateActivityOutboundRecords");
		// UPDATE THE RESPONSE TO THE ACTIVITY_OUTBOUND DTT TABLE
		ActivityOutBoundDAO activityOutboundDAO = new ActivityOutBoundDAO();
		System.out.println("SAPRFCConnector : Output from table before ABCD");
		printRFCOutput(table);
		int updatedRecordcount = activityOutboundDAO
				.updateActivityOutboundRecords(table);
		System.out.println("SAPRFCConnector : Output from table");
		printRFCOutput(table);
		System.out.println("SAPRFCConnector : updateActivityOutboundRecords");
		return updatedRecordcount;

	}

	private static Properties getSpiridonConnectionProperties(
			Properties jcoProperties) throws IOException {
		ReadProperties read = new ReadProperties();
		Map propmap = read.getPropertiesMap();

		jcoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, propmap
				.get(DTTConstants.JCO_SYSNR).toString());
		jcoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, propmap
				.get(DTTConstants.JCO_ASHOST).toString()); // SAP03276
		jcoProperties.setProperty(DestinationDataProvider.JCO_R3NAME, propmap
				.get(DTTConstants.JCO_R3NAME).toString());
		jcoProperties.setProperty(DestinationDataProvider.JCO_GWSERV, propmap
				.get(DTTConstants.JCO_GWSERV).toString());
		jcoProperties.setProperty(DestinationDataProvider.JCO_GWHOST, propmap
				.get(DTTConstants.JCO_GWHOST).toString());
		jcoProperties.setProperty(DestinationDataProvider.JCO_USER, propmap
				.get(DTTConstants.JCO_USER).toString());
		// jcoProperties.setProperty(DestinationDataProvider.JCO_USER_ID,
		// "USER"); //deprecated
		jcoProperties.setProperty(DestinationDataProvider.JCO_PASSWD, propmap
				.get(DTTConstants.JCO_PASSWD).toString());
		jcoProperties.setProperty(DestinationDataProvider.JCO_CLIENT, propmap
				.get(DTTConstants.JCO_CLIENT).toString()); // GET THIS VALUE
		// jcoProperties.setProperty(DestinationDataProvider.J
		// jcoProperties.setProperty(DestinationDataProvider.JCO_LANG, "value"
		// );
		jcoProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,
				propmap.get(DTTConstants.JCO_POOL_CAPACITY).toString());
		jcoProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,
				propmap.get(DTTConstants.JCO_PEAK_LIMIT).toString());
		return jcoProperties;

	}

	public static void main(String[] args) throws JCoException, SQLException,
			NamingException, IOException {
		System.out.println("SAPRFCConnector : MAIN STARTED ");
		// This code needs to be called as last step of outbound scheduler.
		SpiridonRFCConnector spiridonRFCConnector = new SpiridonRFCConnector();
		spiridonRFCConnector.pushActivityOutboundRecordsToSpiridon();
	}

	public void SpiridonRFCConnector() {
		logger = Logger.getLogger(SpiridonRFCConnector.class);
	}

}
