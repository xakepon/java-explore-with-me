package ru.practicum.api.requestDto;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateCompilationRequest {

    private List<Long> events;
    private Boolean pinned;
    private String title;
}
