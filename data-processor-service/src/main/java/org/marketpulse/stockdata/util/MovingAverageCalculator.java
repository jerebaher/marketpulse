package org.marketpulse.stockdata.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovingAverageCalculator {

    @Value("${stockdata.movingaverage.period:5}")
    private int defaultPeriod;

    /**
     * Calculate moving average using the default period
     */
    public double calculateMovingAverage(Deque<Double> priceQueue) {
        return calculateMovingAverage(priceQueue, defaultPeriod);
    }
    
    /**
     * Calculate moving average using a specific period
     */
    public double calculateMovingAverage(Deque<Double> priceQueue, int period) {
        if (priceQueue == null || priceQueue.isEmpty()) {
            return 0.0;
        }
        
        // Take only the most recent 'period' elements
        List<Double> recentPrices = priceQueue.stream()
                .limit(period)
                .collect(Collectors.toList());
        
        return recentPrices.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
