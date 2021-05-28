package com.javamaster.repository;

import com.javamaster.model.BitcoinPayment;
import com.javamaster.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BitcoinPaymentRepository extends JpaRepository<BitcoinPayment, Long> {

    Optional<BitcoinPayment> findById(Long id);

}