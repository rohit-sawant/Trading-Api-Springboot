package com.api.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Portfolio {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	String symbol;
	int qty;
	double avg_price;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getAvg_price() {
		return avg_price;
	}
	public void setAvg_price(double avg_price) {
		this.avg_price = avg_price;
	}
	
	public Portfolio() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Portfoilio [id=" + id + ", symbol=" + symbol + ", qty=" + qty + ", avg_price=" + avg_price+ "]";
	}
	public Portfolio(String symbol, int qty, double avg_price) {
		super();
		this.symbol = symbol;
		this.qty = qty;
		this.avg_price = avg_price;
	}
	
	
}
