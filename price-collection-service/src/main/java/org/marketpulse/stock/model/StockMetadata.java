package org.marketpulse.stock.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StockMetadata {

    private String information;
    private String symbol;
    private String timestamp;
}
