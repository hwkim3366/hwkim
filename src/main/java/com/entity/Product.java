package com.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 투자 상품 마스터 Entity
 * @author hwkim
 *
 */
@Getter
@Setter
@Entity
public class Product {
	
	//상품 ID
	@Id
	private Long productId;
	
	//상품 제목
	private String title;
	
	//총 모집급액
	private BigDecimal totalInvestingAmount;
	
	//현재 모집급액
	private BigDecimal currInvestingAmount;
	
	//투자자 수(투자 건수)
	private int investerCount;
	
	//투자모집상태(모집중 : 0, 모집완료 : 1)
	private String investStatus;
	
	//상품 모집기간 from
	private String startedAt;
	
	//상품 모집기간 to
	private String finishedAt;
}
