package ru.klokov.tsaccounts.specifications.userSpecification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSearchModel {
    private List<Long> ids;
    private List<String> username;
    private List<String> secondName;
    private List<String> firstName;
    private List<String> thirdName;
    private List<String> email;
    private List<String> phoneNumber;
    private Boolean blocked;
    private Boolean deleted;
}
