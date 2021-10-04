package com.api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sound.midi.SysexMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.support.ReactiveRepositoryFactorySupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.entities.Portfolio;
import com.api.entities.Trade;
import com.api.services.PortfolioService;
import com.api.services.TradeService;
import com.sun.net.httpserver.Authenticator.Success;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.asm.Advice.This;


@RestController
public class TradeController {
	@Autowired
	private TradeService tradeService;

	@Autowired
	private PortfolioService portfolioService;


	public PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}

	public TradeService getTradeService() {
		return tradeService;
	}

	public void setTradeService(TradeService tradeService) {
		this.tradeService = tradeService;
	}
	
	@ApiResponses(
		value= {
				@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
				@ApiResponse(code =500,message = "Internal server error"),
				@ApiResponse(code =204,message = "Succesful but no content recieved from api")
		}
	)
	@ApiOperation(value = "returns specific trade by id")
	@GetMapping("/trade/{id}")
	public ResponseEntity<ResponseTrade> getTradeById(@PathVariable("id") int id){
		System.out.println("inside trade id");
		Trade trade=null;
		try {
			trade = this.tradeService.getTradeById(id);
			if(trade!=null) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","empty ",trade));
			}
			else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseTrade("success","no such id exist! ",null));
			}
		}
		catch (Exception e) {
			System.out.println("error in gettradebyid "+e.getMessage());
		}
		return ResponseEntity.ok(null);
	}
	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@PutMapping("/trade/{id}")
	public ResponseEntity<ResponseTrade> updateTrade(@Valid @RequestBody Trade trade,@PathVariable("id") int id){
		trade.setId(id);
		try{
			Trade tempTrade = this.tradeService.getTradeById(id);

			if(!tempTrade.getSymbol().equals(trade.getSymbol())) {

				System.out.print("insdide symbole in updateTrade2");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			else if(!tempTrade.getType().equals(trade.getType())) {
				System.out.print("insdide type in updateTrade2");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			else if(trade.getType().equals("buy")){
				System.out.print("insdide buy in updateTrade2");
				ResponseEntity<ResponseTrade> responseTrade=  updateBuyTrade(id,trade);
				return responseTrade;
			}
			else if(trade.getType().equals("sell")) {

				System.out.print("insdide sell in updateTrade2");
				ResponseEntity<ResponseTrade> responseTrade=  updateSellTrade(id, tempTrade, trade);
				return responseTrade;
			}
		}catch (Exception e) {
			System.out.println("insdide exception in updateTrade2");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@ApiOperation(value = "returns list of all trades")
	@GetMapping(value = {"portfolio","trade","/"})
	public ResponseEntity<Object> getAllTrades(){
		List<Trade> orders = tradeService.getAllTrades();
		Map<String, String> map = new HashMap<String, String>();
		if(orders.size()<=0) {
			map.put("status", "Success");
			map.put("message", "No value present");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(map);
		}
		return ResponseEntity.ok(orders);
	}
	
	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@ApiOperation(value = "delete a specific trade ")
	@DeleteMapping("trade/{id}")
	public ResponseEntity<ResponseTrade> deleteTrade(@PathVariable("id") int id){
		Trade trade = this.tradeService.getTradeById(id);
		if(trade==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","No such id found!",null));
		}
		if(trade.getType().equalsIgnoreCase("buy")) {
			ResponseEntity<ResponseTrade> responseTrade= deleteBuyTrade2(id);
			return responseTrade;

		}
		else if(trade.getType().equalsIgnoreCase("sell")) {
			ResponseEntity<ResponseTrade> responseTrade= deleteSellTrade2(id);
			return responseTrade;
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","Something went wrong!",null));


	}

	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@ApiOperation(value = "update specific trade")
	@PostMapping("/trade")
	public ResponseEntity<ResponseTrade> addTrades(@Valid @RequestBody Trade trade){	
		ResponseEntity<ResponseTrade> responseTrade = null;
		trade.setSymbol(trade.getSymbol().toUpperCase());
		trade.setType(trade.getType().toLowerCase());
		if(trade.getType().equalsIgnoreCase("buy")) {
			responseTrade =  buyTrade2(trade);
			return responseTrade;
		}
		else if(trade.getType().equalsIgnoreCase("sell")) {
			responseTrade  = sellTrade2(trade);
			return responseTrade;
		}
		else {
			System.out.print("insdie ellse");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}


	}
	ResponseEntity<ResponseTrade> deleteSellTrade2(int id) {
		try {
			Trade trade = this.tradeService.getTradeById(id);
			Portfolio portfolio  = this.portfolioService.getPortfolioBySymbol(trade.getSymbol());
			portfolio.setQty(portfolio.getQty()+trade.getQty());
			portfolio = this.portfolioService.save(portfolio);
			this.tradeService.deleteTradeById(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","deleted",null));


		}catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","eror occured while processing",null));

		}
	}

	ResponseEntity<ResponseTrade> deleteBuyTrade2(int id) {
		try {
			Trade trade = this.tradeService.getTradeById(id);
			Portfolio portfolio=this.portfolioService.getPortfolioBySymbol(trade.getSymbol());
			
				double portfolio__price = portfolio.getAvg_price();
				int portfolio_qty = portfolio.getQty();
				double trade_price = trade.getPrice();
				int trade_qty = trade.getQty();
				double answer =  (((portfolio__price) * (portfolio_qty)) - ((trade_price)*(trade_qty)))/ ((portfolio_qty) - (trade_qty));
				int qty =  portfolio_qty- trade_qty;
				portfolio.setAvg_price(answer);
				portfolio.setQty(qty);
				if(qty==0) {
					this.portfolioService.deletePortfolioById(portfolio.getId());
				}
				else {

					portfolio  =  this.portfolioService.save(portfolio);
				}
				
				this.tradeService.deleteTradeById(id);
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","deleted",null));

		} catch (Exception e) {
			System.out.print("inside null"+e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","eror occured while processing",null));

		}	


	}

	ResponseEntity<ResponseTrade> buyTrade2(Trade trade) {
		Portfolio portfolio = null;
		try{
			portfolio =  this.portfolioService.getPortfolioBySymbol(trade.getSymbol());
			System.out.print(portfolio);
		}
		catch (Exception e) {
			System.out.println("inside buy trade 2 exceepitoon");
			// TODO: handle exception

		}
		if(portfolio!=null) {
			double avg_price =( ( portfolio.getAvg_price() * portfolio.getQty() )+( trade.getPrice() * trade.getQty() )) / ( trade.getQty() + portfolio.getQty() );
			int avg_qty = trade.getQty() + portfolio.getQty();
			portfolio.setAvg_price(avg_price);
			portfolio.setQty(avg_qty);
			portfolio = this.portfolioService.save(portfolio);
			trade = this.tradeService.add(trade);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseTrade("success", "", trade));

		}
		else {
			trade  =  this.tradeService.add(trade);
			portfolio = new Portfolio(trade.getSymbol(),trade.getQty(),trade.getPrice());
			portfolio = this.portfolioService.save(portfolio);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseTrade("success", "", trade));

		}

	}

	ResponseEntity<ResponseTrade> sellTrade2(Trade trade){
		Portfolio portfolio = null;
		portfolio = this.portfolioService.getPortfolioBySymbol(trade.getSymbol());

		if(portfolio!=null) {
			System.out.print("inside sell trade2");
			int remaining_qty = portfolio.getQty() - trade.getQty();
			if(remaining_qty < 0) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseTrade("fail","Please enter proper selling qty",null));
			}
			else
			{
				Trade saveTrade = this.tradeService.add(trade);
				if(remaining_qty == 0) {
					this.portfolioService.deletePortfolioById(portfolio.getId());
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","",saveTrade));
				}
				else {
					portfolio.setQty(remaining_qty);
					this.portfolioService.save(portfolio);
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","",saveTrade));
				}
			}

		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("fail","No such security present",null));
		}

	}
	ResponseEntity<ResponseTrade> updateSellTrade(int id,Trade trade,Trade tempTrade){
		Portfolio portfolio  = this.portfolioService.getPortfolioBySymbol(trade.getSymbol());
		if((portfolio.getQty() + trade.getQty() - tempTrade.getQty())<=0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","enter proper qty",null));
		}
		portfolio.setQty(portfolio.getQty()+trade.getQty());
		
		int remaining_qty = portfolio.getQty() - tempTrade.getQty();
		
			Trade saveTrade = this.tradeService.add(tempTrade);
			if(remaining_qty == 0) {
				this.portfolioService.deletePortfolioById(trade.getId());
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","",saveTrade));
			}
			else {
				portfolio.setQty(remaining_qty);
				this.portfolioService.save(portfolio);
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","",saveTrade));
			}
		
		
	}
	ResponseEntity<ResponseTrade> updateBuyTrade(int id,Trade tempTrade){
		try{
			Trade trade = this.tradeService.getTradeById(id);

			Portfolio portfolio=this.portfolioService.getPortfolioBySymbol(trade.getSymbol());
			if((portfolio.getQty() - trade.getQty()) <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","enter proper qty",null));
			}
			else{
				double portfolio__price = portfolio.getAvg_price();
				int portfolio_qty = portfolio.getQty();
				double trade_price = trade.getPrice();
				int trade_qty = trade.getQty();
				double answer =  (((portfolio__price) * (portfolio_qty)) - ((trade_price)*(trade_qty)))/ ((portfolio_qty) - (trade_qty));
				int qty =  portfolio_qty- trade_qty;
				portfolio.setAvg_price(answer);
				portfolio.setQty(qty);
				portfolio  =  this.portfolioService.save(portfolio);
				tempTrade = this.tradeService.add(tempTrade);
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseTrade("success","deleted",tempTrade));
			}
		}
		catch (Exception e) {
			System.out.print("inside null"+e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTrade("fail","eror occured while processing",null));
		}

	}

	


	
}
