package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;


public interface ActvityGraphReader extends ItemReader<ActivityGraph> {

    public ActivityGraph read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException;
    
    public ActivityGraphFactory getActvityGraphFactory();
    
    public void setActvityGraphFactory(ActivityGraphFactory activityFactory);
    
    
}
