package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import com.primavera.integration.client.bo.object.Activity;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ResourceAssignmentGraph;
import de.proadvise.tool.batch.item.p6api.reader.BoBucketFiller;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class ActivityFiller implements BoBucketFiller<Activity> {

    @Override
    public void fillBucket(BoGraph<?> boGraph, BoResultSet<Activity> boResultSet) {
        
        ResourceAssignmentGraph graph = (ResourceAssignmentGraph) boGraph;
        
        // There is only one project for ResourceAssignment. So use is block!
        if (boResultSet.hasNextFor(graph.getStartObject())) {
            graph.addActivity(boResultSet.nextFor(graph.getStartObject()));
        }
    }
}
