package ru.klokov.tscommon.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Минимальная информация о пользователе, владеющем счетом")
public class UserSimpleDataDto {
    @Schema(description = "Идентификатор пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "Уникальное имя пользователя в системе", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(description = "Идентификаторы счетов пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Long> bankAccountIds;
}
