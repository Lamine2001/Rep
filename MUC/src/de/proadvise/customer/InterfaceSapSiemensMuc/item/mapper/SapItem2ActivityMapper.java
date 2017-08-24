package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import java.util.HashMap;
import java.util.Map;

import com.primavera.integration.client.bo.object.Activity;

import de.proadvise.common.mapper.AbstractMapper;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.SapItem;

public class SapItem2ActivityMapper extends AbstractMapper<SapItem, Activity> {

    @Override
    protected Map<String, Object> extractIdentifyingAttributes(SapItem inputObject) {

        Map<String, Object> identifiyingAttributes = new HashMap<String, Object>();
        identifiyingAttributes.put("netzplannummer", inputObject.getNetzplannummer());
        identifiyingAttributes.put("netzplanvorgang", inputObject.getNetzplanvorgang());

        return identifiyingAttributes;
    }

}
