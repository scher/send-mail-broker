package com.pronin.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Alexander Pronin
 * @since 12/02/2018
 */
@Component
public class SendgridRequest {

    private final RestTemplate restTemplate;
    private HttpHeaders headers;
    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;
    @Value("${sendgrid.send.url}")
    private String sendgridSendUrl;

    @Autowired
    public SendgridRequest(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.setConnectTimeout(10).setReadTimeout(10).build();
    }

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + sendgridApiKey);
    }

    public HttpStatus send(Email email) {
        String json =
                "{\"personalizations\": [{\"to\": [{\"email\": \"apronin@me.com\"}, {\"email\": \"scherkka@gmail.com\"}]}]," +
                        "\"from\": {\"email\": \"test@example.com\"}," +
                        "\"subject\": \"SendGrid is Fun\"," +
                        "\"content\": [{\"type\": \"text/plain\", \"value\": \"and easy to do anywhere, even with cURL\"}]}";

        HttpEntity<String> objectHttpEntity = new HttpEntity<>(json, headers);

        return restTemplate.postForEntity(sendgridSendUrl, objectHttpEntity, Void.class).getStatusCode();
    }

    private static class To {
        private List<Email> email;

        public To(List<Email> email) {
            this.email = email;
        }

        public List<Email> getEmail() {
            return email;
        }

        public void setEmail(List<Email> email) {
            this.email = email;
        }
    }

//    private static class Email {
//        private String;
//    }

//    public static void main(String[] args) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        To to = new To(Arrays.asList("scherkka@gmail.com", "apronin@me.com"));
//        String out = objectMapper.writeValueAsString(to);
//        System.out.println(out);
//    }

}
