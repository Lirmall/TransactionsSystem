package ru.klokov.tstransactions.dtos.soap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ru.klokov.tstransactions.config.enum_converters.TransactionStatusSoapAdapter;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;

import java.time.LocalDateTime;

@XmlType(propOrder = { "senderId", "recipientId", "amount", "typeId", "status", "transactionDate"})
@XmlRootElement(name = "SoapTransactionResponseDto", namespace = "http://tstransactions.klokov.ru/soap")
public class SoapTransactionResponseDto {

    private Long senderId;

    private Long recipientId;

    private Double amount;

    private Long typeId;

    private TransactionStatus status;

    private String transactionDate;

    @XmlElement(required = true, namespace = "http://tstransactions.klokov.ru/soap")
    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @XmlElement(required = true, namespace = "http://tstransactions.klokov.ru/soap")
    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    @XmlElement(required = true, namespace = "http://tstransactions.klokov.ru/soap")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @XmlElement(required = true, namespace = "http://tstransactions.klokov.ru/soap")
    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @XmlElement(required = true, namespace = "http://tstransactions.klokov.ru/soap")
    @XmlJavaTypeAdapter(TransactionStatusSoapAdapter.class)
    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @XmlElement(required = true, namespace = "http://tstransactions.klokov.ru/soap")
    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
