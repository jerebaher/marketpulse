package stock.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marketpulse.stock.publisher.StockPricePublisher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.marketpulse.stock.model.StockPriceResponse;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockPricePublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private StockPricePublisher stockPricePublisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stockPricePublisher, "kafkaTopicStockPrices", "test-stock-prices");
    }

    @Test
    void publishStockPriceSuccess() {
        StockPriceResponse stockPrice = getStockPriceResponse();

        stockPricePublisher.publishStockPrice(stockPrice);

        verify(kafkaTemplate).send(
                eq("test-stock-prices"),
                eq("IBM"),
                contains("\"symbol\":\"IBM\"")
        );
    }

    @Test
    void publishStockPriceJsonException() throws JsonProcessingException {
        ObjectMapper mapperMock = Mockito.mock(ObjectMapper.class);
        ReflectionTestUtils.setField(stockPricePublisher, "objectMapper", mapperMock);

        StockPriceResponse stockPrice = getStockPriceResponse();

        when(mapperMock.writeValueAsString(any(StockPriceResponse.class)))
                .thenThrow(new JsonProcessingException("Simulated JSON error"){});

        stockPricePublisher.publishStockPrice(stockPrice);

        verifyNoInteractions(kafkaTemplate);
    }

    private StockPriceResponse getStockPriceResponse() {
        return new StockPriceResponse(
                "IBM",
                new BigDecimal("261.3000"),
                "2025-03-07 19:55:00"
        );
    }
}
