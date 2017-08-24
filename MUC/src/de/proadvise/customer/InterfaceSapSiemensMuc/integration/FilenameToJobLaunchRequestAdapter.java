package de.proadvise.customer.InterfaceSapSiemensMuc.integration;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;

@MessageEndpoint
public class FilenameToJobLaunchRequestAdapter implements InitializingBean {
    private static final Logger LOG = Logger.getLogger(FilenameToJobLaunchRequestAdapter.class);
    
    private JobRegistry jobRegistry;
    
    private Map<String, String> file2JobNameMap = new HashMap<String, String>();

    private String exportUpdateDatePattern = ".*ab([0-9]{6}).*";

    @ServiceActivator
    public JobLaunchRequest adapt(File file) throws NoSuchJobException {
        String filename = file.getName();
        String jobName = mapJobName(filename);
        
        Job job = null;
        try {
            job = jobRegistry.getJob(jobName);
        } catch (NoSuchJobException e) {
            LOG.error(String.format("No such job is registered for name [%s].", jobName));
            return null;
        }
        
        JobParameters jobParameters = buildJobParametersForFile(file);
        
        jobParameters = new JobParametersBuilder(jobParameters).addLong("run.date", new Date().getTime()).toJobParameters();
        
        LOG.info(String.format("Found job [%s] for file [%s]", job.getName(), filename));
        
        return new JobLaunchRequest(job, jobParameters);
    }

    protected String mapJobName(String fileName) throws NoSuchJobException {
        for (Entry<String, String> mapping : file2JobNameMap.entrySet()) {
            if (PatternMatchUtils.simpleMatch(mapping.getKey(), fileName)) {
                return mapping.getValue();
            }
        }
        
        throw new NoSuchJobException(String.format("No job name found for file [%s].", fileName)); 
    }

    protected JobParameters buildJobParametersForFile(File file) {
        String filePath = file.getAbsolutePath();
        
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                .addString("input.file", filePath);
        
        String fileName = file.getName();
        if (null != fileName && fileName.matches(exportUpdateDatePattern)) {
            String exportUpdateDateString = fileName.replaceAll(exportUpdateDatePattern, "$1");
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("File [%s] matches pattern [%s]. So, add parameter [%s] with value [%s].",
                        fileName, exportUpdateDatePattern, "input.exportUpdateDate", exportUpdateDateString));
            }
            jobParametersBuilder.addString("input.exportUpdateDate", exportUpdateDateString);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("File [%s] doesn't match pattern [%s].", fileName, exportUpdateDatePattern));
            }
        }
        
        return jobParametersBuilder.toJobParameters();
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.jobRegistry, "A job registry must be provided.");
        Assert.notEmpty(file2JobNameMap, "A file to job name map must be provided.");
    }

    public JobRegistry getJobRegistry() {
        return jobRegistry;
    }

    public void setJobRegistry(JobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }

    public Map<String, String> getFile2JobNameMap() {
        return file2JobNameMap;
    }

    public void setFile2JobNameMap(Map<String, String> file2JobNameMap) {
        this.file2JobNameMap = file2JobNameMap;
    }

    public String getExportUpdateDatePattern() {
        return exportUpdateDatePattern;
    }

    public void setExportUpdateDatePattern(String exportUpdateDatePattern) {
        this.exportUpdateDatePattern = exportUpdateDatePattern;
    }
}
