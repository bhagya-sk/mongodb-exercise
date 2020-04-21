package com.reactiveworks.stocktrade.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.reactiveworks.stocktrade.model.StockTrade;
import com.reactiveworks.stocktrade.repository.IStocktradeRepository;
import com.reactiveworks.stocktrade.service.exceptions.InsufficientResourceDetailsException;
import com.reactiveworks.stocktrade.service.exceptions.StockTradeRecordNotFoundException;
import com.reactiveworks.stocktrade.service.exceptions.StockTrdRecordAlreadyExistsException;;

/**
 * This class provides stocktrade service.
 */
@Service
public class StockTradeService {
	private static final Logger LOGGER_OBJ = LoggerFactory.getLogger(StockTradeService.class);
	@Autowired
	private IStocktradeRepository stockTrdRepository;

	/**
	 * returns the stockTrade records from the database.
	 * 
	 * @param pageNo   number of the page to be displayed.
	 * @param pageSize numbers of records required in the page.
	 * @return the stockTrade records from the database.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record is not
	 *                                           present in the database.
	 */
	public List<StockTrade> getStockTrades(int pageNo, int pageSize) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of getStockTrades() started");
		List<StockTrade> stockTradesList;
		if (pageNo > 0) {
			pageNo--;
		}
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		System.out.println("pageable "+pageable);
		Page<StockTrade> pagedResult = stockTrdRepository.findAll(pageable);
		if (pagedResult.hasContent()) {
			stockTradesList = pagedResult.getContent();
		} else {
			LOGGER_OBJ.error("stockTrade records are not available");
			throw new StockTradeRecordNotFoundException("stockTrade records are not available");

		}
		LOGGER_OBJ.debug("execution of getStockTrades() completed");
		return stockTradesList;
	}

	/**
	 * adds the stockTrade records into the database.
	 * 
	 * @param stockTradesList list of the stockTrade records to be inserted into the
	 *                        database.
	 * @throws InsufficientResourceDetailsException when the stockTrade object
	 *                                              doesn't have the required
	 *                                              fields.
	 * @throws StockTrdRecordAlreadyExistsException when the record userwants insert
	 *                                              is already present in the
	 *                                              database.
	 */
	public void addStockTrades(List<StockTrade> stockTradesList)
			throws InsufficientResourceDetailsException, StockTrdRecordAlreadyExistsException {
		LOGGER_OBJ.debug("execution of addStockTrades() started");
		List<StockTrade> stockTradeObjects = new ArrayList<>();
		List<Integer> duplicates = new ArrayList<Integer>();
		for (StockTrade stockTradeObj : stockTradesList) {

			if (isValidStockTrdObj(stockTradeObj) == false) {
				LOGGER_OBJ.error("mandatory fields are not mentioned for the stocktrade object with id "
						+ stockTradeObj.getId());
				throw new InsufficientResourceDetailsException(
						"stocktrade object with id " + stockTradeObj.getId() + " doesn't have the required fields");
			}
			try {
				getStockTradeRecord(stockTradeObj.getId());
				duplicates.add(stockTradeObj.getId());

			} catch (StockTradeRecordNotFoundException e) {
				stockTradeObjects.add(stockTradeObj);
				LOGGER_OBJ.info("stockTrade object with id " + stockTradeObj.getId() + " is valid object ");
			}
		}
		stockTrdRepository.saveAll(stockTradeObjects);
		if (duplicates.size() != 0) {
			throw new StockTrdRecordAlreadyExistsException(
					"stockTrade object with id(s) " + duplicates + " is/are already available");
		}

		LOGGER_OBJ.debug("execution of addStockTrades() completed");
	}

	/**
	 * Gets the stockTrade record from the database with the given id.
	 * 
	 * @param id id of the stockTrade record to be fetched from the database.
	 * @return stockTrade record from the database with the given id.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record is not
	 *                                           present in the database.
	 */
	public StockTrade getStockTradeRecord(int id) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of getStockTradeRecord() started");
		StockTrade stockTradeObj = stockTrdRepository.findById(id).orElse(null);
		if (stockTradeObj == null) {
			LOGGER_OBJ.error("stockTrade record with id " + id + " is not available");
			throw new StockTradeRecordNotFoundException("stockTrade record with id " + id + " is not available");
		}
		LOGGER_OBJ.debug("execution of getStockTradeRecord() completed");
		return stockTradeObj;
	}

	/**
	 * updates or adds the stockTrade record in/into the database.
	 * 
	 * @param id            id of the record to be updated/inserted.
	 * @param stockTradeObj stockTrade object to be updated/inserted.
	 * @throws InsufficientResourceDetailsException when the stockTrade object
	 *                                              doesn't have the required
	 *                                              fields.
	 */
	public void updateStockTrade(int id, StockTrade stockTradeObj) throws InsufficientResourceDetailsException {
		LOGGER_OBJ.debug("execution of updateStockTrade() started");
		stockTradeObj.setId(id);
		try {
			getStockTradeRecord(id);
		} catch (StockTradeRecordNotFoundException e) {
			if (isValidStockTrdObj(stockTradeObj) == false) {
				throw new InsufficientResourceDetailsException(
						"stocktrade object with id " + stockTradeObj.getId() + " doesn't have the required fields");
			}

		}
		stockTrdRepository.save(stockTradeObj);
		LOGGER_OBJ.debug("execution of updateStockTrade() completed");
	}

	/**
	 * updates the stockTrade record with the given id.
	 * 
	 * @param id            id of the stockTrade record to be updated.
	 * @param stockTradeObj stockTrade object with fields to be updated.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record with the
	 *                                           given is not available.
	 */
	public void updateStockTradeDetails(int id, StockTrade stockTradeObj) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of updateStockTradeDetails() started");
		StockTrade stockTradeRecord = getStockTradeRecord(id);
		if (stockTradeObj.getSecurity() != null) {
			stockTradeRecord.setSecurity(stockTradeObj.getSecurity());
		}
		if (stockTradeObj.getDate() != null) {
			stockTradeRecord.setDate(stockTradeObj.getDate());
		}
		if (stockTradeObj.getOpen() != 0) {
			stockTradeRecord.setOpen(stockTradeObj.getOpen());
		}
		if (stockTradeObj.getHigh() != 0) {
			stockTradeRecord.setHigh(stockTradeObj.getHigh());
		}
		if (stockTradeObj.getLow() != 0) {
			stockTradeRecord.setLow(stockTradeObj.getLow());
		}
		if (stockTradeObj.getClose() != 0) {
			stockTradeRecord.setClose(stockTradeObj.getClose());
		}
		if (stockTradeObj.getAdjClose() != 0) {
			stockTradeRecord.setAdjClose(stockTradeObj.getAdjClose());
		}
		stockTrdRepository.save(stockTradeRecord);
		LOGGER_OBJ.debug("execution of updateStockTradeDetails() completed");
	}

	/**
	 * deletes the stockTrade record with the given id from the database.
	 * 
	 * @param id id of the stockTrade record to be deleted.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record with the
	 *                                           given id doesn't exist in the
	 *                                           database.
	 */
	public void deleteStockTrade(int id) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of deleteStockTrade() started");
		StockTrade stockTradeRecord = getStockTradeRecord(id);
		stockTrdRepository.delete(stockTradeRecord);
		LOGGER_OBJ.debug("execution of deleteStockTrade() completed");
	}

	/**
	 * Checks whether the stockTrade object has all the required fields.
	 * 
	 * @param stockTrdObj the stockTrade object to be validated.
	 * @return true if the object is valid otherwise returns false.
	 */
	private boolean isValidStockTrdObj(StockTrade stockTrdObj) {
		boolean isValid = false;
		System.out.println(stockTrdObj);
		if (stockTrdObj.getAdjClose() != 0 && stockTrdObj.getId() != 0 && stockTrdObj.getSecurity() != null
				&& stockTrdObj.getDate() != null && stockTrdObj.getOpen() != 0 && stockTrdObj.getHigh() != 0
				&& stockTrdObj.getLow() != 0 && stockTrdObj.getClose() != 0 && stockTrdObj.getVolume() != 0) {
			isValid = true;
		}
		return isValid;
	}

}
