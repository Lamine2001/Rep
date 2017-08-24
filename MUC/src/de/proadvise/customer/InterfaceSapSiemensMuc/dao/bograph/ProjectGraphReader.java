package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;

public interface ProjectGraphReader extends ItemReader<ProjectGraph> {

    public ProjectGraph read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException;
    
    public ProjectGraphFactory getProjectGraphFactory();
    
    public void setProjectGraphFactory(ProjectGraphFactory projectFactory);
    
    
}
