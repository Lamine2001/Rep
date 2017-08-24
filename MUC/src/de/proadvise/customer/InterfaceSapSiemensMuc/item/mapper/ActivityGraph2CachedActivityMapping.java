package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import org.apache.log4j.Logger;

import com.primavera.integration.client.bo.BusinessObjectException;

import de.proadvise.common.mapper.Mapping;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.CachedActivity;

public class ActivityGraph2CachedActivityMapping implements Mapping<ActivityGraph, CachedActivity> {
    private static final Logger LOG = Logger.getLogger(ActivityGraph2CachedActivityMapping.class);
    
    private String netzplannummerLabel = "UDF_ MFMCH_ sap-netzplannummer";
    private String netzplanvorgangLabel = "UDF_ MFMCH_ sap-vorgangsnummer";

    @Override
    public void mapFields(final ActivityGraph activityGraph, final CachedActivity a) {
        try {
            a.setObjectId(activityGraph.getStartObject().getObjectId().toInteger());
            a.setId(activityGraph.getStartObject().getId());
            a.setProjectObjectId(activityGraph.getStartObject().getProjectObjectId().toInteger());
            a.setProjectId(activityGraph.getStartObject().getProjectId());
            if (null == activityGraph.getStartObject().getWBSObjectId()) {
                a.setWbsObjectId(null);
            } else {
                a.setWbsObjectId(activityGraph.getStartObject().getWBSObjectId().toInteger());
            }
            a.setStatus(activityGraph.getStartObject().getStatus().getDescription());
            a.setNetzplanNummer(activityGraph.getUdfValueByKey(getNetzplannummerLabel()).getText());
            a.setNetzplanVorgang(activityGraph.getUdfValueByKey(getNetzplanvorgangLabel()).getText());
        } catch (BusinessObjectException e) {
            LOG.error("A BusinessObjectException has been thrown while mapping ActivityGraph to CachedActivity: " + e.getMessage());
        }
            
        try {
            a.setStartDate(activityGraph.getStartObject().getStartDate());
        } catch (BusinessObjectException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("StartDate will be set to null");
            }
            a.setStartDate(null);
        }
        
        try {
            a.setActualStartDate(activityGraph.getStartObject().getActualStartDate());
        } catch (BusinessObjectException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("ActualStartDate will be set to null");
            }
            a.setActualStartDate(null);
        }
        
        try {
            a.setFinishDate(activityGraph.getStartObject().getFinishDate());
        } catch (BusinessObjectException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("FinishDate will be set to null");
            }
            a.setFinishDate(null);
        }
        
        try {
            a.setActualFinishDate(activityGraph.getStartObject().getActualFinishDate());
        } catch (BusinessObjectException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("ActualFinishDate will be set to null");
            }
            a.setActualFinishDate(null);
        }
            
    }
    public String getNetzplannummerLabel() {
        return netzplannummerLabel;
    }

    public void setNetzplannummerLabel(String netzplannummerLabel) {
        this.netzplannummerLabel = netzplannummerLabel;
    }

    public String getNetzplanvorgangLabel() {
        return netzplanvorgangLabel;
    }

    public void setNetzplanvorgangLabel(String netzplanvorgangLabel) {
        this.netzplanvorgangLabel = netzplanvorgangLabel;
    }

}
