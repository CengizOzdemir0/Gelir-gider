package com.gelir.gider.controller;

import com.gelir.gider.dto.CategoryDTO;
import com.gelir.gider.enums.CategoryType;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.UserRepository;
import com.gelir.gider.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @GetMapping("/income")
    public ResponseEntity<List<CategoryDTO>> getIncomeCategories(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<CategoryDTO> categories = categoryService.getCategoriesByType(CategoryType.INCOME, userId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/expense")
    public ResponseEntity<List<CategoryDTO>> getExpenseCategories(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<CategoryDTO> categories = categoryService.getCategoriesByType(CategoryType.EXPENSE, userId);
        return ResponseEntity.ok(categories);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return user.getId();
    }
}
