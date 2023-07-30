package com.twinkles.asaptrading.repository;

import com.twinkles.asaptrading.entity.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {
}
