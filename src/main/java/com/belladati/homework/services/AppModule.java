package com.belladati.homework.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * Tapestry IoC module for application configuration and service bindings.Ne
 * This class is instantiated by Tapestry framework via reflection based on
 * the tapestry.app-package parameter in web.xml.
 */
@SuppressWarnings("unused")
public class AppModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(BellaDatiDataService.class);
        binder.bind(BellaDatiServiceFactory.class, DefaultBellaDatiServiceFactory.class);
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,cs");

        configuration.add("belladati.url", getConfigValue("belladati.url", "BELLADATI_URL"));
        configuration.add("belladati.consumerKey", getConfigValue("belladati.consumerKey", "BELLADATI_CONSUMER_KEY"));
        configuration.add("belladati.consumerSecret", getConfigValue("belladati.consumerSecret", "BELLADATI_CONSUMER_SECRET"));
        configuration.add("belladati.username", getConfigValue("belladati.username", "BELLADATI_USERNAME"));
        configuration.add("belladati.password", getConfigValue("belladati.password", "BELLADATI_PASSWORD"));
        configuration.add("belladati.datasetId", getConfigValue("belladati.datasetId", "BELLADATI_DATASET_ID"));
    }

    private static String getConfigValue(String systemProperty, String envVar) {
        String value = System.getProperty(systemProperty);
        if (value == null || value.isBlank()) {
            value = System.getenv(envVar);
        }
        return value == null ? "" : value;
    }
}
