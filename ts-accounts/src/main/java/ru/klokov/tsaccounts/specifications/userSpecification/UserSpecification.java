package ru.klokov.tsaccounts.specifications.userSpecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tsaccounts.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity> {
    private UserSearchModel request;

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        return null;
    }
}
