package com.siemens.windpower.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.siemens.windpower.fltp.dao.DAOManagerForDTT;

public class ExcelFileReader {
	Logger logger = null;

	public ExcelFileReader() {
		logger = Logger.getLogger(ExcelFileReader.class);
	}

	@SuppressWarnings("unused")
	public int excelRead() throws Exception {
		Connection conn = null;
		FileInputStream file = null;
		int fileresult = 0;
		try {

			file = new FileInputStream(new File(DTTConstants.EXCELFILENAME));
			if (file == null) {
				logger.error(DTTErrorConstants.ERR001);
				return fileresult;
			} else {

				// logger.info(DTTErrorConstants.INF001);
				conn = DAOManagerForDTT.getConnection();
				// Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				int noofsheets = workbook.getNumberOfSheets();
				// Get first/desired sheet from the workbook

				for (int i = 0; i < noofsheets; i++) {
					XSSFSheet sheet = workbook.getSheetAt(i);

					List sheetData = new ArrayList();
					// logger.info(DTTErrorConstants.INF002);
					// logger.info(workbook.getSheetName(i));
					int rownumber = 0;
					ArrayList<String> excellist = new ArrayList<String>();
					String sql = "INSERT INTO " + workbook.getSheetName(i)
							+ "  (";
					workbook.getMissingCellPolicy();
					// //logger.info(sheet.getRow(0).getRowNum() + "  ");
					System.out.print(sheet.getRow(0).getRowNum() + "  ");
					int maxNumOfCells = sheet.getRow(0).getLastCellNum(); // The
																			// the
																			// maximum
																			// number
																			// of
																			// columns
					Iterator rows = sheet.rowIterator();
					int sheetrowcount = sheet.getLastRowNum();
					while (rows.hasNext()) {
						Row row = (Row) rows.next();
						Iterator cells = row.cellIterator();

						List data = new ArrayList();
						for (int cellCounter = 0; cellCounter < maxNumOfCells; cellCounter++) { // Loop
																								// through
																								// cells

							Cell cell;

							if (row.getCell(cellCounter) == null) {
								cell = row.createCell(cellCounter);
								System.out.println(cellCounter);
								System.out.println(row.getCell(cellCounter));
							} else {
								cell = row.getCell(cellCounter);
								System.out.println(cellCounter);
								System.out.println(row.getCell(cellCounter));
							}

							data.add(cell);

						}
						/*
						 * if(sheetData.size()<(sheetrowcount/2)){
						 * sheetData.add(data); }else{ sheetData1.add(data); }
						 */
						sheetData.add(data);
					}

					for (int n = 0; n < sheetData.size(); n++) {
						List list = (List) sheetData.get(n);
						String insertsql = null;
						insertsql = sql;
						for (int j = 1; j < list.size(); j++) {
							Cell cell = (Cell) list.get(j);

							// Check the cell type and format accordingly
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:

								if (DateUtil.isCellDateFormatted(cell)) {
									Date exceldate = cell.getDateCellValue();
									DateFormat df = new SimpleDateFormat(
											"dd/MM/yyyy");
									String reportDate = df.format(exceldate);
									excellist.add(reportDate);
									if (reportDate != null) {
										insertsql = insertsql
												+ "CONVERT(datetime,'"
												+ reportDate + "',103),";
									}
									System.out.print(reportDate + "  ");
								} else {
									System.out.print(String.valueOf((int) cell
											.getNumericCellValue()) + "  ");
									excellist.add(String.valueOf((int) cell
											.getNumericCellValue()));
									insertsql = insertsql
											+ String.valueOf((int) cell
													.getNumericCellValue())
											+ ",";
								}
								break;
							case Cell.CELL_TYPE_STRING:
								String stringvalue = cell.getStringCellValue();
								if (stringvalue.contains("'")) {
									String substringvalue = stringvalue
											.substring(0,
													stringvalue.indexOf("'"));
									String remainsubstringvalue = stringvalue
											.substring(
													stringvalue.indexOf("'"),
													stringvalue.length());
									substringvalue = substringvalue.concat("'");
									stringvalue = substringvalue
											.concat(remainsubstringvalue);

								}
								System.out.print(cell.getStringCellValue()
										+ "  ");
								excellist.add(cell.getStringCellValue());
								if (rownumber == 0) {
									if (sheet.getRow(0).getRowNum() == 0) {
										if (j == list.size() - 1) {
											sql = sql
													+ cell.getStringCellValue()
													+ ") VALUES(";

										} else {
											sql = sql
													+ cell.getStringCellValue()
													+ ",";
										}
									}
								} else {
									if (j == list.size() - 1) {
										if (n == sheetData.size() - 1) {
											insertsql = insertsql + "'"
													+ stringvalue + "')";
										} else {
											insertsql = insertsql + "'"
													+ stringvalue + "'),(";
										}
									} else {
										insertsql = insertsql + "'"
												+ stringvalue + "',";
									}
								}

								break;
							case Cell.CELL_TYPE_BLANK:
								System.out.print(cell.getStringCellValue()
										+ "  ");
								excellist.add(cell.getStringCellValue());

								if (j == list.size() - 1) {
									if (n == sheetData.size() - 1) {
										insertsql = insertsql + "''" + ")";
									} else {
										insertsql = insertsql + "''" + ")";
									}
								} else {
									insertsql = insertsql + "null" + ",";
								}
								break;
							}

						}

						// //logger.info("SQL" + sql);
						// logger.info("SQL" + insertsql);
						System.out.println(" SQL" + insertsql);
						if (rownumber != 0) {
							PreparedStatement ps = null;
							// logger.info(DTTErrorConstants.INF003);
							ps = conn.prepareStatement(insertsql);

							int result = ps.executeUpdate();
							if (result != 0) {
								// logger.info(result);
								// logger.info(DTTErrorConstants.INF004);
								// logger.info(workbook.getSheetName(i));
							}
						}
						rownumber = 1;
					}
					// System.out.println("excellist" + excellist);

				}
			}
			fileresult = 1;
		} catch (FileNotFoundException iofl) {
			iofl.printStackTrace();
			logger.error(DTTErrorConstants.ERR002, iofl);
			throw iofl;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error(DTTErrorConstants.ERR003, ioe);
			throw ioe;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(DTTErrorConstants.ERR004, e);
			throw e;
		} finally {

			file.close();
		}
		return fileresult;
	}

	public static void main(String[] args) throws Exception {

		/*
		 * ExcelFileReader efr = new ExcelFileReader(); efr.excelRead();
		 */

		String stringvalue = "Sudha'kar";
		if (stringvalue.contains("'")) {
			String substringvalue = stringvalue.substring(0,
					stringvalue.indexOf("'"));
			String remainsubstringvalue = stringvalue.substring(
					stringvalue.indexOf("'"), stringvalue.length());
			substringvalue = substringvalue.concat("'");
			stringvalue = substringvalue.concat(remainsubstringvalue);
			System.out.println(stringvalue);

		}

	}

}
