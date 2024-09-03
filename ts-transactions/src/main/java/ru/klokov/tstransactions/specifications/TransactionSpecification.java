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

import java.time.LocalDateTime;

@AllArgsConstructor
public class TransactionSpecification implements Specification<TransactionEntity> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<TransactionEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if(criteria.getFieldValue() instanceof LocalDateTime) {
            return localDateTimeHandler(criteria.getFieldValue(), root, query, criteriaBuilder);
        }

        switch (criteria.getSearchOperation()) {
            case GREATER_THAN:
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.<String>get(criteria.getFieldName()), criteria.getFieldValue().toString());
            case LESS_THAN:
                return criteriaBuilder.lessThanOrEqualTo(
                        root.<String>get(criteria.getFieldName()), criteria.getFieldValue().toString());
            case EQUALITY:
                return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
            default:
                return null;
        }

//        if (criteria.getSearchOperation() == SearchOperation.GREATER_THAN) {
//            return criteriaBuilder.greaterThanOrEqualTo(
//                    root.get(criteria.getFieldName()), criteria.getFieldValue().toString());
//        } else if (criteria.getSearchOperation() == SearchOperation.LESS_THAN) {
//            return criteriaBuilder.lessThanOrEqualTo(
//                    root.get(criteria.getFieldName()), criteria.getFieldValue().toString());
//        } else if (criteria.getSearchOperation() == SearchOperation.EQUALITY) {
//            return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
//        }
//        return null;
    }

    private Predicate localDateTimeHandler(Object date, Root<TransactionEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        LocalDateTime dateTimeValue = (LocalDateTime) date;
        String fieldName = criteria.getFieldName();

        switch (criteria.getSearchOperation()) {
            case GREATER_THAN:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), dateTimeValue);
            case LESS_THAN:
                return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), dateTimeValue);
            case EQUALITY:
                return criteriaBuilder.equal(root.get(fieldName), dateTimeValue);
            default:
                return null;
        }
    }
}
