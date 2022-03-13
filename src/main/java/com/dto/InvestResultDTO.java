package com.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 투자하기 결과 DTO
 * @author hwkim
 *
 */
@Getter
@Setter
public class InvestResultDTO {
	
	//결과 응답 코드
	private String resultStatus;
	
	//결과 응답 메시지
	private String resultMsg;
	
	//투자 처리 성공 금액
	private BigDecimal investAmount;
}
