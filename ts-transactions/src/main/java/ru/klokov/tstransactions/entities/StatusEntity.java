package ru.klokov.tstransactions.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "statuses")
@Getter
@Setter
@NoArgsConstructor
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statuses_id_seq")
    @SequenceGenerator(
            name="statuses_id_seq",
            sequenceName = "statuses_id_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "statusId", fetch = FetchType.LAZY)
    private List<TransactionEntity> transactionEntityList;
}
