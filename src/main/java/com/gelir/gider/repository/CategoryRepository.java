package com.gelir.gider.repository;

import com.gelir.gider.entity.Category;
import com.gelir.gider.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByTypeAndUserIdIsNullAndDeletedFalse(CategoryType type);

    List<Category> findByTypeAndUserIdAndDeletedFalse(CategoryType type, Long userId);

    List<Category> findByUserIdAndDeletedFalse(Long userId);
}
