package org.marketpulse.stockdata.persistence;

import org.marketpulse.stockdata.persistence.entity.StockDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDataRepository extends JpaRepository<StockDataEntity, Long> {

    Page<StockDataEntity> findBySymbol(String symbol, Pageable pageable);
}
