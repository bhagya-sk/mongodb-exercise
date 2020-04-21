package com.reactiveworks.stocktrade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.reactiveworks.stocktrade.model.StockTrade;

/**
 * repository for stocktrade.
 */
public interface IStocktradeRepository extends MongoRepository<StockTrade, Integer> {

	//public Page<StockTrade> findAll(Pageable pageable);

}
