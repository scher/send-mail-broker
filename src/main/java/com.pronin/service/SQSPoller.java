package com.pronin.service;

import com.amazonaws.services.sqs.model.Message;
import com.pronin.domain.MailGunRequest;
import com.pronin.domain.SendgridRequest;
import com.pronin.processing.EmailSendingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexander Pronin
 * @since 09/02/2018
 */
@Component
public class SQSPoller implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SQSPoller.class);
    private static final Executor workers = Executors.newFixedThreadPool(20);
    private final EMailQueue source;
    private final SendgridRequest sendgridRequest;
    private final MailGunRequest mailGunRequest;
    @Value("${sqs.polling.interval.seconds}")
    private int sqsPollingIntervalSeconds;

    @Autowired
    public SQSPoller(EMailQueue source,
                     SendgridRequest sendgridRequest,
                     MailGunRequest mailGunRequest) {
        this.source = source;
        this.mailGunRequest = mailGunRequest;
        this.sendgridRequest = sendgridRequest;
    }

    @PostConstruct
    private void start() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                this, 0, sqsPollingIntervalSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        List<Message> emails = source.take();
        log.info("Received messages from SQS(" + emails.size() + "): " + emails.toString());
        emails.forEach(message -> workers.execute(createTask(message)));
    }

    private EmailSendingTask createTask(Message message) {
        return new EmailSendingTask(mailGunRequest, sendgridRequest, source, message);
    }
}
