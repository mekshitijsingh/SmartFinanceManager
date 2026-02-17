package com.kshitij.smartfinancemanager.finance;

import com.kshitij.smartfinancemanager.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.kshitij.smartfinancemanager.dashboard.dto.MonthlySummaryResponse;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Page<Transaction> findByUser(User user, Pageable pageable);
	
	List<Transaction> findByUser(User user);

    
    @Query("""
    	    SELECT COALESCE(SUM(t.amount), 0)
    	    FROM Transaction t
    	    WHERE t.user = :user AND t.type = 'INCOME'
    	""")
    	BigDecimal getTotalIncome(@Param("user") User user);

    	@Query("""
    	    SELECT COALESCE(SUM(t.amount), 0)
    	    FROM Transaction t
    	    WHERE t.user = :user AND t.type = 'EXPENSE'
    	""")
    	BigDecimal getTotalExpense(@Param("user") User user);
    	
    	Page<Transaction> findByUserAndDateBetween(
    	        User user,
    	        LocalDate startDate,
    	        LocalDate endDate,
    	        Pageable pageable
    	);

    	@Query("""
    		    SELECT new com.kshitij.smartfinancemanager.dashboard.dto.MonthlySummaryResponse(
    		        YEAR(t.date),
    		        MONTH(t.date),
    		        SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END),
    		        SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END)
    		    )
    		    FROM Transaction t
    		    WHERE t.user = :user
    		    GROUP BY YEAR(t.date), MONTH(t.date)
    		    ORDER BY YEAR(t.date), MONTH(t.date)
    		""")
    		List<MonthlySummaryResponse> getMonthlySummary(@Param("user") User user);


}