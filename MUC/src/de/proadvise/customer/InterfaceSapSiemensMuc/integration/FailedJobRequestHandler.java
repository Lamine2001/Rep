package de.proadvise.customer.InterfaceSapSiemensMuc.integration;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.util.ResourceUtils;

public class FailedJobRequestHandler {
    private static final Logger LOG = Logger.getLogger(FailedJobRequestHandler.class);
    private String fileParameterName = "input.file";
    
    public File processFailedJobRequest(JobLaunchRequest jobLaunchRequest) {
        LOG.error(String.format("The job request for job [%s] has been failed with parameters: [%s]", 
                jobLaunchRequest.getJob().getName(), jobLaunchRequest.getJobParameters().toString()));
        
        String filePath = jobLaunchRequest.getJobParameters().getString(fileParameterName);
        
        if (null != filePath) {
            try {
                return ResourceUtils.getFile(filePath);
            } catch (FileNotFoundException e) {
                LOG.error(String.format("The file [%s] could not be found and therefore not be returned from FailedJobRequest.", 
                        filePath));
            }
        }
        
        LOG.error("There is no input file in the job request.");
        throw new IllegalStateException("The is no input file in the job request");
    }

    public String getFileParameterName() {
        return fileParameterName;
    }

    public void setFileParameterName(String fileParameterName) {
        this.fileParameterName = fileParameterName;
    }
}
