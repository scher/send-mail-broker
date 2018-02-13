package com.pronin.service.local;

import com.pronin.domain.Email;
import com.pronin.service.EMailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Alexander Pronin
 * @since 08/02/2018
 */
//@Service
public class SimpleEmailQueue implements EMailQueue {
    private static final int DEFAULT_QUEUE_CAPACITY = 100;
    private static final Logger log = LoggerFactory.getLogger(SimpleEmailQueue.class);

    private final BlockingQueue<Email> queue;

    @Autowired
    public SimpleEmailQueue() {
        this.queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
    }

    @Override
    public boolean submit(Email email) {
        log.info("Submitting to Simple queue");
        return queue.add(email);
    }

    @Override
    public Email take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
