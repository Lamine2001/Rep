/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     01.10.2014 15:30:15
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.domain;

/**
 * Repraesentiert eine Zeile einer ausgelesenen Datei
 * 
 * @author proadvise GmbH
 * 
 */
public class AvBaItem {

    private String projectId;
    private String activityId;
    private String activityName;
    private String resourceId;
    private Double units;

    public String toString() {
        return "Item [Project ID = " + projectId + ", Activity ID = " + activityId
                + ", Activity Name = " + activityName + ", Resource ID = " + resourceId
                + ", Units = " + units + "]";
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

}
