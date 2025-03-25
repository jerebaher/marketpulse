package org.marketpulse.stockdata.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "stock_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDataMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String information;

    @Column
    private String symbol;

    @Column
    private String timestamp;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_data_id")
    private StockDataEntity stockData;
}
