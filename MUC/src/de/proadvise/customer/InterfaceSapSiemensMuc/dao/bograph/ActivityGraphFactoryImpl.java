package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.apache.log4j.Logger;

import com.primavera.integration.client.bo.object.Activity;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.tool.batch.item.p6api.reader.AbstractRelatedBoService;
import de.proadvise.tool.batch.item.p6api.reader.BoGraphFactory;
import de.proadvise.tool.p6util.dao.BoDao;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.P6QueryProvider;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;
import de.proadvise.tool.p6util.dao.namedparam.ConditionParameters;

public class ActivityGraphFactoryImpl extends BoGraphFactory<ActivityGraph> implements ActivityGraphFactory {
    private static final Logger LOG = Logger.getLogger(ActivityGraphFactoryImpl.class);
    
    //MainObject
    private BoDao<Activity> activityDao;
    private BoResultSet<Activity> activityResultSet;
    private ConditionParameters activityConditionParameters;
    private P6QueryProvider activityQueryProvider;
    
    
    /* (non-Javadoc)
     * @see de.proadvise.customer.InterfaceSapSiemensMuc.dao.graph.ActivityGraphFactory#createNextGraph()
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActivityGraph createNextGraph() {
        if(activityResultSet == null){
            this.loadStartObject();
            this.doLoad();
        }
        @SuppressWarnings("rawtypes")
        BoGraph graph = readNextGraph();
        if (null != graph) {
            for (AbstractRelatedBoService<?> rbs : this.getRelatedBoServiceList()) {
                rbs.fillGraph(graph);
            }
            return (ActivityGraph) graph;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see de.proadvise.customer.InterfaceSapSiemensMuc.dao.graph.ActivityGraphFactory#loadStartObject()
     */
    @Override
    public void loadStartObject() {
        LOG.info("Load Activitys for GraphFactory.");
        activityResultSet = activityDao.load(activityQueryProvider, activityConditionParameters);
        
    }

    @Override
    protected ActivityGraph readNextGraph() {
        if(activityResultSet.hasNext()){
            ActivityGraph graph = new ActivityGraph();
            Activity ra = activityResultSet.next();
            graph.setStartObject(ra);
            graph.initBoBuckets();
            
            return graph;
        }
        return null;
        
    }

    /* (non-Javadoc)
     * @see de.proadvise.customer.InterfaceSapSiemensMuc.dao.graph.ActivityGraphFactory#reset()
     */
    @Override
    public void reset(){
        activityResultSet = null;
        for (AbstractRelatedBoService<?> service : getRelatedBoServiceList()) {
            service.setBoResultSet(null);
        }
    }

    public BoDao<Activity> getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(BoDao<Activity> activityDao) {
        this.activityDao = activityDao;
    }

    public ConditionParameters getActivityConditionParameters() {
        return activityConditionParameters;
    }

    public void setActivityConditionParameters(ConditionParameters activityConditionParameters) {
        this.activityConditionParameters = activityConditionParameters;
    }

    public P6QueryProvider getActivityQueryProvider() {
        return activityQueryProvider;
    }

    public void setActivityQueryProvider(P6QueryProvider activityQueryProvider) {
        this.activityQueryProvider = activityQueryProvider;
    }

    public BoResultSet<Activity> getActivityResultSet() {
        return activityResultSet;
    }

    public void setActivityResultSet(BoResultSet<Activity> activityResultSet) {
        this.activityResultSet = activityResultSet;
    }

}
