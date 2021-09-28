package com.api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.api.entities.Portfolio;
import com.api.services.PortfolioService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class PortfolioController {
	@Autowired
	PortfolioService portfolioService;

	public PortfolioController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PortfolioController [portfolioService=" + portfolioService + "]";
	}

	public PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}
	
	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@GetMapping("returns")
	public ResponseEntity<Object> getReturn(){
		double returns = 0.0;
		Map<String, Double> answer = new HashMap<String, Double>();
		List<Portfolio> portfolios = this.portfolioService.getallPortfolios();
		if(portfolios.size()<=0) {
			returns = 0; 
		}
		else {
			for(Portfolio portfolio: portfolios) {
				returns = returns +(double) (100.0 - portfolio.getAvg_price())* portfolio.getQty();
			}
				
		
		}
		answer.put("returns", returns);
		return new ResponseEntity<Object>(answer,HttpStatus.OK);
	}

	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@GetMapping("holdings")
	public ResponseEntity<List<Portfolio>> getPortfolios(){
		List<Portfolio> portfolios = portfolioService.getallPortfolios();
		if(portfolios.size()<=0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(portfolios);
	}

	@ApiResponses(
			value= {
					@ApiResponse(code = 422,message = "Unprocessable Entity,some input is wrong"),
					@ApiResponse(code =500,message = "Internal server error"),
					@ApiResponse(code =204,message = "Succesful but no content recieved from api")
			}
		)
	@GetMapping("holdings/{id}")
	public ResponseEntity<Portfolio> getPortfolio(@PathVariable("id") int id){
		Portfolio portfolios = portfolioService.findPortfolioById(id);

		if(portfolios==null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

		return ResponseEntity.ok(portfolios);
	}
}
