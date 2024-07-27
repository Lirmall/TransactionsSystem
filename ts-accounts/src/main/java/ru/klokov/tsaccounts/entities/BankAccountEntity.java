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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_id_seq")
    @SequenceGenerator(
            name="bank_accounts_id_seq",
            sequenceName = "bank_accounts_id_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_user", insertable = false, updatable = false)
    private Long ownerUserId;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "blocked")
    private Boolean blocked;

    @Column(name = "deleted")
    private Boolean deleted;
}
