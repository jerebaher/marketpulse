package org.marketpulse.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.marketpulse.stock.publisher.StockDataPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class PublishStockDataJob {

    private final StockDataPublisher stockDataPublisher;

    @Scheduled(cron = "0 0 * * * *")
    public void publishStockData() {
        log.info("Getting stock price from alpha-vantage API...");
        stockDataPublisher.publishTestStockData();
    }
}
