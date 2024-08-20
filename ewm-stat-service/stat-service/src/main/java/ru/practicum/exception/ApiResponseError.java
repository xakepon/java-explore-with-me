package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiResponseError {

    private String status;
    private String reason;
    private String message;
    private String timestamp;

}
