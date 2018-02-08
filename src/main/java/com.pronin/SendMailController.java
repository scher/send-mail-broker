package com.pronin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(name = "/send", method = RequestMethod.POST)
    public String sendMailPost(@ModelAttribute Email recipients) {
        log.debug(recipients.toString());
        return recipients.toString();
    }
}
