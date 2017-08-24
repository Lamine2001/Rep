package com.siemens.windpower.fltp.scheduler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.ExcelFileReader;
import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.fltp.dao.DAOManagerForDTT;
import com.siemens.windpower.fltp.hanawsclient.FLTPHANAWSConnector;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.fltp.saprfcclient.SpiridonRFCConnector;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class FLTPOutboundSchedule implements Runnable {

	Logger logger = null;
	PrimaveraWSClient wsclient = null;

	public void loadPrimaveraWSData() throws Exception {

		FLTPHANAWSConnector obj = new FLTPHANAWSConnector();
		try {
			logger.info("FLTPHANAWSConnector Outbound loadPrimaveraWSData: STARTED");
			obj = new FLTPHANAWSConnector();
			FLTPHANAWSConnector.loadPrimaveraData();
			logger.info("FLTPHANAWSConnector Outbound loadPrimaveraWSData: DATA LOAD COMPLETED");

		} catch (Exception ex) {
			System.out.println("Error...");
			ex.printStackTrace();
		} finally {
			obj.releaseURLConnections();
			logger.info("releaseURLConnections :Completed ");
		}

	}

	public void loadActivityOurboundToSpiridon() throws MalformedURLException,
			IOException {
		SpiridonRFCConnector spiridonRFCConnector = new SpiridonRFCConnector();

		try {
			System.out.println(" Outbound loadtoSpiridonData: STARTED");

			spiridonRFCConnector.pushActivityOutboundRecordsToSpiridon();
			System.out
					.println(" Outbound loadtoSpiridonData: DATA LOAD COMPLETED");

		} catch (Exception ex) {
			System.out.println("Error...");
			ex.printStackTrace();
		}

	}

	public void run() {
		logger = Logger.getLogger(FLTPDTTOutboundScheduler.class);

		try {

			loadPrimaveraWSData();
			loadActivityOurboundToSpiridon();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				DAOManagerForDTT.closeDBResources();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
