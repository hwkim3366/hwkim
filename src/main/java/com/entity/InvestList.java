package com.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 투자 내역 Entity
 * @author hwkim
 *
 */
@Getter
@Setter
@Entity
public class InvestList {
	
	//투자건 ID
	@Id
	@GeneratedValue
	private Long investId;
	
	//사용자 ID
	private String userId;
	
	//상품 ID
	private Long productId;
	
	//투자 금액
	private BigDecimal amount;
	
	//투자 일시
	private String investingAt;
	
	
	
	//상품 제목(Product)
	private String title;
	
	//총 모집급액(Product)
	private BigDecimal totalInvestingAmount;
}