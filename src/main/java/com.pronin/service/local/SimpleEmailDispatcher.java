package com.pronin.service.local;

import com.pronin.domain.Email;
import com.pronin.domain.MailgunRequest;
import com.pronin.domain.MailgunResponse;
import com.pronin.domain.SendgridRequest;
import com.pronin.service.EMailDispatcher;
import com.pronin.service.EMailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Pronin
 * @since 09/02/2018
 */
@Component
public class SimpleEmailDispatcher implements EMailDispatcher {
    private static final Logger log = LoggerFactory.getLogger(SimpleEmailDispatcher.class);

    private final EMailQueue source;
    private SendgridRequest sendgridRequest;
    private MailgunRequest mailgunRequest;

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
        while (true) {
            Email email = source.take();
            log.info("Fetching message from queue");

            MailgunResponse response = mailgunRequest.send(email);

            sendgridRequest.send(email);
            log.info(response.toString());
        }
    }
}
