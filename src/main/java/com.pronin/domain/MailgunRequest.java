package com.pronin.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static java.util.stream.Collectors.joining;

/**
 * @author Alexander Pronin
 * @since 11/02/2018
 */
@Component
public class MailgunRequest {

    private final RestTemplateBuilder builder;
    private final HttpHeaders headers;
    @Value("${mailgun.user}")
    private String mailgunUser;
    @Value("${mailgun.api.key}")
    private String mailgunPassword;
    @Value("${mailgun.send.url}")
    private String mailgunSendUrl;
    private RestTemplate restTemplate;

    @Autowired
    public MailgunRequest(RestTemplateBuilder builder) {
        this.builder = builder;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

    @PostConstruct
    public void init() {
        restTemplate = builder.basicAuthorization(mailgunUser, mailgunPassword).build();
    }

    public MailgunResponse send(Email email) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "apronin123@me.com");
        map.add("to", email.getRecipients().stream().collect(joining(",")));
        map.add("subject", "MailGun");
        map.add("text", email.getMessage());

        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(map, headers);

        return restTemplate.postForObject(mailgunSendUrl, objectHttpEntity, MailgunResponse.class);
    }
}
