package com.belladati.homework.pages;

import com.belladati.homework.services.BellaDatiDataService;
import com.belladati.sdk.dataset.data.DataRow;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Page displaying the list of data from BellaDati dataset.
 * Contains functionality to view and delete rows.
 */
public class DataList {

    private static final Logger LOG = LoggerFactory.getLogger(DataList.class);

    @Inject
    private BellaDatiDataService bellaDatiDataService;

    @Property
    private DataRow row;

    @Property
    private List<DataRow> dataRows;

    @Property
    private String errorMessage;

    void setupRender() {
        try {
            errorMessage = null;
            dataRows = bellaDatiDataService.loadData();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            dataRows = List.of();
        }
    }

    public Object onActionFromDelete(String uid) {
        try {
            bellaDatiDataService.deleteData(uid);
        } catch (Exception e) {
            errorMessage = "Failed to delete row: " + e.getMessage();
        }
        return DataList.class;
    }

    public boolean isHasError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }

    // Getters for displaying data in the template
    public String getRowId() {
        return row.get(BellaDatiDataService.COL_ID);
    }

    public String getRowName() {
        return row.get(BellaDatiDataService.COL_NAME);
    }

    public String getRowEmail() {
        return row.get(BellaDatiDataService.COL_EMAIL);
    }

    public String getRowRole() {
        return row.get(BellaDatiDataService.COL_ROLE);
    }

    public String getRowStatus() {
        return row.get(BellaDatiDataService.COL_STATUS);
    }

    public String getRowUid() {
        return row.getId();
    }
}
