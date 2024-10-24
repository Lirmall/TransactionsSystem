package ru.klokov.tsreports.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.dtos.UserDto;
import ru.klokov.tsreports.dtos.ReportDto;
import ru.klokov.tsreports.entities.ReportEntity;

@Component
@RequiredArgsConstructor
public class ReportsMapper {

    private final ModelMapper modelMapper;

    public ReportEntity convertDtoToEntity (ReportDto dto) {
        return modelMapper.map(dto, ReportEntity.class);
    }

    public ReportDto convertEntityToDto(ReportEntity entity) {
        return modelMapper.map(entity, ReportDto.class);
    }

    public ReportEntity convertDtosToReport(TransactionDto transactionDto, UserDto sender, UserDto recipient) {
        ReportEntity entity = new ReportEntity();

        entity.setSenderUserId(sender.getId());
        entity.setSenderUserUsername(sender.getUsername());
        entity.setSenderUserBankAccountId(transactionDto.getSenderId());
        entity.setAmount(transactionDto.getAmount());
        entity.setRecipientUserId(recipient.getId());
        entity.setRecipientUserUsername(recipient.getUsername());
        entity.setRecipientUserBankAccountId(transactionDto.getRecipientId());
        entity.setTypeId(transactionDto.getTypeId());
        entity.setTypeName(transactionDto.getType());
        entity.setStatusId(transactionDto.getStatusId());
        entity.setStatusName(transactionDto.getStatus());
        entity.setTransactionDate(transactionDto.getTransactionDate());

        return entity;
    }
}
