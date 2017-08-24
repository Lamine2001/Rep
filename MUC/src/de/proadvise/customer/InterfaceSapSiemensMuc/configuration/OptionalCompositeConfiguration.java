package de.proadvise.customer.InterfaceSapSiemensMuc.configuration;

import java.util.Collection;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class OptionalCompositeConfiguration extends CompositeConfiguration {
    private static final Logger LOG = Logger.getLogger(OptionalCompositeConfiguration.class);
    public OptionalCompositeConfiguration() {
        super();
    }
    public OptionalCompositeConfiguration(Collection<? extends Configuration> configurations) {
        super(configurations);
    }
    
    public void setConfigurationLocations(Collection<String> configurationLocations) {
        for (String location : configurationLocations) {
            if (!location.endsWith("properties")) {
                LOG.error(String.format("Only property configurations are supported. Configuration [%s] will be ignored.",
                        location));
            }
            
            try {
                AbstractConfiguration configuration = new PropertiesConfiguration(location);
                super.addConfiguration(configuration);
            } catch (ConfigurationException e) {
                LOG.warn(String.format("Configuration could not be loaded from location [%s] and will be ignored", location));
            }
        }
    }
}
