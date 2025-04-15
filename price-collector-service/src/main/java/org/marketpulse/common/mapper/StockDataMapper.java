package org.marketpulse.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.marketpulse.stock.model.StockData;
import org.marketpulse.stock.model.StockMetadata;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface StockDataMapper {

    @Mapping(target = "metadata", expression = "java(extractFilteredMetadata(stockDataJson))")
    @Mapping(target = "open", expression = "java(getLatestOpen(stockDataJson))")
    @Mapping(target = "high", expression = "java(getLatestHigh(stockDataJson))")
    @Mapping(target = "low", expression = "java(getLatestLow(stockDataJson))")
    @Mapping(target = "close", expression = "java(getLatestClose(stockDataJson))")
    @Mapping(target = "volume", expression = "java(getLatestVolume(stockDataJson))")
    StockData toStockData(Map<String, Object> stockDataJson);

    default StockMetadata extractFilteredMetadata(Map<String, Object> stockDataJson) {
        StockMetadata.StockMetadataBuilder metadata = StockMetadata.builder();
        Map<String, String> rawMetadata = (Map<String, String>) stockDataJson.get("Meta Data");

        if (rawMetadata != null) {
            metadata.information(rawMetadata.get("1. Information"));
            metadata.symbol(rawMetadata.get("2. Symbol"));
            metadata.timestamp(rawMetadata.get("3. Last Refreshed")); // Renombrar lastRefreshed a timestamp
        }

        return metadata.build();
    }

    default String getLatestOpen(Map<String, Object> stockDataJson) {
        Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) stockDataJson.get("Time Series (5min)");
        return timeSeries.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().get("1. open"))
                .orElse(null);
    }

    default String getLatestHigh(Map<String, Object> stockDataJson) {
        Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) stockDataJson.get("Time Series (5min)");
        return timeSeries.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().get("2. high"))
                .orElse(null);
    }

    default String getLatestLow(Map<String, Object> stockDataJson) {
        Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) stockDataJson.get("Time Series (5min)");
        return timeSeries.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().get("3. low"))
                .orElse(null);
    }

    default String getLatestClose(Map<String, Object> stockDataJson) {
        Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) stockDataJson.get("Time Series (5min)");
        return timeSeries.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().get("4. close"))
                .orElse(null);
    }

    default String getLatestVolume(Map<String, Object> stockDataJson) {
        Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) stockDataJson.get("Time Series (5min)");
        return timeSeries.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().get("5. volume"))
                .orElse(null);
    }
}
