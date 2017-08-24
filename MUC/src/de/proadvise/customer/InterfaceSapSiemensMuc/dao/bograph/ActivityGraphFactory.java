package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;

public interface ActivityGraphFactory {

    public abstract ActivityGraph createNextGraph();

    public abstract void loadStartObject();

    public abstract void reset();

}