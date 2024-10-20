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
@Schema(description = "Информация для создания банковского счета и привязки к пользователю")
public class CreateBankAccountDto {

    @Schema(description = "Идентификатор владельца счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ownerUserId;

}
