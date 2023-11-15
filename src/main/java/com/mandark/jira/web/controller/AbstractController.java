package com.mandark.jira.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mandark.jira.spi.util.Defaults;


public abstract class AbstractController {


    protected String appName;
    protected String appTitle;
    protected String hostUrl;


    // Init binders
    // ------------------------------------------------------------------------

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        final DateFormat dateFormat = new SimpleDateFormat(Defaults.DATE_FORMAT_REQ_PARAM);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    // Model Attributes
    // ------------------------------------------------------------------------

    @ModelAttribute("appName")
    public String getAppName() {
        return appName;
    }

    @ModelAttribute("appTitle")
    public String getAppTitle() {
        return appTitle;
    }

    @ModelAttribute("hostUrl")
    public String getHostURL() {
        return hostUrl;
    }



    // Util Methods
    // ------------------------------------------------------------------------



    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }


}
