package org.marketpulse.stock.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.marketpulse.common.exception.StockPricePublisherException;
import org.marketpulse.stock.model.StockPriceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StockPricePublisher {

    @Value(value = "${kafka.topic.stock-prices}")
    private String kafkaTopicStockPrices;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public StockPricePublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishStockPrice(StockPriceResponse stockPrice) {
        log.info("Publishing stock price to kafka topic '{}'", kafkaTopicStockPrices);
        try {
            String message = objectMapper.writeValueAsString(stockPrice);
            log.info("Publishing stock price to kafka: {}", message);

            kafkaTemplate.send(kafkaTopicStockPrices, stockPrice.getSymbol(), message);
        } catch (JsonProcessingException e) {
            log.error("Error in serialization data: {}", e.getMessage());
            throw new StockPricePublisherException("Error in serialization data", e);
        }
    }
}
