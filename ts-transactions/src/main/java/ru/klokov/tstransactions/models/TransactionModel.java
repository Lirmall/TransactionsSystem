package ru.klokov.tstransactions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
    private UUID id;
    private Long senderId;
    private Long recipientId;
    private Double amount;
    private String type;
    private String status;
    private LocalDateTime transactionDate;
}
