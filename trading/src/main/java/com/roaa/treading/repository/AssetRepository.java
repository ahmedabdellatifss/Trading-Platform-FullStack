package com.roaa.treading.repository;

import com.roaa.treading.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByUserId(Long userId);
    Asset getAssetByUserIdAndCoinId(Long userId, Long coinId);

}
