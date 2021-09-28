package com.api.dao;

import org.springframework.data.repository.CrudRepository;

import com.api.entities.Trade;

public interface TradeRepositary extends CrudRepository<Trade, Integer> {

	public Trade findById(int id);
	
	public Trade findBySymbol(String name);
}
