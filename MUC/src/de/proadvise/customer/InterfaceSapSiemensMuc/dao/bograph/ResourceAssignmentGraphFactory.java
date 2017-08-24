package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ResourceAssignmentGraph;

public interface ResourceAssignmentGraphFactory {

    public ResourceAssignmentGraph createNextGraph();

    public void loadStartObject();
    
    public void reset();

}