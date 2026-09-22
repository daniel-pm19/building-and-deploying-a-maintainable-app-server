package com.escuelaing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RequestTest {

    @Test
    void parsesMultipleQueryParameters() {
        Request request = Request.fromQuery("name=Ana&language=es");

        assertEquals("Ana", request.getValue("name"));
        assertEquals("es", request.getValue("language"));
    }

    @Test
    void parameterWithoutValueDefaultsToEmptyString() {
        Request request = Request.fromQuery("flag");

        assertEquals("", request.getValue("flag"));
    }

    @Test
    void missingKeyReturnsNullWithoutThrowing() {
        Request request = Request.fromQuery("name=Ana");

        assertNull(request.getValue("language"));
    }

    @Test
    void emptyQueryProducesNoEntries() {
        Request request = Request.fromQuery("");

        assertNull(request.getValue("anything"));
        assertTrue(request.body.isEmpty());
    }
}
