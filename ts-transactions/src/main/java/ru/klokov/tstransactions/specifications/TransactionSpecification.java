package ru.klokov.tstransactions.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tstransactions.entities.TransactionEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class TransactionSpecification implements Specification<TransactionEntity> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<TransactionEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if(criteria.getFieldValue() instanceof LocalDateTime || criteria.getFieldName().equals("transactionDate")) {
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
    }

    private Predicate localDateTimeHandler(Object date, Root<TransactionEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        LocalDateTime dateTimeValue;

        if(date instanceof LocalDateTime) {
            dateTimeValue = (LocalDateTime) date;
        } else {
            dateTimeValue = LocalDateTime.parse((String) date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
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
