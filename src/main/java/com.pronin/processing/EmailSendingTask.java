package com.pronin.processing;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pronin.domain.Email;
import com.pronin.domain.MailGunRequest;
import com.pronin.domain.SendgridRequest;
import com.pronin.service.EMailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

/**
 * @author Alexander Pronin
 * @since 13/02/2018
 */
public class EmailSendingTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(EmailSendingTask.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final EMailQueue eMailQueue;
    private final Message message;
    private MailGunRequest mailGunRequest;
    private SendgridRequest sendgridRequest;

    public EmailSendingTask(MailGunRequest mailGunRequest, SendgridRequest sendgridRequest,
                            EMailQueue eMailQueue, Message message) {

        this.mailGunRequest = mailGunRequest;
        this.sendgridRequest = sendgridRequest;
        this.eMailQueue = eMailQueue;
        this.message = message;
    }

    @Override
    public void run() {
        boolean deleteMessage = false;
        HttpStatus httpStatus;
        Email email = null;

        try {
            email = OBJECT_MAPPER.readValue(message.getBody(), Email.class);

            httpStatus = mailGunRequest.send(email);
            if (httpStatus == HttpStatus.OK) {
                deleteMessage = true;
            } else {
                throw new RestClientException("Failed to send message via Mailgun. HttpStatus: " + httpStatus);
            }

        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            httpStatus = sendgridRequest.send(email);
            if (httpStatus == HttpStatus.ACCEPTED) {
                deleteMessage = true;
            } else {
                log.error("Message can not be sent neither via Mailgun nor via SendGrid. It will remain in SQS.");
            }
        } catch (IOException e) {
            // OBJECT_MAPPER.readValue
            log.error(e.getMessage(), e);
            deleteMessage = true;
        }

        if (deleteMessage) {
            eMailQueue.deleteMessage(message.getReceiptHandle());
        }
    }
}
