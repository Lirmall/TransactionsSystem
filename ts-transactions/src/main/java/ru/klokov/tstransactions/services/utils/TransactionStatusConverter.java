package ru.klokov.tstransactions.services.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;

@Converter(autoApply = true)
public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, Long> {

    @Override
    public Long convertToDatabaseColumn(TransactionStatus status) {
        if(status == null) {
            return null;
        }
        return status.getId();
    }

    @Override
    public TransactionStatus convertToEntityAttribute(Long id) {
        return TransactionStatus.getById(id);
    }
}
