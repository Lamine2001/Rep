package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import java.util.Map;

import org.apache.log4j.Logger;

import com.primavera.common.value.BeginDate;
import com.primavera.common.value.EndDate;
import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.Session;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.enm.ActivityStatus;
import com.primavera.integration.client.bo.object.Activity;

import de.proadvise.common.mapper.Identifying;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.CachedActivity;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.CachedActivityService;
import de.proadvise.tool.p6util.connection.P6SessionConsumer;
import de.proadvise.tool.p6util.connection.P6SessionFactory;

public class CachedActivityIdentifying implements Identifying<Activity>, P6SessionConsumer {
    private static final Logger LOG = Logger.getLogger(CachedActivityIdentifying.class);
    
    private CachedActivityService activityService;
    
    private P6SessionFactory sessionFactory;
    private String sessionName = "";

    @Override
    public Activity identify(Map<String, Object> identifyingAttributes) {
        CachedActivity c = activityService.findByNetzplan(
                (String) identifyingAttributes.get("netzplannummer"), (String) identifyingAttributes.get("netzplanvorgang"));
        
        if (null == c) {
            return null;
        }
        
        Activity a = new Activity(getSession());
        try {
            a.setObjectId(new ObjectId(c.getObjectId()));
            a.setId(c.getId());
            a.setProjectObjectId(new ObjectId(c.getProjectObjectId())); // ProjectId can't be set, as it is read-only
            if (null != c.getWbsObjectId()) {
                a.setWBSObjectId(new ObjectId(c.getWbsObjectId()));
            } else {
                a.setWBSObjectId(null); // WBSObjectId is set to null
            }
            a.setStatus(ActivityStatus.getActivityStatus(c.getStatus()));
            a.setStartDate(new BeginDate(c.getStartDate()));
            
            if (null != c.getActualStartDate()) {
                a.setActualStartDate(new BeginDate(c.getActualFinishDate()));
            } else {
                a.setActualStartDate(null); // ActualStartDate is set to null
            }
            
            a.setFinishDate(new EndDate(c.getFinishDate()));
            if (null != c.getActualFinishDate()) {
                a.setActualFinishDate(new EndDate(c.getActualFinishDate()));
            } else {
                a.setActualFinishDate(null); // ActualFinishDate is set to null
            }
            
        } catch (BusinessObjectException e) {
            LOG.error("A BusinessObjectException has been thrown while setting fields. Skip object. " + e.getMessage());
            return null;
        }
        
        return a;
    }

    public CachedActivityService getActivityService() {
        return activityService;
    }

    public void setActivityService(CachedActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public void setSessionFactory(P6SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    
    public Session getSession() {
        return null == this.sessionFactory ? null : this.sessionFactory.getSession(this.sessionName);
    }
}
