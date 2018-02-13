package com.pronin.service.local;

import com.pronin.domain.Email;
import com.pronin.domain.MailgunRequest;
import com.pronin.domain.SendgridRequest;
import com.pronin.service.EMailDispatcher;
import com.pronin.service.EMailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Alexander Pronin
 * @since 09/02/2018
 */
@Component
public class SimpleEmailDispatcher implements EMailDispatcher {
    private static final Logger log = LoggerFactory.getLogger(SimpleEmailDispatcher.class);

    private static final Executor exec = Executors.newFixedThreadPool(20);
    private final EMailQueue source;
    private final SendgridRequest sendgridRequest;
    private final MailgunRequest mailgunRequest;

    @Autowired
    public SimpleEmailDispatcher(EMailQueue source,
                                 SendgridRequest sendgridRequest,
                                 MailgunRequest mailgunRequest) {
        this.source = source;
        this.mailgunRequest = mailgunRequest;
        this.sendgridRequest = sendgridRequest;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Email email = source.take();
            exec.execute(() -> {
                log.info("Fetching message from queue");
                HttpStatus httpStatus;
                try {
                    httpStatus = mailgunRequest.send(email);
                    if (httpStatus != HttpStatus.OK) {
                        throw new RestClientException("Mailgun http status: " + httpStatus);
                    }
                } catch (RestClientException e) {
                    log.error(e.getMessage(), e);
                    httpStatus = sendgridRequest.send(email);
                }
                log.info("Response status: " + httpStatus.toString());
            });
        }
    }
}
