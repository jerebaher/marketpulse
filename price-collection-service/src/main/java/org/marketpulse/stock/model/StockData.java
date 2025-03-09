package org.marketpulse.stock.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StockData {

    private StockMetadata metadata;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
}
