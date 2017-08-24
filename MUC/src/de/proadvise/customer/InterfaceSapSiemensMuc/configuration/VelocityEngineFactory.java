package de.proadvise.customer.InterfaceSapSiemensMuc.configuration;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.beans.factory.FactoryBean;

public class VelocityEngineFactory implements FactoryBean<VelocityEngine>{

    @Override
    public VelocityEngine getObject() throws Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, 
                "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "de.proadvise");
        return velocityEngine;
    }

    @Override
    public Class<?> getObjectType() {
        return VelocityEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
