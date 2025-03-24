package org.marketpulse.stockdata.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.marketpulse.common.exception.KafkaMessageException;
import org.marketpulse.stockdata.model.StockData;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
@Slf4j
@AllArgsConstructor
public class StockDataProcessor {

    private final Deque<Double> priceQueue = new ArrayDeque<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processMessage(String message) {
        try {
            var stockData = objectMapper.readValue(message, StockData.class);
            var price = stockData.getClose().doubleValue();
            var symbol = stockData.getSymbol();

            enqueuePrice(price);

            var movingAverage = calculateMovingAverage();
            log.info("Ticker: {}, current price: {}, moving average: {}", symbol, price, movingAverage);

            if (price > movingAverage * 1.05) {
                log.warn("Price is above moving average threshold. Moving average is {}", movingAverage);
            }

        } catch (Exception e) {
            log.error("Error processing message {}", message);
            throw new KafkaMessageException("Kafka message no valid. Please, look out for bad format messages.", e);
        }
    }

    private void enqueuePrice(double price) {
        int movingAverageWindow = 5;
        if (priceQueue.size() >= movingAverageWindow) {
            priceQueue.pollFirst();
        }
        priceQueue.addLast(price);
    }

    private double calculateMovingAverage() {
        return priceQueue.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
