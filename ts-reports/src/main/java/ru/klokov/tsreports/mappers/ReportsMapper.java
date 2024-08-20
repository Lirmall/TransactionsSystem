package ru.klokov.tsreports.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.klokov.tsreports.dtos.ReportDto;
import ru.klokov.tsreports.entities.ReportEntity;

@Component
@RequiredArgsConstructor
public class ReportsMapper {

    private final ModelMapper modelMapper;

    public ReportEntity convertDtoToEntity (ReportDto dto) {
        return modelMapper.map(dto, ReportEntity.class);
    }

    public ReportDto convertEntityToDtp (ReportDto entity) {
        return modelMapper.map(entity, ReportDto.class);
    }
}
