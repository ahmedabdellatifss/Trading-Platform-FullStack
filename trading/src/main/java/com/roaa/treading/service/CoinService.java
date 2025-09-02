package com.roaa.treading.service;

import com.roaa.treading.entity.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetail(String coinId) throws Exception;

    Coin findById(String coinId) throws Exception;

    String searchCoin(String keyword) throws Exception;

    String getTo50CoinByMarketCapRank() throws Exception;

    String getTreadingCoins() throws Exception;
}
