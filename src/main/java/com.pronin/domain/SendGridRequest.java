package com.pronin.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author Alexander Pronin
 * @since 12/02/2018
 */
@Component
public class SendGridRequest {
    private static final Logger log = LoggerFactory.getLogger(SendGridRequest.class);
    private static final String JSON_BODY = "{\"personalizations\": [{\"to\": [%s]}]," +
            "\"from\": {\"email\": \"%s\"}," +
            "\"subject\": \"%s\"," +
            "\"content\": [{\"type\": \"text/plain\", \"value\": \"%s\"}]}";
    private final RestTemplate restTemplate;
    private HttpHeaders headers;
    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;
    @Value("${sendgrid.send.url}")
    private String sendgridSendUrl;
    @Value("${connect.timeout.millis}")
    private int connectTimeoutMillis;
    @Value("${sendgrid.from}")
    private String from;
    @Value("${sendgrid.subject}")
    private String subject;

    @Autowired
    public SendGridRequest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate =
                restTemplateBuilder.setConnectTimeout(connectTimeoutMillis).setReadTimeout(connectTimeoutMillis)
                        .build();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error(response.toString());
            }
        });
    }

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + sendgridApiKey);
    }

    public HttpStatus send(Email email) {
        log.info("Sending message via SendGrid");
        String body = String.format(JSON_BODY, recipientsToString(email), from, subject, email.getMessage());
        HttpEntity<String> objectHttpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(sendgridSendUrl, objectHttpEntity, Void.class).getStatusCode();
    }

    private String recipientsToString(Email email) {
        return email.getRecipients().stream()
                .map(e -> String.format("{\"email\": \"%s\"}", e))
                .collect(Collectors.joining(", "));
    }
}
