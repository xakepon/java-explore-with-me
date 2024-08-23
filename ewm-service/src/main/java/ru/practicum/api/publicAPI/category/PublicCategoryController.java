package ru.practicum.api.publicAPI.category;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.responseDto.CategoryDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class PublicCategoryController {

    private final PublicCategoryService service;

    @GetMapping(path = "/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("PUBLIC-GET-request, getCategories: from={}, size={}", from, size);
        return service.getAllCategories(from, size);
    }

    @GetMapping(path = "/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@Validated @Positive @PathVariable Long catId) {
        log.info("PUBLIC-GET-request, getCategory: catId={}", catId);
        return service.getCategoryById(catId);
    }

}
