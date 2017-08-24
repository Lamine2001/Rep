/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     13.10.2014 10:14:31
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
 * Der {@link ResourceAssignmentFilterItemProcessor} filtert Elemente des
 * Graphen anhand der konfigurierten Parameter und gibt {@code null} zurueck,
 * wenn das Filterkriterium dem ausgewerteten Element des BoGraphen entspricht:
 * </p>
 * <p>
 * Gefiltert (ausselektiert) wird nach folgenden Kriterien:
 * <ul>
 * <li>{@code ActualUnits}-NICHT >0 (Parameter
 * {@code p6.dictionary.activity.filter.units})</li>
 * <li>{@link ActivityCodeType} (Parameter
 * {@code p6.dictionary.activity_code_type.filter.label})</li>
 * <ul>
 * <li>{@link ActivityCode} NICHT = (Parameter
 * {@code p6.dictionary.activity_code_value.filter.label})</li>
 * </ul>
 * </ul> Beispiel: Es sollen nur ResourceAssignments weiterverarbeitet werden,
 * die zu Aktivitaeten gehoeren, deren ActualUnits > 0 ist und die den
 * ActivityCode "Ja" fuer den ActivityCode 'Fertigungsstunden' zugewiesen haben.
 * </p>
 * 
 * @author proadvise GmbH
 * 
 */
public class ResourceAssignmentFilterItemProcessor implements
        ItemProcessor<BoGraph<ResourceAssignment>, BoGraph<ResourceAssignment>> {

    private static final Logger LOG = Logger.getLogger(ResourceAssignmentFilterItemProcessor.class);

    private Double units;
    private String activityCodeType;
    private String activityCodeValue;

    @Override
    public BoGraph<ResourceAssignment> process(BoGraph<ResourceAssignment> boGraph)
            throws Exception {

        if (skipByUnits((ActivityBucket) boGraph)) {
            return null;
        }

        if (skipByActivityCodeAssignment((ActivityCodeAssignmentBucket) boGraph)) {
            return null;
        }

        return boGraph;
    }

    private boolean skipByUnits(ActivityBucket boGraph) {

        if (null != units) {
            ObjectId oid = null;
            try {
                for (Activity a : boGraph.getActivities()) {
                    oid = a.getObjectId();
                    if (a.getActualLaborUnits().doubleValue() > units) {
                        return false;// Don't skip
                    }
                }
            } catch (BusinessObjectException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("BusinessObjectException while filtering for Activity. Skip. ObjectId:"
                            + oid);
                }
                return true;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parameter [p6.dictionary.activity.filter.units] not set. So, skip.");
        }
        return true;
    }

    private boolean skipByActivityCodeAssignment(ActivityCodeAssignmentBucket boGraph) {

        if ((null != activityCodeType) && (null != activityCodeValue)) {
            ObjectId caoid = null;
            try {
                for (ActivityCodeAssignment aca : boGraph.getActivityCodeAssignments()) {
                    caoid = aca.getObjectId();
                    if ((activityCodeType.equals(String.valueOf(aca.getActivityCodeTypeName())))
                            && (activityCodeValue
                                    .equals(String.valueOf(aca.getActivityCodeValue())))) {
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

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
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
