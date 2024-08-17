package ru.klokov.tstransactions.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tstransactions.config.enum_converters.StringToStatusConverter;
import ru.klokov.tstransactions.config.enum_converters.StringToTypeConverter;
import ru.klokov.tstransactions.config.enum_converters.TransactionStatusToStringConverter;
import ru.klokov.tstransactions.config.enum_converters.TransactionTypeToStringConverter;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setUp() {
        modelMapper.addConverter(new TransactionStatusToStringConverter());
        modelMapper.addConverter(new TransactionTypeToStringConverter());
        modelMapper.addConverter(new StringToStatusConverter());
        modelMapper.addConverter(new StringToTypeConverter());
    }

    public TransactionDto convertEntityToDto(TransactionEntity entity) {
        return modelMapper.map(entity, TransactionDto.class);
    }

    public TransactionEntity convertDtoToEntity(TransactionDto dto) {
        return modelMapper.map(dto, TransactionEntity.class);
    }
}
