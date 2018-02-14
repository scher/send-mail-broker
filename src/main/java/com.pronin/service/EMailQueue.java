package com.pronin.service;

import com.amazonaws.services.sqs.model.Message;
import com.pronin.domain.Email;

import java.util.List;

/**
 * @author Alexander Pronin
 * @since 08/02/2018
 */
public interface EMailQueue {
    boolean submit(Email email);

    List<Message> take();

    void deleteMessage(String receiptHandler);
}
