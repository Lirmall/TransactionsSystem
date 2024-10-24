package ru.klokov.tsreports.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private Long senderUserId;
    private String senderUserUsername;
    private Long senderUserBankAccountId;
    private Double amount;
    private Long recipientUserId;
    private String recipientUserUsername;
    private Long recipientUserBankAccountId;
    private Long typeId;
    private String typeName;
    private Long statusId;
    private String statusName;
    private LocalDateTime transactionDate;
}
