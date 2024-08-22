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
@Schema(description = "Данные для проведения транзакции")
public class TransactionDataDto {
    @Schema(description = "Идентификатор счета отправителя средств", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long senderId;

    @Schema(description = "Идентификатор счета получателя средств", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long recipientId;

    @Schema(description = "Идентификатор суммы транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double amount;
}
