package com.belladati.homework.services;

import com.belladati.sdk.BellaDati;
import com.belladati.sdk.BellaDatiConnection;
import com.belladati.sdk.BellaDatiService;

/**
 * Default factory that creates BellaDatiService via xAuth.
 */
public class DefaultBellaDatiServiceFactory implements BellaDatiServiceFactory {

    @Override
    public BellaDatiService create(String url, String consumerKey, String consumerSecret, String username, String password) {
        BellaDatiConnection connection = BellaDati.connect(url);
        return connection.xAuth(consumerKey, consumerSecret, username, password);
    }
}
