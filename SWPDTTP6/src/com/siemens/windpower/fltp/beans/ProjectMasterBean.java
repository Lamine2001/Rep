package com.siemens.windpower.fltp.beans;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.eps.EPSFieldType;

@SuppressWarnings("serial")
public class ProjectMasterBean implements java.io.Serializable {

	public ProjectMasterBean() {

	}

	// Project UDF Fields

	private String sourcesystem = null;
	private String sapprojectid = null;
	private String turbinequantity = null;
	private String opportunityid = null;
	private String worktype = null;
	private String customerIFA = null;
	private String contractid = null;
	private String probability = null;
	private Date contractstart = null;
	private Date contractend = null;

	// Project Code fields
	private String locationshore = null;
	private String customername = null;
	private String region = null;
	private String subregion = null;
	private String externalstatus = null;

	// Project fields For SFDC
	private String opportunityname = null;
	// Project fields For Spirodon
	private String contractname = null;

	// EPS fields
	private String countryid = null;
	private String countryname = null;
	private String siteid = null;
	private String sitename = null;

	// Processing Flags for Error handling
	private String statusflag = null;
	private String processedflag = null;
	private String projecterrormessage = null;
	private Date projecterrordate = null;

	// Business project List values
	private List opputunityprojectcodelist = null;
	private List opputunityprojectUDFlist = null;
	private List opputunityprojectnamelist = null;

	// Business EPS List values
	private List<EPSFieldType> dttEPSlist = null;
	private List sfdcsitelist = null;
	private List sfdccountrylist = null;
	private List sppsitelist = null;
	private List sppcountrylist = null;
	private List locationshorelist = null;
	private List<Map<String, Object>> projectmasterdata = null;

	/**
	 * @param projectmasterdata
	 *            the projectmasterdata to set
	 */
	public void setProjectmasterdata(List<Map<String, Object>> projectmasterdata) {
		this.projectmasterdata = projectmasterdata;
	}

	/**
	 * @return the projectmasterdata
	 */
	public List getProjectmasterdata() {
		return projectmasterdata;
	}

	/**
	 * @return the locationshorelist
	 */
	public List getLocationshorelist() {
		return locationshorelist;
	}

	/**
	 * @param locationshorelist
	 *            the locationshorelist to set
	 */
	public void setLocationshorelist(List locationshorelist) {
		this.locationshorelist = locationshorelist;
	}

	/**
	 * @return the sfdccountrylist
	 */
	public List getSfdccountrylist() {
		return sfdccountrylist;
	}

	/**
	 * @param sfdccountrylist
	 *            the sfdccountrylist to set
	 */
	public void setSfdccountrylist(List sfdccountrylist) {
		this.sfdccountrylist = sfdccountrylist;
	}

	/**
	 * @return the sppsitelist
	 */
	public List getSppsitelist() {
		return sppsitelist;
	}

	/**
	 * @param sppsitelist
	 *            the sppsitelist to set
	 */
	public void setSppsitelist(List sppsitelist) {
		this.sppsitelist = sppsitelist;
	}

	/**
	 * @return the sfdcsitelist
	 */
	public List getSfdcsitelist() {
		return sfdcsitelist;
	}

	/**
	 * @param sfdcsitelist
	 *            the sfdcsitelist to set
	 */
	public void setSfdcsitelist(List sfdcsitelist) {
		this.sfdcsitelist = sfdcsitelist;
	}

	/**
	 * @return the dttEPSlist
	 */
	public List<EPSFieldType> getDttEPSlist() {
		return dttEPSlist;
	}

	/**
	 * @param dttEPSlist
	 *            the dttEPSlist to set
	 */
	public void setDttEPSlist(List<EPSFieldType> dttEPSlist) {
		this.dttEPSlist = dttEPSlist;
	}

	/**
	 * @return the sourcesystem
	 */
	public String getSourcesystem() {
		return sourcesystem;
	}

	/**
	 * @param sourcesystem
	 *            the sourcesystem to set
	 */
	public void setSourcesystem(String sourcesystem) {
		this.sourcesystem = sourcesystem;
	}

	/**
	 * @return the sapprojectid
	 */
	public String getSapprojectid() {
		return sapprojectid;
	}

	/**
	 * @param sapprojectid
	 *            the sapprojectid to set
	 */
	public void setSapprojectid(String sapprojectid) {
		this.sapprojectid = sapprojectid;
	}

	/**
	 * @return the turbinequantity
	 */
	public String getTurbinequantity() {
		return turbinequantity;
	}

	/**
	 * @param turbinequantity
	 *            the turbinequantity to set
	 */
	public void setTurbinequantity(String turbinequantity) {
		this.turbinequantity = turbinequantity;
	}

	/**
	 * @return the opportunityid
	 */
	public String getOpportunityid() {
		return opportunityid;
	}

	/**
	 * @param opportunityid
	 *            the opportunityid to set
	 */
	public void setOpportunityid(String opportunityid) {
		this.opportunityid = opportunityid;
	}

	/**
	 * @return the worktype
	 */
	public String getWorktype() {
		return worktype;
	}

	/**
	 * @param worktype
	 *            the worktype to set
	 */
	public void setWorktype(String worktype) {
		this.worktype = worktype;
	}

	/**
	 * @return the customerIFA
	 */
	public String getCustomerIFA() {
		return customerIFA;
	}

	/**
	 * @param customerIFA
	 *            the customerIFA to set
	 */
	public void setCustomerIFA(String customerIFA) {
		this.customerIFA = customerIFA;
	}

	/**
	 * @return the contractid
	 */
	public String getContractid() {
		return contractid;
	}

