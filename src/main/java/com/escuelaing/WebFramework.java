package com.escuelaing;

import java.io.IOException;

public class WebFramework {

    private final Router router = new Router();
    private final StaticFileService staticFileService = new StaticFileService();
    private final HttpServer httpServer = new HttpServer(router, staticFileService);

    public void get(String route, WebService ws) {
        router.add(route, ws);
    }

    public void staticfiles(String staticDir) {
        staticFileService.setStaticDir(staticDir);
    }

    public void start() throws IOException {
        httpServer.start();
    }

    public void start(int port) throws IOException {
        httpServer.start(port);
    }

    public void stop() {
        httpServer.stop();
    }
}
