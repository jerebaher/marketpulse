package org.marketpulse.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.marketpulse.common.exception.KafkaMessageException;
import org.marketpulse.stockdata.mapper.StockDataMapper;
import org.marketpulse.stockdata.model.StockData;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.marketpulse.stockdata.service.StockDataService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockDataConsumer {

    private final Deque<Double> priceQueue = new ArrayDeque<>();
    private final StockDataService stockDataService;
    private final StockDataMapper stockDataMapper;

    @KafkaListener(topics = "stock-prices", groupId = "data-processor-group", containerFactory = "stockDataKafkaListenerContainerFactory")
    public void getStockData(ConsumerRecord<String, StockData> record) {
        this.processMessage(record.value());
    }

    private void processMessage(StockData stockData) {
        try {
            var price = stockData.getClose().doubleValue();
            var symbol = stockData.getSymbol();

            enqueuePrice(price);

            var movingAverage = stockDataService.calculateMovingAverage(priceQueue);
            log.info("Ticker: {}, current price: {}, moving average: {}", symbol, price, movingAverage);

            if (price > movingAverage * 1.05) {
                log.warn("Price is above moving average threshold. Moving average is {}", movingAverage);
            }

            StockDataEntity entity = stockDataMapper.mapStockDataToStockDataEntity(stockData);

            stockDataService.save(entity);
        } catch (Exception e) {
            log.error("Error processing message {}", stockData);
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
}
