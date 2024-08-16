package ru.klokov.tsaccounts.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(
            name="users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "third_name")
    private String thirdName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "blocked")
    private Boolean blocked;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "ownerUserId", fetch = FetchType.LAZY)
    private List<BankAccountEntity> bankAccountsList;
}
