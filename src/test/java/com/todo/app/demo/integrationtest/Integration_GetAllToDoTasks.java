package com.todo.app.demo.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class Integration_GetAllToDoTasks extends AbstractTest {

    String uuid = "sdefrghjn";
    @BeforeEach
    public void prepareStub() {
        wireMockServer.resetAll();
        stubMatchingUrlFromFile("/todoapp/getALlbyId/.*", HttpStatus.OK.value(), "responses/getAllByUserId_testData.json");
    }

    @Test
    public void getAllByUserId_test() {
        try {
            ResponseEntity responseAffinity = restTemplate.getForEntity("http://localhost:" + this.localPort + "/todoapp/getALlbyId/" + uuid, String.class);

            String responseAffinity_String = responseAffinity.getBody().toString();

            assertEquals(HttpStatus.OK.value(), responseAffinity.getStatusCodeValue(), "The response should be processed with status code 200.");
            assertFalse(responseAffinity_String.isEmpty(), "The response should be processed with status code 200.");

        } catch (Exception e) {
            LOGGER.error("Method failed " + e);
            fail("Failed in " + e.getMessage());
        }
    }

    @Test
    public void getAllByUserId_test_emptyCase() {
        try {
            stubMatchingUrlFromFile("/todoapp/getALlbyId/.*" + "", HttpStatus.OK.value(), "responses/getAllByUserId_testData.json");

            ResponseEntity responseAffinity = restTemplate.getForEntity("http://localhost:" + this.localPort + "/todoapp/getALlbyId/" + uuid, String.class);

            String responseAffinity_String = responseAffinity.getBody().toString();
//            validateResponseWithSwagger("/affinity/{uuid}", responseAffinity_String);

            assertEquals(HttpStatus.OK.value(), responseAffinity.getStatusCodeValue(), "The response should be processed with status code 200.");
            assertFalse(responseAffinity_String.isEmpty(), "The response should be processed with status code 200.");

        } catch (Exception e) {
            LOGGER.error("Method failed " + e);
            fail("Failed in " + e.getMessage());
        }
    }

}
