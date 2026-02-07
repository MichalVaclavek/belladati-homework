package com.belladati.homework.pages;

import com.belladati.homework.services.BellaDatiDataService;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
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

    @Component
    private Form insertForm;

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

    public Object onSuccess() {
        try {
            bellaDatiDataService.insertData(id, name, email, role, status);
            return DataList.class;
        } catch (Exception e) {
            insertForm.recordError(e.getMessage());
            return null;  // Stay on the same page to show error
        }
    }
}
