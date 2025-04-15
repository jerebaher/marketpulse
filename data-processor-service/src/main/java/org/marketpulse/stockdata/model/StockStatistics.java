package org.marketpulse.stockdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing statistical data for a stock
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockStatistics {
    private String symbol;
    private double averagePrice;
    private double minPrice;
    private double maxPrice;
    private double movingAverage;
    private long dataPointCount;
}

