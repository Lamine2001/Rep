/**
 * 
 */
package com.siemens.windpower.common;

public class DTTErrorConstants {

	/* ERROR CODES */

	// DTTErrorConstants.
	// ExcelFileReader
	public static final String ERR001 = "ERROR: Input File not found,Check the file and its location";
	public static final String ERR002 = "ERROR:Missing input file,Check the file and its location";
	public static final String ERR003 = "ERROR: Excel file Read Failed,Check the Format Or Contents of the File";
	public static final String ERR004 = "ERROR:Unable to upload the excel data to DTT";
	// InInterceptor
	public static final String ERR005 = "ERROR:Unable to verify security information sent back by server.";
	// OutInterceptor
	public static final String ERR006 = "ERROR:Create security credentials Failed.";
	// ReadProperties
	public static final String ERR007 = "ERROR:Missing DTT.properties file ";
	// DAOManagerForDTT
	public static final String ERR008 = "ERROR:Database driver loaded, but cannot connect to db: ";
	// InconsistantRecordsDAO
	public static final String ERR009 = "ERROR:SQL Exception while inserting inconsistant records";
	// ProjectMasterDAO
	public static final String ERR010 = "ERROR:Got an exception! ";
	// UpdateProcessRecordsDAO
	public static final String ERR011 = "ERROR:In update records Operation";
	// PrimaveraEPS
	public static final String ERR012 = "ERROR:While getting the read eps";
	public static final String ERR013 = "ERROR:While reading EPS";
	public static final String ERR014 = "ERROR:While creating the EPS ";
	// PrimaveraEPSWSClient
	public static final String ERR015 = "ERROR:Read EPS";
	public static final String ERR016 = "ERROR:While reading EPS";
	// PrimaveraProject
	public static final String ERR017 = "ERROR:While creating the serviceport for Projecct Service";
	public static final String ERR018 = "ERROR:While reading projects ";
	public static final String ERR019 = "ERROR:While creating  projects";
	// PrimaveraProjectCode
	public static final String ERR020 = "ERROR:While reading project codes";
	public static final String ERR021 = "ERROR:While creating service port for project code service";
	public static final String ERR022 = "ERROR:While creating serviceport for project code assingment service";
	public static final String ERR023 = "ERROR:While creating project codes";
	public static final String ERR024 = "ERROR:While creating project code assingments";
	public static final String ERR025 = "ERROR:While updating project code assignments";
	public static final String ERR026 = "ERROR:While reading projecct code assingments";
	// PrimaveraProjectUDF
	public static final String ERR027 = "ERROR:While creating serviceport for UDF value service";
	public static final String ERR028 = "ERROR:While creating UDF values";
	public static final String ERR029 = "ERROR:While  reading UDF values from primavera";
	public static final String ERR030 = "ERROR:While creating service port for UDF type service";
	public static final String ERR031 = "ERROR:While read UDF";
	public static final String ERR032 = "ERROR:While Calling Update UDF  value service";
	// PrimaveraWSClient
	public static final String ERR033 = "ERROR:While Creating Site";
	public static final String ERR034 = "ERROR:While Creating the  Opportunity Projects  ";
	public static final String ERR035 = "ERROR:While Updating Opportunity Projects  ";
	public static final String ERR036 = "ERROR:Unable To Create Projects";
	public static final String ERR037 = "ERROR:Unable To Update Projects";
	public static final String ERR038 = "ERROR:Unable To Create project codes";
	public static final String ERR039 = "ERROR:Unable To Update Project codes";
	public static final String ERR040 = "ERROR:Unable To Create project UDF";
	public static final String ERR041 = "ERROR:Unable To Create WBS  ";
	public static final String ERR042 = "ERROR:Unable To Update WBS  ";
	public static final String ERR043 = "ERROR:Unable To Create WBS UDF ";
	public static final String ERR044 = "ERROR:Unable To Update WBS UDF ";

	// FLTPServletContextListener
	public static final String ERR045 = "ERROR:FLTPServletContextListener - Inbound Scheduler Starting Error";

