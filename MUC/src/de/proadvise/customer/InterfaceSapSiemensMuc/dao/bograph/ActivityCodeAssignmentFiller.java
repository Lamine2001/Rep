package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import com.primavera.integration.client.bo.object.ActivityCodeAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ResourceAssignmentGraph;
import de.proadvise.tool.batch.item.p6api.reader.BoBucketFiller;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class ActivityCodeAssignmentFiller implements BoBucketFiller<ActivityCodeAssignment> {

    @Override
    public void fillBucket(BoGraph<?> boGraph, BoResultSet<ActivityCodeAssignment> boResultSet) {
        ResourceAssignmentGraph graph = (ResourceAssignmentGraph) boGraph;

        // There is a n:1 relationship between resource assignment and activity code, so a
        // NotRemovingLookAheadRelatedBoResultSet is used. This must not be used with a while-loop
        // as this would never end!
        // So, only one code can be filled into ResourceAssignmentGraph!
        if (boResultSet.hasNextFor(graph.getStartObject())) {
            graph.addActivityCodeAssignment(boResultSet.nextFor(graph.getStartObject()));
        }
    }
}
