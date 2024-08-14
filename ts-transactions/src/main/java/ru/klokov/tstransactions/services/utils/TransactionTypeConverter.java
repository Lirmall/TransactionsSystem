package ru.klokov.tstransactions.services.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.klokov.tstransactions.entities.enums.TransactionType;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, Long> {

    @Override
    public Long convertToDatabaseColumn(TransactionType status) {
        if(status == null) {
            return null;
        }
        return status.getId();
    }

    @Override
    public TransactionType convertToEntityAttribute(Long id) {
        return TransactionType.getById(id);
    }
}
