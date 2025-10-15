package com.roaa.treading.controller;

import com.roaa.treading.entity.Asset;
import com.roaa.treading.entity.User;
import com.roaa.treading.service.AssetService;
import com.roaa.treading.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAsset(@PathVariable("assetId") Long assetId) {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long coinId
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssetsForUser(
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserByJwt(jwt);
        List<Asset> assets = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok(assets);

    }



}
