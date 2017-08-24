package de.proadvise.customer.InterfaceSapSiemensMuc.item;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;

import de.proadvise.tool.mail.MailService;

public class MailSenderTasklet implements Tasklet {

    private static final Logger LOG = Logger.getLogger(MailSenderTasklet.class);
    
    private MailService mailService;
    private Map<String, Object> mailContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
    
    public MailSenderTasklet() {
    }
    
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String subject = String.format("Schnittstelle %s - %s", chunkContext.getStepContext().getJobName(),
                dateFormat.format(chunkContext.getStepContext().getStepExecution().getJobExecution().getStartTime()));
        mailContext.put("mailsubject", subject);
        mailContext.put("jobname", chunkContext.getStepContext().getJobName());
        LOG.info(String.format("Set mail subject to [%s]", subject));
        
        Assert.notNull(mailService);
        try {
            LOG.info(String.format("Try to send mail for job [%s].", 
                    chunkContext.getStepContext().getJobName()));
            mailService.send();
        } catch (Exception e) {
            LOG.error("Mail could not be send. Please check stack trace.", e);
        }
        
        LOG.info("Finish Mail sending step.");
        return RepeatStatus.FINISHED;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public Map<String, Object> getMailContext() {
        return mailContext;
    }

    public void setMailContext(Map<String, Object> mailContext) {
        this.mailContext = mailContext;
    }

    public void setDateFormat(String dateFormatString) {
        this.dateFormat = new SimpleDateFormat(dateFormatString);
    }
}
