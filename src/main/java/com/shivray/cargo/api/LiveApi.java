package com.shivray.cargo.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LiveApi {

    private final HttpClient client =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

    private final ObjectMapper mapper =
            new ObjectMapper();

    public String exchangeRates(String base) throws Exception {

        String url =
                "https://api.frankfurter.dev/v1/latest?base="
                        + base;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(15))
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            throw new Exception(
                    "API error: HTTP "
                            + response.statusCode()
            );
        }

        JsonNode data =
                mapper.readTree(response.body());

        return "Base: "
                + data.path("base").asText()
                + "\nDate: "
                + data.path("date").asText()
                + "\n\n"
                + data.path("rates").toPrettyString();
    }
}