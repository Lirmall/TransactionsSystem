package ru.klokov.tsaccounts.specifications.bank_account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import ru.klokov.tsaccounts.entities.BankAccountEntity;
import ru.klokov.tsaccounts.specifications.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BankAccountSpecificationBuilder {
    private final List<SearchCriteria> criteriaList;

    public BankAccountSpecificationBuilder() {
        criteriaList = new ArrayList<>();
    }

    public Specification<BankAccountEntity> build() {
        if(criteriaList.isEmpty()) {
            return null;
        }

        Specification<BankAccountEntity> result = new BankAccountSpecification(criteriaList.get(0));

        for (int i = 1; i < criteriaList.size(); i++) {
            result = criteriaList.get(i).isOrPredicate()
                    ? Specification.where(result).or(new BankAccountSpecification(criteriaList.get(i)))
                    : Specification.where(result).and(new BankAccountSpecification(criteriaList.get(i)));
        }
        return result;
    }
}
