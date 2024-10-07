package ru.klokov.tscommon.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о банковском счете пользователя")
public class ReportBankAccountDto {

    @Schema(description = "Идентификатор счета пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "Идентификатор владельца счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ownerUserId;

    @Schema(description = "Идентификатор заблокированного счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean blocked;

    @Schema(description = "Идентификатор удаленного счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean deleted;
}
