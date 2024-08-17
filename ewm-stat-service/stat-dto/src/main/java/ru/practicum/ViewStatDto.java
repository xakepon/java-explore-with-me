package ru.practicum;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatDto {
    private String app;
    private String uri;
    private Long hits;
}
