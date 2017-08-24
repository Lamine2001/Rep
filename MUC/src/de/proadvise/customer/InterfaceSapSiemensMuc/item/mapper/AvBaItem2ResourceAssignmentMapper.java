package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import java.util.HashMap;
import java.util.Map;

import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.common.mapper.AbstractMapper;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.AvBaItem;

public class AvBaItem2ResourceAssignmentMapper extends AbstractMapper<AvBaItem, ResourceAssignment> {

    @Override
    protected Map<String, Object> extractIdentifyingAttributes(AvBaItem inputObject) {

        Map<String, Object> identifiyingAttributes = new HashMap<String, Object>();
        identifiyingAttributes.put("projectId", inputObject.getProjectId());
        identifiyingAttributes.put("activityId", inputObject.getActivityId());
        identifiyingAttributes.put("resourceId", inputObject.getResourceId());

        return identifiyingAttributes;
    }

}
