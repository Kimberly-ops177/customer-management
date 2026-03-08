package com.kimberly.customermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimberly.customermanagement.dto.CustomerDTO;
import com.kimberly.customermanagement.exception.CustomerException.*;
import com.kimberly.customermanagement.model.Customer;
import com.kimberly.customermanagement.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@DisplayName("CustomerController Integration Tests")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDTO.Response mockResponse() {
        return CustomerDTO.Response.builder()
                .id(1L)
                .firstName("Kimberly")
                .lastName("Githinji")
                .email("kimberly@test.com")
                .phoneNumber("+254712345678")
                .status(Customer.CustomerStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/customers - Should return all customers")
    void getAllCustomers_ShouldReturn200() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("kimberly@test.com"))
                .andExpect(jsonPath("$[0].firstName").value("Kimberly"));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} - Should return customer by ID")
    void getCustomerById_ShouldReturn200() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(mockResponse());

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Githinji"));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} - Should return 404 when not found")
    void getCustomerById_WhenNotFound_ShouldReturn404() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new CustomerNotFoundException(99L));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/customers - Should create customer")
    void createCustomer_WithValidData_ShouldReturn201() throws Exception {
        CustomerDTO.CreateRequest request = CustomerDTO.CreateRequest.builder()
                .firstName("Kimberly")
                .lastName("Githinji")
                .email("kimberly@test.com")
                .phoneNumber("+254712345678")
                .build();

        when(customerService.createCustomer(any())).thenReturn(mockResponse());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("kimberly@test.com"));
    }

    @Test
    @DisplayName("POST /api/v1/customers - Should return 400 for invalid data")
    void createCustomer_WithInvalidData_ShouldReturn400() throws Exception {
        CustomerDTO.CreateRequest request = CustomerDTO.CreateRequest.builder()
                .firstName("")
                .email("not-valid-email")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v1/customers/{id} - Should update customer")
    void updateCustomer_ShouldReturn200() throws Exception {
        CustomerDTO.UpdateRequest request = CustomerDTO.UpdateRequest.builder()
                .firstName("Updated")
                .build();

        when(customerService.updateCustomer(eq(1L), any())).thenReturn(mockResponse());

        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v1/customers/{id} - Should delete customer")
    void deleteCustomer_ShouldReturn204() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());
    }
}
