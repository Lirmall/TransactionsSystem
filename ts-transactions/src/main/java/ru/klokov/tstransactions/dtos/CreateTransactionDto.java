package ru.klokov.tstransactions.dtos;

import jakarta.persistence.Column;

public class CreateTransactionDto {
    private Long senderId;
    private Long recipientId;
    private Double amount;
    private Long typeId;
    private Long statusId;
}
