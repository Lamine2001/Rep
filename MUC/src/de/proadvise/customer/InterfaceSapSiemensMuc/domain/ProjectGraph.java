package de.proadvise.customer.InterfaceSapSiemensMuc.domain;

import java.util.Collection;

import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.ProjectCodeAssignment;

import de.proadvise.tool.p6util.dao.bograph.AbstractBoGraph;
import de.proadvise.tool.p6util.dao.bograph.ProjectCodeAssignmentBucket;
import de.proadvise.tool.p6util.dao.bograph.ProjectCodeAssignmentBucketImpl;

public class ProjectGraph extends AbstractBoGraph<Project> implements ProjectCodeAssignmentBucket{
    
    private static final String PROJECT_CODE_ASSIGMENT_BUCKET_NAME = "ProjectCodeAssignment";
    
    @Override
    public void initBoBuckets() {
        registerBoBucket(PROJECT_CODE_ASSIGMENT_BUCKET_NAME, new ProjectCodeAssignmentBucketImpl());
    }

    @Override
    public void addProjectCodeAssignment(ProjectCodeAssignment arg0) {
        ((ProjectCodeAssignmentBucket) getBuckets().get(PROJECT_CODE_ASSIGMENT_BUCKET_NAME)).addProjectCodeAssignment(arg0);
    }

    @Override
    public ProjectCodeAssignment getProjectCodeAssignmentByKey(String arg0) {
        return ((ProjectCodeAssignmentBucket) getBuckets().get(PROJECT_CODE_ASSIGMENT_BUCKET_NAME)).getProjectCodeAssignmentByKey(arg0);
    }

    @Override
    public Collection<ProjectCodeAssignment> getProjectCodeAssignments() {
        return ((ProjectCodeAssignmentBucket) getBuckets().get(PROJECT_CODE_ASSIGMENT_BUCKET_NAME)).getProjectCodeAssignments();
    }

}
