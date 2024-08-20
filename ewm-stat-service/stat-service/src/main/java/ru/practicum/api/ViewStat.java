package ru.practicum.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStat {
    private String app;
    private String uri;
    private Long hits;
}
