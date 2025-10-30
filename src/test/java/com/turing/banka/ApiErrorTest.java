package com.turing.banka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.turing.banka.exceptions.ApiError;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApiErrorTest {

    @Test
    void testApiErrorBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        ApiError error = ApiError.builder()
            .timestamp(now)
            .status(404)
            .error("Not Found")
            .message("Resource not found")
            .path("/api/users/1")
            .details(List.of("User not found in database"))
            .build();

        assertEquals(now, error.getTimestamp());
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("Resource not found", error.getMessage());
        assertEquals("/api/users/1", error.getPath());
        assertNotNull(error.getDetails());
        assertEquals(1, error.getDetails().size());
        assertTrue(error.getDetails().get(0).contains("User not found"));
    }

    @Test
    void testToStringContainsExpectedFields() {
        ApiError error = ApiError.builder()
            .status(400)
            .error("Bad Request")
            .message("Invalid input")
            .build();

        String output = error.toString();
        assertTrue(output.contains("Bad Request"));
        assertTrue(output.contains("Invalid input"));
        assertTrue(output.contains("status=400"));
    }
}
