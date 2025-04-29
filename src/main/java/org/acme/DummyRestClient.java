package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@RegisterRestClient(baseUri = "https://postman-echo.com")
@Path("/get")
public interface DummyRestClient {

    @GET
    String call(@QueryParam("name") String name);
}
