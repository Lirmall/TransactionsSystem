package ru.klokov.tsaccounts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String username;
    private String secondName;
    private String firstName;
    private String thirdName;
    private String email;
    private String phoneNumber;
    private Boolean blocked;
    private Boolean deleted;
}
