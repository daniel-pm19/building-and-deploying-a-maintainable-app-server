package com.escuelaing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class WebFrameworkIntegrationTest {

    private static final int TEST_PORT = 18080;

    @Test
    void servesRegisteredRouteAndShutsDownGracefullyAfterShutdownRoute()
        throws Exception {
        WebFramework webFramework = new WebFramework();
        webFramework.get("/greet", (req, resp) -> "Hello test");
        webFramework.get("/shutdown", (req, resp) -> {
            webFramework.stop();
            return "Server will stop after this response.";
        });

        Thread serverThread = new Thread(() -> {
            try {
                webFramework.start(TEST_PORT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
        waitUntilPortIsOpen();

        String greetResponse = rawGet("/greet");
        assertTrue(greetResponse.contains("HTTP/1.1 200 OK"));
        assertTrue(greetResponse.contains("Hello test"));

        String shutdownResponse = rawGet("/shutdown");
        assertTrue(shutdownResponse.contains("Server will stop after this response."));

        serverThread.join(TimeUnit.SECONDS.toMillis(5));
        assertFalse(serverThread.isAlive());
    }

    private void waitUntilPortIsOpen() throws InterruptedException {
        for (int attempt = 0; attempt < 50; attempt++) {
            try (Socket probe = new Socket("localhost", TEST_PORT)) {
                return;
            } catch (IOException e) {
                Thread.sleep(100);
            }
        }
        throw new IllegalStateException("Server never started listening on port " + TEST_PORT);
    }

    private String rawGet(String path) throws IOException {
        try (Socket socket = new Socket("localhost", TEST_PORT)) {
            OutputStream out = socket.getOutputStream();
            out.write(("GET " + path + " HTTP/1.1\r\nHost: localhost\r\n\r\n").getBytes());
            out.flush();

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append('\n');
            }
            return response.toString();
        }
    }
}
