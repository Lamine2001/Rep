package de.proadvise.customer.InterfaceSapSiemensMuc;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.ClassUtils;


@Configuration
@ImportResource("classpath:integration-context.xml")
public class InterfaceSapSiemensMucApplication {
    private static final Logger LOG = Logger.getLogger(InterfaceSapSiemensMucApplication.class);
    
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(InterfaceSapSiemensMucApplication.class);
        application.setRegisterShutdownHook(false);
        
        ConfigurableApplicationContext context = application.run(args);
        
        ApplicationRunner appRunner = context.getBean("applicationRunner", ApplicationRunnerImpl.class);
        appRunner.run(args);
        
        LOG.info(String.format("Exit %s.", ClassUtils.getShortName(InterfaceSapSiemensMucApplication.class)));
        context.close();
        System.exit(0);
    }
}
