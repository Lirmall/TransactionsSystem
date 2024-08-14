package ru.klokov.tstransactions.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;
import ru.klokov.tstransactions.services.utils.TransactionStatusConverter;
import ru.klokov.tstransactions.services.utils.TransactionTypeConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id")
    private UUID id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "amount")
    private Double amount;

    @Convert(converter = TransactionTypeConverter.class)
    @Column(name = "type")
    private TransactionType typeId;

    @Convert(converter = TransactionStatusConverter.class)
    @Column(name = "status")
    private TransactionStatus statusId;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
}
