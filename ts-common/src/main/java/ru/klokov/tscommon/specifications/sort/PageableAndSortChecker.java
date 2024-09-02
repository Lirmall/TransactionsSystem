package ru.klokov.tscommon.specifications.sort;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.klokov.tscommon.exceptions.VerificationException;
import ru.klokov.tscommon.specifications.BaseSearchModel;

import java.util.List;

public interface PageableAndSortChecker {

    default Pageable getPageableAndSort(BaseSearchModel model) {
        Sort sort = this.sortColumnCheck(model);

        int page = model.getPages() != null ? model.getPages() : 0;
        int size = (model.getLimit() != null && model.getLimit() != 0) ? model.getLimit() : 5;

        return PageRequest.of(page, size, sort);
    }

    default Sort sortColumnCheck(BaseSearchModel model) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortColumn = model.getSortColumn();

        if (sortColumn != null) {
            if (sortColumn.startsWith("-")) {
                sortDirection = Sort.Direction.DESC;
                sortColumn = sortColumn.substring(1);
                this.columnCheck(sortColumn);
            } else {
                this.columnCheck(sortColumn);
                sortColumn = sortColumn.toLowerCase();
            }
        } else {
            sortColumn = "id";
        }

        return Sort.by(sortDirection, sortColumn);
    }

    default void columnCheck(String sortColumn) {
        if(!getColumnMapping().contains(sortColumn)) {
            throw new VerificationException(String.format("Sort field \"%s\" is not supported", sortColumn));
        }
    }

    List<String> getColumnMapping();
}
