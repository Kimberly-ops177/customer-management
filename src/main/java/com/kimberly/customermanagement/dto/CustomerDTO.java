package com.kimberly.customermanagement.dto;

import com.kimberly.customermanagement.model.Customer.CustomerStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

public class CustomerDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50)
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50)
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;

        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
        private String phoneNumber;

        @Size(max = 255)
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        @Size(min = 2, max = 50)
        private String firstName;

        @Size(min = 2, max = 50)
        private String lastName;

        @Email(message = "Email must be valid")
        private String email;

        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
        private String phoneNumber;

        @Size(max = 255)
        private String address;

        private CustomerStatus status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String address;
        private CustomerStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
