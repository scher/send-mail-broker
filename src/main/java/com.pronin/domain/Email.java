package com.pronin.domain;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Alexander Pronin
 * @since 07/02/2018
 */
public class Email {
    @NotNull
    private List<String> recipients;
    @NotNull
    private String message;

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Email{" +
                "recipients=" + recipients +
                ", message='" + message + '\'' +
                '}';
    }
}
