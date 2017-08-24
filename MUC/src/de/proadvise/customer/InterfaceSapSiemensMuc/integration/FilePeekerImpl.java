package de.proadvise.customer.InterfaceSapSiemensMuc.integration;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.util.ResourceUtils;

public class FilePeekerImpl implements FilePeeker {
    private static final Logger LOG = Logger.getLogger(FilePeekerImpl.class);
    
    private MessageSource<File> fileSource;
    private String explicitFilePath;

    @Override
    public File peekFile() {
        if (isExplicitFileAvailable()) {
            return getExplicitFile();
        }
        Message<File> message = fileSource.receive();
        
        if (null == message) {
            LOG.info("No file is currently available for processing.");
            return null;
        }
        
        LOG.info("Found file " + message.getPayload().getAbsolutePath());
        return message.getPayload();
    }
    
    protected boolean isExplicitFileAvailable() {
        return null != getExplicitFilePath() && !getExplicitFilePath().isEmpty();
    }
    
    protected File getExplicitFile() {
        try {
            File file = ResourceUtils.getFile("file://" + getExplicitFilePath());
            if (file.exists()) {
                LOG.info(String.format("Found explicit file [%s]", getExplicitFilePath()));
                return file;
            } 
        } catch (FileNotFoundException e) {
            // do nothing, as the error will be logged anyway
        }
        
        LOG.error(String.format("The explicit file [%s] could not be found. Please check file location.", 
                getExplicitFilePath()));
        return null;
    }

    public MessageSource<File> getFileSource() {
        return fileSource;
    }

    public void setFileSource(MessageSource<File> fileSource) {
        this.fileSource = fileSource;
    }

    public String getExplicitFilePath() {
        return explicitFilePath;
    }

    public void setExplicitFilePath(String explicitFilePath) {
        this.explicitFilePath = explicitFilePath;
    }
}
