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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static java.util.stream.Collectors.joining;

/**
 * @author Alexander Pronin
 * @since 11/02/2018
 */
@Component
public class MailGunRequest {
    private static final Logger log = LoggerFactory.getLogger(MailGunRequest.class);

    private final RestTemplateBuilder builder;
    private final HttpHeaders headers;
    @Value("${connect.timeout.millis}")
    private int connectTimeoutMillis;
    @Value("${mailgun.user}")
    private String mailgunUser;
    @Value("${mailgun.api.key}")
    private String mailgunPassword;
    @Value("${mailgun.send.url}")
    private String mailGunSendUrl;
    @Value("${mailgun.from}")
    private String mailGunFrom;
    @Value("${mailgun.subject}")
    private String mailGunSubject;


    private RestTemplate restTemplate;

    @Autowired
    public MailGunRequest(RestTemplateBuilder builder) {
        this.builder = builder.setConnectTimeout(connectTimeoutMillis).setReadTimeout(connectTimeoutMillis);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

    @PostConstruct
    public void init() {
        restTemplate = builder.basicAuthorization(mailgunUser, mailgunPassword).build();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error(response.toString());
            }
        });
    }

    public HttpStatus send(Email email) {
        log.info("Sending message via MailGun");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", mailGunFrom);
        map.add("to", email.getRecipients().stream().collect(joining(",")));
        map.add("subject", mailGunSubject);
        map.add("text", email.getMessage());

        HttpEntity<Object> httpEntity = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(mailGunSendUrl, httpEntity, Void.class).getStatusCode();
    }
}
