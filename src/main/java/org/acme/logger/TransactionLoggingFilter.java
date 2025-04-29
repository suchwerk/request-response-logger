package org.acme.logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Provider
public class TransactionLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    @Inject
    RequestLoggingContext requestLoggingContext;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String transactionId = requestLoggingContext.getTransactionId();
        if (transactionId != null) {
            requestContext.getHeaders().add("X-Transaction-ID", transactionId);
        }
        Object entity = requestContext.getEntity();
        if (entity != null) {
            System.out.println("[Request Body] " + entity.toString());
        }
        System.out.println("[Request] Transaction-ID: " + transactionId + " -> " + requestContext.getUri());
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        String transactionId = requestLoggingContext.getTransactionId();
        System.out
                .println("[Response] Transaction-ID: " + transactionId + " <- Status: " + responseContext.getStatus());
        if (responseContext.hasEntity()) {
            String body = readAndBufferResponseBody(responseContext);
            System.out.println("[Response Body] " + body);
        }
    }

    /**
     * Reads the body from a ClientResponseContext and resets the stream
     * so it can be read again later.
     *
     * @param responseContext the client response context
     * @return the response body as String, or null if no entity
     * @throws IOException if stream reading fails
     */
    public static String readAndBufferResponseBody(ClientResponseContext responseContext) throws IOException {
        if (!responseContext.hasEntity()) {
            return null;
        }

        InputStream originalStream = responseContext.getEntityStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = originalStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] bodyBytes = buffer.toByteArray();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        // Reset the entity stream so the rest of the application can still use it
        responseContext.setEntityStream(new ByteArrayInputStream(bodyBytes));

        return body;
    }

}
