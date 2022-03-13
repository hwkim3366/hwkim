package com.service;

import java.util.List;

import com.dto.InvestResultDTO;
import com.entity.InvestList;
import com.entity.Product;

public interface InvestService {
	
	public List<Product> findAllValidProduct(String nowDate);
	
	public InvestResultDTO investing(InvestList investList);
	
	public List<InvestList> findInvestByUserId(String userId);
}
