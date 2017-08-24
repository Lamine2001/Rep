package com.siemens.windpower.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;

public class ExcelFileReader {
	//Logger logger = null;
	static PrimaveraWSClient wsclient = null;
	public ExcelFileReader() {
		//logger = Logger.getLogger(ExcelFileReader.class);
	}

	@SuppressWarnings("unused")
	public List<List<String>> excelRead() throws Exception {
		Connection conn = null;
		List<List<String>> sheetData = new ArrayList<List<String>>();
		
		FileInputStream file = null;
		int fileresult = 0;
		try {
			file = new FileInputStream(new File("C:/ActvityCode.xlsx"));
		//	file = new FileInputStream(new File(DTTConstants.EXCELFILENAME));
			if (file == null) {
				//logger.error(DTTErrorConstants.ERR001);
				
			} else {

				// logger.info(DTTErrorConstants.INF001);
				//conn = DAOManagerForDTT.getConnection();
				// Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				int noofsheets = workbook.getNumberOfSheets();
				// Get first/desired sheet from the workbook

				for (int i = 0; i < noofsheets; i++) {
					XSSFSheet sheet = workbook.getSheetAt(i);

					try {
					// logger.info(DTTErrorConstants.INF002);
					// logger.info(workbook.getSheetName(i));
					int rownumber = 0;
					ArrayList<String> excellist = new ArrayList<String>();
					String sql = "INSERT INTO " + workbook.getSheetName(i)
							+ "  (";
					workbook.getMissingCellPolicy();
					// //logger.info(sheet.getRow(0).getRowNum() + "  ");
					
					int maxNumOfCells = sheet.getRow(0).getLastCellNum(); // The
																			// the
					//System.out.print(maxNumOfCells + "  ");												// maximum
																			// number
					Map<String,String> data = new HashMap<String,String>();												// of
					List Data =null;
				//	UtilMap m1 = new UtilMap();	
					Map<String,Map<String,List<String>>> maps = new HashMap<String,Map<String,List<String>>>();// columns
					Iterator rows = sheet.rowIterator();
					int sheetrowcount = sheet.getLastRowNum();
					while (rows.hasNext()) {
						Row row = (Row) rows.next();
						Iterator cells = row.cellIterator();
						 Data = new ArrayList();
						
						for (int cellCounter = 0; cellCounter < maxNumOfCells; cellCounter++) { // Loop
																								// through
																							// cells

							Cell cell;

							if (row.getCell(cellCounter) == null) {
								cell = row.createCell(cellCounter);
								//System.out.println(cellCounter);
								//System.out.println(row.getCell(cellCounter));
							} else {
								cell = row.getCell(cellCounter);
								//System.out.println(cellCounter);
								//System.out.println(row.getCell(cellCounter));
								//System.out.println(row.getCell(cellCounter).getStringCellValue().toString());
								//System.out.println(row.getCell(cellCounter+1).getStringCellValue().toString());
								//	System.out.println(row.getCell(cellCounter));
							
								
									/* previous=row.getCell(cellCounter).getStringCellValue().toString();
									//System.out.println("previous"+previous);
								if(row.getCell(cellCounter).getStringCellValue().toString().equalsIgnoreCase(previous)){
									//System.out.println("In");
									
									UtilMap m = new UtilMap();	
									m.put(row.getCell(cellCounter+1).toString(), row.getCell(cellCounter+2).getStringCellValue().toString());
									//System.out.println(m.size());
									maps.put(row.getCell(cellCounter).toString(),m);
									cellCounter=cellCounter+2;
								}*/
								Data.add(row.getCell(cellCounter).toString());
							}
							
							}

							
						sheetData.add(Data);

						}
					
					
						
					} catch (Exception e) {
						// TODO: handle exception
					}	
				}System.out.println(sheetData.size());
				
				for(List<String> csv : sheetData)
		    	{
		    		//dumb logic to place the commas correctly
		    		if(!csv.isEmpty())
		    		{
		    			System.out.print(csv.get(0));
		    			for(int i=1; i < csv.size(); i++)
		    			{
		    				System.out.print("," + csv.get(i));
		    			}
		    		}
		    		System.out.print("\n");
		    	}
					/*for (int n = 0; n < sheetData.size(); n++) {
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
						//System.out.println(" SQL" + insertsql);
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
					}*/
					// System.out.println("excellist" + excellist);

				}
			
			//fileresult = 1;
		} catch (FileNotFoundException iofl) {
			iofl.printStackTrace();
			//logger.error(DTTErrorConstants.ERR002, iofl);
			throw iofl;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			//logger.error(DTTErrorConstants.ERR003, ioe);
			throw ioe;
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error(DTTErrorConstants.ERR004, e);
			throw e;
		} finally {

			file.close();
		}
		return sheetData;
	}
	public static void loadExcelData() throws Exception {

		ExcelFileReader filereader = new ExcelFileReader();
		// logger.info("Executing Excel data upload");
		List<List<String>> fileresult = filereader.excelRead();
		// logger.info(" Excel file upload completes");

	}

	public static void loadDataToPrimavera() throws Exception {

		wsclient = new PrimaveraWSClient();
		// logger.info("Uploading data to Primavera");
		wsclient.primaveraEPSwsclientcode();
		// logger.info("Data uploaded to Primavera");

	}

	public static void main(String[] args) throws Exception {

		
		  ExcelFileReader efr = new ExcelFileReader(); efr.excelRead();
		 

		String stringvalue = "Sudha'kar";
		if (stringvalue.contains("'")) {
			String substringvalue = stringvalue.substring(0,
					stringvalue.indexOf("'"));
			String remainsubstringvalue = stringvalue.substring(
					stringvalue.indexOf("'"), stringvalue.length());
			substringvalue = substringvalue.concat("'");
			stringvalue = substringvalue.concat(remainsubstringvalue);
			System.out.println(stringvalue);
			
			ReadProperties read = new ReadProperties();
			Map prop = read.getPropertiesMap();
			String inputtypeIndicator = prop.get("IS_EXCEL_FILE_READ")
					.toString();

			if (inputtypeIndicator.equalsIgnoreCase("Y")) {
				loadExcelData();
			} 
			String isprimaveraloadneeded = prop.get("IS_PRIMAVERA_LOAD_NEEDED")
					.toString();
			if (isprimaveraloadneeded.equalsIgnoreCase("Y")) {
				
				loadDataToPrimavera();
			}


		}

	}

}

