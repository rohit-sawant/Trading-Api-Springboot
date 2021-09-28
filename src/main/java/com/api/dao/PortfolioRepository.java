package com.api.dao;

import org.springframework.data.repository.CrudRepository;

import com.api.entities.Portfolio;

public interface PortfolioRepository extends CrudRepository<Portfolio, Integer>{
public Portfolio findById(int id);
public Portfolio findBySymbol(String symbol);
}
