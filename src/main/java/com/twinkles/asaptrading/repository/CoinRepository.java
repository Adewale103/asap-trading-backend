package com.twinkles.asaptrading.repository;

import com.twinkles.asaptrading.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    boolean existsByName(String name);
    Optional<Coin> getCoinByName(String name);
}
