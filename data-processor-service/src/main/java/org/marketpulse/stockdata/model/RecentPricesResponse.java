package org.marketpulse.stockdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;

import java.util.List;

/**
 * DTO for recent prices response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentPricesResponse {
    private String symbol;
    private int count;
    private List<StockDataEntity> data;
}

