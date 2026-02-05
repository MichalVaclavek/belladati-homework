package com.belladati.homework.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Layout component providing common page structure with navigation.
 */
@Import(stylesheet = "context:css/style.css")
@SuppressWarnings("unused")
public class Layout {

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String title;

    public String getTitle() {
        return title != null ? title : "BellaDati App";
    }
}
