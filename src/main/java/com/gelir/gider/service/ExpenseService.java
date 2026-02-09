package com.gelir.gider.service;

import com.gelir.gider.dto.ExpenseDTO;
import com.gelir.gider.entity.Category;
import com.gelir.gider.entity.Expense;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.CategoryRepository;
import com.gelir.gider.repository.ExpenseRepository;
import com.gelir.gider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Expense expense = Expense.builder()
                .user(user)
                .amount(expenseDTO.getAmount())
                .description(expenseDTO.getDescription())
                .transactionDate(expenseDTO.getTransactionDate())
                .build();

        if (expenseDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
            expense.setCategory(category);
        }

        expense = expenseRepository.save(expense);
        return mapToDTO(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseDTO> getMonthlyExpenses(Long userId, int year, int month) {
        return expenseRepository.findByUserIdAndYearAndMonthAndDeletedFalse(userId, year, month)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO, Long userId) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gider bulunamadı"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu gideri güncelleme yetkiniz yok");
        }

        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setTransactionDate(expenseDTO.getTransactionDate());

        if (expenseDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
            expense.setCategory(category);
        }

        expense = expenseRepository.save(expense);
        return mapToDTO(expense);
    }

    @Transactional
    public void deleteExpense(Long id, Long userId) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gider bulunamadı"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu gideri silme yetkiniz yok");
        }

        expense.setDeleted(true);
        expenseRepository.save(expense);
    }

    private ExpenseDTO mapToDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .categoryId(expense.getCategory() != null ? expense.getCategory().getId() : null)
                .categoryName(expense.getCategory() != null ? expense.getCategory().getName() : null)
                .transactionDate(expense.getTransactionDate())
                .year(expense.getYear())
                .month(expense.getMonth())
                .build();
    }
}
