package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;


public interface ProjectGraphFactory {
    public ProjectGraph createNextGraph();

    public void loadStartObject();
    
    public void reset();
}
