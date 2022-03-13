package com.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = " SELECT "
					 + " product_id "
					 + ",title "
					 + ",invester_count "
					 + ",invest_status "
					 + ",curr_investing_amount "
					 + ",total_investing_amount "
					 + ",PARSEDATETIME(started_at, 'yyyyMMddHHmmssSSS') started_at "
					 + ",PARSEDATETIME(finished_at, 'yyyyMMddHHmmssSSS') finished_at "
					 + " FROM product "
					 + " WHERE ? BETWEEN started_at AND finished_at"
					 , nativeQuery=true)
	List<Product> findAllValidProduct(String nowDate);
	
	
	@Query(value = " SELECT "
					 + " product_id "
					 + ",title "
					 + ",invester_count "
					 + ",invest_status "
					 + ",curr_investing_amount "
					 + ",total_investing_amount "
					 + ",started_at "
					 + ",finished_at "
					 + " FROM product "
					 + " WHERE product_id = ? "
					 + " AND ? BETWEEN started_at AND finished_at "
					 , nativeQuery=true)
	Optional<Product> findValidProduct(Long productId, String nowDate);
}
