package de.proadvise.customer.InterfaceSapSiemensMuc.integration;

import org.apache.log4j.Logger;
import org.springframework.messaging.MessagingException;

public class ExceptionHandlerService {
    private static final Logger LOG = Logger.getLogger(ExceptionHandlerService.class);
    public void handleThrowable(MessagingException throwable) {
        LOG.error(throwable.getFailedMessage(), throwable.getCause());
    }
}
