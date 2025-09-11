package com.roaa.treading.service;


import com.roaa.treading.entity.Coin;
import com.roaa.treading.entity.User;
import com.roaa.treading.entity.WatchList;

public interface WatchListService {

    WatchList findUserWatchList(Long userId) throws Exception;

    WatchList createWatchList(User user);

    WatchList findById(Long id) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;


}
