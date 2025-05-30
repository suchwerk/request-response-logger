package org.acme.logger;


import java.util.UUID;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class RequestLoggingContext {

    private String transactionId;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String  createAndSetTransactionId() {
        transactionId = UUID.randomUUID().toString();
        return transactionId;
    }

}
