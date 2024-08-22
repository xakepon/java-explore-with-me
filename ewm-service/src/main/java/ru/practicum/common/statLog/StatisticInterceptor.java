package ru.practicum.common.statLog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatClient;
import ru.practicum.common.constants.Constants;

import java.time.LocalDateTime;

@Slf4j
@Component
public class StatisticInterceptor implements HandlerInterceptor, Constants {
    private final StatClient statClient;

    public StatisticInterceptor(StatClient statClient) {
        this.statClient = statClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            EndpointHitDto hitDto = EndpointHitDto.builder()
                    .app("ewm-main-service")
                    .uri(request.getRequestURI())
                    .ip(request.getRemoteAddr())
                    .timestamp(LocalDateTime.now().format(formatter))
                    .build();
            statClient.addEndpointHit(hitDto);
        } catch (Exception e) {
            log.info("StatisticInterceptor: Error sending hit: " + e.getMessage());
        }
        return true;
    }

}
