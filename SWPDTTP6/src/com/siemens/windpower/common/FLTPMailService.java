package com.siemens.windpower.common;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.activation.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.fltp.dao.DAOManagerForDTT;

public class FLTPMailService {

	private static final String SMTP_HOST_NAME = DTTConstants.SMTP_HOST_NAME;
	private static final String SMTP_AUTH_USER = DTTConstants.SMTP_AUTH_USER;
	private static final String SMTP_AUTH_PWD = DTTConstants.SMTP_AUTH_PWD;
	ReadProperties read = null;
	Map prop;

	// String filePath = "C:/FLTPErrorRecords.xlsx"; //whatever path you are
	// using
	public void readFromTable(String filePath) throws Exception {
		/* Create Connection objects */
		Connection conn = null;
		/*
		 * String url =
		 * "jdbc:sqlserver://DKBDKB7TST014A:1433"+";databaseName=FLTPDTTDB_EMEA"
		 * ; String userName = "fltp_app_user"; String password = "1.fltpapp";
		 */
		conn = DAOManagerForDTT.getConnection();
		System.out.println("conn object " + conn);
		Statement stmt = conn.createStatement();

		/* Create Workbook and Worksheet objects */
		XSSFWorkbook new_workbook = new XSSFWorkbook(); // create a blank
														// workbook object
		XSSFSheet sheet = new_workbook.createSheet("ErrorTableData"); // create
																		// a
																		// worksheet
																		// with
																		// caption
																		// score_details

		/* Define the SQL query */
		ResultSet query_set = stmt
				.executeQuery("select Source_Unique_ID,Error_Message from ERROR");

		// Creating header
		Row headerRow = sheet.createRow(0);
		Cell cellone = headerRow.createCell(0);
		Cell celltwo = headerRow.createCell(1);
		cellone.setCellValue("SAP_PARENT_OBJECT ID");
		celltwo.setCellValue("ERROR MESSAGE");

		CellStyle headerStyle = new_workbook.createCellStyle();
		Font headerFont = new_workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(headerFont);

		cellone.setCellStyle(headerStyle);
		celltwo.setCellStyle(headerStyle);

		/* Create Map for Excel Data */
		Map<String, Object[]> excel_data = new HashMap<String, Object[]>(); // create
																			// a
																			// map
																			// and
																			// define
																			// data
		int row_counter = 0;
		/* Populate data into the Map */
		while (query_set.next()) {
			row_counter = row_counter + 1;
			String source_unique_id = query_set.getString("Source_Unique_ID");
			String error_msgs = query_set.getString("Error_Messages");
			// System.out.println("source_unique_id"+source_unique_id+"error_msgs"+error_msgs);
			excel_data.put(Integer.toString(row_counter), new Object[] {
					source_unique_id, error_msgs });
		}
		/* Close all DB related objects */
		query_set.close();
		stmt.close();

		/* Load data into logical worksheet */
		Set<String> keyset = excel_data.keySet();
		int rownum = 1;

		for (String key : keyset) { // loop through the data and add them to the
									// cell
			Row row = sheet.createRow(rownum++);
			Object[] objArr = excel_data.get(key);
			int cellnum = 0;

			for (Object obj : objArr) {
				if (cellnum == 0) {
					Cell cell = row.createCell(cellnum);
					cell.setCellValue("SOURCEID");
				}
				Cell cell = row.createCell(cellnum++);

				if (obj instanceof Double)
					cell.setCellValue((Double) obj);
				else
					cell.setCellValue((String) obj);
			}
		}
		System.out.println("filePath in readfrmtable" + filePath);
		FileOutputStream output_file = new FileOutputStream(new File(filePath)); // create
																					// XLS
																					// file
		new_workbook.write(output_file);// write excel document to output stream
		output_file.close(); // close the file
	}

	public void mailService() throws Exception {
		read = new ReadProperties();
		prop = read.getPropertiesMap();
		Properties props = new Properties();
		props.put(DTTConstants.MAIL_TRANSPORT_PROTOCOL,
				DTTConstants.MAIL_TRANSPORT_PROTOCOL_VALUE);
		props.put(DTTConstants.MAIL_SMTP_HOST, SMTP_HOST_NAME);
		props.put(DTTConstants.MAIL_SMTP_AUTH, DTTConstants.VALID);
		props.put(DTTConstants.MAIL_SMTP_PORT,
				DTTConstants.MAIL_SMTP_PORT_NUMBER);

		Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getDefaultInstance(props, auth);

		try {
			Date currentDate = new java.util.Date();
			// System.out.println("currntdate"+currentDate);
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String newDateString = df.format(currentDate);
			System.out.println("newDateString" + newDateString);
			String fileName = DTTConstants.ERRORFILE_COMPLETE_FILE_NAME
					+ newDateString + ".xlsx";// change accordingly
			// String filePath =
			// DTTConstants.ERRORFILE_COMPLETE_FILE_PATH+newDateString+".xlsx";
			String filePath = prop.get("ERRORFILE_COMPLETE_FILE_PATH")
					.toString() + newDateString + ".xlsx";

			readFromTable(filePath);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(DTTConstants.MAIL_SUBJECT);
			message.setContent(DTTConstants.CONTENT, DTTConstants.CONTENT_TYPE);
			message.setFrom(new InternetAddress(DTTConstants.EMAIL_ADDRESS));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					DTTConstants.EMAIL_ADDRESS));

			// create MimeBodyPart object and set your message text
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setContent(DTTConstants.CONTENT,
					DTTConstants.CONTENT_TYPE);

			// create new MimeBodyPart object and set DataHandler object to this
			// object

			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			// String filename = "FLTPErrorRecords.xlsx";// change accordingly
			System.out.println("filePath in mail" + filePath);
			System.out.println("fileName in mail" + fileName);
			DataSource source = new FileDataSource(filePath);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(fileName);

			// create Multipart object and add MimeBodyPart objects to this
			// object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);

			// set the multiplart object to the message object
			message.setContent(multipart);

			System.out.println("Connect before");
			transport.connect();
			System.out.println("Connect after" + transport);
			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));
			System.out.println("Message ready" + message);
			System.out.println("Sent message successfully....");
			transport.close();
		} catch (AddressException e) {
			System.out.println("ADDRESS EXC");
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			System.out.println("NoSuchProviderException");
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("MessagingException");
			e.printStackTrace();
		}
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * Date currentDate = new java.util.Date();
	 * //System.out.println("currntdate"+currentDate); DateFormat df = new
	 * SimpleDateFormat( "dd-MM-yyyy"); String newDateString =
	 * df.format(currentDate);
	 * System.out.println("newDateString"+newDateString); String fileName =
	 * "FLTPErrorRecords_"+newDateString+".xlsx";// change accordingly String
	 * filePath = "C:/FLTPErrorRecords_"+newDateString+".xlsx";
	 * System.out.println("filename"+fileName);
	 * System.out.println("filePath"+filePath); new
	 * FLTPMailService().readFromTable(filePath); new
	 * FLTPMailService().mailService(fileName,filePath); }
	 */
}