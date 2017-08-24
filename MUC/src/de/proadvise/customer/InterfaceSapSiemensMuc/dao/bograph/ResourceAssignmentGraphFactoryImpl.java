package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.apache.log4j.Logger;

import com.primavera.integration.client.bo.object.ResourceAssignment;



import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ResourceAssignmentGraph;
import de.proadvise.tool.batch.item.p6api.reader.AbstractRelatedBoService;
import de.proadvise.tool.batch.item.p6api.reader.BoGraphFactory;
import de.proadvise.tool.p6util.dao.BoDao;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.P6QueryProvider;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;
import de.proadvise.tool.p6util.dao.namedparam.ConditionParameters;

public class ResourceAssignmentGraphFactoryImpl extends BoGraphFactory<ResourceAssignmentGraph> implements ResourceAssignmentGraphFactory{
    private static final Logger LOG = Logger.getLogger(ResourceAssignmentGraphFactoryImpl.class);
    
 // Main Object Load
    private BoDao<ResourceAssignment> resourceAssignmentDao;
    private BoResultSet<ResourceAssignment> resourceAssignmentResultSet;
    private ConditionParameters resourceAssignmentConditionParameters;
    private P6QueryProvider resourceAssignmentQueryProvider;
    
    /* (non-Javadoc)
     * @see de.proadvise.customer.cautool.dao.bograph.ResourceAssignmentGraphReaderFactory#createNextGraph()
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResourceAssignmentGraph createNextGraph() {
        
        if(resourceAssignmentResultSet == null){
            this.loadStartObject();
            this.doLoad();
        }
        BoGraph graph = readNextGraph();
        if (null != graph) {
            for (AbstractRelatedBoService<?> rbs : this.getRelatedBoServiceList()) {
                rbs.fillGraph(graph);
            }
            return (ResourceAssignmentGraph) graph;
        }
        return null;
    }
    
    
    protected ResourceAssignmentGraph readNextGraph() {
        if(resourceAssignmentResultSet.hasNext()){
            ResourceAssignmentGraph graph = new ResourceAssignmentGraph();
            ResourceAssignment ra = resourceAssignmentResultSet.next();
            graph.setStartObject(ra);
            graph.initBoBuckets();
            
            return graph;
        }
        return null;
    }
    
    @Override
    public void loadStartObject() {
        LOG.info("Load ResourceAssignments for GraphFactory.");
        resourceAssignmentResultSet = resourceAssignmentDao.load(resourceAssignmentQueryProvider, resourceAssignmentConditionParameters);
    }

    /**
     * Reset resourceAssignmentResultSet, damit beim naechsten Aufruf die BOs neu geladen werden.
     * 
     */
    @Override
    public void reset() {
        resourceAssignmentResultSet = null;
        for (AbstractRelatedBoService<?> service : getRelatedBoServiceList()) {
            service.setBoResultSet(null);
        }
    }

    public BoDao<ResourceAssignment> getResourceAssignmentDao() {
        return resourceAssignmentDao;
    }


    public void setResourceAssignmentDao(BoDao<ResourceAssignment> resourceAssignmentDao) {
        this.resourceAssignmentDao = resourceAssignmentDao;
    }


    public BoResultSet<ResourceAssignment> getResourceAssignmentResultSet() {
        return resourceAssignmentResultSet;
    }


    public void setResourceAssignmentResultSet(
            BoResultSet<ResourceAssignment> resourceAssignmentResultSet) {
        this.resourceAssignmentResultSet = resourceAssignmentResultSet;
    }


    public ConditionParameters getResourceAssignemntConditionParameters() {
        return resourceAssignmentConditionParameters;
    }


    public void setResourceAssignmentConditionParameters(
            ConditionParameters resourceAssignemntConditionParameters) {
        this.resourceAssignmentConditionParameters = resourceAssignemntConditionParameters;
    }


    public P6QueryProvider getResourceAssignmentQueryProvider() {
        return resourceAssignmentQueryProvider;
    }


    public void setResourceAssignmentQueryProvider(
            P6QueryProvider resourceAssignemntQueryProvider) {
        this.resourceAssignmentQueryProvider = resourceAssignemntQueryProvider;
    }
    
    
    
}
