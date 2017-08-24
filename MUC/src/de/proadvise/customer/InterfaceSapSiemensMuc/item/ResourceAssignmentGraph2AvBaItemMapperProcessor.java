/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     09.10.2014 09:35:23
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.item;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.AvBaItem;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

/**
 * Prozessor zum Mappen von ResourceAssignmentGraph-Elementen (ist neben dem
 * Hauptobjekt ResourceAssignment, ein ActivityCodeAssignmentBucket (hier
 * obsolet) und ein ActivityBucket) auf AvBa-P6 Beans.
 * 
 * @author proadvise GmbH
 * 
 */
public class ResourceAssignmentGraph2AvBaItemMapperProcessor implements
        ItemProcessor<BoGraph<ResourceAssignment>, AvBaItem> {

    private static final Logger LOG = Logger
            .getLogger(ResourceAssignmentGraph2AvBaItemMapperProcessor.class);

    /**
     * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
     */
    @Override
    public AvBaItem process(BoGraph<ResourceAssignment> boGraph) {

        AvBaItem item = new AvBaItem();

        ResourceAssignment ra = boGraph.getStartObject();
        ObjectId raoid = null;
        try {
            item.setProjectId(ra.getProjectId());
            item.setActivityId(ra.getActivityId());
            item.setActivityName(ra.getActivityName());
            item.setResourceId(ra.getResourceId());
            item.setUnits(ra.getPlannedUnits().doubleValue());

            raoid = ra.getObjectId();
        } catch (BusinessObjectException e) {
            LOG.error("ResourceAssignment could not be mapped to AvBaItem because of BusinessObjectException. ResourceAssignment ObjectId: "
                    + raoid + "\n" + e.getMessage());
        }

        LOG.debug("Processor creates item: " + item.toString());
        return item;
    }

}
