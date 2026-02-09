package com.gelir.gider.controller;

import com.gelir.gider.dto.IncomeDTO;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.UserRepository;
import com.gelir.gider.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<IncomeDTO> createIncome(@Valid @RequestBody IncomeDTO incomeDTO,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        IncomeDTO created = incomeService.createIncome(incomeDTO, userId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<IncomeDTO>> getMonthlyIncomes(@PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<IncomeDTO> incomes = incomeService.getMonthlyIncomes(userId, year, month);
        return ResponseEntity.ok(incomes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@PathVariable Long id,
            @Valid @RequestBody IncomeDTO incomeDTO,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        IncomeDTO updated = incomeService.updateIncome(id, incomeDTO, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncome(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        incomeService.deleteIncome(id, userId);
        return ResponseEntity.ok("Gelir silindi");
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return user.getId();
    }
}
