package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.tool.batch.item.p6api.reader.BoBucketFiller;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class ResourceAssignmentFillerActivityGraph implements BoBucketFiller<ResourceAssignment> {

    @Override
    public void fillBucket(BoGraph<?> boGraph, BoResultSet<ResourceAssignment> boResultSet) {
        ActivityGraph graph = (ActivityGraph) boGraph;

        // if code assignments are not filtered, there will be more than one code assignment
        while (boResultSet.hasNextFor(graph.getStartObject())) {
            graph.addResourceAssignment(boResultSet.nextFor(graph.getStartObject()));
        }
    }

}
