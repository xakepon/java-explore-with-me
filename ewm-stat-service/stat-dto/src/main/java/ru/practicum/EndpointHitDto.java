package ru.practicum;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHitDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

}