package org.marketpulse.stockdata.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockDataMetadata {

    private String information;
    private String symbol;
    private Instant timestamp;
}
