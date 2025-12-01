package com.bajaj.assignment.model;

/**
 * Data Transfer Object for the request body sent to the testWebhook API.
 */
public class SubmissionRequest {
    
    // The final SQL query solution
    private String finalQuery;

    public SubmissionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}
