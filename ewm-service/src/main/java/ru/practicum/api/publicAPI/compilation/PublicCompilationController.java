package ru.practicum.api.publicAPI.compilation;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.responseDto.CompilationDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class PublicCompilationController {
    private final PublicCompilationService service;

    @GetMapping(path = "/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("PUBLIC-GET-request, getCompilations: pinned={}, from={}, size={}", pinned, from, size);
        return service.getAllCompilations(pinned, from, size);
    }

    @GetMapping(path = "/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilation(@Validated @Positive @PathVariable Long compId) {
        log.info("PUBLIC-GET-request, getCompilation: compId={}", compId);
        return service.getCompilationById(compId);
    }

}
