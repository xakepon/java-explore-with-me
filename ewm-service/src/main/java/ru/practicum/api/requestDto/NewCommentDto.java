package ru.practicum.api.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class NewCommentDto {

    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;
}
