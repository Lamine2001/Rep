package de.proadvise.customer.InterfaceSapSiemensMuc.protocol;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;

import com.primavera.integration.client.bo.BusinessObject;

import de.proadvise.tool.batch.protocol.ProtocolImpl;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

/**
 * Eine spezialisierte Protocol-Klasse. 
 * 
 * Waehrend des Prozessierens wird gegenueber der Standardimplementierung zusaetzlich manuell eine konfigurierte 
 * Protokoll-Nachricht mit der Anzahl der verarbeiteten ResourceAssignments und Projekt-Eigenschaften ausgegeben.
 * 
 * Wichtiger Hinweis:
 * Wird die Reihenfolge der konfigurierten Messages geaendert oder eine neue Protocol-Messages-Properties-Datei
 * konfiguriert, wird diese Klasse waehrend der Verarbeitung entweder eine falsche oder gar keine Message ausgeben!
 * 
 * @author proadvise GmbH
 *
 */
public class JobProtocol extends ProtocolImpl {
    
    private static final Logger LOG = Logger.getLogger(JobProtocol.class);
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void beforeJob(JobExecution execution) {
        if (LOG.isDebugEnabled()) {
            try {
                LOG.info("protocol file: " + getProtocolResource().getFile().getAbsolutePath());
            } catch (IOException e) { }
        }
        
        execution.getExecutionContext().put("job.name", execution.getJobInstance().getJobName());
        execution.getExecutionContext().put("job.params", execution.getJobParameters());
        execution.getExecutionContext().put("job.start", dateFormat.format(execution.getStartTime()));
        
        super.beforeJob(execution);
    }
    
    @Override
    public void afterProcess(Object input, Object output) {
        if ((input instanceof BusinessObject || input instanceof BoGraph<?>) && null == output) {
            // ignore filter of exported business objects
        } else {
            super.afterProcess(input, output);
        }
    }
    
    @Override
    public void afterJob(JobExecution execution) {
        execution.getExecutionContext().put("job.finish", dateFormat.format(execution.getEndTime()));
        super.afterJob(execution);
    }
    
}
