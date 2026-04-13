package com.javatraining.bank.repository;

import com.javatraining.bank.domain.Account;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {

  List<Account> findAllByOrderByCreatedAtAsc();
}
