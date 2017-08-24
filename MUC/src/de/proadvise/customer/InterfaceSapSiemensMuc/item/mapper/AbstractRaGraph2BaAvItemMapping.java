package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import org.apache.log4j.Logger;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.common.mapper.Mapping;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.AvBaItem;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public abstract class AbstractRaGraph2BaAvItemMapping implements Mapping<BoGraph<ResourceAssignment>, AvBaItem> {
    private static final Logger LOG = Logger.getLogger(AbstractRaGraph2BaAvItemMapping.class);

    public AbstractRaGraph2BaAvItemMapping() {
        super();
    }

    protected abstract void mapUnits(final BoGraph<ResourceAssignment> inputObject, final AvBaItem outputObject);

    @Override
    public void mapFields(final BoGraph<ResourceAssignment> inputObject, final AvBaItem outputObject) {
        mapProjectId(inputObject, outputObject);
        mapActivityId(inputObject, outputObject);
        mapActivityName(inputObject, outputObject);
        mapResourceId(inputObject, outputObject);
        mapUnits(inputObject, outputObject);
    }

    protected void mapProjectId(final BoGraph<ResourceAssignment> inputObject, final AvBaItem outputObject) {
    
        ResourceAssignment ra = inputObject.getStartObject();
        String resourceId = null;
    
        try {
            resourceId = ra.getResourceId();
            outputObject.setProjectId(ra.getProjectId());
        } catch (BusinessObjectException e) {
            LOG.error("ProjectId of ResourceAssignment [Id: " + resourceId
                    + "] could not be mapped to AvBaItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

    protected void mapActivityId(final BoGraph<ResourceAssignment> inputObject, final AvBaItem outputObject) {
    
        ResourceAssignment ra = inputObject.getStartObject();
        String resourceId = null;
    
        try {
            resourceId = ra.getResourceId();
            outputObject.setActivityId(ra.getActivityId());
        } catch (BusinessObjectException e) {
            LOG.error("ActivityId of ResourceAssignment [Id: " + resourceId
                    + "] could not be mapped to AvBaItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

    protected void mapActivityName(final BoGraph<ResourceAssignment> inputObject, final AvBaItem outputObject) {
    
        ResourceAssignment ra = inputObject.getStartObject();
        String resourceId = null;
    
        try {
            resourceId = ra.getResourceId();
            outputObject.setActivityName(ra.getActivityName());
        } catch (BusinessObjectException e) {
            LOG.error("ActivityName of ResourceAssignment [Id: " + resourceId
                    + "] could not be mapped to AvBaItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

    protected void mapResourceId(final BoGraph<ResourceAssignment> inputObject, final AvBaItem outputObject) {
    
        ResourceAssignment ra = inputObject.getStartObject();
        String resourceId = null;
    
        try {
            resourceId = ra.getResourceId();
            outputObject.setResourceId(ra.getResourceId());
        } catch (BusinessObjectException e) {
            LOG.error("ResourceId of ResourceAssignment [Id: " + resourceId
                    + "] could not be mapped to AvBaItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

}