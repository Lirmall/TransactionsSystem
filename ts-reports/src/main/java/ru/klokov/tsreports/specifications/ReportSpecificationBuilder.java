package ru.klokov.tsreports.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tsreports.entities.ReportEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReportSpecificationBuilder {
    private final List<SearchCriteria> criteriaList;

    public ReportSpecificationBuilder() {
        criteriaList = new ArrayList<>();
    }

    public Specification<ReportEntity> build() {
        if(criteriaList.isEmpty()) {
            return null;
        }

        Specification<ReportEntity> result = new ReportSpecification(criteriaList.get(0));

        for (int i = 1; i < criteriaList.size(); i++) {
            result = criteriaList.get(i).isOrPredicate()
                    ? Specification.where(result).or(new ReportSpecification(criteriaList.get(i)))
                    : Specification.where(result).and(new ReportSpecification(criteriaList.get(i)));
        }
        return result;
    }
}
