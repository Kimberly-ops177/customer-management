package com.kimberly.customermanagement.exception;

import com.kimberly.customermanagement.exception.CustomerException.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerException Unit Tests")
class CustomerExceptionTest {

    @Test
    @DisplayName("CustomerNotFoundException should contain id in message")
    void customerNotFoundExceptionShouldContainId() {
        CustomerNotFoundException ex = new CustomerNotFoundException(42L);
        assertThat(ex.getMessage()).contains("42");
    }

    @Test
    @DisplayName("EmailAlreadyExistsException should contain email in message")
    void emailAlreadyExistsShouldContainEmail() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("test@test.com");
        assertThat(ex.getMessage()).contains("test@test.com");
    }

    @Test
    @DisplayName("CustomerNotFoundException should be RuntimeException")
    void customerNotFoundShouldBeRuntimeException() {
        CustomerNotFoundException ex = new CustomerNotFoundException(1L);
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("EmailAlreadyExistsException should be RuntimeException")
    void emailAlreadyExistsShouldBeRuntimeException() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("email@test.com");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("ErrorResponse should store status and message")
    void errorResponseShouldStoreValues() {
        GlobalExceptionHandler.ErrorResponse response =
                new GlobalExceptionHandler.ErrorResponse(404, "Not found");

        assertThat(response.status()).isEqualTo(404);
        assertThat(response.message()).isEqualTo("Not found");
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.errors()).isNull();
    }

    @Test
    @DisplayName("ErrorResponse with errors should store all fields")
    void errorResponseWithErrorsShouldStoreAllFields() {
        java.util.Map<String, String> errors = new java.util.HashMap<>();
        errors.put("email", "must be valid");

        GlobalExceptionHandler.ErrorResponse response =
                new GlobalExceptionHandler.ErrorResponse(400, "Validation failed", errors);

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.message()).isEqualTo("Validation failed");
        assertThat(response.errors()).isEqualTo(errors);
        assertThat(response.timestamp()).isNotNull();
    }
}