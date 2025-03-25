package org.marketpulse.stockdata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketpulse.stockdata.model.StockData;
import org.marketpulse.stockdata.persistence.StockDataRepository;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.marketpulse.stockdata.util.MovingAverageCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Deque;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockDataService {

    private final StockDataRepository stockDataRepository;
    private final MovingAverageCalculator movingAverageCalculator;

    public Page<StockDataEntity> getStockData(String symbol) {
        Pageable pageable = PageRequest.of(0, 10);

        return stockDataRepository.findBySymbol(symbol, pageable);
    }

    public double calculateMovingAverage(Deque<Double> prices) {
        return movingAverageCalculator.calculateMovingAverage(prices);
    }

    public void save(StockDataEntity stockData) {
        stockDataRepository.save(stockData);
    }
}
