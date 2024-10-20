package ru.klokov.tscommon.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о транзакции")
public class TransactionDto {
    @Schema(description = "Идентификатор транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "Идентификатор счета отправителя средств", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long senderId;

    @Schema(description = "Идентификатор счета получателя средств", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long recipientId;

    @Schema(description = "Идентификатор суммы транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double amount;

    @Schema(description = "Идентификатор типа транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long typeId;

    @Schema(description = "Тип транзакции",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"Payment", "Deposit", "Cash Withdrawal", "Transfer"})
    private String type;

    @Schema(description = "Идентификатор статуса транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long statusId;

    @Schema(description = "Статус транзакции")
    private String status;

    @Schema(description = "Дата и время транзакции")
    private LocalDateTime transactionDate;
}
