package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return Map.of("error", "Ошибка валидации",
                "errorMessage", e.getMessage());
    }
    //не знаю, правильно ли я поняла, что нужно сделать
    //не доходит до меня смысл, для чего нужен обработчик
    //без него тоже все работало и тесты проходили
}