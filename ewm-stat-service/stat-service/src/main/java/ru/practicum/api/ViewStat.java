package ru.practicum.api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStat {
    private String app;
    private String uri;
    private Long hits;
}
