package com.gelir.gider.repository;

import com.gelir.gider.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserIdAndYearAndMonthAndDeletedFalse(Long userId, Integer year, Integer month);

    List<Expense> findByUserIdAndDeletedFalse(Long userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.year = :year AND e.month = :month AND e.deleted = false")
    BigDecimal sumAmountByUserIdAndYearAndMonth(@Param("userId") Long userId, @Param("year") Integer year,
            @Param("month") Integer month);
}
