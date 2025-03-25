package org.marketpulse.stockdata.persistence;

import org.marketpulse.stockdata.model.StockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDataRepository extends JpaRepository<StockData, Long> {
}
