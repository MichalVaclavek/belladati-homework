package com.belladati.homework.pages;

import com.belladati.homework.services.BellaDatiDataService;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page with a form to insert new data into the BellaDati dataset.
 */
@SuppressWarnings("unused")
public class Insert {

    private static final Logger LOG = LoggerFactory.getLogger(Insert.class);

    @Inject
    private BellaDatiDataService bellaDatiDataService;

    @Property
    private String id;

    @Property
    private String name;

    @Property
    private String email;

    @Property
    private String status;

    @Property
    private String role;

    @Property
    private String errorMessage;

    public Object onSuccess() {
        try {
            bellaDatiDataService.insertData(id, name, email, role, status);
            return DataList.class;
        } catch (Exception e) {
            LOG.error("Error inserting data: {}", e.getMessage());
            errorMessage = "Failed to insert data: " + e.getMessage();
            return null;  // Stay on the same page to show error
        }
    }

    public boolean isHasError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }
}
