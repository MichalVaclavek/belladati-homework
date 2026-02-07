package com.belladati.homework.services;

import com.belladati.sdk.BellaDatiService;
import com.belladati.sdk.dataset.data.DataColumn;
import com.belladati.sdk.dataset.data.DataRow;
import com.belladati.sdk.exception.BellaDatiRuntimeException;
import com.belladati.sdk.exception.ConnectionException;
import com.belladati.sdk.impl.BellaDatiClient;
import com.belladati.sdk.impl.BellaDatiServiceImpl;
import com.belladati.sdk.impl.TokenHolder;
import com.belladati.sdk.util.PaginatedIdList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Arrays;
import java.util.List;

/**
 * Service for communication with BellaDati API.
 * Handles data operations for the Sample List dataset.
 */
public class BellaDatiDataService {

    private static final Logger LOG = LoggerFactory.getLogger(BellaDatiDataService.class);

    // Column codes for the dataset
    public static final String COL_ID = "L_ID";
    public static final String COL_NAME = "L_NAME";
    public static final String COL_EMAIL = "L_EMAIL";
    public static final String COL_ROLE = "L_ROLE";
    public static final String COL_STATUS = "L_STATUS";

    private final String belladatiUrl;
    private final String consumerKey;
    private final String consumerSecret;
    private final String username;
    private final String password;
    private final String datasetId;

    private final BellaDatiServiceFactory serviceFactory;

    private BellaDatiService service;

    public BellaDatiDataService(
            @Inject @Symbol("belladati.url") String belladatiUrl,
            @Inject @Symbol("belladati.consumerKey") String consumerKey,
            @Inject @Symbol("belladati.consumerSecret") String consumerSecret,
            @Inject @Symbol("belladati.username") String username,
            @Inject @Symbol("belladati.password") String password,
            @Inject @Symbol("belladati.datasetId") String datasetId,
            @Inject BellaDatiServiceFactory serviceFactory) {
        this.belladatiUrl = belladatiUrl;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.username = username;
        this.password = password;
        this.datasetId = datasetId;
        this.serviceFactory = serviceFactory;
    }

    /**
     * Establishes connection to BellaDati service.
     *
     * @throws BellaDatiRuntimeException if connection fails
     */
    private void connect() {
        try {
            LOG.info("Connecting to BellaDati at {}", belladatiUrl);
            this.service = serviceFactory.create(belladatiUrl, consumerKey, consumerSecret, username, password);
            LOG.info("Successfully connected to BellaDati");
        } catch (BellaDatiRuntimeException e) {
            LOG.error("Failed to connect to BellaDati: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to connect to BellaDati: {}", e.getMessage(), e);
            throw new ConnectionException("Failed to connect to BellaDati API", e);
        }
    }

    /**
     * Loads all data rows from the dataset.
     *
     * @return list of data rows
     * @throws BellaDatiRuntimeException if loading fails
     */
    public List<DataRow> loadData() {
        try {
            ensureService();
            LOG.debug("Loading data from dataset {}", datasetId);
            PaginatedIdList<DataRow> dataRows = service.getDataSetData(datasetId);
            dataRows.load();
            List<DataRow> result = dataRows.toList();
            LOG.debug("Successfully loaded {} rows", result.size());
            return result;
        } catch (BellaDatiRuntimeException e) {
            LOG.error("Failed to load data from dataset {}: {}", datasetId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to load data from dataset {}: {}", datasetId, e.getMessage(), e);
            throw new ConnectionException("Failed to load data", e);
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
     * @throws BellaDatiRuntimeException if insert operation fails
     */
    public void insertData(String id, String name, String email, String role, String status) {
        try {
            ensureService();
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
            
            service.postDataSetData(datasetId, row);
            LOG.info("Successfully inserted row with ID: {}", id);
        } catch (BellaDatiRuntimeException e) {
            LOG.error("Failed to insert row with ID {}: {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to insert row with ID {}: {}", id, e.getMessage(), e);
            throw new ConnectionException("Failed to insert data", e);
        }
    }

    /**
     * Deletes a data row from the dataset by its UID (internal row identifier).
     * Uses the REST API endpoint: DELETE /api/dataSets/:id/data/:rowIds
     *
     * @param uid the UID of the row to delete (internal row identifier, not column value)
     * @throws BellaDatiRuntimeException if delete operation fails
     */
    public void deleteData(String uid) {
        try {
            ensureService();
            LOG.info("Deleting row with UID: {}", uid);

            // Cast to implementation to access internal client for direct API call
            if (!(service instanceof BellaDatiServiceImpl serviceImpl)) {
                throw new UnsupportedOperationException(
                    "Delete requires BellaDatiServiceImpl, got: " + service.getClass().getName()
                );
            }

            BellaDatiClient client = serviceImpl.getClient();
            TokenHolder tokenHolder = serviceImpl.getTokenHolder();
            
            // Call DELETE /api/dataSets/:dataSetId/data/:rowIds endpoint directly
            String deleteEndpoint = "api/dataSets/" + datasetId + "/data/" + uid;
            client.delete(deleteEndpoint, tokenHolder);

            LOG.info("Successfully deleted row with UID: {}", uid);
        } catch (BellaDatiRuntimeException e) {
            LOG.error("Failed to delete row with UID {}: {}", uid, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to delete row with UID {}: {}", uid, e.getMessage(), e);
            throw new ConnectionException("Failed to delete data", e);
        }
    }

    private void ensureService() {
        if (service == null) {
            connect();
        }
    }
}
