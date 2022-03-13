package com.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.dto.InvestResultDTO;
import com.entity.InvestList;
import com.entity.Product;
import com.exception.ProductNotFoundException;
import com.repository.InvestListRepository;
import com.repository.ProductRepository;
import com.service.InvestService;

@Service
public class InvestServiceImpl implements InvestService {
	
	private ProductRepository productRepository;
	
	private InvestListRepository investListRepository;
	
    public InvestServiceImpl(ProductRepository productRepository, InvestListRepository investListRepository)
    {
        this.productRepository = productRepository;
        this.investListRepository = investListRepository;
    }
	
    /**
     * 모집 기간에 현재 일시가 포함되는 전체 투자 상품 조회
     */
	public List<Product> findAllValidProduct(String nowDate)
	{
		return productRepository.findAllValidProduct(nowDate);
	}
	
	/**
	 * 투자하기
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public InvestResultDTO investing(InvestList investList) {
		
		InvestResultDTO investResultDTO = new InvestResultDTO();
		
		//투자 대상 상품 현황 조회
		Optional<Product> currentProductStatus = productRepository.findValidProduct(investList.getProductId(), investList.getInvestingAt());
		
		//투자 대상 상품이 존재하지 않거나 유효하지 않은 경우
		if(!currentProductStatus.isPresent())
		{
			throw new ProductNotFoundException();
		}
		
		currentProductStatus.ifPresent
		(
			currPrdStat ->
			{
				//투자 대상 상품의 총 모집 금액과 현재까지 누적 투자 금액 + 투자 신청금액을 비교
				int compareResult = currPrdStat.getTotalInvestingAmount().compareTo(currPrdStat.getCurrInvestingAmount().add(investList.getAmount()));
				
				//투자 성공 처리
				if(compareResult >= 0) 
				{
					//투자 대상 상품의 현재까지 누적 투자 금액에 투자 신청금액 추가
					currPrdStat.setCurrInvestingAmount(currPrdStat.getCurrInvestingAmount().add(investList.getAmount()));
					
					//투자 대상 상품의 투자 건수 증가 처리
					currPrdStat.setInvesterCount(currPrdStat.getInvesterCount() + 1);
					
					//현재 투자 처리로 모집 마감되는 경우
					if(compareResult == 0)
					{
						// 모집완료로 셋팅
						currPrdStat.setInvestStatus("Recruitment completed");
					}
					
					//투자 내역 반영
					InvestList resultInvestList = investListRepository.save(investList);
					
					//투자 상품 마스터 투자 내역 반영
					productRepository.save(currPrdStat);
					
					/*
					 * 투자 완료 처리
					 */
					investResultDTO.setResultStatus("SUCCESS");
					
					if(compareResult == 0)//현재 투자 처리로 모집 마감되는 경우
					{
						investResultDTO.setResultMsg("Sold out");
					}
					else
					{
						investResultDTO.setResultMsg("Done, more investment is possible");
					}
					
					//투자 성공 금액 셋팅
					investResultDTO.setInvestAmount(resultInvestList.getAmount());
				} 
				//투자 실패 처리
				else
				{
					investResultDTO.setResultStatus("FAIL");
					
					if(currPrdStat.getInvestStatus().equals("Recruitment completed")) //모집완료인 경우
					{
						investResultDTO.setResultMsg("Sold out");
					}
					else //모집완료는 아니나 총 모집 금액을 초과해서 투자하려 한 경우
					{
						investResultDTO.setResultMsg("Investment limit exceeded");
					}
					
					investResultDTO.setInvestAmount(BigDecimal.ZERO);
					
				}
			}
		);
		
		return investResultDTO;
	}
	
	/**
	 * 나의(특정 유저의) 투자 상품 내역 조회
	 */
	public List<InvestList> findInvestByUserId(String userId)
	{	
		return investListRepository.findInvestByUserId(userId);
	}
}
