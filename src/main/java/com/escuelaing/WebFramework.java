package com.escuelaing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WebFramework {

    Map<String, WebService> webServices = new HashMap<>();
    String staticDir = "";
    private static boolean running = false;

    public void get(String route, WebService ws) {
        webServices.put(route, ws);
    }

    public void start() throws IOException {
        running = true;

        String portValue = System.getenv("PORT");
        int port =
            portValue == null || portValue.isBlank()
                ? 8080
                : Integer.parseInt(portValue);

        try(ServerSocket server = new ServerSocket(port)){
            System.out.println("Listening on port:" + server.getLocalPort());
            while(running){
                try(Socket client = server.accept()){
                    handleRequest(client);
                }
            }
        }

    }

    public void stop(){
        running = false;
    }

    public void staticfiles(String staticfile) {
        this.staticDir = staticfile;
    }

    private void handleRequest(Socket client) {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            )
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }

            String[] parts = requestLine.split(" ");

            String method = "";
            if (parts.length < 2) {
                sendResponse(client, "Bad Request", 400);
                client.close();
                return;
            }

            method = parts[0].toUpperCase();

            if (!"GET".equals(method)) {
                sendResponse(client, "Method Not Allowed", 405);
                client.close();
                return;
            }

            String fullPath = parts[1];

            String path = fullPath.split("\\?")[0];

            WebService service = webServices.get(path);
            if (service == null) {
                if (serveStatic(client, path)) {
                    return;
                }
                sendResponse(client, "Not Found", 404);
                return;
            }

            Request req = new Request();
            Response resp = new Response();

            String query = fullPath.contains("?")
                ? fullPath.split("\\?", 2)[1]
                : "";
            for (String param : query.split("&")) {
                if (param.isEmpty()) {
                    continue;
                }
                String[] kv = param.split("=", 2);
                String key = kv[0];
                String value = kv.length > 1 ? kv[1] : "";
                req.body.put(key, value);
            }

            try {
                String result = service.invoque(req, resp);
                sendResponse(client, result, resp.getStatus());
            } catch (Exception e) {
                sendResponse(client, "Internal Server Error", 500);
            } finally {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean serveStatic(Socket client, String path) {
        String resourcePath = "/".equals(path) ? "/index.html" : path;
        while (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }

        String name = staticDir + "/" + resourcePath;
        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        try (
            InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(name)
        ) {
            if (is == null) {
                return false;
            }

            byte[] data = is.readAllBytes();
            sendBytes(client, data, contentType(resourcePath), 200);
            client.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void sendResponse(Socket client, String body, int status) {
        sendBytes(client, body.getBytes(), "text/plain; charset=UTF-8", status);
    }

    private void sendBytes(
        Socket client,
        byte[] body,
        String contentType,
        int status
    ) {
        try {
            OutputStream out = client.getOutputStream();
            out.write(buildResponse(body, contentType, status));
            out.flush();
        } catch (IOException ignored) {}
    }

    private byte[] buildResponse(byte[] body, String contentType, int status) {
        String statusLine = switch (status) {
            case 200 -> "HTTP/1.1 200 OK\r\n";
            case 400 -> "HTTP/1.1 400 BAD REQUEST\r\n";
            case 404 -> "HTTP/1.1 404 NOT FOUND\r\n";
            case 405 -> "HTTP/1.1 405 NOT ALLOWED\r\n";
            case 500 -> "HTTP/1.1 500 INTERNAL SERVER ERROR\r\n";
            default -> "HTTP/1.1 500 INTERNAL SERVER ERROR\r\n";
        };

        String headers =
            "Content-Type: " +
            contentType +
            "\r\n" +
            "Content-Length: " +
            body.length +
            "\r\n" +
            "Connection: close\r\n" +
            "\r\n";

        byte[] headBytes = (statusLine + headers).getBytes();
        byte[] response = new byte[headBytes.length + body.length];
        System.arraycopy(headBytes, 0, response, 0, headBytes.length);
        System.arraycopy(body, 0, response, headBytes.length, body.length);
        return response;
    }

    private String contentType(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".html")) return "text/html; charset=UTF-8";
        if (lower.endsWith(".css")) return "text/css";
        if (lower.endsWith(".js")) return "application/javascript";
        if (lower.endsWith(".png")) return "image/png";
        if (
            lower.endsWith(".jpg") || lower.endsWith(".jpeg")
        ) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }
}
