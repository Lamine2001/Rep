package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.springframework.batch.item.ItemReader;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ResourceAssignmentGraph;

public interface ResourceAssignmentGraphReader extends ItemReader<ResourceAssignmentGraph> {

    public ResourceAssignmentGraphFactory getResourceAssignmentGraphFactory();
    
    public void setResourceAssignmentGraphFactory(ResourceAssignmentGraphFactory resourceAssignmentFactory);

}
