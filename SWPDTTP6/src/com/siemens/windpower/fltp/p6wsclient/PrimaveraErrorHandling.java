package com.siemens.windpower.fltp.p6wsclient;

import java.util.ArrayList;
import java.util.List;

import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.fltp.dao.UpdateProcessRecordsDAO;

public class PrimaveraErrorHandling {
	UpdateProcessRecordsDAO processeddao = new UpdateProcessRecordsDAO();

	public void errorHandling(String type, String sourcetablename,
			List errordetails) throws Exception {
		String flag = DTTConstants.FLAG;
		try {
			String uniqueid = null;
			String errormessage = null;

			if (type.equalsIgnoreCase("EPS")) {
				errormessage = "Unable Create Site";
				for (int i = 0; i < errordetails.size(); i += 2) {

					uniqueid = errordetails.get(i).toString();

					System.out.println(uniqueid);
					/*
					 * inserterror.insertError(sourcetablename, uniqueid,
					 * errormessage);
					 * 
					 * processeddao.updaterecords(sourcetablename, flag,
					 * errordetails);
					 */
				}
			}

			if (type.equalsIgnoreCase("OPPProject")) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}

			if (type.equalsIgnoreCase("OPPProjectUpdate")) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}

			if (type.equalsIgnoreCase(DTTConstants.PROJECTCREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.PROJECTUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.PROJECTCODECREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.PROJECTCODEUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.PROJECTUDFCREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.PROJECTUDFUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}

			if (type.equalsIgnoreCase(DTTConstants.WBSCREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.WBSUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.WBSUDFCREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}

			if (type.equalsIgnoreCase(DTTConstants.WBSUDFUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}

			if (type.equalsIgnoreCase(DTTConstants.ACTIVITYUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.ACTIVITYCODECREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.ACTIVITYCODEUPDATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.ACTIVITYUDFCREATE)) {
				errormessage = "Unable Create Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.ACTIVITYUDFUPDATE)) {
				errormessage = "Unable To Opportunity Project";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.ASSIGNMENTCREATE)) {
				errormessage = "Unable To Create Assignment";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}
			if (type.equalsIgnoreCase(DTTConstants.ASSIGNMENTUPDATE)) {
				errormessage = "Unable To Update Assignment";
				for (int i = 0; i < errordetails.size(); i += 3) {

					uniqueid = errordetails.get(i + 1).toString();

					System.out.println(uniqueid);
					// inserterror.insertError(sourcetablename, uniqueid,
					// errormessage);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void main(String args[]) throws Exception {
		PrimaveraErrorHandling updatedao = new PrimaveraErrorHandling();
		List errordetails = new ArrayList();
		// errordetails.add("GB");
		// errordetails.add("GBname");
		// errordetails.add("GBnamedetails");
		// errordetails.add("IE");
		// errordetails.add("IEname");
		// errordetails.add("IEnamedetails");

		updatedao.errorHandling("OPPProject", "ProjectMaster", errordetails);
		;
	}

}
