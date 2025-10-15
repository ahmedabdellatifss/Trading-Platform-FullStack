package com.roaa.treading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roaa.treading.entity.Coin;
import com.roaa.treading.repository.CoinRepository;
import com.roaa.treading.response.CoinApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Coin> getCoinList(int page) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Deserialize to DTO first
            List<CoinApiResponse> apiResponses = objectMapper.readValue(response.getBody(),
                    new TypeReference<>() {
                    });
            // Convert to your Coin entities
            return apiResponses.stream()
                    .map(this::convertToCoin)
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception("API call failed: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new Exception("Failed to parse API response: " + e.getMessage());
        }
    }

    public Long generateNumericIdFromString(String coinId) {
        return (long) Math.abs(coinId.hashCode()); // Not guaranteed unique, but works for local caching
    }

    private Coin convertToCoin(CoinApiResponse apiResponse) {
        Coin coin = new Coin();
        coin.setId(generateNumericIdFromString(apiResponse.getId()));
        coin.setSymbol(apiResponse.getSymbol());
        coin.setName(apiResponse.getName());
        coin.setImage(apiResponse.getImage());
        if (apiResponse.getCurrentPrice() != null) {
            coin.setCurrentPrice(apiResponse.getCurrentPrice().doubleValue());
        }
        coin.setMarketCap(apiResponse.getMarketCap());
        coin.setMarketCapRank(apiResponse.getMarketCapRank());
        if (apiResponse.getFullyDilutedValuation() != null) {
            coin.setFullyDilutedValuation(BigDecimal.valueOf(apiResponse.getFullyDilutedValuation()));
        }
        coin.setTotalVolume(apiResponse.getTotalVolume());
        if(apiResponse.getHigh24h() != null){
            coin.setHigh24h(apiResponse.getHigh24h().longValue());
        }
        if(apiResponse.getLow24h() != null){
            coin.setLow24h(apiResponse.getLow24h().doubleValue());
        }
        if (apiResponse.getPriceChange24h() != null){
            coin.setPriceChange24h(apiResponse.getPriceChange24h().doubleValue());
        }
        if (apiResponse.getPriceChangePercentage24h() != null) {
            coin.setPriceChangePercentage24h(apiResponse.getPriceChangePercentage24h().doubleValue());
        }
        if(apiResponse.getMarketCapChange24h() != null){
            coin.setMarketCapChange24h(apiResponse.getMarketCapChange24h().longValue());
        }
        if(apiResponse.getMarketCapChangePercentage24h() != null){
            coin.setMarketCapChangePercentage24h(apiResponse.getMarketCapChangePercentage24h().doubleValue());
        }

        if(apiResponse.getCirculatingSupply() != null){
            coin.setCirculatingSupply(apiResponse.getCirculatingSupply().doubleValue());
        }
        if(apiResponse.getTotalSupply() != null){
            coin.setTotalSupply(apiResponse.getTotalSupply().doubleValue());
        }
        if(apiResponse.getMaxSupply() !=  null){
            coin.setMaxSupply(apiResponse.getMaxSupply().doubleValue());
        }
        coin.setAth(apiResponse.getAth());
        if(apiResponse.getAthChangePercentage() != null){
            coin.setAthChangePercentage(apiResponse.getAthChangePercentage().doubleValue());
        }
        coin.setAthDate(apiResponse.getAthDate());
        coin.setAtl(apiResponse.getAtl().doubleValue());
        if(apiResponse.getAtlChangePercentage() != null){
            coin.setAtlChangePercentage(apiResponse.getAtlChangePercentage().doubleValue());
        }
        coin.setAtlDate(apiResponse.getAtlDate());

        coin.setLastUpdated(apiResponse.getLastUpdated());
        return coin;
    }


    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.getForObject(url, String.class);
            //System.out.println("Response of Market Chart ---> " + response);

            if (response == null || response.isEmpty()) {
                throw new Exception("Empty response from CoinGecko API");
            }

            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error from API: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw new Exception("API Error: " + e.getMessage(), e);
        }
    }


    @Override
    public String getCoinDetail(String coinId) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            Coin coin = new Coin();
            coin.setApiId(jsonNode.get("id").asText());
            coin.setName(jsonNode.get("name").asText());
            coin.setSymbol(jsonNode.get("symbol").asText());
            coin.setImage(jsonNode.get("image").get("large").asText());

            JsonNode marketData = jsonNode.get("market_data");
            coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
            coin.setHigh24h(marketData.get("high_24h").get("usd").asLong());
            coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
            coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            coin.setTotalSupply(marketData.get("total_supply").asDouble());

            coinRepository.save(coin);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            System.out.println("error---- " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> coin = coinRepository.findByApiId(coinId);
        if(coin.isEmpty()) throw new Exception("coin not found!");
        return coin.get();
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        String url = "https://api.coingecko.com/api/v3/search?query="+keyword;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTo50CoinByMarketCapRank() throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTrendingCoins() throws Exception {
        String url = "https://api.coingecko.com/api/v3/search/trending";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);

            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }
}
