package ru.klokov.tstransactions.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;
import ru.klokov.tstransactions.entities.TransactionEntity;

@AllArgsConstructor
public class TransactionSpecification implements Specification<TransactionEntity> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<TransactionEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getSearchOperation() == SearchOperation.GREATER_THAN) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getFieldName()), criteria.getFieldValue().toString());
        }
        else if (criteria.getSearchOperation() == SearchOperation.LESS_THAN) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.<String> get(criteria.getFieldName()), criteria.getFieldValue().toString());
        }
        else if (criteria.getSearchOperation() == SearchOperation.EQUALITY) {
            return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
        }
        return null;
    }
}
