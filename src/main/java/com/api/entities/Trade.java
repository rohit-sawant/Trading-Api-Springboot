package com.api.entities;

import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import com.sun.istack.NotNull;



@Entity
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	String symbol;

	@CreationTimestamp
	private LocalDateTime createdOn;

	@UpdateTimestamp
	private LocalDateTime upDatedOn;
	
	@NotNull
	@Size(min = 3,max = 4,message = "please enter buy or sell")
	String type;

	@Range(min = 1,message = "please enter numbers greater than equal to 1")
	int Qty;
	
	@Positive(message = "please select only postivie ")
	double price;

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

	public Trade() {
		super();
	}

	public Trade(String symbol, int qty, double price,String type) {
		super();
		this.symbol = symbol;
		Qty = qty;
		this.price = price;
		this.type=type;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpDatedOn() {
		return upDatedOn;
	}

	public void setUpDatedOn(LocalDateTime upDatedOn) {
		this.upDatedOn = upDatedOn;
	}

	public int getQty() {
		return Qty;
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", symbol=" + symbol + ", createdOn=" + createdOn + ", upDatedOn=" + upDatedOn
				+ ", type=" + type + ", Qty=" + Qty + ", price=" + price + "]";
	}
	public void setQty(int qty) {
		Qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}


}
