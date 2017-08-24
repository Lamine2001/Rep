/**
 * 
 */
package com.siemens.windpower.common;

/**
 * @author SINGA00S
 *
 */
public class DTTConstants {

	// Excelfile reader
	public static final String EXCELFILENAME = "D:/Oracle/Middleware/user_projects/domains/SWPDTTP6_PROD/swpdttp6/config/EMEAdata.xlsx";

	// Out Interceptor class
	public static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	public static final String USERNAME_TOKEN = "UsernameToken";
	public static final String SAML_ASSERTION = "Assertion";
	public static final String SAML11_ASSERTION_NAMESPACE = "urn:oasis:names:tc:SAML:1.0:assertion";
	public static final String SAML20_ASSERTION_NAMESPACE = "urn:oasis:names:tc:SAML:2.0:assertion";
	public static final String TIMESTAMP_ID_PREFIX = "Timestamp-";
	public static final String SCHEMA_DATE_TIME = "http://www.w3.org/2001/XMLSchema/dateTime";
	public static final String ENVELOPENAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String MUSTUNDERSTAND = "mustUnderstand";
	public static final String MUSTUNDERSTANDVALUE = "1";
	public static final String AUTHENTICATIONSERVICEURL = "http://xmlns.oracle.com/Primavera/P6/WS/Authentication/V1";
	public static final String DATABASEINSTANCEIDXMLELEMENT = "DatabaseInstanceId";
	public static final String DATABASEINSTANCEID = "DATABASE_INSTANCE_ID";

	// PrimaveraUserValidation
	public static final String HTTP = "HTTP://";
	public static final int USERNAME_TOKEN_MODE = 0;
	/*
	 * public static final int SAML_11_MODE = 1; public static final int
	 * SAML_20_MODE = 2; public static final int COOKIE_MODE = 3;
	 */
	public static final String WEBSERVICEUSERNAME = "WEBSERVICE_USERNAME";
	public static final String WEBSERVICEPASSWORD = "WEBSERVICE_PASSWORD";
	public static final String PRIMAVERAWEBSERVICESERVER = "PRIMAVERA_WEBSERVICE_SERVER";
	public static final String WEBSERVICEPORT = "WEBSERVICE_PORT";
	public static final String FLTP = "fltp";
	public static final String KEYSTOREFILENAME = "C:/keystore.jks";
	public static final String JKS = "JKS";
	public static final String FLTP123 = "fltp123";
	public static final String MYKEYS = "mykeys";
	public static final String SAMLALIAS = "samlalias";
	public static final String STOREPASS = "storepass";
	public static final String SAMLKEYPASS = "samlkeypass";

	/*
	 * public static final String LOCATIONONSHORE = "on-shore"; public static
	 * final String LOCATIONOFFSHORE = "off-shore"; public static final String
	 * LOCATIONSHOREON = "ONS"; public static final String LOCATIONSHOREOFF =
	 * "OFS";
	 */

	// FLTPMailService
	// public static final String SMTP_HOST_NAME =
	// "DKBDKB7ESA.ww007.siemens.net";

	public static final String SMTP_HOST_NAME = "mail.siemens.de";

	public static final String SMTP_AUTH_USER = "SINGA00S";
	public static final String SMTP_AUTH_PWD = "Thanvin572";
	public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	public static final String MAIL_TRANSPORT_PROTOCOL_VALUE = "smtp";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String VALID = "true";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String MAIL_SMTP_PORT_NUMBER = "25";

	// public static final int PORT_NUMBER=37898;
	public static final String CONTENT = "This is a test";
	public static final String MAIL_SUBJECT = "Test mail from fltp code";

	public static final String CONTENT_TYPE = "text/plain";
	public static final String EMAIL_ADDRESS = "sudhakar.singareddy.ext@siemens.com";
	// public static final String
	// ERRORFILE_COMPLETE_FILE_PATH="D:/Middleware/user_projects/domains/SWPDTTP6_DEV/swpdttp6/config/logs/FLTPErrorRecords_";
	public static final String ERRORFILE_COMPLETE_FILE_NAME = "FLTPErrorRecords_";

	// ReadProperties
	public static final String DTT_PROPERTIES_FILE_NAME = "dtt.properties";
	// public static final String	 DTT_PROPERTIES_COMPLETE_FILE_PATH="C:/Oracle/FLTP_CONFIG/dtt.properties";
//public static final String DTT_PROPERTIES_COMPLETE_FILE_PATH="D:/Oracle/Middleware/Oracle_Home/user_projects/domains/SWPDTTP6_PROD/swpdttp6/config/dtt.properties";
public static final String DTT_PROPERTIES_COMPLETE_FILE_PATH="C:/dtt.properties";
//	public static final String DTT_PROPERTIES_COMPLETE_FILE_PATH = "D:/Middleware/user_projects/domains/SWPDTTP6_UAT/swpdttp6/config/dtt.properties";
	// public static final String DATABASE="database";
	// public static final String DATABASE_USER="dbuser";

