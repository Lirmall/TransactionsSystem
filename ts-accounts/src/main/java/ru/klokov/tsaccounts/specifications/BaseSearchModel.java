package ru.klokov.tsaccounts.specifications;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "Базовая модель, содержит информацию о пагинации и колонку сортировки")
public class BaseSearchModel {

    @Schema(description = "Стартовая страница",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "0")
    private Integer pages = 0;

    @Schema(description = "Количество записей на странице",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "5")
    private Integer limit = 5;

    @Schema(description = "По какому параметру будет производиться сортировка. Также направление сортировки. " +
            "Для обратной сортировки поставьте \"-\" перед параметром",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "id")
    private String sortColumn;
}
