package com.todo.app.demo.integrationtest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.todo.app.demo.ToDoAppApplication;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ToDoAppApplication.class,
        properties = {"server.port=8083"})
public abstract class AbstractTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);

    public TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    public int localPort;

    public static WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8089));

    @BeforeAll
    public static void startServer() {
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    protected void stubMatchingUrlFromFile(String URL, int statusCode, String responseFileName) {
        stubFor(get(urlMatching(URL))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(loadFile(responseFileName))));
    }

    protected String loadFile(String fileName) {
        try {
            return FileUtils.readFileToString(new File("src/test/resources/" + fileName).getAbsoluteFile(),
                            String.valueOf(StandardCharsets.UTF_8)).trim();
        } catch (IOException e) {
            LOGGER.error("Error loading file", e);
        }
        return "";
    }

//    protected void validateResponseWithSwagger(final String urlCalled, final String urlResponse) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        final Map swagger = SwaggerUtil.getSwagger(Paths.get("src/main/java/com/channel4/affinity/config/SwaggerConfig.java"));
//        final JsonSchema jsonSchema = SwaggerUtil.getJsonSchema(swagger, "['" + urlCalled + "']");
//        final ProcessingReport report = SwaggerUtil.validate(mapper.readValue(urlResponse, Map.class), jsonSchema);
//        assertTrue(report.isSuccess(), "Swagger validation fails with " + report);
//    }

}
