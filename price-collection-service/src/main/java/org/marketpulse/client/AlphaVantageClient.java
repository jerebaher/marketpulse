package org.marketpulse.client;

import org.marketpulse.config.alphavantage.AlphaVantageConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AlphaVantageClient {

    private final AlphaVantageConfig config;
    private final RestTemplate restTemplate;

    public AlphaVantageClient(AlphaVantageConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> getStockPrice(String symbol) {
        String url = String.format("%s?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=%s",
                config.getBaseUrl(), symbol, config.getApiKey());

        return restTemplate.getForObject(url, Map.class);
    }
}
