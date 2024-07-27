package ru.klokov.tsaccounts.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id = null;
    private String username;
    private String secondName;
    private String firstName;
    private String thirdName;
    private String email;
    private String phoneNumber;
    private Boolean blocked;
    private Boolean deleted;
}