	/**
	 * @param contractid
	 *            the contractid to set
	 */
	public void setContractid(String contractid) {
		this.contractid = contractid;
	}

	/**
	 * @return the probability
	 */
	public String getProbability() {
		return probability;
	}

	/**
	 * @param probability
	 *            the probability to set
	 */
	public void setProbability(String probability) {
		this.probability = probability;
	}

	/**
	 * @return the contractstart
	 */
	public Date getContractstart() {
		return contractstart;
	}

	/**
	 * @param contractstart
	 *            the contractstart to set
	 */
	public void setContractstart(Date contractstart) {
		this.contractstart = contractstart;
	}

	/**
	 * @return the contractend
	 */
	public Date getContractend() {
		return contractend;
	}

	/**
	 * @param contractend
	 *            the contractend to set
	 */
	public void setContractend(Date contractend) {
		this.contractend = contractend;
	}

	/**
	 * @return the locationshore
	 */
	public String getLocationshore() {
		return locationshore;
	}

	/**
	 * @param locationshore
	 *            the locationshore to set
	 */
	public void setLocationshore(String locationshore) {
		this.locationshore = locationshore;
	}

	/**
	 * @return the customername
	 */
	public String getCustomername() {
		return customername;
	}

	/**
	 * @param customername
	 *            the customername to set
	 */
	public void setCustomername(String customername) {
		this.customername = customername;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the subregion
	 */
	public String getSubregion() {
		return subregion;
	}

	/**
	 * @param subregion
	 *            the subregion to set
	 */
	public void setSubregion(String subregion) {
		this.subregion = subregion;
	}

	/**
	 * @return the externalstatus
	 */
	public String getExternalstatus() {
		return externalstatus;
	}

	/**
	 * @param externalstatus
	 *            the externalstatus to set
	 */
	public void setExternalstatus(String externalstatus) {
		this.externalstatus = externalstatus;
	}

	/**
	 * @return the opportunityname
	 */
	public String getOpportunityname() {
		return opportunityname;
	}

	/**
	 * @param opportunityname
	 *            the opportunityname to set
	 */
	public void setOpportunityname(String opportunityname) {
		this.opportunityname = opportunityname;
	}

	/**
	 * @return the contractname
	 */
	public String getContractname() {
		return contractname;
	}

	/**
	 * @param contractname
	 *            the contractname to set
	 */
	public void setContractname(String contractname) {
		this.contractname = contractname;
	}

	/**
	 * @return the countryid
	 */
	public String getCountryid() {
		return countryid;
	}

	/**
	 * @param countryid
	 *            the countryid to set
	 */
	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}

	/**
	 * @return the countryname
	 */
	public String getCountryname() {
		return countryname;
	}

	/**
	 * @param countryname
	 *            the countryname to set
	 */
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	/**
	 * @return the siteid
	 */
	public String getSiteid() {
		return siteid;
	}

	/**
	 * @param siteid
	 *            the siteid to set
	 */
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	/**
	 * @return the sitename
	 */
	public String getSitename() {
		return sitename;
	}

	/**
	 * @param sitename
	 *            the sitename to set
	 */
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	/**
	 * @return the statusflag
	 */
	public String getStatusflag() {
		return statusflag;
	}

	/**
	 * @param statusflag
	 *            the statusflag to set
	 */
	public void setStatusflag(String statusflag) {
		this.statusflag = statusflag;
	}

	/**
	 * @return the processedflag
	 */
	public String getProcessedflag() {
		return processedflag;
	}

	/**
	 * @param processedflag
	 *            the processedflag to set
	 */
	public void setProcessedflag(String processedflag) {
		this.processedflag = processedflag;
	}

	/**
	 * @return the projecterrormessage
	 */
	public String getProjecterrormessage() {
		return projecterrormessage;
	}

	/**
	 * @param projecterrormessage
	 *            the projecterrormessage to set
	 */
	public void setProjecterrormessage(String projecterrormessage) {
		this.projecterrormessage = projecterrormessage;
	}

	/**
	 * @return the projecterrordate
	 */
	public Date getProjecterrordate() {
		return projecterrordate;
	}

	/**
	 * @param projecterrordate
	 *            the projecterrordate to set
	 */
	public void setProjecterrordate(Date projecterrordate) {
		this.projecterrordate = projecterrordate;
	}

	public List getOpputunityprojectcodelist() {
		return opputunityprojectcodelist;
	}

	/**
	 * @param opputunityprojectcodelist
	 *            the opputunityprojectcodelist to set
	 */
	public void setOpputunityprojectcodelist(List opputunityprojectcodelist) {
		this.opputunityprojectcodelist = opputunityprojectcodelist;
	}

	/**
	 * @return the opputunityprojectUDFlist
	 */
	public List getOpputunityprojectUDFlist() {
		return opputunityprojectUDFlist;
	}

	/**
	 * @param opputunityprojectUDFlist
	 *            the opputunityprojectUDFlist to set
	 */
	public void setOpputunityprojectUDFlist(List opputunityprojectUDFlist) {
		this.opputunityprojectUDFlist = opputunityprojectUDFlist;
	}

	/**
	 * @return the opputunityprojectnamelist
	 */
	public List getOpputunityprojectnamelist() {
		return opputunityprojectnamelist;
	}

	/**
	 * @param opputunityprojectnamelist
	 *            the opputunityprojectnamelist to set
	 */
	public void setOpputunityprojectnamelist(List opputunityprojectnamelist) {
		this.opputunityprojectnamelist = opputunityprojectnamelist;
	}

	public List getSppcountrylist() {
		return sppcountrylist;
	}

	public void setSppcountrylist(List sppcountrylist) {
		this.sppcountrylist = sppcountrylist;
	}

}
