package org.marketpulse.stockdata.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marketpulse.stockdata.model.MovingAverageResponse;
import org.marketpulse.stockdata.model.RecentPricesResponse;
import org.marketpulse.stockdata.model.StockStatistics;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.marketpulse.stockdata.service.StockDataService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockDataControllerTest {

    @Mock
    private StockDataService stockDataService;

    @InjectMocks
    private StockDataController controller;

    @BeforeEach
    void setUp() {
        // No configuration needed in controller
    }

    @Test
    void testGetMovingAverageWithDefaultPeriod() {
        // Given
        String symbol = "AAPL";
        MovingAverageResponse mockResponse = new MovingAverageResponse(symbol, 5, 30.0);
        
        when(stockDataService.getMovingAverageResponse(symbol, null)).thenReturn(mockResponse);
        
        // When
        ResponseEntity<MovingAverageResponse> response = controller.getMovingAverage(symbol, null);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(symbol, response.getBody().getSymbol());
        assertEquals(5, response.getBody().getPeriod());
        assertEquals(30.0, response.getBody().getMovingAverage());
    }

    @Test
    void testGetMovingAverageWithCustomPeriod() {
        // Given
        String symbol = "AAPL";
        int period = 10;
        MovingAverageResponse mockResponse = new MovingAverageResponse(symbol, period, 25.0);
        
        when(stockDataService.getMovingAverageResponse(symbol, period)).thenReturn(mockResponse);
        
        // When
        ResponseEntity<MovingAverageResponse> response = controller.getMovingAverage(symbol, period);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(symbol, response.getBody().getSymbol());
        assertEquals(period, response.getBody().getPeriod());
        assertEquals(25.0, response.getBody().getMovingAverage());
    }

    @Test
    void testGetRecentPrices() {
        // Given
        String symbol = "AAPL";
        int limit = 5;
        List<StockDataEntity> mockData = createTestStockData(symbol, limit);
        RecentPricesResponse mockResponse = new RecentPricesResponse(symbol, limit, mockData);
        
        when(stockDataService.getRecentPricesResponse(symbol, limit)).thenReturn(mockResponse);
        
        // When
        ResponseEntity<RecentPricesResponse> response = controller.getRecentPrices(symbol, limit);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(symbol, response.getBody().getSymbol());
        assertEquals(limit, response.getBody().getCount());
        assertNotNull(response.getBody().getData());
        assertEquals(mockData, response.getBody().getData());
    }

    @Test
    void testGetStatisticsWithDefaultDataPoints() {
        // Given
        String symbol = "AAPL";
        StockStatistics mockStats = new StockStatistics(symbol, 30.0, 10.0, 50.0, 25.0, 20);
        
        when(stockDataService.getStockStatistics(symbol, 20)).thenReturn(mockStats);
        
        // When
        ResponseEntity<StockStatistics> response = controller.getStatistics(symbol, null);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(symbol, response.getBody().getSymbol());
        assertEquals(30.0, response.getBody().getAveragePrice());
        assertEquals(10.0, response.getBody().getMinPrice());
        assertEquals(50.0, response.getBody().getMaxPrice());
        assertEquals(25.0, response.getBody().getMovingAverage());
        assertEquals(20, response.getBody().getDataPointCount());
    }

    @Test
    void testGetStatisticsWithCustomDataPoints() {
        // Given
        String symbol = "AAPL";
        int dataPoints = 10;
        StockStatistics mockStats = new StockStatistics(symbol, 30.0, 10.0, 50.0, 25.0, 10);
        
        when(stockDataService.getStockStatistics(symbol, dataPoints)).thenReturn(mockStats);
        
        // When
        ResponseEntity<StockStatistics> response = controller.getStatistics(symbol, dataPoints);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(symbol, response.getBody().getSymbol());
        assertEquals(30.0, response.getBody().getAveragePrice());
        assertEquals(10.0, response.getBody().getMinPrice());
        assertEquals(50.0, response.getBody().getMaxPrice());
        assertEquals(25.0, response.getBody().getMovingAverage());
        assertEquals(10, response.getBody().getDataPointCount());
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
    
    @Test
    void testGetMovingAverageWithNegativePeriod() {
        // Given
        String symbol = "AAPL";
        int invalidPeriod = -5;
        MovingAverageResponse mockResponse = new MovingAverageResponse(symbol, 1, 0.0); // Default to minimum valid period
        
        // Assuming service validates and corrects the input
        when(stockDataService.getMovingAverageResponse(symbol, invalidPeriod)).thenReturn(mockResponse);
        
        // When
        ResponseEntity<MovingAverageResponse> response = controller.getMovingAverage(symbol, invalidPeriod);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getPeriod()); // Should use minimum valid period
    }
    
    @Test
    void testGetRecentPricesWithExcessiveLimit() {
        // Given
        String symbol = "AAPL";
        int excessiveLimit = 10000; // A very large limit
        int reasonableLimit = 100; // Reasonable maximum
        
        List<StockDataEntity> mockData = createTestStockData(symbol, reasonableLimit);
        RecentPricesResponse mockResponse = new RecentPricesResponse(symbol, reasonableLimit, mockData);
        
        // Assuming service caps the limit to a reasonable value
        when(stockDataService.getRecentPricesResponse(symbol, excessiveLimit)).thenReturn(mockResponse);
        
        // When
        ResponseEntity<RecentPricesResponse> response = controller.getRecentPrices(symbol, excessiveLimit);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reasonableLimit, response.getBody().getCount()); // Should be capped
    }
    
    @Test
    void testGetStatisticsWithEmptySymbol() {
        // Given
        String emptySymbol = "";
        StockStatistics emptyStats = new StockStatistics(emptySymbol, 0.0, 0.0, 0.0, 0.0, 0);
        
        // Assuming service handles empty symbol gracefully
        when(stockDataService.getStockStatistics(emptySymbol, null)).thenReturn(emptyStats);
        
        // When
        ResponseEntity<StockStatistics> response = controller.getStatistics(emptySymbol, null);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(emptySymbol, response.getBody().getSymbol());
        assertEquals(0, response.getBody().getDataPointCount());
    }
