package ru.practicum.api.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class NewUserRequest {
    @NotNull
    @Email
    @Size(min = 6, max = 254)
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
