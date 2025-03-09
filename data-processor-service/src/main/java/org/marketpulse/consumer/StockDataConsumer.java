package org.marketpulse.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockDataConsumer {

    private final MessageProcessor messageProcessor;

    @KafkaListener(topics = "stock-prices", groupId = "data-processor-group")
    public void getStockData(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        log.info("{} : {}", key, value);

        messageProcessor.processMessage(value);
    }
}
