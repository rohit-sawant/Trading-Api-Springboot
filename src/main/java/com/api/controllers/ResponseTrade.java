package com.api.controllers;

import com.api.entities.Trade;

public class ResponseTrade {
String status;
String message;
Trade trade;
public ResponseTrade(String status, String message, Trade trade) {
	super();
	this.status = status;
	this.message = message;
	this.trade = trade;
}
public ResponseTrade() {
	super();
	// TODO Auto-generated constructor stub
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Trade getTrade() {
	return trade;
}
public void setTrade(Trade trade) {
	this.trade = trade;
}

}
