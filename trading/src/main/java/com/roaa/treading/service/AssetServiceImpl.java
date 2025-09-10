package com.roaa.treading.service;

import com.roaa.treading.entity.Asset;
import com.roaa.treading.entity.Coin;
import com.roaa.treading.entity.User;
import com.roaa.treading.repository.AssetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssetServiceImpl implements AssetService{

    private final AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        return null;
    }

    @Override
    public Asset getAssetById(Long id) {
        return null;
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        return List.of();
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        return null;
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return null;
    }

    @Override
    public void deleteAsset(Long assetId) {

    }
}
