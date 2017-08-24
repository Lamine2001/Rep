package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.List;

import com.primavera.common.value.ObjectId;

import de.proadvise.tool.p6util.connection.P6SessionConsumer;

public interface EpsHierarchyFilter extends P6SessionConsumer {

    public List<ObjectId> getDescendantNodeKeysFor(List<ObjectId> keys);

}