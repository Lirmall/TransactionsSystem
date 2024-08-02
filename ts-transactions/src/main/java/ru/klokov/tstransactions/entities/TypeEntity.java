package ru.klokov.tstransactions.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "types")
@Getter
@Setter
@NoArgsConstructor
public class TypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "types_id_seq")
    @SequenceGenerator(
            name="types_id_seq",
            sequenceName = "types_id_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "typeId", fetch = FetchType.LAZY)
    private List<TransactionEntity> transactionEntityList;
}
