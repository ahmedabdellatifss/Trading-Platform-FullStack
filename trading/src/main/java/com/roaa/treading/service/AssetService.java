package com.roaa.treading.service;

import com.roaa.treading.entity.Asset;
import com.roaa.treading.entity.Coin;
import com.roaa.treading.entity.User;

import java.util.List;

public interface AssetService {
    Asset createAsset(User user, Coin coin, double quantity);
    Asset getAssetById(Long id);
    Asset getAssetByUserIdAndId(Long userId, Long assetId);
    List<Asset> getUsersAssets(Long userId);
    Asset updateAsset(Long assetId, double quantity);
    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long assetId);
}
