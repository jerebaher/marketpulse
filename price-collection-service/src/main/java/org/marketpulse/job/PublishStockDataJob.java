package org.marketpulse.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketpulse.stock.model.StockPriceResponse;
import org.marketpulse.stock.publisher.StockPricePublisher;
import org.marketpulse.stock.service.StockPriceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class PublishStockDataJob {

    private final StockPriceService stockPriceService;
    private final StockPricePublisher stockPricePublisher;

    @Scheduled(cron = "0 0 * * * *")
    public void getStockPrice() {
        log.info("Getting stock price from alpha-vantage API...");
        StockPriceResponse response = stockPriceService.getStockPrice("AAPL");
        stockPricePublisher.publishStockPrice(response);
    }
}