	// SAML11
	public static final String SAML_ISSUER = "http://your.saml.issuer.com";
	// ProjectMasterBean (src\com\siemens\windpower\fltp\beans)
	// public static final String SERIAL="serial";

	// ActivityMasterDAO

	public static final String ATTACH_D = "D";
	public static final String ATTACH_EFFORT = "_Effort";
	public static final String ATTACH_DEMAND = "_Demand";

	// DAOManagerForDTT
	public static final String JNDINAME = "JNDINAME";

	// InconsistantRecordsDAO
	public static final String STATUSFLAG = "E";
	public static final String SAP_Parent_Object_ID = "SAP_Parent_Object_ID";
	public static final String ACT_SAP_Parent_Object_ID = "ACT_SAP_Parent_Object_ID";
	// PrimaveraActivity
	public static final String ACTIVITY_SERVICE = "/p6ws/services/ActivityService?wsdl";
	public static final String PROJECT_BASELINE_SERVICE = "/p6ws/services/BaselineProjectService?wsdl";
	// PrimaveraActivityCode
	public static final String ACTIVITY_CODE_ASSIGNMENT_SERVICE = "/p6ws/services/ActivityCodeAssignmentService?wsdl";
	public static final String ACTIVITY_CODE_SERVICE = "/p6ws/services/ActivityCodeService?wsdl";

	// PrimaveraActivityWSClient

	public static final String DEMAND = "Demand";
	public static final String DEMAND_DURATION = "Fixed Duration and Units/Time";
	public static final String DEMAND_TYPE = "Level Of Effort";
	public static final String EFFORT = "Effort";
	public static final String EFFORT_DURATION = "Fixed Units";
	public static final String EFFORT_TYPE = "Task Dependent";

	public static final String STARTDATE = "Start_Date";
	public static final String MP_CALL_NUMBER = "MP_Call_Number";
	public static final String NOTIFICATION_ID = "Notification_ID";
	public static final String NOTIFICATION_STATUS = "Notification_Status";
	public static final String SERVICE_ORDER_ID = "Service_Order_ID";
	public static final String ORDER_STATUS = "Order_Status";
	public static final String RU_TECH_ID_NUMBER = "RU_TECH_ID_NUMBER";

	public static final String Non_LABOR_HOURS = "Non_Labor_Hours";

	// PrimaveraAssignment
	public static final String RESOURCE_SERVICE = "/p6ws/services/ResourceAssignmentService?wsdl";
	public static final String ROLE_SERVICE = "/p6ws/services/RoleService?wsdl";
	// PrimaveraAssignmentWSClient

	public static final String ACTIVITYNAME = "Activity_Name";

	public static final String ROLEID = "Role_ID";
	public static final String ROLEID_MAIN = "Main";
	public static final String ROLENAME_MAIN = "Maintanence-3.6 Variable Speed (VS) turbines";
	public static final String ROLEID_TS = "TS";
	public static final String ROLENAME_TS = "Trouble Shooter-3.6 Variable Speed (VS) turbines";
	public static final String ROLEID_AT = "AT";
	public static final String ROLENAME_AT = "Advanced Technician-3.6 Variable Speed (VS) turbines";
	public static final String PLANNED_HOURS = "Planned_Hours";
	public static final String DTTACTIVITYID_ENDSWITH_D = "D";
	// PrimaveraEPS
	public static final String EPS_SERVICE = "/p6ws/services/EPSService?wsdl";
	// PrimaveraEPSWSClient
	public static final String SITE_NAME = "Site_Name";

	// PrimaveraErrorHandling
	public static final String FLAG = "E";

	public static final String PROJECTCREATE = "ProjectCreate";
	public static final String PROJECTUPDATE = "ProjectUpdate";
	public static final String PROJECTCODECREATE = "ProjectCodeCreate";
	public static final String PROJECTCODEUPDATE = "ProjectCodeUpdate";
	public static final String PROJECTUDFCREATE = "ProjectUDFCreate";
	public static final String PROJECTUDFUPDATE = "ProjectUDFUpdate";
	public static final String WBSCREATE = "WBSCreate";
	public static final String WBSUPDATE = "WBSUpdate";

