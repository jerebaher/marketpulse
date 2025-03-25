package org.marketpulse.stockdata.mapper;

import org.marketpulse.stockdata.model.StockData;
import org.marketpulse.stockdata.model.StockDataMetadata;
import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.marketpulse.stockdata.persistence.entity.StockDataMetadataEntity;
import org.springframework.stereotype.Component;

@Component
public class StockDataMapper {

    public StockDataEntity mapStockDataToStockDataEntity(StockData stockData) {
        return StockDataEntity.builder()
                .metadata(mapStockDataMetadataToStockDataMetadataEntity(stockData.getMetadata()))
                .open(stockData.getOpen())
                .close(stockData.getClose())
                .high(stockData.getHigh())
                .low(stockData.getLow())
                .volume(stockData.getVolume())
                .build();
    }

    private StockDataMetadataEntity mapStockDataMetadataToStockDataMetadataEntity(StockDataMetadata stockDataMetadata) {
        return StockDataMetadataEntity.builder()
                .symbol(stockDataMetadata.getSymbol())
                .information(stockDataMetadata.getInformation())
                .timestamp(stockDataMetadata.getTimestamp().toString())
                .build();
    }
}
