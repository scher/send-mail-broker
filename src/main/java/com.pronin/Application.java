package com.pronin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author Alexander Pronin
 * @since 06/02/2018
 */
@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
//        String trustStore = Application.class.getClassLoader().getResource("trustStore").getPath();
//        System.setProperty("javax.net.ssl.trustStore", trustStore);

        SpringApplication.run(Application.class, args);


//        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
//        credentialsProvider.getCredentials();

//        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
//                .withCredentials(credentialsProvider)
//                .withRegion(Regions.US_EAST_2).build();

//        CreateQueueResult newQ = amazonSQS.createQueue("newQ");
//        log.info(newQ.toString());

//        ListQueuesResult result = amazonSQS.listQueues();
//        log.info(result.toString());

//        SendMessageResult sendMessageResult = amazonSQS.sendMessage("siteminder", "hey hey");
//        log.info(sendMessageResult.toString());
    }


    @PostConstruct
    public void postConstruct() {
//        String trustStore = this.getClass().getClassLoader().getResource("trustStore").getPath();
//        System.setProperty("javax.net.ssl.trustStore", trustStore);
    }

//    @Bean
//    public boolean dispatcher(SimpleEmailDispatcher dispatcher) {
//        Executors.newSingleThreadExecutor().submit(dispatcher);
//        return true;
//    }

    @Bean
    RestTemplate sendGridRestTemplate(RestTemplateBuilder builder) {
        return null;
    }


}
