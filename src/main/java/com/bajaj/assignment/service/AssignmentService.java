package com.bajaj.assignment.service;

import com.bajaj.assignment.model.SubmissionRequest;
import com.bajaj.assignment.model.UserRequest;
import com.bajaj.assignment.model.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class that handles the main logic of the assignment.
 * It implements CommandLineRunner to execute the logic on application startup.
 */
@Service
public class AssignmentService implements CommandLineRunner {

    private final RestTemplate restTemplate;

    // Hardcoded values as per assignment instructions/placeholders
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String USER_NAME = "John Doe";
    private static final String REG_NO = "REG12347";
    private static final String EMAIL = "john@example.com";

    
    
    
    // Placeholder for the SQL query solution. 
    // TODO: Replace this with the actual SQL query derived from the assignment PDF.
    private static final String SQL_SOLUTION = "WITH HighEarners AS ( " +
                "SELECT DISTINCT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB, e.DEPARTMENT " +
                "FROM EMPLOYEE e JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID WHERE p.AMOUNT > 70000), " +
                "RankedEarners AS ( " +
                "SELECT he.*, d.DEPARTMENT_NAME, d.DEPARTMENT_ID, " +
                "EXTRACT(YEAR FROM AGE(CURRENT_DATE, he.DOB)) AS emp_age, " +
                "ROW_NUMBER() OVER(PARTITION BY d.DEPARTMENT_ID ORDER BY he.FIRST_NAME) as rn " +
                "FROM HighEarners he JOIN DEPARTMENT d ON he.DEPARTMENT = d.DEPARTMENT_ID) " +
                "SELECT DEPARTMENT_NAME, CAST(AVG(emp_age) AS INTEGER) as AVERAGE_AGE, " +
                "STRING_AGG(FIRST_NAME || ' ' || LAST_NAME, ', ') as EMPLOYEE_LIST " +
                "FROM RankedEarners WHERE rn <= 10 " +
                "GROUP BY DEPARTMENT_NAME, DEPARTMENT_ID " +
                "ORDER BY DEPARTMENT_ID DESC;"; 

                

    @Autowired
    public AssignmentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting Assignment Flow...");
        
        try {
            // Step 1: Generate Webhook
            WebhookResponse webhookResponse = generateWebhook();
            System.out.println("-------------------------------");
            System.out.println(webhookResponse);
            
            if (webhookResponse != null && webhookResponse.getWebhookUrl() != null && webhookResponse.getAccessToken() != null) {
                System.out.println("Webhook generated successfully.");
                System.out.println("Webhook URL: " + webhookResponse.getWebhookUrl());
                
                // Step 2: Submit Solution
                submitSolution(webhookResponse);
            } else {
                System.err.println("Failed to generate webhook or received invalid response.");
            }
            
        } catch (Exception e) {
            System.err.println("An error occurred during the process: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Assignment Flow Completed.");
    }

    /**
     * Sends a POST request to generate a webhook and receive an access token.
     * 
     * @return WebhookResponse containing the webhook URL and access token.
     */
    private WebhookResponse generateWebhook() {
        System.out.println("Sending request to generate webhook...");
        
        UserRequest requestBody = new UserRequest(USER_NAME, REG_NO, EMAIL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<UserRequest> request = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL, 
                    request, 
                    String.class
            );
            
            String responseBody = response.getBody();
            System.out.println("Raw Response: " + responseBody);
            
            if (responseBody != null) {
                WebhookResponse webhookResponse = new WebhookResponse();
                
                // Manual parsing to avoid dependency issues
                String webhookUrl = extractJsonValue(responseBody, "webhook");
                String accessToken = extractJsonValue(responseBody, "accessToken");
                
                webhookResponse.setWebhookUrl(webhookUrl);
                webhookResponse.setAccessToken(accessToken);
                
                return webhookResponse;
            }
        } catch (Exception e) {
            System.err.println("Error during webhook generation request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return json.substring(startIndex, endIndex);
            }
        }
        return null;
    }


    /**
     * Submits the SQL solution to the generated webhook URL.
     * 
     * @param webhookResponse The response from the previous step containing the URL and token.
     */
    private void submitSolution(WebhookResponse webhookResponse) {
        System.out.println("Submitting solution to webhook...");
        
        SubmissionRequest requestBody = new SubmissionRequest(SQL_SOLUTION);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", webhookResponse.getAccessToken()); // Use token as is, or add "Bearer " prefix if required by API? 
        // The prompt says "Authorization : < accessToken >", usually implies Bearer but sometimes raw. 
        // I will send it raw as per the prompt's literal representation, but standard is often Bearer.
        // Given the prompt: "Authorization : < accessToken >", I'll stick to raw for now.
        
        HttpEntity<SubmissionRequest> request = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                webhookResponse.getWebhookUrl(), 
                request, 
                String.class
        );
        
        System.out.println("Solution submitted. Response Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
    }
}
