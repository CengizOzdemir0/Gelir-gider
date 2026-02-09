package com.gelir.gider.service;

import com.gelir.gider.dto.ExpenseDTO;
import com.gelir.gider.dto.IncomeDTO;
import com.gelir.gider.dto.MonthlySummaryDTO;
import com.gelir.gider.repository.ExpenseRepository;
import com.gelir.gider.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @Transactional(readOnly = true)
    public MonthlySummaryDTO getMonthlySummary(Long userId, int year, int month) {
        BigDecimal totalIncome = incomeRepository.sumAmountByUserIdAndYearAndMonth(userId, year, month);
        BigDecimal totalExpense = expenseRepository.sumAmountByUserIdAndYearAndMonth(userId, year, month);

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        List<IncomeDTO> incomes = incomeService.getMonthlyIncomes(userId, year, month);
        List<ExpenseDTO> expenses = expenseService.getMonthlyExpenses(userId, year, month);

        return MonthlySummaryDTO.builder()
                .year(year)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .incomes(incomes)
                .expenses(expenses)
                .build();
    }
}
