package com.gelir.gider.controller;

import com.gelir.gider.dto.MonthlySummaryDTO;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.UserRepository;
import com.gelir.gider.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;
    private final UserRepository userRepository;

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<MonthlySummaryDTO> getMonthlySummary(@PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        MonthlySummaryDTO summary = summaryService.getMonthlySummary(userId, year, month);
        return ResponseEntity.ok(summary);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return user.getId();
    }
}
