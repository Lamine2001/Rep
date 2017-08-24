package com.siemens.windpower.fltp.p6wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;

import com.primavera.ws.p6.udfvalue.IntegrationFault;

import com.primavera.ws.p6.udftype.UDFType;
import com.primavera.ws.p6.udftype.UDFTypeFieldType;
import com.primavera.ws.p6.udftype.UDFTypePortType;
import com.primavera.ws.p6.udftype.UDFTypeService;
import com.primavera.ws.p6.udfvalue.CreateUDFValuesResponse.ObjectId;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.primavera.ws.p6.udfvalue.UDFValueFieldType;
import com.primavera.ws.p6.udfvalue.UDFValuePortType;
import com.primavera.ws.p6.udfvalue.UDFValueService;
import com.siemens.windpower.common.PrimaveraUserValidation;
import com.siemens.windpower.fltp.beans.ProjectMasterBean;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class PrimaveraProjectUDF {

	private static final String UDF_TYPE_SERVICE = DTTConstants.UDF_TYPE_SERVICE;
	private static final String UDF_VALUE_SERVICE = DTTConstants.UDF_VALUE_SERVICE;
	ProjectMasterBean projectmasterbean = null;
	Logger logger = null;
	static UDFValuePortType servicePort = null;
	static UDFValueService service = null;
	static UDFTypeService udftypeservice = null;
	static List<UDFValueFieldType> udfFields = null;
	static UDFTypePortType udftypeservicePort = null;
	static List<UDFTypeFieldType> udftypeFields = null;

	PrimaveraProjectUDF() throws Exception {
		if (servicePort == null || udftypeservicePort == null) {
			initlialiseProjectUDF();
		}

	}

	public void initlialiseProjectUDF() throws Exception {
		logger = Logger.getLogger(PrimaveraProjectUDF.class);
		projectmasterbean = new ProjectMasterBean();
		service = createProjectUDFValueService();
		udfFields = new ArrayList<UDFValueFieldType>();
		udfFields.add(UDFValueFieldType.CODE_VALUE);
		udfFields.add(UDFValueFieldType.DESCRIPTION);
		udfFields.add(UDFValueFieldType.FOREIGN_OBJECT_ID);
		udfFields.add(UDFValueFieldType.UDF_CODE_OBJECT_ID);
		udfFields.add(UDFValueFieldType.UDF_TYPE_OBJECT_ID);
		udfFields.add(UDFValueFieldType.DOUBLE);
		udfFields.add(UDFValueFieldType.TEXT);
		udfFields.add(UDFValueFieldType.INTEGER);
		udfFields.add(UDFValueFieldType.START_DATE);
		udfFields.add(UDFValueFieldType.FINISH_DATE);

		udftypeservice = createProjectUDFService();
		udftypeFields = new ArrayList<UDFTypeFieldType>();
		udftypeFields.add(UDFTypeFieldType.TITLE);
		udftypeFields.add(UDFTypeFieldType.SUBJECT_AREA);
		udftypeFields.add(UDFTypeFieldType.OBJECT_ID);
		udftypeFields.add(UDFTypeFieldType.DATA_TYPE);

	}

	private UDFValueService createProjectUDFValueService() throws Exception {

		try {
			// //logger.info("Calling UDF Value Service");
			String url = PrimaveraUserValidation
					.makeHttpURLString(UDF_VALUE_SERVICE);
			URL wsdlURL = new URL(url);
			service = new UDFValueService(wsdlURL);

		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR027 + e);
			throw e;
		}
		return service;
	}

	List<ObjectId> createUDFValue(List<UDFValue> values)
			throws IntegrationFault, Exception {
		List<ObjectId> objIds = null;
		try {
			servicePort = service.getUDFValuePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			// //logger.info("Calling Create UDF Value Service");
			// projectmasterbean.setContractname("Test");

			// Create project with required fields
			System.out.println("Project  UDF  create  Primavera  "
					+ java.util.Calendar.getInstance().getTime());
			objIds = servicePort.createUDFValues(values);
			System.out.println(objIds.size());
			System.out.println("Project  UDF  create  Primavera end  "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR028 + e);
			throw e;
		}

		return objIds;
	}

	List<UDFValue> readUDFValue(Integer objectid) throws Exception {
		List<UDFValue> objIds = null;
		try {
			servicePort = service.getUDFValuePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			System.out.println("Project  UDF  Read  Primavera   "
					+ java.util.Calendar.getInstance().getTime());

			// Create project with required fields

			String object = "ForeignObjectId=" + objectid.toString();
			objIds = servicePort.readUDFValues(udfFields, object, null);
			System.out.println(objIds.size());
			System.out.println("Project  UDF  Read  Primavera End  "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR029 + e);
			throw e;
		}
		return objIds;
	}

	List<UDFValue> readUDFValueUDF() throws Exception {
		List<UDFValue> objIds = null;
		try {
			servicePort = service.getUDFValuePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			System.out.println("  UDF value Read  Primavera   "
					+ java.util.Calendar.getInstance().getTime());

			// Create project with required fields

			// String object="ForeignObjectId="+objectid.toString();
			objIds = servicePort.readUDFValues(udfFields, null, null);
			System.out.println(objIds.size());
			System.out.println("  UDF value Read  Primavera End  "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR029 + e);
			throw e;
		}
		return objIds;
	}

	List<UDFValue> readOutbounfUDFValue(Integer objectid) throws Exception {
		List<UDFValue> objIds = null;
		try {
			servicePort = service.getUDFValuePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			String object = "ForeignObjectId=" + objectid.toString();

			objIds = servicePort.readUDFValues(udfFields, object, null);
			System.out.println(objIds.size());
		} catch (Exception e) {
			logger.error("Exception while  reading UDF values from primavera"
					+ e);
			throw e;
		}
		return objIds;
	}

	List<UDFValue> readProjectOutbounfUDFValue(Integer projectobjectid)
			throws Exception {
		List<UDFValue> objIds = null;
		try {
			servicePort = service.getUDFValuePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			String projectobject = "ForeignObjectId="
					+ projectobjectid.toString();

			objIds = servicePort.readUDFValues(udfFields, projectobject, null);
			System.out.println(objIds.size());
		} catch (Exception e) {
			logger.error("Exception while  reading UDF values from primavera"
					+ e);
			throw e;
		}
		return objIds;
	}

	private UDFTypeService createProjectUDFService() throws Exception {

		try {

			// //logger.info("Calling  UDF Type Service");
			String url = PrimaveraUserValidation
					.makeHttpURLString(UDF_TYPE_SERVICE);
			URL wsdlURL = new URL(url);
			udftypeservice = new UDFTypeService(wsdlURL);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(DTTErrorConstants.ERR030 + e);
			throw e;
		}
		return udftypeservice;
	}

	List<UDFType> readProjectUDF() throws Exception {
		List<UDFType> objIds = null;
		try {
			udftypeservicePort = udftypeservice.getUDFTypePort();
			Client client = ClientProxy.getClient(udftypeservicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			// //logger.info("Calling  Read UDF Types ");
			// projectmasterbean.setContractname("Test");

			// Create project with required fields
			System.out.println("  UDF type Read  Primavera   "
					+ java.util.Calendar.getInstance().getTime());

			objIds = udftypeservicePort.readUDFTypes(udftypeFields, null, null);
			System.out.println(objIds.size());
			System.out.println("  UDF type Read  Primavera  end "
					+ java.util.Calendar.getInstance().getTime());
		} catch (Exception e) {
			// logger.info( DTTErrorConstants.ERR031+ e);
			throw e;
		}
		return objIds;
	}

	boolean updateUDFValue(List<UDFValue> udfupdate) throws Exception {
		boolean objids = false;
		try {
			servicePort = service.getUDFValuePort();
			Client client = ClientProxy.getClient(servicePort);
			PrimaveraUserValidation.setCookieOrUserTokenData(client);
			// //logger.info("Calling  Update UDF Types ");

			objids = servicePort.updateUDFValues(udfupdate);
			System.out.println(objids + " UDF Updated:");
		} catch (Exception e) {
			logger.error(DTTErrorConstants.ERR032 + e);
			throw e;
		}
		return objids;
	}

}
