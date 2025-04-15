package org.marketpulse.stockdata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.marketpulse.stockdata.model.MovingAverageResponse;
import org.marketpulse.stockdata.model.RecentPricesResponse;
import org.marketpulse.stockdata.model.StockStatistics;
import org.marketpulse.stockdata.persistence.StockDataRepository;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.marketpulse.stockdata.util.MovingAverageCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockDataService {

    private final StockDataRepository stockDataRepository;
    private final MovingAverageCalculator movingAverageCalculator;

    private final String TIMESTAMP_FIELD = "timestamp";

    @Value("${stockdata.movingaverage.period:5}")
    private int defaultPeriod;

    @Value("${stockdata.statistics.datapoints:20}")
    private int defaultDataPoints;


    /**
     * Get recent stock data for a symbol with specified limit
     */
    public List<StockDataEntity> getRecentStockData(String symbol, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, TIMESTAMP_FIELD));
        return stockDataRepository.findBySymbol(symbol, pageable).getContent();
    }

    /**
     * Get moving average for a stock symbol using the default period
     */
    public double getMovingAverage(String symbol) {
        return getMovingAverage(symbol, defaultPeriod);
    }
    
    /**
     * Get moving average for a stock symbol using a specified period
     */
    public double getMovingAverage(String symbol, int period) {
        List<StockDataEntity> recentData = getRecentStockData(symbol, period);
        
        if (recentData.isEmpty()) {
            return 0.0;
        }
        
        Deque<Double> prices = recentData.stream()
                .map(StockDataEntity::getClose)
                .map(BigDecimal::doubleValue)
                .collect(Collectors.toCollection(LinkedList::new));
                
        return movingAverageCalculator.calculateMovingAverage(prices, period);
    }
    
    /**
     * Calculate statistics for a stock symbol
     */
    public StockStatistics getStockStatistics(String symbol, Integer dataPoints) {
        int pointsToUse = dataPoints != null ? dataPoints : defaultDataPoints;
        List<StockDataEntity> recentData = getRecentStockData(symbol, pointsToUse);
        
        if (recentData.isEmpty()) {
            return new StockStatistics(symbol, 0.0, 0.0, 0.0, 0.0, 0);
        }
        
        DoubleSummaryStatistics stats = recentData.stream()
                .mapToDouble(entity -> entity.getClose().doubleValue())
                .summaryStatistics();
        
        double movingAverage = movingAverageCalculator.calculateMovingAverage(recentData.stream()
                .map(StockDataEntity::getClose)
                .map(BigDecimal::doubleValue)
                .collect(Collectors.toCollection(LinkedList::new))
        );
        
        return new StockStatistics(
                symbol,
                stats.getAverage(),
                stats.getMin(),
                stats.getMax(),
                movingAverage,
                stats.getCount()
        );
    }

    public double calculateMovingAverage(Deque<Double> prices) {
        return movingAverageCalculator.calculateMovingAverage(prices);
    }

    public void save(StockDataEntity stockData) {
        stockDataRepository.save(stockData);
    }
    
    /**
     * Create moving average response for a symbol
     */
    public MovingAverageResponse getMovingAverageResponse(String symbol, Integer period) {
        int periodToUse = period != null ? period : defaultPeriod;
        double movingAverage = getMovingAverage(symbol, periodToUse);
        
        return new MovingAverageResponse(symbol, periodToUse, movingAverage);
    }
    
    /**
     * Create recent prices response for a symbol
     */
    public RecentPricesResponse getRecentPricesResponse(String symbol, int limit) {
        List<StockDataEntity> recentData = getRecentStockData(symbol, limit);
        
        return new RecentPricesResponse(symbol, recentData.size(), recentData);
    }
}
