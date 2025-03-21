package org.marketpulse.stockdata.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockDataMetadata {

    private String information;
    private String symbol;
    private String timestamp;
}
