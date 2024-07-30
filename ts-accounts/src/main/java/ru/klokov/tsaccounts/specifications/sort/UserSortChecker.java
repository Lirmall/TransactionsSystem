package ru.klokov.tsaccounts.specifications.sort;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserSortChecker implements PageableAndSortChecker {

    @Override
    public List<String> getColumnMapping() {
        return Arrays.asList(
                "id",
                "username",
                "firstName",
                "thirdName",
                "secondName",
                "email",
                "phoneNumber",
                "blocked",
                "deleted"
        );
    }
}
