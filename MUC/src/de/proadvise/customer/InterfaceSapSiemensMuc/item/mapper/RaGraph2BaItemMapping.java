package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import org.apache.log4j.Logger;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.AvBaItem;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

/**
 * <p>
 * Das {@code RAGraph2BaItemMapping} mappt die Felder des {@link BoGraph
 * <ResourceAssignment>} auf das dazugehoerige {@code AvBaItem}
 * </p>
 */
public class RaGraph2BaItemMapping extends AbstractRaGraph2BaAvItemMapping {
    static final Logger LOG = Logger.getLogger(RaGraph2BaItemMapping.class);

    @Override
    protected void mapUnits(final BoGraph<ResourceAssignment> inputObject,
            final AvBaItem outputObject) {

        ResourceAssignment ra = inputObject.getStartObject();
        String resourceId = null;

        try {
            resourceId = ra.getResourceId();
            outputObject.setUnits(ra.getActualUnits().doubleValue());
        } catch (BusinessObjectException e) {
            LOG.error("Units of ResourceAssignment [Id: " + resourceId
                    + "] could not be mapped to AvBaItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

}
