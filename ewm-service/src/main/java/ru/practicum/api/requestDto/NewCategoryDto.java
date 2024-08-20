package ru.practicum.api.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class NewCategoryDto {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
