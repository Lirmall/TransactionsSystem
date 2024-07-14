package ru.klokov.tsaccounts.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_sequence_generator")
    @SequenceGenerator(
            name="bank_accounts_sequence_generator",
            sequenceName = "bank_accounts_sequence"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_user", insertable = false, updatable = false)
    private Long ownerUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user")
    private UserEntity userEntity;

    @Column(name = "balance")
    private Double balance;
}
