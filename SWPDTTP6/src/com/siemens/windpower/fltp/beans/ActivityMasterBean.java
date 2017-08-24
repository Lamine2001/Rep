package com.siemens.windpower.fltp.beans;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class ActivityMasterBean {

	private List<Map<String, Object>> activitymasterdata = null;

	private List<Map<String, Object>> wbsmasterdata = null;

	/**
	 * @return the wbsmasterdata
	 */
	public List<Map<String, Object>> getWbsmasterdata() {
		return wbsmasterdata;
	}

	/**
	 * @param wbsmasterdata
	 *            the wbsmasterdata to set
	 */
	public void setWbsmasterdata(List<Map<String, Object>> wbsmasterdata) {
		this.wbsmasterdata = wbsmasterdata;
	}

	/**
	 * @return the activitymasterdata
	 */
	public List<Map<String, Object>> getActivitymasterdata() {
		return activitymasterdata;
	}

	/**
	 * @param activitymasterdata
	 *            the activitymasterdata to set
	 */
	public void setActivitymasterdata(
			List<Map<String, Object>> activitymasterdata) {
		this.activitymasterdata = activitymasterdata;
	}
}
