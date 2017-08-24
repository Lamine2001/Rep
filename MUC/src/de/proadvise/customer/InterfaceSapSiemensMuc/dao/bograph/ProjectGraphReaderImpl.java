package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.apache.log4j.Logger;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.Assert;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;

public class ProjectGraphReaderImpl implements ProjectGraphReader {
    private static final Logger LOG = Logger.getLogger(ProjectGraphReaderImpl.class);
    
    
    private ProjectGraphFactory projectGraphFactory;

    @Override
    public ProjectGraph read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException {
        Assert.notNull(projectGraphFactory, "The ProjectGraphFactory have to be set!");
        ProjectGraph graph = projectGraphFactory.createNextGraph();
        
        if (null != graph) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Read ProjectGraph from P6: [%s]", graph.getStartObject().getObjectId()));
            }
        } else {
            LOG.info("No more Projects to read.");
        }
        return graph;
    }

    @Override
    public ProjectGraphFactory getProjectGraphFactory() {
        return this.projectGraphFactory;
    }

    @Override
    public void setProjectGraphFactory(ProjectGraphFactory projectFactory) {
        this.projectGraphFactory = projectFactory;
    }


    

}
