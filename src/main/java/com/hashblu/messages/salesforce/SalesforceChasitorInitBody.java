package com.hashblu.messages.salesforce;

import java.util.Collections;
import java.util.List;

/**
 * Created by abhisheks on 08-Nov-17.
 */
public class SalesforceChasitorInitBody {
    private String organizationId;
    private String deploymentId;
    private String buttonId;
    private String sessionId;
    private String userAgent;
    private String language;
    private String screenResolution;
    private String visitorName;
    private List<String> prechatDetails;
    private List<String> prechatEntities;
    private boolean receiveQueueDetails;
    private boolean isPost;

    public SalesforceChasitorInitBody(String organizationId, String deploymentId, String buttonId, String sessionId, String visitorName){
        this.organizationId = organizationId;
        this.deploymentId = deploymentId;
        this.buttonId = buttonId;
        this.sessionId = sessionId;
        this.userAgent = "Lynx/2.8.8";
        this.language = "en-US";
        this.screenResolution = "1920x1080";
        this.visitorName = visitorName;
        this.prechatDetails = Collections.emptyList();
        this.prechatEntities = Collections.emptyList();
        this.receiveQueueDetails = false;
        this.isPost = true;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public List<String> getPrechatDetails() {
        return prechatDetails;
    }

    public void setPrechatDetails(List<String> prechatDetails) {
        this.prechatDetails = prechatDetails;
    }

    public List<String> getPrechatEntities() {
        return prechatEntities;
    }

    public void setPrechatEntities(List<String> prechatEntities) {
        this.prechatEntities = prechatEntities;
    }

    public boolean isReceiveQueueDetails() {
        return receiveQueueDetails;
    }

    public void setReceiveQueueDetails(boolean receiveQueueDetails) {
        this.receiveQueueDetails = receiveQueueDetails;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }
}
