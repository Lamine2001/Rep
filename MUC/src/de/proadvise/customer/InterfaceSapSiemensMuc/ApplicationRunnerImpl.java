package de.proadvise.customer.InterfaceSapSiemensMuc;

import java.io.File;

import org.apache.log4j.Logger;

import de.proadvise.customer.InterfaceSapSiemensMuc.integration.FilePeeker;
import de.proadvise.customer.InterfaceSapSiemensMuc.integration.InputFileGateway;

public class ApplicationRunnerImpl implements ApplicationRunner {
    
    private static final Logger LOG = Logger.getLogger(ApplicationRunnerImpl.class);
    
    private InputFileGateway inboundGateway;
    private FilePeeker filePeeker;

    @Override
    public void run(String... args) {
        try {
            File inputFile = filePeeker.peekFile();
            
            if (null != inputFile) {
                inboundGateway.sendFile(inputFile);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public InputFileGateway getInboundGateway() {
        return inboundGateway;
    }

    public void setInboundGateway(InputFileGateway inboundGateway) {
        this.inboundGateway = inboundGateway;
    }

    public FilePeeker getFilePeeker() {
        return filePeeker;
    }

    public void setFilePeeker(FilePeeker filePeeker) {
        this.filePeeker = filePeeker;
    }

}
