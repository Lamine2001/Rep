package de.proadvise.customer.InterfaceSapSiemensMuc.domain;

import java.util.Collection;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.ActivityCodeAssignment;
import com.primavera.integration.client.bo.object.ResourceAssignment;
import com.primavera.integration.client.bo.object.UDFValue;

import de.proadvise.tool.p6util.dao.bograph.AbstractBoGraph;
import de.proadvise.tool.p6util.dao.bograph.ActivityCodeAssignmentBucket;
import de.proadvise.tool.p6util.dao.bograph.ActivityCodeAssignmentBucketImpl;
import de.proadvise.tool.p6util.dao.bograph.ResourceAssignmentBucket;
import de.proadvise.tool.p6util.dao.bograph.ResourceAssignmentBucketImpl;
import de.proadvise.tool.p6util.dao.bograph.UdfValueBucket;
import de.proadvise.tool.p6util.dao.bograph.UdfValueBucketImpl;

public class ActivityGraph extends AbstractBoGraph<Activity> implements UdfValueBucket, ActivityCodeAssignmentBucket, ResourceAssignmentBucket {

    
    public final static String UDF_VALUE_BUCKET_NAME = "UdfValue"; 
    public final static String ACTIVITY_CODE_ASSIGNMENT_BUCKTE_NAME = "ActivityCodeAssignment";
    public final static String RESOURCE_ASSIGNMENT_BUCKET_NAME = "ResourceAssignment"; 
    
    @Override
    public void initBoBuckets() {
        registerBoBucket(UDF_VALUE_BUCKET_NAME, new UdfValueBucketImpl());
        registerBoBucket(ACTIVITY_CODE_ASSIGNMENT_BUCKTE_NAME, new ActivityCodeAssignmentBucketImpl());
        registerBoBucket(RESOURCE_ASSIGNMENT_BUCKET_NAME, new ResourceAssignmentBucketImpl());
    }
    
    @Override
    public void addUdfValue(UDFValue arg0) {
        ((UdfValueBucket) getBuckets().get(UDF_VALUE_BUCKET_NAME)).addUdfValue(arg0);
    }
    @Override
    public UDFValue getUdfValueByKey(String arg0) {
        return ((UdfValueBucket) getBuckets().get(UDF_VALUE_BUCKET_NAME)).getUdfValueByKey(arg0);
    }
    @Override
    public Collection<UDFValue> getUdfValues() {
        return ((UdfValueBucket) getBuckets().get(UDF_VALUE_BUCKET_NAME)).getUdfValues();
    }

    @Override
    public void addActivityCodeAssignment(ActivityCodeAssignment paramActivityCodeAssignment) {
        ((ActivityCodeAssignmentBucket) getBuckets().get(ACTIVITY_CODE_ASSIGNMENT_BUCKTE_NAME)).addActivityCodeAssignment(paramActivityCodeAssignment);
    }

    @Override
    public ActivityCodeAssignment getActivityCodeAssignmentByKey(String paramString) {
        return ((ActivityCodeAssignmentBucket) getBuckets().get(ACTIVITY_CODE_ASSIGNMENT_BUCKTE_NAME)).getActivityCodeAssignmentByKey(paramString);
    }

    @Override
    public Collection<ActivityCodeAssignment> getActivityCodeAssignments() {
        return ((ActivityCodeAssignmentBucket) getBuckets().get(ACTIVITY_CODE_ASSIGNMENT_BUCKTE_NAME)).getActivityCodeAssignments();
    }
   
    @Override
    public void addResourceAssignment(ResourceAssignment arg0) {
        ((ResourceAssignmentBucket) getBuckets().get(RESOURCE_ASSIGNMENT_BUCKET_NAME)).addResourceAssignment(arg0);
    }

    @Override
    public ResourceAssignment getResourceAssignmentByKey(String arg0) {
        return ((ResourceAssignmentBucket) getBuckets().get(RESOURCE_ASSIGNMENT_BUCKET_NAME)).getResourceAssignmentByKey(arg0);
    }

    @Override
    public Collection<ResourceAssignment> getResourceAssignments() {
        return ((ResourceAssignmentBucket) getBuckets().get(RESOURCE_ASSIGNMENT_BUCKET_NAME)).getResourceAssignments();
    }
    
    @Override
    public String toString() {
        Activity a = getStartObject();
        if (null == a) {
            return "[null]";
        }
        
        try {
            return String.format("ProjectId [%s], ActivityId [%s]", a.getProjectId(), a.getId());
        } catch (BusinessObjectException e) {
            try {
                return String.format("ProjectObjectId [%s], ActivityObjectId [%s]", a.getProjectObjectId(), a.getObjectId());
            } catch (BusinessObjectException e1) {
                return "[null]";
            }
        } 
    }

    
    

}
