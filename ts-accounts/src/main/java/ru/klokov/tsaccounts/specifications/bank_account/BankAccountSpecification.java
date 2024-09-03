package ru.klokov.tsaccounts.specifications.bank_account;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;

@AllArgsConstructor
public class BankAccountSpecification implements Specification<BankAccountEntity> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<BankAccountEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (criteria.getSearchOperation()) {
            case GREATER_THAN:
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get(criteria.getFieldName()), criteria.getFieldValue().toString());
            case LESS_THAN:
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get(criteria.getFieldName()), criteria.getFieldValue().toString());
            case EQUALITY:
                return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
            default:
                return null;
        }
    }
}
