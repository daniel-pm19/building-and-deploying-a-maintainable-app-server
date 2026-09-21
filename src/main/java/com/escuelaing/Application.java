package com.escuelaing;

public class Application {

    public static void main(String[] args) throws Exception {
        WebFramework webFramework = new WebFramework();

        String staticFilePath = System.getenv()
                .getOrDefault("STATIC_FILES_PATH", "/webroot");

        webFramework.staticfiles(staticFilePath);

        String greetingEnPrefix = System.getenv()
                .getOrDefault("GREETING_EN_PREFIX", "Hello");

        webFramework.get("/hello", (req, resp) -> {
            String name = req.getValue("name");
            String language = req.getValue("language");

            if (name == null || name.isBlank()) {
                name = "John Doe";
            }

            if (language == null) {
                language = "en";
            }

            switch (language) {
                case "en":
                    return greetingEnPrefix + name;
                case "es":
                    return "Hola " + name;
                case "fra":
                    return "Bonjour " + name;
                default:
                    return greetingEnPrefix + name;
            }
        });

        webFramework.get("/pi", (req, resp) -> String.valueOf(Math.PI));

        webFramework.get("/e", (req, resp) -> String.valueOf(Math.E));

        webFramework.get("/images", (req, resp) -> {
            String imageid = req.getValue("imageid");

            if (imageid == null || imageid.isBlank()) {
                resp.setStatus(404);
                return "Not Found";
            }

            return "Imagen: " + imageid + ".png";
        });

        webFramework.get("/sin", (req, resp) -> {
            String nStr = req.getValue("n");

            if(nStr == null || nStr.isBlank()){
                resp.setStatus(400);
                return "Bad Request";
            }

            return String.valueOf(Math.sin(Integer.parseInt(nStr)));
        });

        webFramework.get("/unknown", (req, resp) -> {
           resp.setStatus(404);
          return "Not Found";
        });

        String environment = System.getenv()
                .getOrDefault("APP_ENV", "development");

        webFramework.get("/shutdown", (req, resp) -> {
            if(environment.equals("development")){
                webFramework.stop();
                return "Server will stop after this response.";
            }
            resp.setStatus(405);
            return "Not Allowed Operation";

        });

        webFramework.start();
    }
}
