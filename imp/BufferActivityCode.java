package com.siemens.windpower.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.primavera.ws.p6.activity.Activity;
import com.primavera.ws.p6.project.Project;
import com.primavera.ws.p6.wbs.WBS;
import com.siemens.windpower.fsns.p6wsclient.PrimaveraActivity;
import com.siemens.windpower.fsns.p6wsclient.PrimaveraProject;
import com.siemens.windpower.fsns.p6wsclient.PrimaveraWBS;

public class BufferActivityCode {
	
	public void checkAndCreateMajorComponent(){
		
		try {
			PrimaveraProject projectws = new PrimaveraProject();
			PrimaveraWBS wbsws = new PrimaveraWBS();
			PrimaveraActivity actws = new PrimaveraActivity();
			
			
			List<Project> tempList = projectws.readProjectById("SMax Temp");
			Integer tempObjectId = null;
			if(tempList != null && tempList.size() > 0){
				tempObjectId = tempList.get(0).getObjectId();
			}
			List<WBS> wbslist = new ArrayList<WBS>();
			Integer turbineObjectId = null;
			if(tempObjectId != null){
				wbslist = wbsws.readWBSByWBSName(tempObjectId.toString(), "Turbine 1");
			}
			if(wbslist != null && wbslist.size() > 0){
				turbineObjectId = wbslist.get(0).getObjectId();
			}
			Integer mainComponentObjectId = null;
			List<WBS> wbslist2 = new ArrayList<WBS>();
			if(turbineObjectId != null){
				wbslist2 = wbsws.readWBSByParentIdAndWBSName(turbineObjectId.toString(), "Main Component Replacement");
			}
			if(wbslist2 != null && wbslist2.size() > 0){
				mainComponentObjectId = wbslist2.get(0).getObjectId();
			}
			List<Activity> createactivitieslist = new ArrayList<Activity>();
			List<Activity> updateactivitieslist = new ArrayList<Activity>();
			String actname = "major component replacement ";
			//List<Activity> actlist = actws.readActivitiesByName(mainComponentObjectId, actname);
			//List<Activity> actlist = actws.readActivities(mainComponentObjectId);
			Activity activity = new Activity();
			DB_Connection db = new DB_Connection();
			List<Map<String, Object>> result  = db.getData("SELECT TOP 10 Measure,Year,Value FROM dbo.FailureRates where Measure ='Major crane hours' or Measure = 'Major non-crane hours'");
			if(result != null && result.size() > 0){
				for (Map<String, Object> map : result) {
					//System.out.println(map);
					int year = (int) map.get("Year");
					double value =  (double) map.get("Value");
					//System.out.println(year+","+value);
					List<Activity> actlist = actws.readActivitiesByName(mainComponentObjectId, actname+year);
					if(actlist != null && actlist.size() > 0){
						try {
							Activity act = actlist.get(0);
							activity = new Activity();
							activity.setObjectId(act.getObjectId());
							System.out.println(act.getName()+","+act.getPlannedNonLaborUnits());
							activity.setPlannedNonLaborUnits(Double.valueOf(act.getPlannedNonLaborUnits()+value));
							updateactivitieslist.add(activity);
							actws.updateActivities(updateactivitieslist);
							updateactivitieslist = new ArrayList<Activity>();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else{
						activity = new Activity();
						activity.setName(actname+year);
						activity.setWBSObjectId(actws.constructWBSObject(mainComponentObjectId));
						activity.setPlannedNonLaborUnits(value);
						createactivitieslist.add(activity);
						actws.createActivities(createactivitieslist); 
						createactivitieslist = new ArrayList<Activity>();
					}
				}
			}
			/*if(actlist != null && actlist.size() > 0){
				try {
					Activity act = actlist.get(0);
					activity = new Activity();
					activity.setObjectId(act.getObjectId());
					//System.out.println(act.getName()+","+act.getPlannedNonLaborUnits());
					activity.setPlannedNonLaborUnits(Double.valueOf(act.getPlannedNonLaborUnits()+nonLabourUnits));
					updateactivitieslist.add(activity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				activity = new Activity();
				activity.setName(actname);
				//activity.setProjectObjectId(tempObjectId);
				activity.setWBSObjectId(actws.constructWBSObject(mainComponentObjectId));
				activity.setPlannedNonLaborUnits(nonLabourUnits);
				createactivitieslist.add(activity);
			}*/
			
			
			/*if(createactivitieslist != null && createactivitieslist.size() > 0){
				actws.createActivities(createactivitieslist); 
			}
			if(updateactivitieslist != null && updateactivitieslist.size() > 0 ){
				actws.updateActivities(updateactivitieslist);
			}*/
			//System.out.println("size:"+wbslist2.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BufferActivityCode code = new BufferActivityCode();
		code.checkAndCreateMajorComponent();
	}

}
