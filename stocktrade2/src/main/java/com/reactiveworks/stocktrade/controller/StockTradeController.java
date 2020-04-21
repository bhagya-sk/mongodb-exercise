package com.reactiveworks.stocktrade.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import com.reactiveworks.stocktrade.model.StockTrade;
import com.reactiveworks.stocktrade.service.StockTradeService;
import com.reactiveworks.stocktrade.service.exceptions.InsufficientResourceDetailsException;
import com.reactiveworks.stocktrade.service.exceptions.StockTradeRecordNotFoundException;
import com.reactiveworks.stocktrade.service.exceptions.StockTrdRecordAlreadyExistsException;

/**
 * Controller class for the stocktrade resource
 */

@RestController
@RequestMapping(value = "/stocktrades")
public class StockTradeController {

	private static final Logger LOGGER_OBJ = LoggerFactory.getLogger(StockTradeController.class);
	@Autowired
	private StockTradeService stockTrdService;

	/**
	 * returns the stockTrade records from the database.
	 * 
	 * @param pageNo   number of the page to be displayed.
	 * @param pageSize numbers of records required in the page.
	 * @return the stockTrade records from the database.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record is not
	 *                                           present in the database.
	 */
	@GetMapping
	public CollectionModel<StockTrade> getStockTrade(@RequestParam(defaultValue = "0", required = false) int pageNo,
			@RequestParam(defaultValue = "3", required = false) int pageSize) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of getStockTrade() started");
		List<StockTrade> stockTradeList = stockTrdService.getStockTrades(pageNo, pageSize);
		System.out.println("stockTradeList: "+stockTradeList);
//		for (StockTrade stockTrade : stockTradeList) {
//			List<Link> links = new ArrayList<Link>();
//			Link link = WebMvcLinkBuilder.linkTo(StockTradeController.class).slash(stockTrade.getId()).withSelfRel();
//			links.add(link);
//			link = WebMvcLinkBuilder
//					.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).deleteStockTrade(stockTrade.getId()))
//					.withRel("delete");
//			links.add(link);
//			link = WebMvcLinkBuilder
//					.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).deleteStockTrade(stockTrade.getId()))
//					.withRel("update");
//			links.add(link);
//			stockTrade.add(links);
//
//		}

		LOGGER_OBJ.debug("execution of getStockTrade() completed");
		return new CollectionModel<StockTrade>(stockTradeList);
	}

	/**
	 * Gets the stockTrade record from the database with the given id.
	 * 
	 * @param id id of the stockTrade record to be fetched from the database.
	 * @return stockTrade record from the database with the given id.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record is not
	 *                                           present in the database.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<StockTrade> getStockTrade(@PathVariable int id) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of getStockTrade() started");
		StockTrade stockTradeObj = stockTrdService.getStockTradeRecord(id);
		List<Link> links = new ArrayList<Link>();
		Link link = WebMvcLinkBuilder.linkTo(StockTradeController.class).slash(id).withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).deleteStockTrade(id))
				.withRel("delete");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).deleteStockTrade(id))
				.withRel("update");
		links.add(link);
		stockTradeObj.add(links);
		LOGGER_OBJ.debug("execution of getStockTrade() completed");
		return new ResponseEntity<StockTrade>(stockTradeObj, HttpStatus.OK);
	}

	/**
	 * adds the stockTrade records into the database.
	 * 
	 * @param stockTradesList list of the stockTrade records to be inserted into the
	 *                        database.
	 * @return the links to the created record.
	 * @throws InsufficientResourceDetailsException when the stockTrade object
	 *                                              doesn't have the required
	 *                                              fields.
	 * @throws StockTrdRecordAlreadyExistsException when the record userwants insert
	 *                                              is already present in the
	 *                                              database.
	 */
	@PostMapping
	public ResponseEntity<List<Link>> insertStockTrades(@RequestBody List<StockTrade> stockTradesList)
			throws InsufficientResourceDetailsException, StockTrdRecordAlreadyExistsException {
		List<Link> links = new ArrayList<Link>();//
		stockTrdService.addStockTrades(stockTradesList);

		for (StockTrade stockTrdObj : stockTradesList) {
			Link link = WebMvcLinkBuilder.linkTo(StockTradeController.class).slash(stockTrdObj.getId()).withSelfRel();
			links.add(link);
			link = WebMvcLinkBuilder.linkTo(StockTradeController.class).slash(stockTrdObj.getId()).withRel("delete");
			links.add(link);
			link = WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(StockTradeController.class).updateStockTrade(stockTrdObj.getId(), null))
					.withRel("update");
			links.add(link);
		}
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}

	/**
	 * updates or adds the stockTrade record in/into the database.
	 * 
	 * @param id            id of the record to be updated/inserted.
	 * @param stockTradeObj stockTrade object to be updated/inserted.
	 * @return the link to the record with given id.
	 * @throws InsufficientResourceDetailsException when the stockTrade object
	 *                                              doesn't have the required
	 *                                              fields.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<List<Link>> updateStockTrade(@PathVariable int id, @RequestBody StockTrade stockTradeObj)
			throws InsufficientResourceDetailsException {
		LOGGER_OBJ.debug("execution of updateStockTrade() started");
		stockTrdService.updateStockTrade(id, stockTradeObj);
		List<Link> links = new ArrayList<Link>();
		Link link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).updateStockTrade(id, stockTradeObj))
				.withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).updateStockTrade(id, stockTradeObj))
				.withRel("delete");
		links.add(link);
		LOGGER_OBJ.debug("execution of updateStockTrade() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);

	}

	/**
	 * updates the stockTrade record with the given id.
	 * 
	 * @param id            id of the stockTrade record to be updated.
	 * @param stockTradeObj stockTrade object with fields to be updated.
	 * @return the link the updated object.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record with the
	 *                                           given is not available.
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<List<Link>> updateStockTradeProperties(@PathVariable int id,
			@RequestBody StockTrade stockTradeObj) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of updateStockTradeProperties() started");
		stockTrdService.updateStockTradeDetails(id, stockTradeObj);
		List<Link> links = new ArrayList<Link>();
		Link link = WebMvcLinkBuilder.linkTo(StockTradeController.class).slash(id).withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StockTradeController.class).deleteStockTrade(id))
				.withRel("delete");
		links.add(link);
		LOGGER_OBJ.debug("execution of updateStockTradeProperties() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);

	}

	/**
	 * deletes the stockTrade record with the given id from the database.
	 * 
	 * @param id id of the stockTrade record to be deleted.
	 * @return the deletion status.
	 * @throws StockTradeRecordNotFoundException when the stockTrade record with the
	 *                                           given id doesn't exist in the
	 *                                           database.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStockTrade(@PathVariable int id) throws StockTradeRecordNotFoundException {
		LOGGER_OBJ.debug("execution of deleteStockTrade() started");
		String result;
		stockTrdService.deleteStockTrade(id);
		result = "stocktrade with id " + id + " is deleted";
		LOGGER_OBJ.debug("execution of deleteStockTrade() completed");
		return new ResponseEntity<String>(result, HttpStatus.OK);

	}

}
