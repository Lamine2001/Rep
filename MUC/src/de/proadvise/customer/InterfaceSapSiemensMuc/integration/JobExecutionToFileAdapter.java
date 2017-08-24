package de.proadvise.customer.InterfaceSapSiemensMuc.integration;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.util.ResourceUtils;

public class JobExecutionToFileAdapter {
    private static final Logger LOG = Logger.getLogger(JobExecutionToFileAdapter.class);
    
    private String fileParameterName = "input.file";
    
    public File adapt(JobExecution jobExecution) throws FileNotFoundException {
        String fileName = jobExecution.getJobParameters().getString(fileParameterName);
        LOG.debug(String.format("Extract file [%s] from job parameters.", fileName));
        
        LOG.info(String.format("Job [%s] finished with status [%s] for file [%s].", jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(), fileName));
        
        return ResourceUtils.getFile(fileName);
    }

    public String getFileParameterName() {
        return fileParameterName;
    }

    public void setFileParameterName(String fileParameterName) {
        this.fileParameterName = fileParameterName;
    }
}
