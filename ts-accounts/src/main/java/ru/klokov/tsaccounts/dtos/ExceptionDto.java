package ru.klokov.tsaccounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация об исключении в работе приложения")
public class ExceptionDto {
    @Schema(description = "Время возникновения исключения")
    private LocalDateTime exceptionTime;
    @Schema(description = "Http статус исключения")
    private int status;
    @Schema(description = "Краткое описание класса исключения")
    private String exceptionName;
    @Schema(description = "Сообщение исключения")
    private String message;
}
