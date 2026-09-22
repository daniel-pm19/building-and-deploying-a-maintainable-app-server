package com.escuelaing;

import java.io.IOException;
import java.io.InputStream;

public class StaticFileService {

    private String staticDir = "";

    public void setStaticDir(String staticDir) {
        this.staticDir = staticDir;
    }

    public record StaticResource(byte[] data, String contentType) {}

    public StaticResource resolve(String path) {
        String resourcePath = "/".equals(path) ? "/index.html" : path;
        while (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }

        String name = staticDir + "/" + resourcePath;
        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        try (
            InputStream is = getClass().getClassLoader().getResourceAsStream(name)
        ) {
            if (is == null) {
                return null;
            }
            byte[] data = is.readAllBytes();
            return new StaticResource(data, contentType(resourcePath));
        } catch (IOException e) {
            return null;
        }
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
