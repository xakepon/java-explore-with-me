package ru.practicum.api.adminAPI.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.NewCategoryDto;
import ru.practicum.api.responseDto.CategoryDto;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.CategoryMapper;
import ru.practicum.persistence.models.Category;
import ru.practicum.persistence.repository.CategoryRep;
import ru.practicum.persistence.repository.EventRep;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRep categoryRepository;
    private final EventRep eventRepository;

    @Override
    public CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto) {
        Category newCategory = CategoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(newCategory);
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    public void deleteCategoryByAdmin(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        boolean categoryInUse = eventRepository.existsByCategoryId(catId);
        if (categoryInUse) {
            throw new ForbiddenException(String.format("Category with id=%d is not Empty", catId));
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategoryByAdmin(Long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        Category newCat = CategoryMapper.toCategory(newCategoryDto);
        newCat.setId(category.getId());

        categoryRepository.save(newCat);
        return CategoryMapper.toCategoryDto(newCat);
    }

}