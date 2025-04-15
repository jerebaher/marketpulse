package org.marketpulse.stock.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.marketpulse.client.AlphaVantageClient;
import org.marketpulse.common.exception.StockPricePublisherException;
import org.marketpulse.common.mapper.StockDataMapper;
import org.marketpulse.stock.model.StockData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
public class StockDataPublisher {

    @Value(value = "${kafka.topic.stock-prices}")
    private String kafkaTopicStockPrices;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final AlphaVantageClient alphaVantageClient;
    private final StockDataMapper stockDataMapper;

    public StockDataPublisher(KafkaTemplate<String, String> kafkaTemplate,
                              ObjectMapper objectMapper,
                              AlphaVantageClient alphaVantageClient,
                              StockDataMapper stockDataMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.alphaVantageClient = alphaVantageClient;
        this.stockDataMapper = stockDataMapper;
        this.objectMapper = objectMapper;
    }

    public void publishStockData(String symbol) {
        log.info("Publishing stock price to kafka topic '{}'", kafkaTopicStockPrices);
        try {
            Map<String, Object> response = alphaVantageClient.getStockData(symbol);
            StockData stockData = stockDataMapper.toStockData(response);
            String stockDataJson = objectMapper.writeValueAsString(stockData);

            log.info("Publishing stock price to Kafka: {}", stockData);

            kafkaTemplate.send(kafkaTopicStockPrices, stockDataJson);
        } catch (JsonProcessingException e) {
            log.error("Error in serialization data: {}", e.getMessage());
            throw new StockPricePublisherException("Error in serialization data", e);
        }
    }

    public void publishTestStockData() {
        final String symbol = "AAPL";
        publishStockData(symbol);
    }
}
