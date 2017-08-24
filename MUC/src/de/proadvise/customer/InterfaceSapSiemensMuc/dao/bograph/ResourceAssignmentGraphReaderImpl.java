package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.apache.log4j.Logger;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.Assert;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ResourceAssignmentGraph;

public class ResourceAssignmentGraphReaderImpl implements ResourceAssignmentGraphReader {
    private static final Logger LOG = Logger.getLogger(ResourceAssignmentGraphReaderImpl.class);
    
    
    private ResourceAssignmentGraphFactory resourceAssignmnetGraphFactory;

    @Override
    public ResourceAssignmentGraph read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException {
        Assert.notNull(resourceAssignmnetGraphFactory, "The ActivityGraphFactory have to be set!");
        ResourceAssignmentGraph graph = resourceAssignmnetGraphFactory.createNextGraph();
        
        if (null != graph) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Read ResourceAssignmentGraph from P6: [%s]", graph.getStartObject().getObjectId()));
            }
        } 
        
        return graph;
    }

    @Override
    public ResourceAssignmentGraphFactory getResourceAssignmentGraphFactory() {
        return this.resourceAssignmnetGraphFactory;
    }

    @Override
    public void setResourceAssignmentGraphFactory(ResourceAssignmentGraphFactory resourceAssignmnetGraphFactory) {
        this.resourceAssignmnetGraphFactory = resourceAssignmnetGraphFactory;
    }


    

}
