package ru.klokov.tsaccounts.specifications.bank_account;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.specifications.SearchCriteria;

@AllArgsConstructor
public class BankAccountSpecification implements Specification<BankAccountEntity> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<BankAccountEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getFieldName()), criteria.getFieldValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.<String> get(criteria.getFieldName()), criteria.getFieldValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getFieldName()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.<String>get(criteria.getFieldName()), "%" + criteria.getFieldValue() + "%");
            } else {
                return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
            }
        }
        return null;
    }
}
