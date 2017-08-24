package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.BusinessObject;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.UDFValue;

import de.proadvise.common.mapper.Identifying;
import de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph.ActivityGraphFactory;
import de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph.ProjectGraphFactory;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;
import de.proadvise.tool.p6util.dao.bograph.UdfValueBucket;
import de.proadvise.tool.p6util.dao.namedparam.ConditionParameters;

public class ActivityIdentifying implements Identifying<Activity> {
    private static final Logger LOG = Logger.getLogger(ActivityIdentifying.class);
    
    private String netzplannummerLabel = "UDF_ MFMCH_ sap-netzplannummer";
    private String netzplanvorgangLabel = "UDF_ MFMCH_ sap-vorgangsnummer";
    
    private ProjectGraphFactory projectGraphFactory;
    private ActivityGraphFactory activityGraphFactory;
    private ConditionParameters projectConditionParameters;
    private Project currentProject = null;
    
    private int projectCounter = 0;
    
    
    @Override
    public Activity identify(Map<String, Object> identifyingAttributes) {
        // initialize current project.
        if (null == currentProject) {
            currentProject = readProjectAndResetGraphFactory();
            if (null == currentProject) {
                LOG.info("There is no project do read from. So, no Activity could be identified.");
                return null;
            }
        }
        
        ActivityGraph activityGraph = activityGraphFactory.createNextGraph();
        while (!matchesActivityGraph(activityGraph, identifyingAttributes)) {
            
            activityGraph = activityGraphFactory.createNextGraph();
            
            while (null == activityGraph) {
                LOG.info("All activities read from the current project. So, read activities from next project.");
                currentProject = readProjectAndResetGraphFactory();
                
                if (null != currentProject) {
                    activityGraph = activityGraphFactory.createNextGraph();
                } else {
                    // all projecta have been read, so stop looping inner loop
                    break;
                }
            }
            
            if (null == activityGraph) {
                // no more activityGraph is found, all are read; so stop looping
                 break;
            }
        }
        
        if (null != activityGraph) {
            return activityGraph.getStartObject();
        }
        
        return null;
    }
    
    protected Project readProjectAndResetGraphFactory() {
        ProjectGraph projectGraph = projectGraphFactory.createNextGraph();
        if (null == projectGraph) {
            return null;
        }
        
        Project project = projectGraph.getStartObject();
        activityGraphFactory.reset();
        projectConditionParameters.setWrappedContainer(project);
        
        projectCounter++;
        
        return project;
    }
    
    protected List<ObjectId> extractObjectIdsFromBos(List<? extends BusinessObject> bos) {
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        try {
            for (BusinessObject bo : bos) {
                objectIds.add(bo.getObjectId());
            }
        } catch (BusinessObjectException e) {
            LOG.error("Could not retrieve object id of business object: " + e.getMessage());
        }
        
        return objectIds;
    }
    
    protected boolean matchesActivityGraph(UdfValueBucket graph, Map<String, Object> identifyingAttributes) {
        try {
            UDFValue netzplannummerValue = graph.getUdfValueByKey(netzplannummerLabel);
            if (null == netzplannummerValue) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("No UDFValue for netzplannummer set. So, activity doesn't match.");
                }
                return false;
            }
            UDFValue netzplanvorgangValue = graph.getUdfValueByKey(netzplanvorgangLabel);
            if (null == netzplanvorgangValue) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("No UDFValue for netzplanvorgang set. So, activity doesn't match.");
                }
                return false;
            }
            
            String netzplannummer = netzplannummerValue.getText();
            String netzplanvorgang = netzplanvorgangValue.getText();
            
            return identifyingAttributes.get("netzplannummer").equals(netzplannummer) 
                    && identifyingAttributes.get("netzplanvorgang").equals(netzplanvorgang);
        } catch (BusinessObjectException e) {
            LOG.error(String.format("UDF values for [%s] or [%s] could not be retrieved. So, activity doesn't match.",
                    netzplannummerLabel, netzplanvorgangLabel));
            return false;
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

    public ProjectGraphFactory getProjectGraphFactory() {
        return projectGraphFactory;
    }

    public void setProjectGraphFactory(ProjectGraphFactory projectGraphFactory) {
        this.projectGraphFactory = projectGraphFactory;
    }

    public ActivityGraphFactory getActivityGraphFactory() {
        return activityGraphFactory;
    }

    public void setActivityGraphFactory(ActivityGraphFactory activityGraphFactory) {
        this.activityGraphFactory = activityGraphFactory;
    }

    public ConditionParameters getProjectConditionParameters() {
        return projectConditionParameters;
    }

    public void setProjectConditionParameters(ConditionParameters projectConditionParameters) {
        this.projectConditionParameters = projectConditionParameters;
    }

    protected int getProjectCounter() {
        return projectCounter;
    }

    protected void setProjectCounter(int projectCounter) {
        this.projectCounter = projectCounter;
    }

  
}
