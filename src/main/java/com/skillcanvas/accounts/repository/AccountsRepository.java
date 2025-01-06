package com.skillcanvas.accounts.repository;

import com.skillcanvas.accounts.entiity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
}
