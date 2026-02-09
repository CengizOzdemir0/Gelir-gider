package com.gelir.gider.repository;

import com.gelir.gider.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserIdAndYearAndMonthAndDeletedFalse(Long userId, Integer year, Integer month);

    List<Income> findByUserIdAndDeletedFalse(Long userId);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user.id = :userId AND i.year = :year AND i.month = :month AND i.deleted = false")
    BigDecimal sumAmountByUserIdAndYearAndMonth(@Param("userId") Long userId, @Param("year") Integer year,
            @Param("month") Integer month);
}
