package com.belladati.homework.services;

import com.belladati.sdk.BellaDatiService;

/**
 * Factory for creating authenticated BellaDatiService instances.
 */
public interface BellaDatiServiceFactory {

    /**
     * Creates an authenticated BellaDatiService instance using xAuth.
     *
     * @param url the BellaDati instance URL
     * @param consumerKey the consumer key
     * @param consumerSecret the consumer secret
     * @param username the username
     * @param password the password
     * @return authenticated BellaDati service
     */
    BellaDatiService create(String url, String consumerKey, String consumerSecret, String username, String password);
}
