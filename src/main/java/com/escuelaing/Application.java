package com.escuelaing;

public class Application {

    public static void main(String[] args) throws Exception {
        WebFramework webFramework = new WebFramework();

        webFramework.staticfiles("/webroot");

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
                    return "Hello" + name;
                case "es":
                    return "Hola" + name;
                case "fra":
                    return "Bonjour" + name;
                default:
                    return "Hello" + name;
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

        webFramework.start();
    }
}
