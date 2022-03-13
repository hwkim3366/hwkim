package com.controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.entity.InvestList;
import com.exception.AmountNullException;
import com.exception.ProductIdNullException;
import com.exception.UserIdFormatException;
import com.exception.UserIdNullException;
import com.service.InvestService;
import com.config.Response;
import com.config.ResponseCodes;

@RestController
public class InvestController {
	
	private InvestService investService;
	
    public InvestController(InvestService investService)
    {
        this.investService = investService;
    }
    
    /**
     * 모집 기간에 현재 일시가 포함되는 전체 투자 상품 조회
     */
	@GetMapping("/productList")
	public ResponseEntity<Response> findAllValidProduct()
	{
		LocalDateTime now = LocalDateTime.now();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		
		//현재 일시
		String nowDate = now.format(dateTimeFormatter);
		
		
		HttpHeaders headers = new HttpHeaders();

        Response response = Response.of(ResponseCodes.S1, investService.findAllValidProduct(nowDate));
        
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * 투자하기
	 */
	@PostMapping("/invest")
	public ResponseEntity<Response> investing(@RequestHeader("x-user-id") String userId, @RequestBody InvestList investList)
	{
		/**
		 * validation check
		 */
		//유저 Id가 null인 경우
		if(userId.trim().isEmpty())
		{
			throw new UserIdNullException();
		}
		
		//유저 Id가 숫자로만 구성되지 않은 경우
		if(!userId.matches("[0-9]+"))
		{
			throw new UserIdFormatException();
		}
				
		//상품 Id가 null인 경우
		if(investList.getProductId() == null)
		{
			throw new ProductIdNullException();
		}
		
		//투자 금액이 null인 경우
		if(investList.getAmount() == null)
		{
			throw new AmountNullException();
		}
		
		
		LocalDateTime now = LocalDateTime.now();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		
		//현재 일시
		String nowDate = now.format(dateTimeFormatter);  
		
		investList.setUserId(userId);
		investList.setInvestingAt(nowDate);
		
		HttpHeaders headers = new HttpHeaders();

        Response response = Response.of(ResponseCodes.S1, investService.investing(investList));
        
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * 나의(특정 유저의) 투자 상품 내역 조회
	 */
	@GetMapping("/investList")
	public ResponseEntity<Response> findInvestByUserId(@RequestHeader("x-user-id") String userId)
	{
		/**
		 * validation check
		 */
		//유저 Id가 null인 경우
		if(userId.trim().isEmpty())
		{
			throw new UserIdNullException();
		}
		
		//유저 Id가 숫자로만 구성되지 않은 경우
		if(!userId.matches("[0-9]+"))
		{
			throw new UserIdFormatException();
		}
		
		HttpHeaders headers = new HttpHeaders();

        Response response = Response.of(ResponseCodes.S1, investService.findInvestByUserId(userId));
        
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
}
