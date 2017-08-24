package de.proadvise.customer.InterfaceSapSiemensMuc.integration;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;


public class JobResultResolver {
    private static final Logger LOG = Logger.getLogger(JobResultResolver.class);
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd-hhmmss");
    private String completionString = "OK";
    private String failedString = "NOK";
    
    public String mapResult(JobExecution payload) {
        BatchStatus status = payload.getStatus();
        String jobResult = String.format("%s-%s", BatchStatus.COMPLETED.equals(status) ? completionString : failedString, 
                dateFormat.format(new Date()));
        
        LOG.debug(String.format("Map status [%s] from job execution to result [%s].", status, jobResult));
        return jobResult;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    protected String getCompletionString() {
        return completionString;
    }

    protected void setCompletionString(String completionString) {
        this.completionString = completionString;
    }

    protected String getFailedString() {
        return failedString;
    }

    protected void setFailedString(String failedString) {
        this.failedString = failedString;
    }
}
