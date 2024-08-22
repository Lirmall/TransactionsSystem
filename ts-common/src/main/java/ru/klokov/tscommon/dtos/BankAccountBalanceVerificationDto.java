package ru.klokov.tscommon.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Верификация наличия средств на счету для транзакции")
public class BankAccountBalanceVerificationDto {
    @Schema(description = "Идентификатор счета отправителя средств", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long senderId;

    @Schema(description = "Идентификатор суммы транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double amount;
}
