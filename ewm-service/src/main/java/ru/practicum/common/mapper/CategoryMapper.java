package ru.practicum.common.mapper;

import ru.practicum.api.requestDto.NewCategoryDto;
import ru.practicum.api.responseDto.CategoryDto;
import ru.practicum.persistence.models.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }



    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
