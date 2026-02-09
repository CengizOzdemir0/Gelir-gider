package com.gelir.gider.service;

import com.gelir.gider.dto.IncomeDTO;
import com.gelir.gider.entity.Category;
import com.gelir.gider.entity.Income;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.CategoryRepository;
import com.gelir.gider.repository.IncomeRepository;
import com.gelir.gider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public IncomeDTO createIncome(IncomeDTO incomeDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Income income = Income.builder()
                .user(user)
                .amount(incomeDTO.getAmount())
                .description(incomeDTO.getDescription())
                .transactionDate(incomeDTO.getTransactionDate())
                .build();

        if (incomeDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(incomeDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
            income.setCategory(category);
        }

        income = incomeRepository.save(income);
        return mapToDTO(income);
    }

    @Transactional(readOnly = true)
    public List<IncomeDTO> getMonthlyIncomes(Long userId, int year, int month) {
        return incomeRepository.findByUserIdAndYearAndMonthAndDeletedFalse(userId, year, month)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public IncomeDTO updateIncome(Long id, IncomeDTO incomeDTO, Long userId) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gelir bulunamadı"));

        if (!income.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu geliri güncelleme yetkiniz yok");
        }

        income.setAmount(incomeDTO.getAmount());
        income.setDescription(incomeDTO.getDescription());
        income.setTransactionDate(incomeDTO.getTransactionDate());

        if (incomeDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(incomeDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
            income.setCategory(category);
        }

        income = incomeRepository.save(income);
        return mapToDTO(income);
    }

    @Transactional
    public void deleteIncome(Long id, Long userId) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gelir bulunamadı"));

        if (!income.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu geliri silme yetkiniz yok");
        }

        income.setDeleted(true);
        incomeRepository.save(income);
    }

    private IncomeDTO mapToDTO(Income income) {
        return IncomeDTO.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .description(income.getDescription())
                .categoryId(income.getCategory() != null ? income.getCategory().getId() : null)
                .categoryName(income.getCategory() != null ? income.getCategory().getName() : null)
                .transactionDate(income.getTransactionDate())
                .year(income.getYear())
                .month(income.getMonth())
                .build();
    }
}
