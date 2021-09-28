package com.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.dao.PortfolioRepository;
import com.api.entities.Portfolio;
import com.api.entities.Trade;

@Component
public class PortfolioService {
	@Autowired
	PortfolioRepository portfolioRepository;

	public PortfolioRepository getPortfolioRepository() {
		return portfolioRepository;
	}

	public void setPortfolioRepository(PortfolioRepository portfolioRepository) {
		this.portfolioRepository = portfolioRepository;
	}
	public List<Portfolio> getallPortfolios(){
		List<Portfolio> portfolios =  (List<Portfolio>) this.portfolioRepository.findAll();
		return portfolios;
	}

	public Portfolio save(Portfolio portfolio) {
		return this.portfolioRepository.save(portfolio);
	}
	//	 -------------------------add securities
	public Portfolio addTrade(Portfolio portfolio) {
		try {
			Portfolio p =  this.portfolioRepository.findBySymbol(portfolio.getSymbol());



			//		 if security already exist in database
			if(p!=null) {

				//			 get the average of all and store it
				double avg_price =( ( p.getAvg_price() * p.getQty() )+( portfolio.getAvg_price() * portfolio.getQty() )) / ( p.getQty() + portfolio.getQty() );
				int avg_qty = p.getQty() + portfolio.getQty();
				p.setAvg_price(avg_price);
				p.setQty(avg_qty);

				//			update the portfolio
				return this.portfolioRepository.save(p);
			}
		} catch (Exception e) {
			System.out.print("inside add portfolio");
			return null;
		}

		//		 if not presnt return new 
		return this.portfolioRepository.save(portfolio);

	}

	//---------------------------sell securities
	//----------------return null if sell did not funciton properly
	public Portfolio sell(Portfolio portfolio) {
		try{
			Portfolio p =  this.portfolioRepository.findBySymbol(portfolio.getSymbol());


			//		 if security already exist in database
			if(p!=null) {
				if(( p.getQty() - portfolio.getQty()) < 0) {
					return null;
				}
				else if(( p.getQty() - portfolio.getQty()) == 0) {
					this.portfolioRepository.deleteById(p.getId());
					return portfolio;
				}
				else {
					p.setQty(p.getQty() - portfolio.getQty());
				}
			}
			return this.portfolioRepository.save(p);
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public void deletePortfolioById(int id) {
		this.portfolioRepository.deleteById(id);
	}
	public Trade delete(Portfolio portfolio,Trade trade) {
		double portfolio__price = portfolio.getAvg_price();
		int portfolio_qty = portfolio.getQty();
		double trade_price = trade.getPrice();
		int trade_qty = trade.getQty();
		double answer =  (((portfolio__price) * (portfolio_qty)) - ((trade_price)*(trade_qty)))/ ((portfolio_qty) - (trade_qty));
		int qty =  portfolio_qty- trade_qty;
		portfolio.setAvg_price(answer);
		portfolio.setQty(qty);
		this.portfolioRepository.save(portfolio);
		return trade;
	}
	public Portfolio getPortfolioBySymbol(String name) {
		return this.portfolioRepository.findBySymbol(name);

	}
	public Portfolio findPortfolioById(int id) {
		Portfolio portfolio = this.portfolioRepository.findById(id);
		return portfolio;
	}


}
