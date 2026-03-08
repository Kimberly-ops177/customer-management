package com.kimberly.customermanagement.service;

import com.kimberly.customermanagement.dto.CustomerDTO;
import com.kimberly.customermanagement.exception.CustomerException.*;
import com.kimberly.customermanagement.model.Customer;
import com.kimberly.customermanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        mockCustomer = Customer.builder()
                .id(1L)
                .firstName("Kimberly")
                .lastName("Githinji")
                .email("kimberly@test.com")
                .phoneNumber("+254712345678")
                .address("Nairobi, Kenya")
                .status(Customer.CustomerStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Should return all customers")
    void getAllCustomers_ShouldReturnList() {
        when(customerRepository.findAll()).thenReturn(List.of(mockCustomer));

        List<CustomerDTO.Response> result = customerService.getAllCustomers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("kimberly@test.com");
        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Should return customer by ID")
    void getCustomerById_WhenExists_ShouldReturnCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        CustomerDTO.Response result = customerService.getCustomerById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Kimberly");
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void getCustomerById_WhenNotExists_ShouldThrowException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should create customer successfully")
    void createCustomer_WithValidData_ShouldReturnCreatedCustomer() {
        CustomerDTO.CreateRequest request = CustomerDTO.CreateRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@test.com")
                .phoneNumber("+254700000000")
                .address("Nairobi")
                .build();

        when(customerRepository.existsByEmail("jane@test.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        CustomerDTO.Response result = customerService.createCustomer(request);

        assertThat(result).isNotNull();
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void createCustomer_WhenEmailExists_ShouldThrowException() {
        CustomerDTO.CreateRequest request = CustomerDTO.CreateRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("existing@test.com")
                .build();

        when(customerRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("existing@test.com");
    }

    @Test
    @DisplayName("Should update customer successfully")
    void updateCustomer_WhenExists_ShouldReturnUpdated() {
        CustomerDTO.UpdateRequest request = CustomerDTO.UpdateRequest.builder()
                .firstName("Updated")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        CustomerDTO.Response result = customerService.updateCustomer(1L, request);

        assertThat(result).isNotNull();
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should delete customer successfully")
    void deleteCustomer_WhenExists_ShouldDelete() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        assertThatCode(() -> customerService.deleteCustomer(1L)).doesNotThrowAnyException();
        verify(customerRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent customer")
    void deleteCustomer_WhenNotExists_ShouldThrowException() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> customerService.deleteCustomer(99L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("Should search customers by name")
    void searchCustomers_ShouldReturnMatchingCustomers() {
        when(customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "kim", "kim")).thenReturn(List.of(mockCustomer));

        List<CustomerDTO.Response> result = customerService.searchCustomers("kim");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Kimberly");
    }
}
