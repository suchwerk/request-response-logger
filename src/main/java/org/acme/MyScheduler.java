package org.acme;


import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

import org.acme.logger.RequestLoggingContext;

@ApplicationScoped
public class MyScheduler {

    @Inject
    RequestLoggingContext requestLoggingContext;

    @Inject
    MyScheduledExecutor myScheduledExecutor;

    @Scheduled(every = "10s")
    @ActivateRequestContext
    void run() {
        String transactionId = UUID.randomUUID().toString();
        requestLoggingContext.createTransactionId();
        System.out.println("[Scheduler] Start execution with Transaction-ID: " + transactionId);
        myScheduledExecutor.step1();
        myScheduledExecutor.step2();
    }
}
