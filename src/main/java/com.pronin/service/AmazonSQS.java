package com.pronin.service;

import com.pronin.domain.Email;
import com.pronin.service.local.SimpleEmailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Pronin
 * @since 08/02/2018
 */
//@Service
public class AmazonSQS implements EMailQueue {
    private static final Logger log = LoggerFactory.getLogger(SimpleEmailQueue.class);

    @Override
    public boolean submit(Email email) {
        log.info("Submitting to AWS SQS");
        return false;
    }

    @Override
    public Email take() {
        return null;
    }
}
