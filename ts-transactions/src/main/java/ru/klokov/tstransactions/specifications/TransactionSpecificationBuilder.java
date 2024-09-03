package ru.klokov.tstransactions.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tstransactions.entities.TransactionEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionSpecificationBuilder {

    private final List<SearchCriteria> criteriaList;

    public TransactionSpecificationBuilder() {
        criteriaList = new ArrayList<>();
    }

    public Specification<TransactionEntity> build() {
        if(criteriaList.isEmpty()) {
            return null;
        }

        Specification<TransactionEntity> result = new TransactionSpecification(criteriaList.get(0));

        for (int i = 1; i < criteriaList.size(); i++) {
            result = criteriaList.get(i).isOrPredicate()
                    ? Specification.where(result).or(new TransactionSpecification(criteriaList.get(i)))
                    : Specification.where(result).and(new TransactionSpecification(criteriaList.get(i)));
        }
        return result;
    }
}
