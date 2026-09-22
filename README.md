# Building and Deploying a Maintainable App Server

Mini framework web HTTP/1.1 escrito en Java puro (sin dependencias externas), estilo Spark/Javalin, con soporte para:

- Servir recursos estáticos (incluidos **binarios**: imágenes PNG/JPEG, iconos, etc.) desde el classpath.
- Registrar rutas dinámicas con **lambdas** (`webFramework.get("/ruta", (req, resp) -> ...)`).
- Leer **parámetros de query string** (`/hello?name=Pedro&language=es`).
- Resolución de rutas con fallback a archivos estáticos y códigos de error HTTP (400/404/405/500).
- Configuración por **variables de entorno** (puerto, saludos, entorno).
- Apagado controlado (`/shutdown`, solo en entorno de desarrollo).

---

## Cómo ejecutar (local)

```bash
mvn package
java -jar target/building-and-deploying-a-maintainable-app-server-1.0-SNAPSHOT.jar
```

Con esto queda escuchando en `http://localhost:8080`.

---

## Cómo probarlo desde el navegador

Abre `http://localhost:8080/` → verás una página demo con 5 tarjetas que llaman a los servicios dinámicos vía `fetch()`:

| Tarjeta | Petición |
|---|---|
| Saludar | `GET /hello?name=Name&language=Language` |
| Pi | `GET /pi` |
| E | `GET /e` |
| Seno | `GET /sin?n=Valor` |
| Imágenes | `GET /images?imageid=Valor` |

También puedes probar con curl:

```bash
curl "http://localhost:8080/hello?name=Ana&language=es"   # → Hola Ana
curl http://localhost:8080/pi                            # → 3.1415...
curl http://localhost:8080/sin?n=3                        # → 0.1411...
curl http://localhost:8080/images?imageid=logo            # → Imagen: logo.png
curl --output /tmp/logo.png http://localhost:8080/images/logo.png   # binario
```

---

## Rutas dinámicas registradas (en `Application.java`)

| Ruta | Lambda | Notas |
|---|---|---|
| `/hello` | `(req, resp) -> ...` | Saluda según `name` y `language` (en/es/fra). `name` vacío o ausente → `John Doe`. |
| `/pi` | `(req, resp) -> String.valueOf(Math.PI)` | |
| `/e` | `(req, resp) -> String.valueOf(Math.E)` | |
| `/sin` | `(req, resp) -> ...` | `sin(n)` con `n` del query. Sin valor → `400 Bad Request`. |
| `/images` | `(req, resp) -> ...` | Devuelve el `<imageid>.png`. Sin valor → `404`. |
| `/shutdown` | `(req, resp) -> ...` | Detiene el servidor. **Solo disponible** si `APP_ENV=development` (default). |

---

## Variables de entorno

| Variable | Default | Uso |
|---|---|---|
| `PORT` | `8080` | Puerto HTTP del servidor |
| `GREETING_EN_PREFIX` | `Hello` | Prefijo de saludo en inglés |
| `STYLES_HELLO_LANGUAGE` | … | (opcional) hook de estilos |
| `STATIC_FILES_PATH` | `webroot` | Carpeta de estáticos (en classpath) |
| `APP_ENV` | `development` | `development` habilita `/shutdown` |
| `APP_NAME` | `webapp` | Nombre de la app (saluda en consola) |

Ejemplo:

```bash
PORT=9090 GREETING_EN_PREFIX="Hey" APP_ENV=production \
    java -jar target/building-and-deploying-a-maintainable-app-server-1.0-SNAPSHOT.jar
```

---

## Pruebas

```bash
mvn test
```

---

## Docker

```bash
docker build -t app-server .
docker run -p 8080:8080 app-server
```

---

## Licencia

Proyecto académico — Semestre 8, TDSE — Material de clase.
