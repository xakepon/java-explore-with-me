package ru.practicum.api.publicAPI.category;

import ru.practicum.api.responseDto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {


    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);

}
