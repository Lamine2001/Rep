package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import com.primavera.integration.client.bo.object.ProjectCodeAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;
import de.proadvise.tool.batch.item.p6api.reader.BoBucketFiller;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class ProjectCodeAssignmentFiller implements BoBucketFiller<ProjectCodeAssignment> {

    @Override
    public void fillBucket(BoGraph<?> boGraph, BoResultSet<ProjectCodeAssignment> boResultSet) {
        ProjectGraph graph = (ProjectGraph) boGraph;

        // there is more than one project code value for the project. So, use while-loop.
        while (boResultSet.hasNextFor(graph.getStartObject())) {
            graph.addProjectCodeAssignment(boResultSet.nextFor(graph.getStartObject()));
        }
    }
}
