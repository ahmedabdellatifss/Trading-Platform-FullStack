package com.roaa.treading.repository;

import com.roaa.treading.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    Optional<Coin> findByApiId(String apiId);
}
