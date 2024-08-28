package ru.klokov.tsreports.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
public class ReportEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id")
    private UUID id;

    @Column(name = "sender_user_id")
    private Long senderUserId;

    @Column(name = "sender_user_username")
    private String senderUserUsername;

    @Column(name = "sender_bank_account_id")
    private Long senderUserBankAccountId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "recipient_user_id")
    private Long recipientUserId;

    @Column(name = "recipient_user_username")
    private String recipientUserUsername;

    @Column(name = "recipient_bank_account_id")
    private Long recipientUserBankAccountId;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
}