	public static final String WBSUDFCREATE = "WBSUDFCreate";
	public static final String WBSUDFUPDATE = "WBSUDFUpdate";
	public static final String ACTIVITYUPDATE = "ActivityUpdate";
	public static final String ACTIVITYCODECREATE = "ActivityCodeCreate";
	public static final String ACTIVITYCODEUPDATE = "ActivityCodeUpdate";
	public static final String ACTIVITYUDFCREATE = "ActivityUDFCreate";
	public static final String ACTIVITYUDFUPDATE = "ActivityUDFUpdate";
	public static final String ASSIGNMENTCREATE = "AssignmentCreate";
	public static final String ASSIGNMENTUPDATE = "AssignmentUpdate";
	// public static final String GB="GB";
	// public static final String GBNAME="GBname";
	// public static final String GBNAMEDETAILS="GBnamedetails";
	// public static final String IE="IE";
	// public static final String IENAME="IEname";
	// public static final String IENAMEDETAILS="IEnamedetails";
	// public static final String ProjectMaster="ProjectMaster";

	// PrimaveraProject
	public static final String PROJECT_SERVICE = "/p6ws/services/ProjectService?wsdl";

	// PrimaveraProjectCode
	public static final String PROJECT_CODE_SERVICE = "/p6ws/services/ProjectCodeService?wsdl";
	public static final String PROJECT_CODE_ASSIGNMENT_SERVICE = "/p6ws/services/ProjectCodeAssignmentService?wsdl";

	// PrimaveraProjectUDF
	public static final String UDF_TYPE_SERVICE = "/p6ws/services/UDFTypeService?wsdl";
	public static final String UDF_VALUE_SERVICE = "/p6ws/services/UDFValueService?wsdl";
	// PrimaveraProjectWSClient
	public static final String COUNTRYIDCREATE = "countryidcreate";

	public static final String OFFSHORECODE = "62";

	public static final String ONSHORECODE = "61";

	public static final String PROJECTNAME = "Project_Name";

	public static final int WORKTYPE_1 = 1;
	public static final int WORKTYPESTRING_1 = 55;
	public static final int WORKTYPE_2 = 2;
	public static final int WORKTYPESTRING_2 = 56;
	public static final int WORKTYPE_3 = 3;
	public static final int WORKTYPESTRING_3 = 57;
	public static final int WORKTYPE_4 = 4;
	public static final int WORKTYPESTRING_4 = 58;
	public static final int WORKTYPE_5 = 5;
	public static final int WORKTYPESTRING_5 = 59;
	public static final int WORKTYPE_6 = 6;
	public static final int WORKTYPESTRING_6 = 60;
	public static final int WORKTYPE_7 = 7;
	public static final int WORKTYPESTRING_7 = 57;

	public static final String CUSTOMER_IFA = "Customer_IFA";
	public static final String CUSTOMER_NAME = "Customer_Name";
	public static final String PROBABILITY = "Probability";
	public static final String CONTRACT_START = "Contract_Start";
	public static final String CONTRACT_END = "Contract_End";

	public static final String TURBINE_QUANTITY = "Turbine_Quantity";
	public static final String CYCLES = "Cycles";
	public static final String EXTERNAL_STATUS = "External_Status";
	public static final String EXTERNAL_STATUS_OPEN = "Open-Included";
	public static final String EXTERNAL_STATUS_OPENCODE = "78";
	public static final String EXTERNAL_STATUS_LOST = "LOST";
	public static final String EXTERNAL_STATUS_LOSTCODE = "79";
	public static final String EXTERNAL_STATUS_CTO = "Converted To Order";
	public static final String EXTERNAL_STATUS_CTOCODE = "80";

	public static final String ATTACHOPP = "OPP-";
	public static final String STARTS_WITH_OPPOR = "Oppor";

	public static final String NONE = "none";
	public static final String ZERO = "0";

	// PrimaveraWBS
	public static final String WBS_SERVICE = "/p6ws/services/WBSService?wsdl";
	// PrimaveraWBSWSClient
	public static final String COUNTRYID = "Country_Id";
	public static final String LOCATIONSHORE = "Location_Shore";
	public static final String TURBINE_NAME = "Turbine_Name";
	public static final String PROJECTNUMBER = "Project_Number";
	public static final String SOURCESYSTEM = "Source_System";
	public static final String WORKTYPE = "Work_Type";
	public static final String SAP_PARENT_OBJECT_ID = "SAP_Parent_Object_ID";
	public static final String SITEID = "Site_Id";
	public static final String DTT_ACTIVITY_OBJECT_ID = "DTT_Activity_Object_ID";
	public static final String HQ_TECH_ID_NUMBER = "HQ_TECH_ID_NUMBER";
	public static final String ROWID = "row_Id";
	public static final String SFDC = "SFDC";
	public static final String ATTACH_OPP = "OPP-";
	public static final String OPPOR = "Oppor";
	public static final String ONSHORE = "1";
	public static final String ATTACH_ONS = "ONS-";
	public static final String OFFSHORE = "2";
	public static final String ATTACH_OFS = "OFS-";
	public static final String WARRANTY_START_DATE = "Warranty_Start_Date";
	public static final String WARRANTY_END_DATE = "Warranty_End_Date";
	public static final String TURBINE_FUNCT_LOC_ID = "Turbine_Funct_Loc_ID";
	public static final String TURBINE_TYPE = "Turbine_Type";

