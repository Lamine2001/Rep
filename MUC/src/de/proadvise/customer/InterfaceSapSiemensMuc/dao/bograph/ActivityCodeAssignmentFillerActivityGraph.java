package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import com.primavera.integration.client.bo.object.ActivityCodeAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.tool.batch.item.p6api.reader.BoBucketFiller;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class ActivityCodeAssignmentFillerActivityGraph implements BoBucketFiller<ActivityCodeAssignment> {

    @Override
    public void fillBucket(BoGraph<?> boGraph, BoResultSet<ActivityCodeAssignment> boResultSet) {
        ActivityGraph graph = (ActivityGraph) boGraph;

        // if code assignments are not filtered, there will be more than one code assignment
        while (boResultSet.hasNextFor(graph.getStartObject())) {
            graph.addActivityCodeAssignment(boResultSet.nextFor(graph.getStartObject()));
        }
    }
}
