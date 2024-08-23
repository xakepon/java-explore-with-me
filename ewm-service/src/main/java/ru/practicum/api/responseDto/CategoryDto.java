package ru.practicum.api.responseDto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String name;
}
