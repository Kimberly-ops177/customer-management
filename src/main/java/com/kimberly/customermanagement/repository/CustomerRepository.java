package com.kimberly.customermanagement.repository;

import com.kimberly.customermanagement.model.Customer;
import com.kimberly.customermanagement.model.Customer.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Customer> findByStatus(CustomerStatus status);

    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE' ORDER BY c.createdAt DESC")
    List<Customer> findAllActiveCustomers();

    long countByStatus(CustomerStatus status);
}
