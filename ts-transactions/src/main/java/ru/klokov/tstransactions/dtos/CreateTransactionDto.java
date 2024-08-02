package ru.klokov.tstransactions.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDto {
    private Long senderId;
    private Long recipientId;
    private Double amount;
    private Long typeId;
}
