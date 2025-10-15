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
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long coinId) {
        return assetRepository.getAssetByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        Asset asset = getAssetById(assetId);
        asset.setQuantity(quantity);
        return assetRepository.save(asset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, Long coinId) {

        return assetRepository.getAssetByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new RuntimeException("Asset not found with id: " + assetId);
        }
        assetRepository.deleteById(assetId);;
    }
}
