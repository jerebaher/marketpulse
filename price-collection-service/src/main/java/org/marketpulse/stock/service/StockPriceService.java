package org.marketpulse.stock.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketpulse.client.AlphaVantageClient;
import org.marketpulse.common.exception.StockPriceException;
import org.marketpulse.stock.model.StockPriceResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class StockPriceService {

    private final AlphaVantageClient alphaVantageClient;

    public StockPriceResponse getStockPrice(String symbol) {
        try {
            Map<String, Object> stockData = alphaVantageClient.getStockPrice(symbol);
            StockPriceResponse stockPriceResponse = mapToStockPriceResponse(symbol, stockData);

            return stockPriceResponse;
        } catch (Exception e) {
            log.error("Error retrieving stock data", e);
            throw new StockPriceException("Something went wrong. Could not get stock price for: "+symbol);
        }
    }

    private StockPriceResponse mapToStockPriceResponse(String symbol, Map<String, Object> stockData) {
        if (stockData == null || !stockData.containsKey("Time Series (5min)")) {
            throw new IllegalArgumentException("Invalid stock data. No time series data found.");
        }

        Map<String, Object> timeSeries = (Map<String, Object>) stockData.get("Time Series (5min)");

        String latestTimestamp = timeSeries.keySet().iterator().next();
        Map<String, Object> latestData = (Map<String, Object>) timeSeries.get(latestTimestamp);
        BigDecimal price = new BigDecimal((String) latestData.get("1. open"));

        return new StockPriceResponse(symbol, price, latestTimestamp);
    }
}
