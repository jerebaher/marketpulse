package org.marketpulse.stockdata.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockData {

    private StockDataMetadata metadata;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal open;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal high;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal low;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal close;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal volume;

    public String getSymbol() {
        return this.metadata.getSymbol();
    }

    public String getInformation() {
        return this.metadata.getInformation();
    }

    public Instant getTimestamp() {
        return this.metadata.getTimestamp();
    }
}
