package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.List;

import com.primavera.integration.client.bo.object.EPS;

import de.proadvise.tool.p6util.connection.P6SessionConsumer;

public interface EpsService extends P6SessionConsumer {
    public EPS findById(String id);
    public List<EPS> findLikeId(String likeIdClause);
}
