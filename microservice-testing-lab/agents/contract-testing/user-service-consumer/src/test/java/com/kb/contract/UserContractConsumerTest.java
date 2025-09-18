package com.kb.contract;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.*;
import au.com.dius.pact.core.model.RequestResponsePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.apache.hc.client5.http.fluent.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "user-service", port = "8081")
public class UserContractConsumerTest extends ContractBaseTest {

    @Pact(consumer = "order-service")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return builder
            .given("user with ID 1 exists")
            .uponReceiving("A request to fetch user by ID")
                .path("/api/users/1")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(headers)
                .body("""
                    {
                      "id": 1,
                      "name": "Alice",
                      "email": "alice@example.com"
                    }
                    """)
            .toPact();
    }

    @Test
    void testContract(MockServer mockServer) throws IOException {
        String response = Request.get(mockServer.getUrl() + "/api/users/1")
                                 .execute().returnContent().asString();
        assertThat(response).contains("alice@example.com");
    }
}
