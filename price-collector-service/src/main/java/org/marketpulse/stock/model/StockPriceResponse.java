package org.marketpulse.stock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StockPriceResponse {

    private String symbol;
    private BigDecimal price;
    private String timestamp;
}
