package com.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.dao.TradeRepositary;
import com.api.entities.Trade;

@Component
public class TradeService {
	
@Autowired
private TradeRepositary tradeRepositary;
public TradeService(TradeRepositary orderRepositary) {
	super();
	this.tradeRepositary = orderRepositary;
}



public TradeRepositary getTradeRepositary() {
	return tradeRepositary;
}



public void setTradeRepositary(TradeRepositary tradeRepositary) {
	this.tradeRepositary = tradeRepositary;
}


//-------------list of trade
public List<Trade> getAllTrades()
{
	List<Trade> trades =  (List<Trade>) this.tradeRepositary.findAll();
	return trades;
}

//-----------add trade
public Trade addTrades(Trade order) {
	return this.tradeRepositary.save(order);
}
public Trade add(Trade trade) {
	return this.tradeRepositary.save(trade);
}

//---------------delete a trade
public void deleteTradeById(int id) {
	this.tradeRepositary.deleteById(id);
}


//----------------find by id
public Trade getTradeById(int id) {
	try {
		Trade trade =  this.tradeRepositary.findById(id);
		return trade;
	} catch (Exception e) {
		// TODO: handle exception
		return null;
	}
	
}


//----------------find by name
public Trade getTradeByName(String name) {
	Trade trade =  this.tradeRepositary.findBySymbol(name);
	System.out.println(trade);
	return trade;
}









//end of class
}
