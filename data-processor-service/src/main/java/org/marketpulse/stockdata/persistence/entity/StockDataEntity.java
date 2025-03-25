package org.marketpulse.stockdata.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stock_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDataEntity {

    @Id
    private Long id;

    @OneToOne(mappedBy = "stockData", cascade = CascadeType.ALL, optional = false)
    private StockDataMetadataEntity metadata;

    @Column
    private BigDecimal open;

    @Column
    private BigDecimal high;

    @Column
    private BigDecimal low;

    @Column
    private BigDecimal close;

    @Column
    private BigDecimal volume;

    @PrePersist
    protected void onCreate() {
        if (metadata == null) {
            metadata = new StockDataMetadataEntity();
        }
    }

    public String getSymbol() {
        return this.metadata.getSymbol();
    }

    public String getInformation() {
        return this.metadata.getInformation();
    }

    public String getTimestamp() {
        return this.metadata.getTimestamp();
    }
}
