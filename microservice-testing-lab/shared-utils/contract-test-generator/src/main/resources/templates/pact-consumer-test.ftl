package com.kb.contract.${endpoint.operationId?lower_case};

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "${providerName}", port = "${port?string}")
public class ${endpoint.operationId}ConsumerTest {

    @Pact(consumer = "${consumerName}")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        <#if customHeaders??>
        <#list customHeaders?keys as key>
        headers.put("${key}", "${customHeaders[key]}");
        </#list>
        </#if>

        return builder
            .given("${endpoint.summary!'A request to ${endpoint.method} ${endpoint.path}'}")
            .uponReceiving("${endpoint.description!'A request to ${endpoint.method} ${endpoint.path}'}")
                .path("${endpoint.path}")
                .method("${endpoint.method}")
                <#if endpoint.requestBody??>
                .body(${sampleRequestBody!'{}'})
                </#if>
                <#if endpoint.parameters??>
                <#list endpoint.parameters as param>
                <#if param.in == "path">
                .matchPath("${endpoint.path}", "${endpoint.path}")
                </#if>
                <#if param.in == "query">
                .queryParameter("${param.name}", "sample_${param.name}")
                </#if>
                </#list>
                </#if>
            .willRespondWith()
                .status(200)
                .headers(headers)
                .body(${sampleResponseBody!'{}'})
            .toPact();
    }

    @Test
    void testContract(MockServer mockServer) throws IOException {
        String url = mockServer.getUrl() + "${endpoint.path}";
        
        <#if endpoint.method == "GET">
        String response = Request.get(url)
            <#if customHeaders??>
            <#list customHeaders?keys as key>
            .addHeader("${key}", "${customHeaders[key]}")
            </#list>
            </#if>
            .execute().returnContent().asString();
        <#elseif endpoint.method == "POST">
        String response = Request.post(url)
            .bodyString(${sampleRequestBody!'{}'}, ContentType.APPLICATION_JSON)
            <#if customHeaders??>
            <#list customHeaders?keys as key>
            .addHeader("${key}", "${customHeaders[key]}")
            </#list>
            </#if>
            .execute().returnContent().asString();
        <#elseif endpoint.method == "PUT">
        String response = Request.put(url)
            .bodyString(${sampleRequestBody!'{}'}, ContentType.APPLICATION_JSON)
            <#if customHeaders??>
            <#list customHeaders?keys as key>
            .addHeader("${key}", "${customHeaders[key]}")
            </#list>
            </#if>
            .execute().returnContent().asString();
        <#elseif endpoint.method == "DELETE">
        String response = Request.delete(url)
            <#if customHeaders??>
            <#list customHeaders?keys as key>
            .addHeader("${key}", "${customHeaders[key]}")
            </#list>
            </#if>
            .execute().returnContent().asString();
        <#elseif endpoint.method == "PATCH">
        String response = Request.patch(url)
            .bodyString(${sampleRequestBody!'{}'}, ContentType.APPLICATION_JSON)
            <#if customHeaders??>
            <#list customHeaders?keys as key>
            .addHeader("${key}", "${customHeaders[key]}")
            </#list>
            </#if>
            .execute().returnContent().asString();
        </#if>
        
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
    }
} 