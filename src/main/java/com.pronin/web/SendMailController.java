package com.pronin.web;

import com.pronin.domain.Email;
import com.pronin.service.EMailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Alexander Pronin
 * @since 06/02/2018
 */
@RestController
public class SendMailController {
    private static final Logger log = LoggerFactory.getLogger(SendMailController.class);

    private final EMailQueue eMailQueue;

    @Autowired
    public SendMailController(EMailQueue eMailQueue) {
        this.eMailQueue = eMailQueue;
    }

    @RequestMapping(name = "/send", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendMailPost(@Valid @ModelAttribute Email email) {
        log.debug(email.toString());
        eMailQueue.submit(email);
    }
}
