package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.apache.log4j.Logger;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.Assert;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;

public class ActivityGraphReaderImpl implements ActvityGraphReader {
    private static final Logger LOG = Logger.getLogger(ActivityGraphReaderImpl.class);
    
    
    private ActivityGraphFactory activityGraphFactory;

    @Override
    public ActivityGraph read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException {
        Assert.notNull(activityGraphFactory, "The ActivityGraphFactory have to be set!");
        ActivityGraph graph = activityGraphFactory.createNextGraph();
        
        if (null != graph) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Read ActivityGraph from P6: [%s]", graph.getStartObject().getObjectId()));
            }
        } else {
            LOG.info("No more Projects to read.");
        }
        return graph;
    }

    @Override
    public ActivityGraphFactory getActvityGraphFactory() {
        return this.activityGraphFactory;
    }

    @Override
    public void setActvityGraphFactory(ActivityGraphFactory activityFactory) {
        this.activityGraphFactory = activityFactory;
    }


    

}
