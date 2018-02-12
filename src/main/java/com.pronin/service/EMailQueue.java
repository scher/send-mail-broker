package com.pronin.service;

import com.pronin.domain.Email;

/**
 * @author Alexander Pronin
 * @since 08/02/2018
 */
public interface EMailQueue {
    boolean submit(Email email);

    Email take();
}
