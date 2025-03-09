package stock.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marketpulse.client.AlphaVantageClient;
import org.marketpulse.common.exception.StockPricePublisherException;
import org.marketpulse.common.mapper.StockDataMapperImpl;
import org.marketpulse.stock.model.StockData;
import org.marketpulse.stock.publisher.StockDataPublisher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockDataPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private AlphaVantageClient alphaVantageClient;
    @Spy
    private ObjectMapper objectMapper;
    @Spy
    private StockDataMapperImpl stockDataMapper;
    @InjectMocks
    private StockDataPublisher stockDataPublisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stockDataPublisher, "kafkaTopicStockPrices", "test-stock-prices");
    }

    @Test
    void publishStockDataSuccess() {
        when(alphaVantageClient.getStockData(any())).thenReturn(mockStockData());

        stockDataPublisher.publishStockData();

        verify(kafkaTemplate).send(
                eq("test-stock-prices"),
                contains("\"symbol\":\"IBM\"")
        );
    }

    @Test
    void publishStockDataJsonException() throws JsonProcessingException {
        when(alphaVantageClient.getStockData(any())).thenReturn(mockStockData());
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(any(StockData.class));

        StockPricePublisherException exception =
                assertThrows(StockPricePublisherException.class, () -> stockDataPublisher.publishStockData());

        assertEquals("Error in serialization data", exception.getMessage());
        verifyNoInteractions(kafkaTemplate);
    }

    private Map<String, Object> mockStockData() {
        Map<String, Object> metadata = Map.of(
                "1. Information", "Intraday (5min) open, high, low, close prices and volume",
                "2. Symbol", "IBM",
                "3. Last Refreshed", "2025-03-07 19:55:00"
        );
        Map<String, Object> timeSeries = Map.of(
                "2025-03-07 19:55:00", Map.of(
                        "1. open", "261.3000",
                        "2. high", "261.3000",
                        "3. low", "261.2500",
                        "4. close", "261.2500",
                        "5. volume", "40"
                )
        );

        return Map.of(
                "Meta Data", metadata,
                "Time Series (5min)", timeSeries
        );
    }
}
