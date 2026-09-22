package com.escuelaing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StaticFileServiceTest {

    private StaticFileService staticFileService;

    @BeforeEach
    void setUp() {
        staticFileService = new StaticFileService();
        staticFileService.setStaticDir("/webroot");
    }

    @Test
    void rootPathResolvesToIndexHtml() {
        StaticFileService.StaticResource resource = staticFileService.resolve("/");

        assertEquals("text/html; charset=UTF-8", resource.contentType());
        assertTrue(resource.data().length > 0);
    }

    @Test
    void cssFileResolvesWithCssContentType() {
        StaticFileService.StaticResource resource = staticFileService.resolve(
            "/styles.css"
        );

        assertEquals("text/css", resource.contentType());
    }

    @Test
    void jsFileResolvesWithJavascriptContentType() {
        StaticFileService.StaticResource resource = staticFileService.resolve(
            "/app.js"
        );

        assertEquals("application/javascript", resource.contentType());
    }

    @Test
    void binaryImageResolvesWithPngContentTypeAndNonEmptyBytes() {
        StaticFileService.StaticResource resource = staticFileService.resolve(
            "/images/logo.png"
        );

        assertEquals("image/png", resource.contentType());
        assertTrue(resource.data().length > 0);
    }

    @Test
    void unknownResourceResolvesToNull() {
        assertNull(staticFileService.resolve("/does-not-exist.html"));
    }
}
