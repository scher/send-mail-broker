package com.pronin;

import java.util.List;

/**
 * @author Alexander Pronin
 * @since 07/02/2018
 */
public class Email {
    private List<String> recipients;
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
