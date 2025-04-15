package org.marketpulse.stockdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for moving average response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovingAverageResponse {
    private String symbol;
    private int period;
    private double movingAverage;
}

