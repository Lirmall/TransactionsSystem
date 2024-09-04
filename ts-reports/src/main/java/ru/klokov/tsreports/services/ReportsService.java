package ru.klokov.tsreports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.PeriodDto;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tsreports.repositories.GetReportsRepository;
import ru.klokov.tsreports.repositories.ReportsDatabaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsDatabaseRepository databaseRepository;
    private final GetReportsRepository getReportsRepository;

    @Transactional
    public void fillReportsToDB(PeriodDto periodDto) {
        Optional<LocalDateTime> optionalLastReportDate = databaseRepository.getReportEntityWithMaxTransactionDate();

        LocalDateTime lastReportDate = optionalLastReportDate.orElse(LocalDateTime.of(1970, 1, 1, 0, 0));

        Page<TransactionDto> transactionDtoPage = getTransactionsByPeriod(periodDto);

        System.out.println();
    }

    private Page<TransactionDto> getTransactionsByPeriod(PeriodDto periodDto) {
        return getReportsRepository.getTransactionsByPeriod(periodDto);
    }
}
