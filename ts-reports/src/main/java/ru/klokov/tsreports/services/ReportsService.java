package ru.klokov.tsreports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.*;
import ru.klokov.tsreports.entities.ReportEntity;
import ru.klokov.tsreports.mappers.ReportsMapper;
import ru.klokov.tsreports.repositories.GetReportsRepository;
import ru.klokov.tsreports.repositories.ReportsDatabaseRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsDatabaseRepository databaseRepository;
    private final GetReportsRepository getReportsRepository;
    private final ReportsMapper reportsMapper;

    @Transactional
    public void fillReportsToDB(PeriodDto periodDto) {
        Optional<LocalDateTime> optionalLastReportDate = databaseRepository.getReportEntityWithMaxTransactionDate();

        LocalDateTime lastReportDate = optionalLastReportDate.orElse(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 1));

        log.info("{}", lastReportDate);

        periodDto.setPeriodStart(lastReportDate);

        if(periodDto.getPeriodEnd() == null) {
            periodDto.setPeriodEnd(LocalDateTime.now().minusDays(1L).withHour(23).withMinute(59).withSecond(59).withNano(999999999));
        }

        Page<ReportTransactionDto> transactionDtoPage = getTransactionsByPeriod(periodDto);

        List<ReportBankAccountDto> bankAccountDtos = getBankAccountDtos(transactionDtoPage);

        List<ReportUserDto> userDtos = getUsersData(bankAccountDtos);

        List<ReportEntity> entities = createReportEntities(transactionDtoPage, userDtos, bankAccountDtos);

        log.info("transactions {}", transactionDtoPage.getTotalElements());
        log.info("users {}", userDtos.size());
        log.info("reports {}", entities.size());
        databaseRepository.saveAll(entities);
    }

    private Page<ReportTransactionDto> getTransactionsByPeriod(PeriodDto periodDto) {
        return getReportsRepository.getTransactionsByPeriod(periodDto);
    }

    private List<ReportBankAccountDto> getBankAccountDtos(Page<ReportTransactionDto> transactionDtoPage) {
        Set<Long> bankAccountDtos = new HashSet<>();

        for (ReportTransactionDto dto : transactionDtoPage.getContent()) {
            bankAccountDtos.add(dto.getSenderId());
            bankAccountDtos.add(dto.getRecipientId());
        }

        return getReportsRepository.getBankAccounts(bankAccountDtos);
    }

    private List<ReportUserDto> getUsersData(List<ReportBankAccountDto> bankAccountDtos) {
        Set<Long> allAccountsIds = new HashSet<>();

        bankAccountDtos.forEach(dto -> allAccountsIds.add(dto.getOwnerUserId()));

        List<ReportUserDto> result = getReportsRepository.getUsersByIds(allAccountsIds);

        return result;
    }

    private List<ReportEntity> createReportEntities(Page<ReportTransactionDto> transactionDtos, List<ReportUserDto> userDtos, List<ReportBankAccountDto> bankAccountDtos) {
        List<ReportEntity> entities = new ArrayList<>();

        for (ReportTransactionDto transaction: transactionDtos) {
            Optional<ReportBankAccountDto> optSenderBA = bankAccountDtos.stream().filter(b -> b.getId().equals(transaction.getSenderId())).findFirst();
            Optional<ReportBankAccountDto> optRecBA = bankAccountDtos.stream().filter(b -> b.getId().equals(transaction.getRecipientId())).findFirst();

            if(optSenderBA.isEmpty() || optRecBA.isEmpty()) {
                throw new RuntimeException("Sender or recipient bank account is empty");
            }

            Optional<ReportUserDto> optSender = userDtos.stream().filter(u -> u.getId().equals(optSenderBA.get().getOwnerUserId())).findFirst();
            Optional<ReportUserDto> optRecipient = userDtos.stream().filter(u -> u.getId().equals(optRecBA.get().getOwnerUserId())).findFirst();

            if(optSender.isEmpty() || optRecipient.isEmpty()) {
                throw new RuntimeException("Sender or recipient is empty");
            }

            ReportUserDto sender = optSender.get();
            ReportUserDto recipient = optSender.get();

            entities.add(reportsMapper.convertDtosToReport(transaction, sender, recipient));
        }

        return entities;
    }
}
