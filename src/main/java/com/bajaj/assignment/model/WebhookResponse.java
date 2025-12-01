package com.bajaj.assignment.model;

/**
 * Data Transfer Object for the response received from the generateWebhook API.
 */
public class WebhookResponse {
    
    // The URL where the final solution should be submitted
    private String webhookUrl;
    
    // The access token to be used for authorization in the submission API
    private String accessToken;

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
