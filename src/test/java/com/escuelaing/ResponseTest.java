package com.escuelaing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResponseTest {

    @Test
    void defaultStatusIsTwoHundred() {
        assertEquals(200, new Response().getStatus());
    }

    @Test
    void setStatusAndGetStatusRoundTrip() {
        Response response = new Response();

        response.setStatus(404);
        assertEquals(404, response.getStatus());

        response.setStatus(500);
        assertEquals(500, response.getStatus());
    }
}
