package com.belladati.homework.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * Tapestry IoC module for application configuration and service bindings.
 * This class is instantiated by Tapestry framework via reflection based on
 * the tapestry.app-package parameter in web.xml.
 */
@SuppressWarnings("unused")
public class AppModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(BellaDatiDataService.class);
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,cs");
    }
}
