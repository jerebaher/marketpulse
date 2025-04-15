package org.marketpulse.stockdata.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketpulse.stockdata.model.MovingAverageResponse;
import org.marketpulse.stockdata.model.RecentPricesResponse;
import org.marketpulse.stockdata.model.StockStatistics;
import org.marketpulse.stockdata.service.StockDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for stock data operations
 */
@RestController
@RequestMapping("/market-pulse/api/stock")
@RequiredArgsConstructor
@Slf4j
public class StockDataController {

    private final StockDataService stockDataService;

    /**
     * Get moving average for a stock symbol
     * 
     * @param symbol Stock symbol
     * @param period Optional period parameter (defaults to configured value)
     * @return Moving average response
     */
    @GetMapping("/{symbol}/moving-average")
    public ResponseEntity<MovingAverageResponse> getMovingAverage(@PathVariable String symbol, @RequestParam(required = false) Integer period) {
        log.info("Requesting moving average for {} with period {}", symbol, period);
        MovingAverageResponse response = stockDataService.getMovingAverageResponse(symbol, period);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get recent prices for a stock symbol
     * 
     * @param symbol Stock symbol
     * @param limit Optional limit parameter (defaults to 10)
     * @return Recent prices response
     */
    @GetMapping("/{symbol}/recent-prices")
    public ResponseEntity<RecentPricesResponse> getRecentPrices(@PathVariable String symbol, @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("Requesting recent prices for {} with limit {}", symbol, limit);
        RecentPricesResponse response = stockDataService.getRecentPricesResponse(symbol, limit);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get statistics for a stock symbol
     * 
     * @param symbol Stock symbol
     * @param dataPoints Optional number of data points to use (defaults to configured value)
     * @return Statistics for the stock
     */
    @GetMapping("/{symbol}/statistics")
    public ResponseEntity<StockStatistics> getStatistics(@PathVariable String symbol, @RequestParam(required = false) Integer dataPoints) {
        log.info("Requesting statistics for {} with data points {}", symbol, dataPoints);
        StockStatistics statistics = stockDataService.getStockStatistics(symbol, dataPoints);
        
        return ResponseEntity.ok(statistics);
    }
}
