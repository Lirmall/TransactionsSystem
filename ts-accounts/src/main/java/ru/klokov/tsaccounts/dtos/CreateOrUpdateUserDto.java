package ru.klokov.tsaccounts.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о пользователе для создания нового пользователя")
public class CreateOrUpdateUserDto {
    @Schema(description = "Уникальный логин пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Фамилия пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String secondName;

    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Nullable
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Признак заблокированного пользователя", defaultValue = "false")
    private Boolean blocked = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Признак удалённоно пользователя", defaultValue = "false")
    private Boolean deleted = false;
}
