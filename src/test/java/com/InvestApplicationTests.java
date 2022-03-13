package com;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.dto.InvestResultDTO;
import com.entity.InvestList;
import com.entity.Product;
import com.exception.ProductNotFoundException;
import com.service.InvestService;

/**
 * data.sql의 insert script로 생성되는 투자 상품 마스터 데이터 기준 테스트
 * @author hwkim
 *
 */
@SpringBootTest
class InvestApplicationTests {
	
	@Autowired
	private InvestService investService;
	
	@Test
	@Transactional
	@DisplayName("[투자하기] 특정 유저 아이디로 투자 성공 한뒤 모집 금액 한도가 남은 경우")
	public void test03() {
		
		InvestList investList = new InvestList();
		
		String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
		
		investList.setInvestingAt(testDate);
		investList.setUserId("111");
		investList.setAmount(BigDecimal.TEN);
		investList.setProductId(1L);//1번 상품 모집 한도는 100
		
		//투자 처리
		InvestResultDTO investResultDTO = investService.investing(investList);
		
		//투자 완료, 추가 모집 가능
		assertEquals(investResultDTO.getResultStatus(), "SUCCESS");
		assertEquals(investResultDTO.getResultMsg(), "Done, more investment is possible");
		assertEquals(investResultDTO.getInvestAmount(), BigDecimal.TEN);
	}
	
	@Test
	@Transactional
	@DisplayName("[투자하기] 특정 유저 아이디로 투자 성공 한뒤 모집 마감 되는 경우")
	public void test04() {
		
		InvestList investList = new InvestList();
		
		String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
		
		BigDecimal bigNumber = new BigDecimal("100"); //1번 상품 모집 한도는 100
		
		investList.setInvestingAt(testDate);
		investList.setUserId("111");
		investList.setAmount(bigNumber);
		investList.setProductId(1L);//1번 상품 모집 한도는 100
		
		//투자 처리
		InvestResultDTO investResultDTO = investService.investing(investList);
		
		//투자 완료, 이 투자건을 마지막으로 모집 마감
		assertEquals(investResultDTO.getResultStatus(), "SUCCESS");
		assertEquals(investResultDTO.getResultMsg(), "Sold out");
		assertEquals(investResultDTO.getInvestAmount(), bigNumber);
	}
	
	@Test
	@Transactional
	@DisplayName("[투자하기] 특정 유저 아이디로 투자 실패 처리(모집 완료 후 투자하려 한 경우)")
	public void test05() {
		
		InvestList investList = new InvestList();
		
		String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
		
		BigDecimal bigNumber = new BigDecimal("100"); //1번 상품 모집 한도는 100
		
		investList.setInvestingAt(testDate);
		investList.setUserId("111");
		investList.setAmount(bigNumber);
		investList.setProductId(1L);//1번 상품 모집 한도는 100
		
		InvestResultDTO investResultDTO = investService.investing(investList);
		
		//투자 완료, 이 투자건을 마지막으로 모집 마감
		assertEquals(investResultDTO.getResultStatus(), "SUCCESS");
		assertEquals(investResultDTO.getResultMsg(), "Sold out");
		assertEquals(investResultDTO.getInvestAmount(), bigNumber);
		
		//모집 마감 후 투자 시도
		investList.setInvestingAt(testDate);
		investList.setUserId("111");
		investList.setAmount(bigNumber);
		investList.setProductId(1L);//1번 상품 모집 한도는 100
		
		InvestResultDTO investResultDTO2 = investService.investing(investList);
		
		//이미 모집 마감 상태라 실패
		assertEquals(investResultDTO2.getResultStatus(), "FAIL");
		assertEquals(investResultDTO2.getResultMsg(), "Sold out");
		assertEquals(investResultDTO2.getInvestAmount(), BigDecimal.ZERO);
		
	}
	
	@Test
	@Transactional
	@DisplayName("[투자하기] 특정 유저 아이디로 투자 실패 처리(모집 완료는 아니나 총 모집 금액을 초과해서 투자하려 한 경우)")
	public void test06() {
		
		InvestList investList = new InvestList();
		
		String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
		
		BigDecimal bigNumber = new BigDecimal("1000"); 
		
		investList.setInvestingAt(testDate);
		investList.setUserId("111");
		investList.setAmount(bigNumber);
		investList.setProductId(1L); //1번 상품 모집 한도는 100
		
		//투자 처리
		InvestResultDTO investResultDTO = investService.investing(investList);
		
		//현재 모집 금액 + 투자 신청 금액이 총 모집 금액을 초과하여 실패
		assertEquals(investResultDTO.getResultStatus(), "FAIL");
		assertEquals(investResultDTO.getResultMsg(), "Investment limit exceeded");
		assertEquals(investResultDTO.getInvestAmount(), BigDecimal.ZERO);
	}
	
	@Test
	@Transactional
	@DisplayName("[투자하기] 유효하지 않은 투자 상품에 투자시 실패 처리(없거나 투자 기간이 아닌 상품에 투자하려는 경우)")
	public void test07() {
		
		try
		{
			InvestList investList = new InvestList();
			
			String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
			
			investList.setInvestingAt(testDate);
			investList.setUserId("111");
			investList.setAmount(BigDecimal.TEN);
			investList.setProductId(3L);//모집 기간 종료된 3번 상품
			
			//투자 처리
			InvestResultDTO investResultDTO = investService.investing(investList);
			
		}
		catch(ProductNotFoundException e)
		{
			//Exception 발생하면 test pass
		}
		
	}
	
	@Test
	@DisplayName("[전체 투자 상품 조회] 현재 일자(지정 일자)가 모집 기간에 포함되는 모든 상품 조회")
	public void test01() {
		
		String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
		
		List<Product> resultList = investService.findAllValidProduct(testDate);
		
		//1건 이상 조회
		assertThat(resultList.size()).isGreaterThan(0);
	}
	
	@Test
	@DisplayName("[전체 투자 상품 조회] 현재 일자(지정 일자)가 모든 상품의 모집 기간에 포함되지 않는 경우 조회 건수 없음 확인")
	public void test02() {
		
		String testDate = "20200101000000000";//yyyyMMddHHmmssSSS
		
		List<Product> resultList = investService.findAllValidProduct(testDate);
		
		//조회 건 없음
		assertThat(resultList.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	@DisplayName("[나의 투자 상품 조회] 특정 유저 아이디로 투자하고 투자한 내역 조회 확인")
	public void test08() {
		
		InvestList investList = new InvestList();
		
		String testDate = "20220313000000000";//yyyyMMddHHmmssSSS
		
		investList.setInvestingAt(testDate);
		investList.setUserId("111");
		investList.setAmount(BigDecimal.TEN);
		investList.setProductId(1L);
		
		InvestResultDTO investResultDTO = investService.investing(investList);
		
		assertEquals(investResultDTO.getResultStatus(), "SUCCESS");
		assertEquals(investResultDTO.getResultMsg(), "Done, more investment is possible");
		assertEquals(investResultDTO.getInvestAmount(), BigDecimal.TEN);
		
		List<InvestList> result = investService.findInvestByUserId("111");
		
		assertThat(result.size()).isEqualTo(1);
	}
}
