package br.com.imobatlas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import java.net.URI;
import java.net.http.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("demo")
class ApiIntegrationTest {
    @LocalServerPort int port;
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
    private HttpResponse<String> post(String body) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/v1/simulations"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build(), HttpResponse.BodyHandlers.ofString());
    }
    @Test void migrationsCatalogAndRegionFilterWorkTogether() throws Exception {
        var response = get("/api/v1/funds?region=NORDESTE");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("DEMO11", "DEMO12", "Recife", "Campinas").doesNotContain("DEMO13", "DEMO14");
    }
    @Test void tickerAndEmptySearchHaveExpectedResponses() throws Exception {
        assertThat(get("/api/v1/funds/demo11").statusCode()).isEqualTo(200);
        assertThat(get("/api/v1/funds?search=inexistente").body()).isEqualTo("[]");
        assertThat(get("/api/v1/funds/UNKNOWN").statusCode()).isEqualTo(404);
        assertThat(get("/api/v1/funds?region=INVALID").statusCode()).isEqualTo(400);
    }
    @Test void simulationEndpointCalculatesKnownScenario() throws Exception {
        var response = post("""
                {"ticker":"DEMO11","initialInvestment":1000,"monthlyContribution":0,"months":1,"reinvestDistributions":false}
                """);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"finalValue\":1008.00", "\"totalDistributions\":8.00", "\"units\":10");
    }
    @Test void validatesNegativeValuesNullFlagsAndExcessiveDuration() throws Exception {
        assertThat(post("""
                {"ticker":"DEMO11","initialInvestment":-1,"monthlyContribution":0,"months":361,"reinvestDistributions":true}
                """).statusCode()).isEqualTo(400);
        assertThat(post("""
                {"ticker":"DEMO11","initialInvestment":100,"monthlyContribution":0,"months":1}
                """).statusCode()).isEqualTo(400);
        assertThat(post("{}").statusCode()).isEqualTo(400);
        assertThat(post("not json").statusCode()).isEqualTo(400);
    }
}
