package com.siemens.windpower.fltp.p6wsclient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.naming.NamingException;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.activitycodeassignment.ActivityCodeAssignment;
import com.primavera.ws.p6.eps.EPS;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.projectcodeassignment.ProjectCodeAssignment;
import com.primavera.ws.p6.resourceassignment.ResourceAssignment;
import com.primavera.ws.p6.udfvalue.UDFValue;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.fltp.dao.UpdateProcessRecordsDAO;

public class DTTUpdateProcessedRecords {
	UpdateProcessRecordsDAO processeddao = new UpdateProcessRecordsDAO();

	public void updateProcessedRecords(String type, String tablename,
			List updatelist, String flag, String message) throws Exception {
		try {
			String uniqueid = null;
			List processedlist = new ArrayList();
			String columnname = null;
			List columnlist = new ArrayList();

			if (type == "EPS") {

				ListIterator<EPS> epsread = updatelist.listIterator();
				while (epsread.hasNext()) {

					EPS epsreadelement = epsread.next();
					processedlist.add(epsreadelement.getId().toString());
					columnname = "Site_Id";
				}

			}
			if (type == "OppProject") {

				ListIterator<Project> projecctread = updatelist.listIterator();
				while (projecctread.hasNext()) {

					Project epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getId().toString());
					columnname = "Project_Number";
				}

			}
			if (type == "OppUpdateProject") {

				ListIterator<Project> projecctread = updatelist.listIterator();
				while (projecctread.hasNext()) {

					Project epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getId().toString());
					columnname = "Project_Number";
				}

			}
			if (type == "CreateProject") {

				ListIterator<Project> projecctread = updatelist.listIterator();
				while (projecctread.hasNext()) {

					Project epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getId().toString());
					columnname = "Project_Number";
				}

			}
			if (type == "UpdateProject") {

				ListIterator<Project> projecctread = updatelist.listIterator();
				while (projecctread.hasNext()) {

					Project epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getId().toString());
					columnname = "Project_Number";
				}

			}
			if (type == "CreateProjectCode") {

				ListIterator<ProjectCodeAssignment> projecctread = updatelist
						.listIterator();
				while (projecctread.hasNext()) {

					ProjectCodeAssignment epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getProjectId().toString());

					columnname = "Project_Number";
				}

			}
			if (type == "UpdateProjectCode") {

				ListIterator<ProjectCodeAssignment> projecctread = updatelist
						.listIterator();
				while (projecctread.hasNext()) {

					ProjectCodeAssignment epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getProjectId().toString());
					columnname = "Project_Number";
				}

			}
			/*
			 * if (type == "CreateProjectUDF") {
			 * 
			 * 
			 * ListIterator<UDFValue> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * UDFValue epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getUDFTypeTitle().toString());
			 * columnname="Project_Number"; }
			 * 
			 * 
			 * } if (type == "UpdateProjectUDF") {
			 * 
			 * 
			 * ListIterator<UDFValue> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * UDFValue epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getUDFTypeTitle().toString());
			 * columnname="Project_Number"; }
			 * 
			 * 
			 * }
			 */

			if (type == "CreateWBS") {

				ListIterator<WBS> projecctread = updatelist.listIterator();
				while (projecctread.hasNext()) {

					WBS epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getCode().toString());
					columnname = "DTT_Activity_Object_ID";
				}

			}

			if (type == "UpdateWBS") {

				ListIterator<WBS> projecctread = updatelist.listIterator();
				while (projecctread.hasNext()) {

					WBS epsreadelement = projecctread.next();
					processedlist.add(epsreadelement.getCode().toString());
					columnname = "DTT_Activity_Object_ID";
				}

			}
			/*
			 * if (type == "CreateWBSUDF") {
			 * 
			 * 
			 * ListIterator<UDFValue> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * UDFValue epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getUDFTypeTitle().toString());
			 * columnname="DTT_Activity_Object_ID"; }
			 * 
			 * 
			 * } if (type == "UpdateWBSUDF") {
			 * 
			 * 
			 * ListIterator<UDFValue> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * UDFValue epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getUDFTypeTitle().toString());
			 * columnname="DTT_Activity_Object_ID"; }
			 * 
			 * 
			 * }
			 */
			/*
			 * if (type == "CreateActivity") {
			 * 
			 * 
			 * ListIterator<Activity> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * Activity epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getId().toString()); }
			 * 
			 * 
			 * }
			 * 
			 * if (type == "UpdateActivity") {
			 * 
			 * 
			 * ListIterator<Activity> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * Activity epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getId().toString()); }
			 * 
			 * 
			 * } if (type == "CreateActivityCode") {
			 * 
			 * 
			 * ListIterator<ActivityCodeAssignment> projecctread =
			 * updatelist.listIterator(); while (projecctread.hasNext()) {
			 * 
			 * ActivityCodeAssignment epsreadelement = projecctread.next();
			 * processedlist
			 * .add(epsreadelement.getActivityCodeDescription().toString()); }
			 * 
			 * 
			 * } if (type == "UpdateActivityCode") {
			 * 
			 * 
			 * ListIterator<ActivityCodeAssignment> projecctread =
			 * updatelist.listIterator(); while (projecctread.hasNext()) {
			 * 
			 * ActivityCodeAssignment epsreadelement = projecctread.next();
			 * processedlist
			 * .add(epsreadelement.getActivityCodeDescription().toString()); }
			 * 
			 * 
			 * } if (type == "CreateActivityUDF") {
			 * 
			 * 
			 * ListIterator<UDFValue> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * UDFValue epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getUDFTypeTitle().toString()); }
			 * 
			 * 
			 * } if (type == "UpdateActivityUDF") {
			 * 
			 * 
			 * ListIterator<UDFValue> projecctread = updatelist.listIterator();
			 * while (projecctread.hasNext()) {
			 * 
			 * UDFValue epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getUDFTypeTitle().toString()); }
			 * 
			 * 
			 * } if (type == "CreateAssignment") {
			 * 
			 * 
			 * ListIterator<ResourceAssignment> projecctread =
			 * updatelist.listIterator(); while (projecctread.hasNext()) {
			 * 
			 * ResourceAssignment epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getRoleName().toString()); }
			 * 
			 * 
			 * } if (type == "UpdateAssignment") {
			 * 
			 * 
			 * ListIterator<ResourceAssignment> projecctread =
			 * updatelist.listIterator(); while (projecctread.hasNext()) {
			 * 
			 * ResourceAssignment epsreadelement = projecctread.next();
			 * processedlist.add(epsreadelement.getRoleName().toString()); }
			 * 
			 * 
			 * }
			 */
			processeddao.updaterecords(tablename, flag, processedlist, message,
					columnname);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