	/* INFO CODES */
	// ExcelFileReader
	public static final String INF001 = "INFO:Upload File found and uploading the data to DTT";
	public static final String INF002 = "INFO:Reading Data For";
	public static final String INF003 = "INFO:Inserting Data into DTT";
	public static final String INF004 = "INFO:Inserted Data into DTT table";
	// DAOManagerForDTT
	public static final String INF005 = "INFO:Reusing the existing connection object";
	public static final String INF006 = "INFO:Driver loaded,  connected to db: ";
	// ProjectMasterDAO
	public static final String INF007 = "INFO:Getting Project Master Data from DTT";
	public static final String INF008 = "INFO:Getting EPS Data from DTT";
	public static final String INF009 = "INFO:epsmasterdata from DTT";
	public static final String INF010 = "INFO:epsmasterdata from DTT";
	// UpdateProcessRecordsDAO
	public static final String INF011 = "INFO:In Update records ";
	public static final String INF012 = "INFO:In Update records for successfully processed";
	public static final String INF013 = "INFO:In Update records for error";
	public static final String INF014 = "INFO:SQL";
	public static final String INF015 = "INFO:InsertSQL";
	public static final String INF016 = "INFO:Inserted Error Records";
	public static final String INF017 = "INFO:Record details  ";
	// PrimaveraEPS
	public static final String INF018 = "INFO:No EPS node available";
	// PrimaveraEPSWSClient
	public static final String INF019 = "INFO:Read and Create EPS";
	public static final String INF020 = "INFO:Call readEPSforAll";
	public static final String INF021 = "INFO:Call readEPSforAll";
	public static final String INF022 = "INFO:projectmasterdata";
	public static final String INF023 = "INFO:Inside  readEPSforAll";
	// PrimaveraProjectCode
	public static final String INF024 = "INFO:Creating Project code assignment service port";
	// PrimaveraProjectWSClient
	public static final String INF025 = "INFO:Check and Create Project";
	public static final String INF026 = "INFO:projectmasterdata size ";
	public static final String INF027 = "INFO:Project already exists in primavera";
	public static final String INF028 = "INFO:projectdetails size";
	public static final String INF029 = "INFO:Check and Update Project";
	public static final String INF030 = "INFO:Project update size()";
	public static final String INF031 = "INFO:Check and Update Opp Project";
	public static final String INF032 = "INFO:Check and Create Opp Project";
	public static final String INF033 = "INFO:projectmasterdata";
	public static final String INF034 = "INFO:Check and Create  Project Codes";
	public static final String INF035 = "INFO:sourcesystem";
	public static final String INF036 = "INFO:epsss1";
	public static final String INF037 = "INFO:Location";
	public static final String INF038 = "INFO:epsss2";
	public static final String INF039 = "INFO:Project codes already exits in primavera";
	public static final String INF040 = "INFO:Worktype";
	public static final String INF041 = "INFO:customernamecode";
	public static final String INF042 = "INFO:Check and Update  Project Codes";
	public static final String INF043 = "INFO:Check and Create  Project UDF";
	public static final String INF044 = "INFO:Project UDF already exists in Primavera";
	public static final String INF045 = "INFO:Check and Update  Project UDF";
	// PrimaveraWBS
	public static final String INF046 = "INFO:Error while creating wbs";
	// PrimaveraWBSWSClient
	public static final String INF047 = "INFO:Check and Create  WBS";
	public static final String INF048 = "INFO:activitymasterdata";
	public static final String INF049 = "INFO:WBS exists in primavera with ";
	public static final String INF050 = "INFO:dttactivityid";
	public static final String INF051 = "INFO:ProjectNumber";
	public static final String INF052 = "INFO:create wbs  ";
	public static final String INF053 = "INFO:Check and Update  WBS";
	public static final String INF054 = "INFO:update wbs Size";
	public static final String INF055 = "INFO:Check and Create  WBS UDF";
	public static final String INF056 = "INFO:Check and Update  WBS UDF";
	public static final String INF057 = "INFO:Update  WBS UDF Size";
	// PrimaveraWSClient
	public static final String INF058 = "INFO:Site EPS Create Started";
	public static final String INF059 = "INFO:Site EPS Size";
	public static final String INF060 = "INFO:Site EPS Created";
	public static final String INF061 = "INFO:Opportunity Project Create Started";
	public static final String INF062 = "INFO:Opportunity Project Create Size";
	public static final String INF063 = "INFO:Opportunity Projects  Created";
	public static final String INF064 = "INFO:Opp Project Update Started";
	public static final String INF065 = "INFO:Opportunity Project update Size";
	public static final String INF066 = "INFO:Opportunity Projects Updated ";
	public static final String INF067 = "INFO:Projects Creation Started ";
	public static final String INF068 = "INFO:Project Create Size";
	public static final String INF069 = "INFO:Projects Created ";
	public static final String INF070 = "INFO:Projects Update Started ";
	public static final String INF071 = "INFO:Project Update Size";
	public static final String INF072 = "INFO:Projects Update have Done ";
	public static final String INF073 = "INFO:Projects Code creation started ";
	public static final String INF074 = "INFO:Create Project Code Size";
	public static final String INF075 = "INFO:Projects Code created ";
	public static final String INF076 = "INFO:Projects Code Update Started ";
	public static final String INF077 = "INFO:Update Project Code Size";
	public static final String INF078 = "INFO:Projects Code Update done ";
	public static final String INF079 = "INFO:Projects UDF Creation Started ";
	public static final String INF080 = "INFO:Create Project UDF Size";
	public static final String INF081 = "INFO:Projects UDF Created";
	public static final String INF082 = "INFO:Projects UDF update Started";
	public static final String INF083 = "INFO:Update Project UDF Size";
	public static final String INF084 = "INFO:Projects UDF updated ";
	public static final String INF085 = "INFO:WBS Creation Started ";
	public static final String INF086 = "INFO:WBS Create Size";
	public static final String INF087 = "INFO:Create WBS SIZE  ";
	public static final String INF088 = "INFO:WBS Created ";
	public static final String INF089 = "INFO:WBS Update Started ";
	public static final String INF090 = "INFO:WBS Update Size";
	public static final String INF091 = "INFO:WBS Update Done ";
	public static final String INF092 = "INFO:WBS UDF Create Started";
	public static final String INF093 = "INFO:Create WBS UDF Size";
	public static final String INF094 = "INFO:WBS UDF SIZE";
	public static final String INF095 = "INFO:WBS UDF created size";
	public static final String INF096 = "INFO:WBS UDF Created ";
	public static final String INF097 = "INFO:WBS UDF Update Started";
	public static final String INF098 = "INFO:Update WBS UDF Size";
	public static final String INF099 = "INFO:WBS UDF Updates ";
	public static final String INF100 = "INFO:Activity Creation Started ";
	// FLTPDTTInboundScheduler
	public static final String INF101 = "INFO:In FLTPInboundScheduler";
	public static final String INF102 = "INFO:Scheduler will start in 60 Seconds";
	// FLTPDTTOutboundScheduler
	public static final String INF103 = "INFO:In FLTPOutboundScheduler";
	// FLTPSchedule
	public static final String INF104 = "INFO:Scheduler Started";
	public static final String INF105 = "INFO:START:Excel Data Upload To DTT Database";
	public static final String INF106 = "INFO:END:Excel File upload To DTT Database is Completed";
	public static final String INF107 = "INFO:START:Uploading data to Primavera";
	public static final String INF108 = "INFO:END:Data Uploaded to Primavera Successfully";
	// FLTPServletContextListener
	public static final String INF109 = "INFO:FLTPServletContextListener :CONTEXT INTIALIZED";
	public static final String INF110 = "INFO:FLTPServletContextListener :Inbound Scheduler About To Start";
	public static final String INF111 = "INFO:FLTPServletContextListener :Inbound Scheduler Started";
	public static final String INF112 = "INFO:Multiregion Enabled";
	public static final String INF113 = "INFO:FLTPServletContextListener :Outbound Scheduler About To Start";
	public static final String INF114 = "INFO:FLTPServletContextListener :Outbound Scheduler Started";
	// public static final String INF115 =
	// "FLTPServletContextListener :Inbound Scheduler Starting Error";

	// SAPRFCConnector
	public static final String RFC_RESPONSE_EMPTY = "ActivityOutboundResponseStructure is Empty";
	public static final String RFC_NOT_FOUND = "/SIE/NWE_CS_PM_FIXPLAN not found in SAP";

}
