package ru.klokov.tstransactions.dtos.soap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.time.LocalDateTime;
import java.util.UUID;

@XmlType(propOrder = { "senderId", "recipientId", "amount", "typeId" })
@XmlRootElement(name = "SoapTransactionRequestDto", namespace = "http://tstransactions.klokov.ru/soap")
public class SoapTransactionRequestDto {

    private Long senderId;
    private Long recipientId;
    private Double amount;
    private Long typeId;

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
}
