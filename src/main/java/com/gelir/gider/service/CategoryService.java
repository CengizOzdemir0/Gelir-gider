package com.gelir.gider.service;

import com.gelir.gider.dto.CategoryDTO;
import com.gelir.gider.entity.Category;
import com.gelir.gider.entity.User;
import com.gelir.gider.enums.CategoryType;
import com.gelir.gider.repository.CategoryRepository;
import com.gelir.gider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .type(categoryDTO.getType())
                .parentId(categoryDTO.getParentId())
                .description(categoryDTO.getDescription())
                .build();

        if (categoryDTO.getUserId() != null) {
            User user = userRepository.findById(categoryDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            category.setUser(user);
        }

        category = categoryRepository.save(category);
        return mapToDTO(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesByType(CategoryType type, Long userId) {
        List<Category> categories = new ArrayList<>();

        // Global kategoriler
        categories.addAll(categoryRepository.findByTypeAndUserIdIsNullAndDeletedFalse(type));

        // Kullanıcıya özel kategoriler
        if (userId != null) {
            categories.addAll(categoryRepository.findByTypeAndUserIdAndDeletedFalse(type, userId));
        }

        return categories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> !category.getDeleted())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setParentId(categoryDTO.getParentId());
        category.setDescription(categoryDTO.getDescription());

        category = categoryRepository.save(category);
        return mapToDTO(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        category.setDeleted(true);
        categoryRepository.save(category);
    }

    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .parentId(category.getParentId())
                .userId(category.getUser() != null ? category.getUser().getId() : null)
                .description(category.getDescription())
                .build();
    }
}
