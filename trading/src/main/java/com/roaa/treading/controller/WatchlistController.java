package com.roaa.treading.controller;

import com.roaa.treading.entity.Coin;
import com.roaa.treading.entity.User;
import com.roaa.treading.entity.WatchList;
import com.roaa.treading.service.CoinService;
import com.roaa.treading.service.UserService;
import com.roaa.treading.service.WatchListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchListService watchListService;
    private final UserService userService;
    private final CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchlist(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());
        return ResponseEntity.ok(watchList);

    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<WatchList> getWatchlistById(
            @PathVariable Long watchlistId
    ) throws Exception{
        WatchList watchList = watchListService.findById(watchlistId);
        return ResponseEntity.ok(watchList);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) throws Exception {
      User user = userService.findUserByJwt(jwt);
      Coin coin = coinService.findById(coinId);
      Coin addedCoin = watchListService.addItemToWatchList(coin, user);
      return ResponseEntity.ok(addedCoin);
    }
}
