package org.marketpulse.stockdata.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marketpulse.stockdata.model.StockStatistics;
import org.marketpulse.stockdata.persistence.StockDataRepository;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.marketpulse.stockdata.util.MovingAverageCalculator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marketpulse.stockdata.model.MovingAverageResponse;
import org.marketpulse.stockdata.model.RecentPricesResponse;
import org.marketpulse.stockdata.model.StockStatistics;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockDataServiceTest {

    @Mock
    private StockDataRepository stockDataRepository;

    @Mock
    private MovingAverageCalculator movingAverageCalculator;

    @InjectMocks
    private StockDataService stockDataService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stockDataService, "defaultPeriod", 5);
        ReflectionTestUtils.setField(stockDataService, "defaultDataPoints", 20);
    }

    @Test
    void testGetMovingAverageWithDefaultPeriod() {
        // Given
        String symbol = "AAPL";
        List<StockDataEntity> stockData = createTestStockData(symbol, 5);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        when(movingAverageCalculator.calculateMovingAverage(any(Deque.class), eq(5)))
                .thenReturn(30.0);
        
        // When
        double result = stockDataService.getMovingAverage(symbol);
        
        // Then
        assertEquals(30.0, result, 0.001);
    }

    @Test
    void testGetMovingAverageWithCustomPeriod() {
        // Given
        String symbol = "AAPL";
        int period = 3;
        List<StockDataEntity> stockData = createTestStockData(symbol, 3);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        when(movingAverageCalculator.calculateMovingAverage(any(Deque.class), eq(period)))
                .thenReturn(20.0);
        
        // When
        double result = stockDataService.getMovingAverage(symbol, period);
        
        // Then
        assertEquals(20.0, result, 0.001);
    }

    @Test
    void testGetMovingAverageWithEmptyData() {
        // Given
        String symbol = "UNKNOWN";
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        
        // When
        double result = stockDataService.getMovingAverage(symbol);
        
        // Then
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testGetStockStatistics() {
        // Given
        String symbol = "AAPL";
        Integer dataPoints = 5;
        List<StockDataEntity> stockData = createTestStockData(symbol, dataPoints);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        when(movingAverageCalculator.calculateMovingAverage(any(Deque.class)))
                .thenReturn(30.0);
        
        // When
        StockStatistics result = stockDataService.getStockStatistics(symbol, dataPoints);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(30.0, result.getAveragePrice(), 0.001);
        assertEquals(10.0, result.getMinPrice(), 0.001);
        assertEquals(50.0, result.getMaxPrice(), 0.001);
        assertEquals(30.0, result.getMovingAverage(), 0.001);
        assertEquals(5, result.getDataPointCount());
    }

    @Test
    void testGetStockStatisticsWithEmptyData() {
        // Given
        String symbol = "UNKNOWN";
        Integer dataPoints = 5;
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        
        // When
        StockStatistics result = stockDataService.getStockStatistics(symbol, dataPoints);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(0.0, result.getAveragePrice(), 0.001);
        assertEquals(0.0, result.getMinPrice(), 0.001);
        assertEquals(0.0, result.getMaxPrice(), 0.001);
        assertEquals(0.0, result.getMovingAverage(), 0.001);
        assertEquals(0, result.getDataPointCount());
    }
    
    @Test
    void testGetMovingAverageResponse() {
        // Given
        String symbol = "AAPL";
        Integer period = 7;
        List<StockDataEntity> stockData = createTestStockData(symbol, 7);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        when(movingAverageCalculator.calculateMovingAverage(any(Deque.class), eq(7)))
                .thenReturn(40.0);
        
        // When
        MovingAverageResponse result = stockDataService.getMovingAverageResponse(symbol, period);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(period, result.getPeriod());
        assertEquals(40.0, result.getMovingAverage(), 0.001);
    }
    
    @Test
    void testGetMovingAverageResponseWithDefaultPeriod() {
        // Given
        String symbol = "AAPL";
        Integer period = null;
        List<StockDataEntity> stockData = createTestStockData(symbol, 5);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        when(movingAverageCalculator.calculateMovingAverage(any(Deque.class), eq(5)))
                .thenReturn(30.0);
        
        // When
        MovingAverageResponse result = stockDataService.getMovingAverageResponse(symbol, period);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(5, result.getPeriod()); // default period is 5
        assertEquals(30.0, result.getMovingAverage(), 0.001);
    }
    
    @Test
    void testGetRecentPricesResponse() {
        // Given
        String symbol = "AAPL";
        int limit = 3;
        List<StockDataEntity> stockData = createTestStockData(symbol, limit);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        // When
        RecentPricesResponse result = stockDataService.getRecentPricesResponse(symbol, limit);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(limit, result.getCount());
        assertEquals(stockData, result.getData());
    }

    @Test
    void testGetMovingAverageResponseWithNegativePeriod() {
        // Given
        String symbol = "AAPL";
        Integer negativePeriod = -3; // Invalid period
        int minimumValidPeriod = 1; // Assuming this is the minimum valid period
        List<StockDataEntity> stockData = createTestStockData(symbol, minimumValidPeriod);
        
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        when(movingAverageCalculator.calculateMovingAverage(any(Deque.class), eq(minimumValidPeriod)))
                .thenReturn(10.0);
        
        // When - assuming the service corrects the period to a minimum valid value
        MovingAverageResponse result = stockDataService.getMovingAverageResponse(symbol, negativePeriod);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(minimumValidPeriod, result.getPeriod()); // Should use minimum valid period
        assertEquals(10.0, result.getMovingAverage(), 0.001);
    }
    
    @Test
    void testGetRecentPricesResponseWithExcessiveLimit() {
        // Given
        String symbol = "AAPL";
        int excessiveLimit = 10000; // Very large limit
        int reasonableLimit = 100; // Reasonable maximum
        List<StockDataEntity> stockData = createTestStockData(symbol, reasonableLimit);
        
        // Assuming the service caps the limit to a reasonable value
        when(stockDataRepository.findBySymbol(eq(symbol), any(Pageable.class)))
                .thenReturn(new PageImpl<>(stockData));
        
        // When
        RecentPricesResponse result = stockDataService.getRecentPricesResponse(symbol, excessiveLimit);
        
        // Then
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(reasonableLimit, result.getCount()); // Should be capped
    }
    
    private List<StockDataEntity> createTestStockData(String symbol, int count) {
        StockDataEntity[] entities = new StockDataEntity[count];
        for (int i = 0; i < count; i++) {
            StockDataEntity entity = new StockDataEntity();
            entity.setSymbol(symbol);
            entity.setPrice(10.0 * (i + 1)); // 10, 20, 30, ...
            entity.setTimestamp(LocalDateTime.now().minusHours(i));
            entities[i] = entity;
        }
        return Arrays.asList(entities);
    }
}

