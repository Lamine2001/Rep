package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.List;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.object.Project;

import de.proadvise.tool.p6util.connection.P6SessionConsumer;
import de.proadvise.tool.p6util.dao.BoResultSet;

public interface ProjectEpsService extends P6SessionConsumer {
    public BoResultSet<Project> findByEpsObjectIds(List<ObjectId> epsObjectIds);
}
