package com.turing.banka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.turing.banka.exceptions.ApiError;
import com.turing.banka.exceptions.CustomException;
import com.turing.banka.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFound() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/users/1");
        CustomException exception = new CustomException("User not found", HttpStatus.NOT_FOUND);

        // When
        ResponseEntity<ApiError> response = handler.handleCustomException(exception, request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiError error = response.getBody();
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("User not found", error.getMessage());
        assertEquals("/api/users/1", error.getPath());
        assertNotNull(error.getTimestamp());
    }
}
