package org.acme;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MyScheduledExecutor {

    @Inject
    @RestClient
    DummyRestClient client;

    public void step1() {
        String response = client.call("Step 1");
        System.out.println("[MyScheduledExecutor] Received response: " + response);
    }
 
    public void step2() {
        String response = client.call("Step 2");
        System.out.println("[MyScheduledExecutor] Received response: " + response);
    }

}
