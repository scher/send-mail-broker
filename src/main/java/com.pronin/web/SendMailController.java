package com.pronin.web;

import com.pronin.domain.Email;
import com.pronin.service.EMailQueue;
import com.pronin.service.local.SimpleEmailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Pronin
 * @since 06/02/2018
 */
@RestController
public class SendMailController {
    private static final Logger log = LoggerFactory.getLogger(SimpleEmailQueue.class);

    @Autowired
    private EMailQueue eMailQueue;

    @RequestMapping(name = "/send", method = RequestMethod.POST)
    public String sendMailPost(@ModelAttribute Email email) {
        log.debug(email.toString());
        eMailQueue.submit(email);
        return email.toString();
    }
}
