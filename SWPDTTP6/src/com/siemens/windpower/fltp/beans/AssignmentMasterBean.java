package com.siemens.windpower.fltp.beans;

import java.util.List;
import java.util.Map;

public class AssignmentMasterBean {

	List<Map<String, Object>> assignmentmasterdata = null;
	List<Map<String, Object>> nonlaborhoursdata = null;

	/**
	 * @return the nonlaborhoursdata
	 */
	public List<Map<String, Object>> getNonlaborhoursdata() {
		return nonlaborhoursdata;
	}

	/**
	 * @param nonlaborhoursdata
	 *            the nonlaborhoursdata to set
	 */
	public void setNonlaborhoursdata(List<Map<String, Object>> nonlaborhoursdata) {
		this.nonlaborhoursdata = nonlaborhoursdata;
	}

	/**
	 * @return the assignmentmasterdata
	 */
	public List<Map<String, Object>> getAssignmentmasterdata() {
		return assignmentmasterdata;
	}

	/**
	 * @param assignmentmasterdata
	 *            the assignmentmasterdata to set
	 */
	public void setAssignmentmasterdata(
			List<Map<String, Object>> assignmentmasterdata) {
		this.assignmentmasterdata = assignmentmasterdata;
	}

}
