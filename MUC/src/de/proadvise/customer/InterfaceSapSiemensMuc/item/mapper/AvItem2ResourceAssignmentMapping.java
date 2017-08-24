package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import org.apache.log4j.Logger;

import com.primavera.common.value.ObjectId;
import com.primavera.common.value.Unit;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.common.mapper.Mapping;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.AvBaItem;

/**
 * <p>
 * Das {@code AvItem2ResourceAssignmentMapping} mappt die Felder des
 * {@link AvBaItem} auf das dazugehoerige {@code ResourceAssignment}
 * </p>
 */
public class AvItem2ResourceAssignmentMapping implements Mapping<AvBaItem, ResourceAssignment> {

    private static final Logger LOG = Logger.getLogger(AvItem2ResourceAssignmentMapping.class);

    @Override
    public void mapFields(final AvBaItem inputObject, final ResourceAssignment outputObject) {

        mapUnit(inputObject, outputObject);
    }

    protected void mapUnit(final AvBaItem inputObject, final ResourceAssignment outputObject) {

        ObjectId raOid = null;
        try {
            raOid = outputObject.getObjectId();

            if (null != inputObject.getUnits()) {
                outputObject.setPlannedUnits(new Unit(inputObject.getUnits()));
            }
        } catch (BusinessObjectException e) {
            LOG.error("Units of AvBaItem [Id: " + inputObject.getResourceId()
                    + "] could not be mapped to ResourceAssignment [ObjectId: " + raOid
                    + "] because of BusinessObjectException: " + e.getMessage());
        }
    }

}
