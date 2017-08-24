package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;
import de.proadvise.tool.batch.item.support.AbstractNestedReaderMediator;
import de.proadvise.tool.p6util.dao.namedparam.ConditionParameters;

public class ProjectActivityGraphMediator extends AbstractNestedReaderMediator<ProjectGraph, ActvityGraphReader> {
    private final ConditionParameters projectConditionParameters;
    
    public ProjectActivityGraphMediator(ConditionParameters conditionParameters) {
        this.projectConditionParameters = conditionParameters;
    }

    @Override
    protected void doMediate(ProjectGraph parentProject, ActvityGraphReader childReader) {
        this.projectConditionParameters.setWrappedContainer(parentProject.getStartObject());
        childReader.getActvityGraphFactory().reset();
    }

}
