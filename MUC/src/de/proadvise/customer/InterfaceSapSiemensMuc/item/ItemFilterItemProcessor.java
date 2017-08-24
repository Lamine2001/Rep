/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     08.10.2014 16:25:42
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
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.ActivityCode;
import com.primavera.integration.client.bo.object.ActivityCodeAssignment;
import com.primavera.integration.client.bo.object.ActivityCodeType;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.tool.p6util.dao.bograph.ActivityBucket;
import de.proadvise.tool.p6util.dao.bograph.ActivityCodeAssignmentBucket;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

/**
 * <p>
 * Der {@link ItemFilterItemProcessor} filtert Items anhand der konfigurierten
 * Parameter und gibt {@code null} zurueck, wenn das Filterkriterium im BoGraph
 * enthalten ist.
 * </p>
 * <p>
 * Gefiltert (ausselektiert) wird nach folgenden Kriterien:
 * <ul>
 * <li>{@link Activity}-Status NICHT = (Parameter
 * {@code p6.dictionary.activity.filter.status})</li>
 * <li>{@link ActivityCodeType} (Parameter
 * {@code p6.dictionary.activity_code_type.filter.label})</li>
 * <ul>
 * <li>{@link ActivityCode} NICHT = (Parameter
 * {@code p6.dictionary.activity_code_value.filter.label})</li>
 * </ul>
 * </ul>
 * </p>
 * 
 * @author proadvise GmbH
 * 
 */
public class ItemFilterItemProcessor implements
        ItemProcessor<BoGraph<ResourceAssignment>, BoGraph<ResourceAssignment>> {

    private static final Logger LOG = Logger.getLogger(ItemFilterItemProcessor.class);

    private String activityStatus;
    private String activityCodeType;
    private String activityCodeValue;

    public BoGraph<ResourceAssignment> process(BoGraph<ResourceAssignment> boGraph)
            throws Exception {

        if (skipByActivityStatus((ActivityBucket) boGraph)) {
            return null;
        }

        if (skipByActivityCodeAssignment((ActivityCodeAssignmentBucket) boGraph)) {
            return null;
        }

        return boGraph;
    }

    private boolean skipByActivityStatus(ActivityBucket boGraph) {

        if (null != activityStatus) {
            ObjectId oid = null;
            try {
                for (Activity a : boGraph.getActivities()) {
                    oid = a.getObjectId();
                    if (activityStatus.equals(String.valueOf(a.getStatus().toString()))) {
                        return false;// Don't skip
                    }
                }
                
                return true;
            } catch (BusinessObjectException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("BusinessObjectException while filtering for Activity. Skip. ObjectId:"
                            + oid);
                }
                return true;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parameter [p6.dictionary.activity.filter.status] not set. So, skip.");
        }
        return true;
    }

    private boolean skipByActivityCodeAssignment(ActivityCodeAssignmentBucket boGraph) {
        
        if ((null != activityCodeType) && (null != activityCodeValue)) {
            ObjectId caoid = null;
            try {
                for (ActivityCodeAssignment aca : boGraph.getActivityCodeAssignments()) {
                    caoid = aca.getObjectId();
                    if ((activityCodeType.equals(aca.getActivityCodeTypeName()))
                            && (activityCodeValue
                                    .equals(aca.getActivityCodeValue()))) {
                        return false;
                    }
                }
                
            } catch (BusinessObjectException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("BusinessObjectException while filtering for ActivityCodeAssignment. Skip. ObjectId:"
                            + caoid);
                }
                return true;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("BoGraph doesn't match given ActivityCodeObjectId. So, skip it.");
            }
            return true;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parameter [p6.dictionary.activity_code_type.filter.label] or its value-Parameter [p6.dictionary.activity_code_value.filter.label] not set. So, skip.");
        }
        return true;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityCodeType() {
        return activityCodeType;
    }

    public void setActivityCodeType(String activityCodeType) {
        this.activityCodeType = activityCodeType;
    }

    public String getActivityCodeValue() {
        return activityCodeValue;
    }

    public void setActivityCodeValue(String activityCodeValue) {
        this.activityCodeValue = activityCodeValue;
    }

}
