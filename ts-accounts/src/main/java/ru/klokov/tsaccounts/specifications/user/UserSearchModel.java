package ru.klokov.tsaccounts.specifications.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.specifications.BaseSearchModel;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSearchModel extends BaseSearchModel {
    private List<Long> ids;
    private List<String> usernames;
    private List<String> secondNames;
    private List<String> firstNames;
    private List<String> thirdNames;
    private List<String> emails;
    private List<String> phoneNumbers;
    private Boolean blocked;
    private Boolean deleted;
}
