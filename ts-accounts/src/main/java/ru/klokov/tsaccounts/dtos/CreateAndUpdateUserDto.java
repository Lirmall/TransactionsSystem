package ru.klokov.tsaccounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateAndUpdateUserDto",
        description = "Информация о пользователе при создании")
public class CreateAndUpdateUserDto {
    @Schema(description = "Уникальный логин пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(description = "Фамилия пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String secondName;
    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;
    @Schema(description = "Отчество пользователя (не обязательно)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String thirdName;
    @Schema(description = "Электронная почта пользователя",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "example@example.ru")
    private String email;
    @Schema(description = "Номер телефона пользователя",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "+1(111) 111-1111")
    private String phoneNumber;
}
