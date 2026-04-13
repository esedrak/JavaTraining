package com.javatraining.bank.repository;

import com.javatraining.bank.domain.Transfer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

  List<Transfer> findAllByOrderByCreatedAtDesc();
}
