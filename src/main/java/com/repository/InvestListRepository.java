package com.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.entity.InvestList;

@Repository
public interface InvestListRepository extends JpaRepository<InvestList, Long> {
	
	@Query(value = 	"SELECT "
					+ " inv.invest_id "
					+ ",inv.user_id "
					+ ",prd.product_id "
					+ ",prd.title "
					+ ",prd.total_investing_amount "
					+ ",inv.amount "
					+ ",inv.investing_at "
					+ " FROM product prd LEFT JOIN invest_list inv "
					+ " WHERE prd.product_id = inv.product_id "
					+ " AND inv.user_id = ?"
					+ " ORDER BY inv.invest_id DESC"
					, nativeQuery=true)
	List<InvestList> findInvestByUserId(String userId);
}
