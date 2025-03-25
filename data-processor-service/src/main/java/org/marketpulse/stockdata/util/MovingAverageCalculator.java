package org.marketpulse.stockdata.util;

import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class MovingAverageCalculator {

    public double calculateMovingAverage(Deque<Double> priceQueue) {
        return priceQueue.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
