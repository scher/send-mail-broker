package com.pronin.service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pronin.domain.Email;
import com.pronin.service.local.SimpleEmailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Alexander Pronin
 * @since 08/02/2018
 */
@Service
public class AmazonSQSEmailQueue implements EMailQueue {
    private static final Logger log = LoggerFactory.getLogger(SimpleEmailQueue.class);
    private static final String QUEUE_NAME = "siteminder";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AmazonSQS amazonSQS;


    public AmazonSQSEmailQueue() {
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        credentialsProvider.getCredentials();

        amazonSQS = AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_2).build();
    }

    @Override
    public boolean submit(Email email) {
        try {
            String emailJson = OBJECT_MAPPER.writeValueAsString(email);

            log.info("Submitting to AWS SQS: " + emailJson);
            log.info("and back " + OBJECT_MAPPER.readValue(emailJson, Email.class));
            amazonSQS.sendMessage(QUEUE_NAME, emailJson);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Email take() {
        return null;
    }
}
