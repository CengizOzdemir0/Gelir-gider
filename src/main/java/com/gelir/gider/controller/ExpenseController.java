package com.gelir.gider.controller;

import com.gelir.gider.dto.ExpenseDTO;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.UserRepository;
import com.gelir.gider.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@Valid @RequestBody ExpenseDTO expenseDTO,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        ExpenseDTO created = expenseService.createExpense(expenseDTO, userId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<ExpenseDTO>> getMonthlyExpenses(@PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<ExpenseDTO> expenses = expenseService.getMonthlyExpenses(userId, year, month);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id,
            @Valid @RequestBody ExpenseDTO expenseDTO,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        ExpenseDTO updated = expenseService.updateExpense(id, expenseDTO, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        expenseService.deleteExpense(id, userId);
        return ResponseEntity.ok("Gider silindi");
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return user.getId();
    }
}
