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
    public void fillAllOrNewReportsToDB() {
        PeriodDto periodDto = new PeriodDto();
        Optional<LocalDateTime> optionalLastReportDate = databaseRepository.getReportEntityWithMaxTransactionDate();

        LocalDateTime lastReportDate = optionalLastReportDate.orElse(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 1));

        log.info("{}", lastReportDate);

        periodDto.setPeriodStart(lastReportDate);
        periodDto.setPeriodEnd(LocalDateTime.now().minusDays(1L).withHour(23).withMinute(59).withSecond(59).withNano(999999999));

        log.info("{}", periodDto.getPeriodEnd());

        Page<TransactionDto> transactionDtoPage = getTransactionsByPeriod(periodDto);

        List<BankAccountDto> bankAccountDtos = getBankAccountDtos(transactionDtoPage);

        List<UserDto> userDtos = getUsersData(bankAccountDtos);

        List<ReportEntity> entities = createReportEntities(transactionDtoPage, userDtos, bankAccountDtos);

        log.info("transactions {}", transactionDtoPage.getNumberOfElements());
        log.info("transactions all {}", transactionDtoPage.getTotalElements());
        log.info("transactions pages {}", transactionDtoPage.getTotalPages());
        log.info("users {}", userDtos.size());
        log.info("reports {}", entities.size());
//        databaseRepository.saveAll(entities);
    }

    private Page<TransactionDto> getTransactionsByPeriod(PeriodDto periodDto) {
        return getReportsRepository.getTransactionsByPeriod(periodDto);
    }

    private List<BankAccountDto> getBankAccountDtos(Page<TransactionDto> transactionDtoPage) {
        Set<Long> bankAccountDtos = new HashSet<>();

        for (TransactionDto dto : transactionDtoPage.getContent()) {
            bankAccountDtos.add(dto.getSenderId());
            bankAccountDtos.add(dto.getRecipientId());
        }

        return getReportsRepository.getBankAccounts(bankAccountDtos);
    }

    private List<UserDto> getUsersData(List<BankAccountDto> bankAccountDtos) {
        Set<Long> allAccountsIds = new HashSet<>();

        bankAccountDtos.forEach(dto -> allAccountsIds.add(dto.getOwnerUserId()));

        List<UserDto> result = getReportsRepository.getUsersByIds(allAccountsIds);

        return result;
    }

    private List<ReportEntity> createReportEntities(Page<TransactionDto> transactionDtos, List<UserDto> userDtos, List<BankAccountDto> bankAccountDtos) {
        List<ReportEntity> entities = new ArrayList<>();

        for (TransactionDto transaction: transactionDtos) {
            Optional<BankAccountDto> optSenderBA = bankAccountDtos.stream().filter(b -> b.getId().equals(transaction.getSenderId())).findFirst();
            Optional<BankAccountDto> optRecBA = bankAccountDtos.stream().filter(b -> b.getId().equals(transaction.getRecipientId())).findFirst();

            if(optSenderBA.isEmpty() || optRecBA.isEmpty()) {
                throw new RuntimeException("Sender or recipient bank account is empty");
            }

            Optional<UserDto> optSender = userDtos.stream().filter(u -> u.getId().equals(optSenderBA.get().getOwnerUserId())).findFirst();
            Optional<UserDto> optRecipient = userDtos.stream().filter(u -> u.getId().equals(optRecBA.get().getOwnerUserId())).findFirst();

            if(optSender.isEmpty() || optRecipient.isEmpty()) {
                throw new RuntimeException("Sender or recipient is empty");
            }

            UserDto sender = optSender.get();
            UserDto recipient = optSender.get();

            entities.add(reportsMapper.convertDtosToReport(transaction, sender, recipient));
        }

        return entities;
    }
}
