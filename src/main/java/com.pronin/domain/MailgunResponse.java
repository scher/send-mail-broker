package com.pronin.domain;

/**
 * @author Alexander Pronin
 * @since 09/02/2018
 */
public class MailgunResponse {
    private String id;
    private String message;

    @Override
    public String toString() {
        return "MailgunResponse{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
