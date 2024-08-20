package ru.practicum.api.adminAPI.category;

import ru.practicum.api.requestDto.NewCategoryDto;
import ru.practicum.api.responseDto.CategoryDto;

public interface AdminCategoryService {

    CategoryDto createCategoryByAdmin(NewCategoryDto categoryDto);

    void deleteCategoryByAdmin(Long catId);

    CategoryDto updateCategoryByAdmin(Long catId, NewCategoryDto categoryDto);

}