	// PrimaveraWSClient
	public static final String EPS = "EPS";
	public static final String PROJECTMASTER = "ProjectMaster";

	public static final String UPDATE = "Update";
	public static final String E = "E";
	public static final String OPPPROJECT = "OppProject";
	public static final String OPPPROJECTUPDATE = "OppProjectUpdate";
	public static final String CREATEPROJECT = "CreateProject";
	public static final String UPDATEPROJECT = "UpdateProject";
	public static final String CREATEPROJECTCODE = "CreateProjectCode";
	public static final String UPDATEPROJECTCODE = "UpdateProjectCode";
	public static final String CREATEWBS = "CreateWBS";
	public static final String ACTIVITY = "Activity";

	public static final String UPDATEWBS = "UpdateWBS";
	public static final String CREATE_ACTIVITY = "CreateActivity";
	public static final String UPDATE_ACTIVITY = "UpdateActivity";
	public static final String CREATE_ACTIVITYCODE = "CreateActivityCode";
	public static final String UPDATE_ACTIVITYCODE = "UpdateActivityCode";
	public static final String CREATE_ACTIVITYUDF = "CreateActivityUDF";
	public static final String UPDATE_ACTIVITYUDF = "UpdateActivityUDF";
	public static final String CREATE_ASSIGNMENT = "CreateAssignment";
	public static final String ASSIGNMENT = "Assignment";
	public static final String UPDATE_ASSIGNMENT = "UpdateAssignment";

	public static final int MAXFETCHROWS = 50;
	public static final int ASSIGNMENTMAXFETCHROWS = 500;
	// FLTPDTTInboundScheduler
	public static final String SCHEDULER_RUN_TIME = "SCHEDULER_RUN_TIME";
	// FLTPServletContextListener
	public static final String IS_MULTI_REGION_ENABLED = "IS_MULTI_REGION_ENABLED";
	public static final String Y = "Y";
	public static final int MAXIMUM_TASKS = 10;

	// SAPRFCConnector

	public static final String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";
	public static final String RFC_FUNCTION_NAME = "/SIE/NWE_CS_PM_FIXPLAN";
	public static final String RFC_STRUCTURE_NAME = "/SIE/NWE_CS_PM_FIXPLAN";
	public static final String RFC_TABLE_NAME = "IT_FLTP";
	public static final String STATS = "STATS";
	public static final String LOGDT = "LOGDT";

	public static final String JCO_SYSNR = "JCO_SYSNR";
	public static final String JCO_ASHOST = "JCO_ASHOST";
	public static final String JCO_R3NAME = "JCO_R3NAME";
	public static final String JCO_GWSERV = "JCO_GWSERV";
	public static final String JCO_GWHOST = "JCO_GWHOST";
	public static final String JCO_USER = "JCO_USER";
	public static final String JCO_PASSWD = "JCO_PASSWD";
	public static final String JCO_CLIENT = "JCO_CLIENT";
	public static final String JCO_POOL_CAPACITY = "JCO_POOL_CAPACITY";
	public static final String JCO_PEAK_LIMIT = "JCO_PEAK_LIMIT";

	public static final String UK_SHORTTERM_HORIZON = "UK_SHORTTERM_HORIZON";
	public static final String ALL_SHORTTERM_HORIZON = "ALL_SHORTTERM_HORIZON";

	// FLTP STORINNG INPUT IN FILE FLTPHANAWSCONNECTOR
	// public static final String INPUTFILEDIRECTORYPATH
	// ="D:\\Oracle\\Middleware\\user_projects\\domains\\SWPDTTP6_PROD\\swpdttp6\\input\\";
	// public static final String INPUTFILEDIRECTORYPATH
	// ="D:\\Middleware\\user_projects\\domains\\SWPDTTP6_DEV\\swpdttp6\\input\\";

	public static final String PROJECTMASTERINPUTFILENAME = "PROJECTMASTER.json";

	public static final String ACTIVITYINPUTFILENAME = "ACTIVITY.json";

	public static final String ASSIGNMENTINPUTFILENAME = "ASSIGNMENT.json";
	public static final String FILENAMEDATEFORMAT = "dd-MMM-yyyy";

	public static final String ACTIVITY_CODE_TYPE_SERVICE = "/p6ws/services/ActivityCodeTypeService?wsdl";

}
