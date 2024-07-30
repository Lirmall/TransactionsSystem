package ru.klokov.tsaccounts.test_objects;

import ru.klokov.tsaccounts.specifications.user.UserSearchModel;

import java.util.List;

public class UserSearchModelReturner {
    public static UserSearchModel returnModelWithOneId() {
        UserSearchModel model = new UserSearchModel();
        model.setIds(List.of(1L));

        return model;
    }

    public static UserSearchModel returnModelWithThreeId() {
        UserSearchModel model = new UserSearchModel();
        model.setIds(List.of(1L, 3L, 5L));

        model.setSortColumn("id");

        return model;
    }

    public static UserSearchModel returnModelWithUsernameContainsWord() {
        UserSearchModel model = new UserSearchModel();
        model.setUsernames(List.of("acc"));

        model.setSortColumn("username");

        return model;
    }

    public static UserSearchModel returnModelWithTwoSearchFields() {
        UserSearchModel model = new UserSearchModel();
        model.setUsernames(List.of("acc"));
        model.setPhoneNumbers(List.of("567-8901"));

        model.setSortColumn("username");

        return model;
    }

    public static UserSearchModel returnModelWithWrongSortField() {
        UserSearchModel model = new UserSearchModel();
        model.setUsernames(List.of("acc"));
        model.setPhoneNumbers(List.of("567-8901"));

        model.setSortColumn("aaaaaaaaa");

        return model;
    }
}
