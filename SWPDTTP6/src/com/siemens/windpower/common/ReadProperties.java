package com.siemens.windpower.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

import org.apache.log4j.Logger;

import com.siemens.windpower.fltp.dao.DAOManagerForDTT;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;

public class ReadProperties {
	private static Map readpropmap = null;
	Logger logger = null;

	public ReadProperties() throws IOException {
		logger = logger.getLogger(ReadProperties.class);
		readPropertiesFile();
	}

	public Map readPropertiesFile() throws IOException {
		if (readpropmap == null) {
			readpropmap = getPropertiesMap();
		} else {
			/* //logger.info("Reusing the existing properties object"); */
		}
		return readpropmap;
	}

	public Map getPropertiesMap() throws IOException {

		Properties prop = new Properties();
		InputStream input = null;
		Map propmap = new HashMap();
		Logger logger = null;
		try {
			logger = Logger.getLogger(ReadProperties.class);

			String filename = DTTConstants.DTT_PROPERTIES_FILE_NAME;
			// input =
			// getClass().getClassLoader().getResourceAsStream(filename);
			/*
			 * File file = new File(
			 * DTTConstants.DTT_PROPERTIES_COMPLETE_FILE_PATH);
			 */
			File file = new File(DTTConstants.DTT_PROPERTIES_COMPLETE_FILE_PATH);
			FileInputStream fis = new FileInputStream(file);

			if (fis == null) {
				// System.out.println("Missing properties file" + filename);
				// logger.info(DTTErrorConstants.ERR007 + filename);

			} else {

				// load a properties file
				prop.load(fis);
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String value = prop.getProperty(key);
					propmap.put(key, value);
					System.out.println("Key : " + key + ", Value : " + value);
				}

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		return propmap;
	}

	public static void main(String args[]) throws IOException {
		ReadProperties read = new ReadProperties();
		Map propmap = read.readPropertiesFile();
		// System.out.println(propmap.get("database"));
		// System.out.println(propmap.get("dbuser"));
	}
}
