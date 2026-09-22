package com.escuelaing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RouterTest {

    @Test
    void resolveReturnsTheRegisteredHandler() {
        Router router = new Router();
        router.add("/hello", (req, resp) -> "Hello world!");

        WebService service = router.resolve("/hello");

        assertEquals("Hello world!", service.invoque(new Request(), new Response()));
    }

    @Test
    void resolveReturnsNullForAnUnregisteredRoute() {
        Router router = new Router();

        assertNull(router.resolve("/does-not-exist"));
    }
}
