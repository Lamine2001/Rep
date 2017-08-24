package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;
import de.proadvise.tool.batch.item.support.AbstractNestedReaderMediator;
import de.proadvise.tool.p6util.dao.namedparam.ConditionParameters;

public class ProjectResourceAssignmentGraphMediator extends AbstractNestedReaderMediator<ProjectGraph, ResourceAssignmentGraphReader> {
    private final ConditionParameters projectConditionParameters;
    
    public ProjectResourceAssignmentGraphMediator(ConditionParameters conditionParameters) {
        this.projectConditionParameters = conditionParameters;
    }

    @Override
    protected void doMediate(ProjectGraph parentProject, ResourceAssignmentGraphReader childReader) {
        this.projectConditionParameters.setWrappedContainer(parentProject.getStartObject());
        childReader.getResourceAssignmentGraphFactory().reset();
    }

}
