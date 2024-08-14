package ru.klokov.tstransactions.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final ModelMapper modelMapper;

    public TransactionDto convertEntityToDto(TransactionEntity entity) {
        TransactionDto dto = modelMapper.map(entity, TransactionDto.class);
        dto.setStatus(entity.getStatusId().getName());
        dto.setType(entity.getTypeId().getName());
        return dto;
    }

    public TransactionEntity convertDtoToEntity(TransactionDto dto) {
        TransactionEntity entity = modelMapper.map(dto, TransactionEntity.class);
        entity.setTypeId(TransactionType.getByName(dto.getType()));
        return entity;
    }
}
