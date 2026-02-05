package com.belladati.homework.services;

import com.belladati.sdk.BellaDati;
import com.belladati.sdk.BellaDatiConnection;
import com.belladati.sdk.BellaDatiService;
import com.belladati.sdk.dataset.data.DataColumn;
import com.belladati.sdk.dataset.data.DataRow;
import com.belladati.sdk.impl.BellaDatiClient;
import com.belladati.sdk.impl.BellaDatiServiceImpl;
import com.belladati.sdk.impl.TokenHolder;
import com.belladati.sdk.util.PaginatedIdList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Service for communication with BellaDati API.
 * Handles authentication and data operations for the Sample List dataset.
 */
public class BellaDatiDataService {

    private static final Logger LOG = LoggerFactory.getLogger(BellaDatiDataService.class);

    private static final String BELLADATI_URL = "https://belladati-demo.belladati.com/";
    private static final String CONSUMER_KEY = "demoKey";
    private static final String CONSUMER_SECRET = "demoSecret";
    private static final String USERNAME = "domain14";
    private static final String PASSWORD = "rn7iQzQ87M";
    private static final String DATASET_ID = "576";

    // Column codes for the dataset
    public static final String COL_ID = "L_ID";
    public static final String COL_NAME = "L_NAME";
    public static final String COL_EMAIL = "L_EMAIL";
    public static final String COL_ROLE = "L_ROLE";
    public static final String COL_STATUS = "L_STATUS";

    private BellaDatiService service;

    public BellaDatiDataService() {
        connect();
    }

    /**
     * Establishes connection to BellaDati using xAuth.
     *
     * @throws RuntimeException if connection fails
     */
    private void connect() {
        try {
            LOG.info("Connecting to BellaDati at {}", BELLADATI_URL);
            BellaDatiConnection connection = BellaDati.connect(BELLADATI_URL);
            this.service = connection.xAuth(CONSUMER_KEY, CONSUMER_SECRET, USERNAME, PASSWORD);
            LOG.info("Successfully connected to BellaDati");
        } catch (Exception e) {
            LOG.error("Failed to connect to BellaDati: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to connect to BellaDati API", e);
        }
    }

    /**
     * Loads all data rows from the dataset.
     *
     * @return list of data rows, or empty list if loading fails
     */
    public List<DataRow> loadData() {
        try {
            LOG.debug("Loading data from dataset {}", DATASET_ID);
            PaginatedIdList<DataRow> dataRows = service.getDataSetData(DATASET_ID);
            dataRows.load();
            List<DataRow> result = dataRows.toList();
            LOG.debug("Successfully loaded {} rows", result.size());
            return result;
        } catch (Exception e) {
            LOG.error("Failed to load data from dataset {}: {}", DATASET_ID, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Inserts a new data row into the dataset.
     *
     * @param id     the ID value
     * @param name   the name value
     * @param email  the email value
     * @param role   the role value
     * @param status the status value
     * @throws RuntimeException if insert operation fails
     */
    public void insertData(String id, String name, String email, String role, String status) {
        try {
            LOG.info("Inserting new row with ID: {}", id);
            List<DataColumn> columns = Arrays.asList(
                new DataColumn(COL_ID),
                new DataColumn(COL_NAME),
                new DataColumn(COL_EMAIL),
                new DataColumn(COL_ROLE),
                new DataColumn(COL_STATUS)
            );
            
            DataRow row = new DataRow(columns);
            row.set(COL_ID, id);
            row.set(COL_NAME, name);
            row.set(COL_EMAIL, email);
            row.set(COL_ROLE, role);
            row.set(COL_STATUS, status);
            
            service.postDataSetData(DATASET_ID, row);
            LOG.info("Successfully inserted row with ID: {}", id);
        } catch (Exception e) {
            LOG.error("Failed to insert row with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to insert data: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a data row from the dataset by its UID (internal row identifier).
     * Uses the REST API endpoint: DELETE /api/dataSets/:id/data/:rowIds
     *
     * @param uid the UID of the row to delete (internal row identifier, not column value)
     * @throws RuntimeException if delete operation fails
     */
    public void deleteData(String uid) {
        try {
            LOG.info("Deleting row with UID: {}", uid);
            
            // Cast to implementation to access internal client for direct API call
            BellaDatiServiceImpl serviceImpl = (BellaDatiServiceImpl) service;
            BellaDatiClient client = serviceImpl.getClient();
            TokenHolder tokenHolder = serviceImpl.getTokenHolder();
            
            // Call DELETE /api/dataSets/:dataSetId/data/:rowIds endpoint directly
            String deleteEndpoint = "api/dataSets/" + DATASET_ID + "/data/" + uid;
            client.delete(deleteEndpoint, tokenHolder);
            
            LOG.info("Successfully deleted row with UID: {}", uid);
        } catch (Exception e) {
            LOG.error("Failed to delete row with UID {}: {}", uid, e.getMessage(), e);
            throw new RuntimeException("Failed to delete data: " + e.getMessage(), e);
        }
    }
}
