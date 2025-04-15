package stock.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marketpulse.client.AlphaVantageClient;
import org.marketpulse.stock.model.StockPriceResponse;
import org.marketpulse.stock.service.StockPriceService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class StockPriceServiceTest {

    @Mock
    AlphaVantageClient alphaVantageClient;
    @InjectMocks
    StockPriceService stockPriceService;

    @Test
    void testGetStockPriceSuccess() {
        final String symbol = "IBM";

        Map<String, Object> validTimeSeriesData = new HashMap<>();
        Map<String, Object> timeSeriesMap = new HashMap<>();

        String latestTimestamp = "2025-03-07 19:55:00";
        Map<String, Object> latestData = new HashMap<>();
        latestData.put("1. open", "261.3000");
        latestData.put("2. high", "261.3000");
        latestData.put("3. low", "261.2500");
        latestData.put("4. close", "261.2500");
        latestData.put("5. volume", "40");

        timeSeriesMap.put(latestTimestamp, latestData);
        validTimeSeriesData.put("Time Series (5min)", timeSeriesMap);

        when(alphaVantageClient.getStockData(symbol)).thenReturn(validTimeSeriesData);

        StockPriceResponse response = stockPriceService.getStockPrice(symbol);

        assertNotNull(response);
        assertEquals(symbol, response.getSymbol());
        assertEquals(new BigDecimal("261.3000"), response.getPrice());
        assertEquals(latestTimestamp, response.getTimestamp());
    }

    @Test
    void testGetStockPriceMissingTimeSeries() {
        final String symbol = "IBM";

        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("Meta Data", new HashMap<>());

        when(alphaVantageClient.getStockData(symbol)).thenReturn(invalidData);

        StockPriceResponse response = stockPriceService.getStockPrice(symbol);

        assertNotNull(response);
        assertNull(response.getSymbol());
        assertNull(response.getPrice());
        assertNull(response.getTimestamp());
    }

    @Test
    void testGetStockPriceApiException() {
        final String symbol = "IBM";

        when(alphaVantageClient.getStockData(symbol)).thenThrow(new RuntimeException("API error"));

        StockPriceResponse response = stockPriceService.getStockPrice(symbol);

        assertNotNull(response);
        assertNull(response.getSymbol());
        assertNull(response.getPrice());
        assertNull(response.getTimestamp());
    }
}
