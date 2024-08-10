package ru.practicum;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatDto {
    private String app;
    private String uri;
    private Long hits;
}
