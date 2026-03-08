package com.kimberly.customermanagement.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Customer Model Unit Tests")
class CustomerTest {

    @Test
    @DisplayName("Should create customer with builder")
    void shouldCreateCustomerWithBuilder() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Kimberly")
                .lastName("Githinji")
                .email("kimberly@test.com")
                .phoneNumber("+254712345678")
                .address("Nairobi, Kenya")
                .status(Customer.CustomerStatus.ACTIVE)
                .build();

        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getFirstName()).isEqualTo("Kimberly");
        assertThat(customer.getLastName()).isEqualTo("Githinji");
        assertThat(customer.getEmail()).isEqualTo("kimberly@test.com");
        assertThat(customer.getPhoneNumber()).isEqualTo("+254712345678");
        assertThat(customer.getAddress()).isEqualTo("Nairobi, Kenya");
        assertThat(customer.getStatus()).isEqualTo(Customer.CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should set default status as ACTIVE")
    void shouldSetDefaultStatusAsActive() {
        Customer customer = Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@test.com")
                .build();

        assertThat(customer.getStatus()).isEqualTo(Customer.CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should set timestamps on onCreate")
    void shouldSetTimestampsOnCreate() {
        Customer customer = new Customer();
        customer.onCreate();

        assertThat(customer.getCreatedAt()).isNotNull();
        assertThat(customer.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should update updatedAt on onUpdate")
    void shouldUpdateTimestampOnUpdate() throws InterruptedException {
        Customer customer = new Customer();
        customer.onCreate();
        LocalDateTime original = customer.getUpdatedAt();

        Thread.sleep(10);
        customer.onUpdate();

        assertThat(customer.getUpdatedAt()).isAfterOrEqualTo(original);
    }

    @Test
    @DisplayName("Should support all customer statuses")
    void shouldSupportAllStatuses() {
        assertThat(Customer.CustomerStatus.values())
                .containsExactlyInAnyOrder(
                        Customer.CustomerStatus.ACTIVE,
                        Customer.CustomerStatus.INACTIVE,
                        Customer.CustomerStatus.SUSPENDED
                );
    }

    @Test
    @DisplayName("Should set and get all fields via setters")
    void shouldSetAndGetAllFields() {
        Customer customer = new Customer();
        customer.setId(2L);
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setEmail("test@test.com");
        customer.setPhoneNumber("+254700000000");
        customer.setAddress("Mombasa, Kenya");
        customer.setStatus(Customer.CustomerStatus.INACTIVE);

        assertThat(customer.getId()).isEqualTo(2L);
        assertThat(customer.getFirstName()).isEqualTo("Test");
        assertThat(customer.getLastName()).isEqualTo("User");
        assertThat(customer.getEmail()).isEqualTo("test@test.com");
        assertThat(customer.getStatus()).isEqualTo(Customer.CustomerStatus.INACTIVE);
    }

    @Test
    @DisplayName("Should support SUSPENDED status")
    void shouldSupportSuspendedStatus() {
        Customer customer = Customer.builder()
                .firstName("Suspended")
                .lastName("User")
                .email("suspended@test.com")
                .status(Customer.CustomerStatus.SUSPENDED)
                .build();

        assertThat(customer.getStatus()).isEqualTo(Customer.CustomerStatus.SUSPENDED);
    }
}