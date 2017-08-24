package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.BusinessObject;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.EPS;
import com.primavera.integration.client.bo.object.Project;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.EpsHierarchyFilter;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.EpsHierarchyFilterImpl;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.EpsService;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.EpsServiceImpl;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.ProjectEpsService;
import de.proadvise.customer.InterfaceSapSiemensMuc.service.ProjectEpsServiceImpl;
import de.proadvise.tool.batch.item.p6api.reader.AbstractRelatedBoService;
import de.proadvise.tool.batch.item.p6api.reader.BoGraphFactory;
import de.proadvise.tool.p6util.connection.P6SessionConsumer;
import de.proadvise.tool.p6util.connection.P6SessionFactory;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class ProjectGraphFactoryImpl extends BoGraphFactory<ProjectGraph> implements ProjectGraphFactory, P6SessionConsumer {
    private static final Logger LOG = Logger.getLogger(ProjectGraphFactoryImpl.class);

    private String likeEpsIdClause = "%CO";
    
    // Main Object Load    
    private ProjectEpsService projectService = new ProjectEpsServiceImpl();
    private EpsHierarchyFilter epsHierarchyFilter = new EpsHierarchyFilterImpl();
    private EpsService epsService = new EpsServiceImpl();
    
    private Validator<ProjectGraph> validator;
    
    private BoResultSet<Project> projectResultSet;
    
    @Override
    public ProjectGraph createNextGraph() {
        if(projectResultSet == null){
            this.loadStartObject();
            this.doLoad();
        }
        ProjectGraph graph = doCreateNextGraph();
        
        if (validator != null && graph != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validator is enabled. So, filter project graph by the validator.");
            }
            while (needsToBeFiltered((ProjectGraph) graph)) {
                graph = doCreateNextGraph();
                
                if (null == graph) {
                    break;
                }
            }
        }
        
        return graph;
    }
    
    @SuppressWarnings("unchecked")
    protected ProjectGraph doCreateNextGraph() {
        @SuppressWarnings("rawtypes")
        BoGraph graph = readNextGraph();
        
        if (null != graph) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Project graph is found. So, fill it's buckets.");
            }
            for (AbstractRelatedBoService<?> rbs : this.getRelatedBoServiceList()) {
                rbs.fillGraph(graph);
            }
            return (ProjectGraph) graph;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("No more project graph is found.");
        }
        return null;
    }

    @Override
    public void loadStartObject() {
        loadRelevantProjects();
    }
    
    protected void loadRelevantProjects() {
        LOG.info("Load several EPS elements.");
        List<EPS> epsObjects = epsService.findLikeId(this.likeEpsIdClause);
        LOG.info(epsObjects.size() + " EPS elements found and loaded.");
        List<ObjectId> epsObjectIds = extractObjectIdsFromBos(epsObjects);
        
        if (null != epsHierarchyFilter) {
            // filtering for descendants could be disabled by injecting null for epsHierarchyFilter
            LOG.info("Get all descendant EPS elements for the loaded EPS elements.");
            epsObjectIds = epsHierarchyFilter.getDescendantNodeKeysFor(epsObjectIds);
            LOG.info(epsObjectIds.size() + " descendant EPS elements found.");
        }
        
        LOG.info("Load relevant projects for EPS elements.");
        projectResultSet = projectService.findByEpsObjectIds(epsObjectIds);
        LOG.info("Relevant projects found and loaded.");
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
    
    @Override
    public void reset() {
        projectResultSet = null;
        for (AbstractRelatedBoService<?> service : getRelatedBoServiceList()) {
            service.setBoResultSet(null);
        }
    }

    @Override
    protected ProjectGraph readNextGraph() {
        if(projectResultSet.hasNext()){
            ProjectGraph graph = new ProjectGraph();
            Project project = projectResultSet.next();
            graph.setStartObject(project);
            graph.initBoBuckets();
            
            return graph;
        }
        return null;
    }
    
    protected boolean needsToBeFiltered(ProjectGraph graph) {
        if (null == graph) {
            return false;
        }
        try {
            validator.validate(graph);
        } catch (ValidationException e) {
            return true;
        }
        
        return false;
    }

    public BoResultSet<Project> getProjectResultSet() {
        return projectResultSet;
    }

    public void setProjectResultSet(BoResultSet<Project> projectResultSet) {
        this.projectResultSet = projectResultSet;
    }

    @Override
    public void setSessionFactory(P6SessionFactory sessionFactory) {
        epsHierarchyFilter.setSessionFactory(sessionFactory);
        epsService.setSessionFactory(sessionFactory);
        projectService.setSessionFactory(sessionFactory);
    }

    @Override
    public void setSessionName(String sessionName) {
        epsHierarchyFilter.setSessionName(sessionName);
        epsService.setSessionName(sessionName);
        projectService.setSessionName(sessionName);
    }

    public String getLikeEpsIdClause() {
        return likeEpsIdClause;
    }

    public void setLikeEpsIdClause(String likeEpsIdClause) {
        this.likeEpsIdClause = likeEpsIdClause;
    }

    public ProjectEpsService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectEpsService projectService) {
        this.projectService = projectService;
    }

    public EpsHierarchyFilter getEpsHierarchyFilter() {
        return epsHierarchyFilter;
    }

    public void setEpsHierarchyFilter(EpsHierarchyFilter epsHierarchyFilter) {
        this.epsHierarchyFilter = epsHierarchyFilter;
    }

    public EpsService getEpsService() {
        return epsService;
    }

    public void setEpsService(EpsService epsService) {
        this.epsService = epsService;
    }

    public Validator<ProjectGraph> getValidator() {
        return validator;
    }

    public void setValidator(Validator<ProjectGraph> validator) {
        this.validator = validator;
    }
    
}
