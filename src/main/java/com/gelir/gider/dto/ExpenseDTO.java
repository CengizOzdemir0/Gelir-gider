package com.gelir.gider.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDTO {

    private Long id;

    @NotNull(message = "Tutar boş olamaz")
    @DecimalMin(value = "0.01", message = "Tutar 0'dan büyük olmalıdır")
    private BigDecimal amount;

    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir")
    private String description;

    private Long categoryId;
    private String categoryName;

    @NotNull(message = "İşlem tarihi boş olamaz")
    private LocalDate transactionDate;

    private Integer year;
    private Integer month;
}
