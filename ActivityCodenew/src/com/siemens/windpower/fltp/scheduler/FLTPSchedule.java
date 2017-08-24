package com.siemens.windpower.fltp.scheduler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.ExcelFileReader;
import com.siemens.windpower.common.FLTPMailService;
import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.fltp.dao.DAOManagerForDTT;
import com.siemens.windpower.fltp.hanawsclient.FLTPHANAWSConnector;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class FLTPSchedule implements Runnable {
	Logger logger = null;
	PrimaveraWSClient wsclient = null;
	FLTPMailService fltpmailservice = null;

	public void loadHanaWSData() throws Exception {

		FLTPHANAWSConnector obj = new FLTPHANAWSConnector();
		try {
			System.out.println("FLTPHANAWSConnector : STARTED");
			obj = new FLTPHANAWSConnector();
			FLTPHANAWSConnector.loadFLTPData();
			System.out.println("FLTPHANAWSConnector : DATA LOAD COMPLETED");

		} catch (Exception ex) {
			System.out.println("Error...");
			ex.printStackTrace();
		} finally {
			obj.releaseURLConnections();
			System.out.println("releaseURLConnections :Completed ");
		}

	}

	public void loadExcelData() throws Exception {

		ExcelFileReader filereader = new ExcelFileReader();
		// logger.info("Executing Excel data upload");
		List<List<String>> fileresult = filereader.excelRead();
		// logger.info(" Excel file upload completes");

	}

	public void loadDataToPrimavera() throws Exception {

		wsclient = new PrimaveraWSClient();
		// logger.info("Uploading data to Primavera");
		wsclient.primaveraEPSwsclientcode();
		// logger.info("Data uploaded to Primavera");

	}

	public void sendEmailErrorData() throws Exception {

		fltpmailservice = new FLTPMailService();
		// logger.info("Maling error records data");
		fltpmailservice.mailService();
		// logger.info("Email sent with error records data");

	}

	public void run() {
		logger = Logger.getLogger(FLTPDTTInboundScheduler.class);

		try {

			logger.info("Scheduler Started");

			ReadProperties read = new ReadProperties();
			Map prop = read.getPropertiesMap();
			String inputtypeIndicator = prop.get("IS_EXCEL_FILE_READ")
					.toString();

			if (inputtypeIndicator.equalsIgnoreCase("Y")) {
				loadExcelData();
			} else {
				String ishanaloadneeded = prop.get("IS_HANA_LOAD_NEEDED")
						.toString();
				if (ishanaloadneeded.equalsIgnoreCase("Y")) {
					logger.info("Loading hana data");
					loadHanaWSData();
				}
			}
			String isprimaveraloadneeded = prop.get("IS_PRIMAVERA_LOAD_NEEDED")
					.toString();
			if (isprimaveraloadneeded.equalsIgnoreCase("Y")) {
				logger.info("Loading data to Primavera");
				loadDataToPrimavera();
			}

			// sendEmailErrorData();

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
