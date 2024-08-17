package ru.klokov.tstransactions.config.enum_converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ru.klokov.tstransactions.entities.enums.TransactionType;

public class StringToTypeConverter implements Converter<String, TransactionType> {
    @Override
    public TransactionType convert(MappingContext<String, TransactionType> context) {
        return context.getSource() == null ? null : TransactionType.getByName(context.getSource());
    }
}
