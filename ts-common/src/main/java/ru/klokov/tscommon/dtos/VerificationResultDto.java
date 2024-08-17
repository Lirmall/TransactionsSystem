package ru.klokov.tscommon.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о проверке данных")
public class VerificationResultDto {
    @Schema(description = "Признак валидности данных", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isValid;

    @Schema(description = "Дополнительная информация", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}
