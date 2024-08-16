package ru.klokov.tsaccounts.specifications;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Критерии условий поиска")
public class SearchCriteria {

    @Schema(description = "Наименование поля, по которому будет производиться поиск",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "id",
            allowableValues = {"id", "ownerUserId", "balance", "blocked", "deleted"})
    private String fieldName;

    @Schema(description = "Операция поиска",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private SearchOperation searchOperation;

    @Schema(description = "Значение поля, по которому производится поиск. Предпочтительно числовое",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "10")
    private Object fieldValue;

    @Schema(description = "Признак условия \"или\"",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "false")
    private boolean orPredicate = false;
}
