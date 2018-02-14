package com.pronin.service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pronin.domain.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.UnsupportedOperationException;
import java.util.List;

/**
 * @author Alexander Pronin
 * @since 08/02/2018
 */
@Service
public class AmazonSQSEmailQueue implements EMailQueue {
    private static final Logger log = LoggerFactory.getLogger(AmazonSQSEmailQueue.class);
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
            SendMessageResult sendMessageResult = amazonSQS.sendMessage(QUEUE_NAME, emailJson);
            log.debug(sendMessageResult.toString());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e); // should not occur
        } catch (InvalidMessageContentsException | UnsupportedOperationException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public List<Message> take() {
        log.info("Fetching messages from AWS SQS");
        GetQueueUrlResult queueUrl = amazonSQS.getQueueUrl(QUEUE_NAME);
        ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(queueUrl.getQueueUrl());
        return receiveMessageResult.getMessages();
    }

    @Override
    public void deleteMessage(String receiptHandler) {
        log.info("Deleting message from AWS SQS " + receiptHandler);
        amazonSQS.deleteMessage(QUEUE_NAME, receiptHandler);
    }
}
