package de.proadvise.customer.InterfaceSapSiemensMuc.domain;

import java.util.Collection;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.ActivityCodeAssignment;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.tool.p6util.dao.bograph.AbstractBoGraph;
import de.proadvise.tool.p6util.dao.bograph.ActivityBucket;
import de.proadvise.tool.p6util.dao.bograph.ActivityBucketImpl;
import de.proadvise.tool.p6util.dao.bograph.ActivityCodeAssignmentBucket;
import de.proadvise.tool.p6util.dao.bograph.ActivityCodeAssignmentBucketImpl;

/**
 * Dieser Graph stellt das Object-Modell der Komponente AV-P6 dar.<br>
 * Graph: (LookAheadNotRemovingRelatedBoResultSet)<br>
 * StartObject: ResourceAssignment (order by ActivityObjectId)<br>
 * ActivityCodeAssignmentBucket (n:1) (order by ActivityObjctId)<br>
 * ActivityBucket (n:1) (order by ObjectId)<br>
 * 
 * 
 */
public class ResourceAssignmentGraph extends AbstractBoGraph<ResourceAssignment> implements
        ActivityCodeAssignmentBucket, ActivityBucket {

    private static final String ACTIVITY_CODE_ASSIGMENT_BUCKET_NAME = "ActivityCodeAssignment";
    private static final String ACTIVITY_BUCKET_NAME = "Activity";

    @Override
    public void initBoBuckets() {
        registerBoBucket(ACTIVITY_CODE_ASSIGMENT_BUCKET_NAME,
                new ActivityCodeAssignmentBucketImpl());
        registerBoBucket(ACTIVITY_BUCKET_NAME, new ActivityBucketImpl());
    }

    @Override
    public void addActivityCodeAssignment(ActivityCodeAssignment arg0) {
        ((ActivityCodeAssignmentBucket) getBuckets().get(ACTIVITY_CODE_ASSIGMENT_BUCKET_NAME))
                .addActivityCodeAssignment(arg0);
    }

    @Override
    public ActivityCodeAssignment getActivityCodeAssignmentByKey(String arg0) {
        return ((ActivityCodeAssignmentBucket) getBuckets()
                .get(ACTIVITY_CODE_ASSIGMENT_BUCKET_NAME)).getActivityCodeAssignmentByKey(arg0);
    }

    @Override
    public Collection<ActivityCodeAssignment> getActivityCodeAssignments() {
        return ((ActivityCodeAssignmentBucket) getBuckets()
                .get(ACTIVITY_CODE_ASSIGMENT_BUCKET_NAME)).getActivityCodeAssignments();
    }

    @Override
    public void addActivity(Activity item) {
        ((ActivityBucket) getBuckets().get(ACTIVITY_BUCKET_NAME)).addActivity(item);
    }

    @Override
    public Collection<Activity> getActivities() {
        return ((ActivityBucket) getBuckets().get(ACTIVITY_BUCKET_NAME)).getActivities();
    }

    @Override
    public Activity getActivityByKey(String key) {
        return ((ActivityBucket) getBuckets().get(ACTIVITY_BUCKET_NAME)).getActivityByKey(key);
    }
    
    @Override
    public String toString() {
        ResourceAssignment ra = getStartObject();
        if (null == ra) {
            return "[null]";
        }
        
        try {
            return String.format("ProjectId [%s], ActivityId [%s], ResourceId [%s]", ra.getProjectId(), 
                    ra.getActivityId(), ra.getResourceId());
        } catch (BusinessObjectException e) {
            try {
                return String.format("ProjectObjectId [%s], ActivityObjectI [%s], ResourceObjectId [%s]",
                        ra.getProjectObjectId(), ra.getActivityObjectId(), ra.getResourceObjectId());
            } catch (BusinessObjectException e1) {
                return "[null]";
            }
        }
    }

}
