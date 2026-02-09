package com.gelir.gider.dto;

import com.gelir.gider.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Kategori adı boş olamaz")
    @Size(max = 100, message = "Kategori adı en fazla 100 karakter olabilir")
    private String name;

    @NotNull(message = "Kategori tipi boş olamaz")
    private CategoryType type;

    private Long parentId;

    private Long userId;

    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir")
    private String description;
}
