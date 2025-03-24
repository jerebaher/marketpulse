package org.marketpulse.stockdata.util;

import java.util.Deque;

public class MovingAverageCalculator {

    public static double calculateMovingAverage(Deque<Double> priceQueue) {
        return priceQueue.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
