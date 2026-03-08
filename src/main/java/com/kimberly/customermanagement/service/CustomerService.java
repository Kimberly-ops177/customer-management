package com.kimberly.customermanagement.service;

import com.kimberly.customermanagement.dto.CustomerDTO;
import com.kimberly.customermanagement.exception.CustomerException.*;
import com.kimberly.customermanagement.model.Customer;
import com.kimberly.customermanagement.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDTO.Response> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerDTO.Response getCustomerById(Long id) {
        log.debug("Fetching customer with id: {}", id);
        return customerRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public CustomerDTO.Response createCustomer(CustomerDTO.CreateRequest request) {
        log.debug("Creating customer with email: {}", request.getEmail());
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();
        Customer saved = customerRepository.save(customer);
        log.info("Customer created with id: {}", saved.getId());
        return toResponse(saved);
    }

    public CustomerDTO.Response updateCustomer(Long id, CustomerDTO.UpdateRequest request) {
        log.debug("Updating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (request.getFirstName() != null) customer.setFirstName(request.getFirstName());
        if (request.getLastName() != null) customer.setLastName(request.getLastName());
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException(request.getEmail());
            }
            customer.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) customer.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getStatus() != null) customer.setStatus(request.getStatus());

        Customer updated = customerRepository.save(customer);
        log.info("Customer updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    public void deleteCustomer(Long id) {
        log.debug("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
        log.info("Customer deleted with id: {}", id);
    }

    public List<CustomerDTO.Response> searchCustomers(String query) {
        log.debug("Searching customers with query: {}", query);
        return customerRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerDTO.Response getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }

    private CustomerDTO.Response toResponse(Customer customer) {
        return CustomerDTO.Response.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
