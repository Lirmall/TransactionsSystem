package ru.klokov.tstransactions.config.enum_converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;

public class StringToStatusConverter implements Converter<String, TransactionStatus> {
    @Override
    public TransactionStatus convert(MappingContext<String, TransactionStatus> context) {
        return context.getSource() == null ? null : TransactionStatus.getByName(context.getSource());
    }
}
