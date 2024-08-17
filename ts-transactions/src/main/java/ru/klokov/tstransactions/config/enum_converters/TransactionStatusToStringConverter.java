package ru.klokov.tstransactions.config.enum_converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;

public class TransactionStatusToStringConverter implements Converter<TransactionStatus, String> {
    @Override
    public String convert(MappingContext<TransactionStatus, String> context) {
        return context.getSource() == null ? null : context.getSource().getName();
    }
}
