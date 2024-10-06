package ru.klokov.tsaccounts.specifications.user;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.klokov.tsaccounts.entities.UserEntity;
import ru.klokov.tscommon.specifications.search_models.UserSearchModel;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity> {
    private UserSearchModel request;

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(!CollectionUtils.isEmpty(request.getIds())) {
            Expression<Long> userIdExpression = root.get("id");
            predicates.add(criteriaBuilder.and(userIdExpression.in(request.getIds())));
        }

        if(!CollectionUtils.isEmpty(request.getUsernames())) {
            for(String search : request.getUsernames()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + search + "%"));
            }
        }

        if(!CollectionUtils.isEmpty(request.getFirstNames())) {
            for(String search : request.getFirstNames()) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + search + "%"));
            }
        }

        if(!CollectionUtils.isEmpty(request.getThirdNames())) {
            for(String search : request.getThirdNames()) {
                predicates.add(criteriaBuilder.like(root.get("thirdName"), "%" + search + "%"));
            }
        }

        if(!CollectionUtils.isEmpty(request.getSecondNames())) {
            for(String search : request.getSecondNames()) {
                predicates.add(criteriaBuilder.like(root.get("secondName"), "%" + search + "%"));
            }
        }

        if(!CollectionUtils.isEmpty(request.getEmails())) {
            for(String search : request.getEmails()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + search + "%"));
            }
        }

        if(!CollectionUtils.isEmpty(request.getPhoneNumbers())) {
            for(String search : request.getPhoneNumbers()) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"));
            }
        }

        if(request.getBlocked()!=null) {
            Expression<Boolean> userBlockedExpression = root.get("blocked");
            predicates.add(criteriaBuilder.and(userBlockedExpression.in(request.getBlocked())));
        }

        if(request.getDeleted()!=null) {
            Expression<Boolean> userDeletedExpression = root.get("deleted");
            predicates.add(criteriaBuilder.and(userDeletedExpression.in(request.getDeleted())));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
