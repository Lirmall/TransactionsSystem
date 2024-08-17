package ru.klokov.tstransactions.config.enum_converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ru.klokov.tstransactions.entities.enums.TransactionType;

public class TransactionTypeToStringConverter implements Converter<TransactionType, String> {
    @Override
    public String convert(MappingContext<TransactionType, String> context) {
        return context.getSource() == null ? null : context.getSource().getName();
    }
}
