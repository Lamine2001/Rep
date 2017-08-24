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
public class RaGraph2AvItemMapping extends AbstractRaGraph2BaAvItemMapping {
    static final Logger LOG = Logger.getLogger(RaGraph2AvItemMapping.class);

    @Override
    protected void mapUnits(final BoGraph<ResourceAssignment> inputObject,
            final AvBaItem outputObject) {

        ResourceAssignment ra = inputObject.getStartObject();
        String resourceId = null;

        try {
            outputObject.setUnits(ra.getPlannedUnits().doubleValue());
            resourceId = ra.getResourceId();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Map planned untis [%s] for Resource [%s] and Activity [%s]", ra.getPlannedUnits(),
                        ra.getResourceId(), ra.getActivityId()));
            }
        } catch (BusinessObjectException e) {
            LOG.error("Units of ResourceAssignment [Id: " + resourceId
                    + "] could not be mapped to AvBaItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

}
