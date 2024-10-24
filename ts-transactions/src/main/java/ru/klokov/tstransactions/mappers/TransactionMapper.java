package ru.klokov.tstransactions.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tstransactions.config.enum_converters.StringToStatusConverter;
import ru.klokov.tstransactions.config.enum_converters.StringToTypeConverter;
import ru.klokov.tstransactions.config.enum_converters.TransactionStatusToStringConverter;
import ru.klokov.tstransactions.config.enum_converters.TransactionTypeToStringConverter;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.models.TransactionModel;

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
        TransactionDto dto = modelMapper.map(entity, TransactionDto.class);
        dto.setTypeId(entity.getType().getId());
        dto.setStatusId(entity.getStatus().getId());
        return dto;
    }

    public TransactionEntity convertDtoToEntity(TransactionDto dto) {
        return modelMapper.map(dto, TransactionEntity.class);
    }

    public TransactionModel convertDtoToModel(TransactionDto dto) {
        return modelMapper.map(dto, TransactionModel.class);
    }

    public TransactionModel convertEntityToModel(TransactionEntity entity) {
        return modelMapper.map(entity, TransactionModel.class);
    }

    public TransactionDto convertModelToDto(TransactionModel model) {
        return modelMapper.map(model, TransactionDto.class);
    }

    public TransactionEntity convertModelToEntity(TransactionModel model) {
        return modelMapper.map(model, TransactionEntity.class);
    }
}
