package ru.klokov.tsreports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klokov.tscommon.dtos.*;
import ru.klokov.tscommon.specifications.search_models.BankAccountSearchModel;
import ru.klokov.tsreports.dtos.ReportDto;
import ru.klokov.tsreports.entities.ReportEntity;
import ru.klokov.tsreports.mappers.ReportsMapper;
import ru.klokov.tsreports.repositories.GetReportsRepository;
import ru.klokov.tsreports.repositories.ReportsDatabaseRepository;
import ru.klokov.tsreports.specifications.ReportSpecificationBuilder;
import ru.klokov.tsreports.specifications.sort.ReportSortChecker;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsDatabaseRepository databaseRepository;
    private final GetReportsRepository getReportsRepository;
    private final ReportsMapper reportsMapper;
    private final ReportSortChecker reportSortChecker;

    public void fillAllOrNewReportsToDB() {
        long startTime = System.currentTimeMillis();
        log.info("Fill reports to DB starts at {}", LocalDateTime.now());
        PeriodDto periodDto = new PeriodDto();
        Optional<LocalDateTime> optionalLastReportDate = databaseRepository.getReportEntityWithMaxTransactionDate();

        LocalDateTime lastReportDate = optionalLastReportDate.orElse(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 1));

        log.info("{}", lastReportDate);

        periodDto.setPeriodStart(lastReportDate);
        periodDto.setPeriodEnd(LocalDateTime.now().minusDays(1L).withHour(23).withMinute(59).withSecond(59).withNano(999999999));

        log.info("{}", periodDto.getPeriodEnd());

        int pageNumber = 0;
        int pageSize = 2000;

        PagedResult<TransactionDto> innerTransactionDtoPage;
        int transactionPage = 0;
        do {
            innerTransactionDtoPage = getTransactionsByPeriod(periodDto, pageNumber, pageSize);
            PagedResult<BankAccountDto> innerBankAccountDtos = getBankAccountDtos(innerTransactionDtoPage, 0, pageSize);
            PagedResult<UserDto> innerUserDtos = getUsersData(innerBankAccountDtos, 0, pageSize);
            List<ReportEntity> innerEntities = createReportEntities2(innerTransactionDtoPage, innerUserDtos, innerBankAccountDtos);

            if(innerTransactionDtoPage.getContent().isEmpty()) {
                break;
            }

            log.info("page {}", pageNumber);
            log.info("inner transactions {}", innerTransactionDtoPage.getSize());
            log.info("first transaction {}", innerTransactionDtoPage.getContent().get(0).getTransactionDate());
            log.info("inner transactions all {}", innerTransactionDtoPage.getTotalElements());
            log.info("inner transactions pages {}", innerTransactionDtoPage.getTotalPages());
            log.info("first transaction sender BA id {}", innerTransactionDtoPage.getContent().get(0).getSenderId());
            log.info("first transaction rec BA id {}", innerTransactionDtoPage.getContent().get(0).getRecipientId());
            log.info("inner users {}", innerUserDtos.getSize());
            log.info("inner reports {}", innerEntities.size());

            log.info("Save {} list of reports", pageNumber );
            pageNumber++;
            saveListOfReports(innerEntities);
        } while (transactionPage < innerTransactionDtoPage.getTotalPages());

        log.info("Fill reports to DB ends at {}", LocalDateTime.now());
        log.info("Method works {} milliseconds", System.currentTimeMillis() - startTime);
    }

    @Transactional
    protected void saveListOfReports(List<ReportEntity> reportEntities) {
        Random random = new Random();
        int randomNumber = random.nextInt(5);
        log.info("random number is {}", randomNumber);
        if (randomNumber == 1) {
            log.warn("Try to fail");
            throw new RuntimeException("Fail to record");
        }

            databaseRepository.saveAll(reportEntities);
            log.info("Saved!");
    }

    private PagedResult<TransactionDto> getTransactionsByPeriod(PeriodDto periodDto, Integer pageNumber, Integer pageSize) {
        return getReportsRepository.getTransactionsByPeriod(periodDto, pageNumber, pageSize);
    }

    private PagedResult<BankAccountDto> getBankAccountDtos(PagedResult<TransactionDto> transactionDtoPage, Integer pageNumber, Integer pageSize) {
        Set<Long> bankAccountDtos = new HashSet<>();

        for (TransactionDto dto : transactionDtoPage.getContent()) {
            bankAccountDtos.add(dto.getSenderId());
            bankAccountDtos.add(dto.getRecipientId());
        }

        return getReportsRepository.getBankAccounts(bankAccountDtos, pageNumber, pageSize);
    }

    private PagedResult<UserDto> getUsersData(PagedResult<BankAccountDto> bankAccountDtos, Integer pageNumber, Integer pageSize) {
        Set<Long> allAccountsIds = new HashSet<>();

        bankAccountDtos.getContent().forEach(dto -> allAccountsIds.add(dto.getOwnerUserId()));

        PagedResult<UserDto> result = getReportsRepository.getUsersByIds(allAccountsIds, pageNumber, pageSize);

        return result;
    }

    private List<ReportEntity> createReportEntities(PagedResult<TransactionDto> transactionDtos, PagedResult<UserDto> userDtos, PagedResult<BankAccountDto> bankAccountDtos) {
        List<ReportEntity> entities = new ArrayList<>();

        for (TransactionDto transaction: transactionDtos.getContent()) {
            Optional<BankAccountDto> optSenderBA = bankAccountDtos.getContent().stream().filter(b -> b.getId().equals(transaction.getSenderId())).findFirst();
            Optional<BankAccountDto> optRecBA = bankAccountDtos.getContent().stream().filter(b -> b.getId().equals(transaction.getRecipientId())).findFirst();

            if(optSenderBA.isEmpty() || optRecBA.isEmpty()) {
                throw new RuntimeException("Sender or recipient bank account is empty");
            }

            Optional<UserDto> optSender = userDtos.getContent().stream().filter(u -> u.getId().equals(optSenderBA.get().getOwnerUserId())).findFirst();
            Optional<UserDto> optRecipient = userDtos.getContent().stream().filter(u -> u.getId().equals(optRecBA.get().getOwnerUserId())).findFirst();

            if(optSender.isEmpty() || optRecipient.isEmpty()) {
                throw new RuntimeException("Sender or recipient is empty");
            }

            UserDto sender = optSender.get();
            UserDto recipient = optSender.get();

            entities.add(reportsMapper.convertDtosToReport(transaction, sender, recipient));
        }

        return entities;
    }

    private List<ReportEntity> createReportEntities2(PagedResult<TransactionDto> transactionDtos, PagedResult<UserDto> userDtos, PagedResult<BankAccountDto> bankAccountDtos) {
        List<ReportEntity> entities = new ArrayList<>();


        for(TransactionDto transaction: transactionDtos.getContent()) {
            BankAccountDto senderBA = findBankAccountDtoInPage(transactionDtos, bankAccountDtos, transaction.getSenderId());
            BankAccountDto recipientBA = findBankAccountDtoInPage(transactionDtos, bankAccountDtos, transaction.getRecipientId());

            UserDto sender = findUserDtoInPage(bankAccountDtos, userDtos, senderBA.getOwnerUserId());
            UserDto recipient = findUserDtoInPage(bankAccountDtos, userDtos, recipientBA.getOwnerUserId());

            entities.add(reportsMapper.convertDtosToReport(transaction, sender, recipient));
        }

        return entities;
    }

    private BankAccountDto findBankAccountDtoInPage(PagedResult<TransactionDto> transactions, PagedResult<BankAccountDto> dtos, Long bankAccountId) {
        int pageNumber = 0;
        int totalPages = dtos.getTotalPages();
        PagedResult<BankAccountDto> innerDtos = dtos;

        while (pageNumber < totalPages) {
            Optional<BankAccountDto> result = innerDtos.getContent().stream().filter(b -> b.getId().equals(bankAccountId)).findFirst();
            if(result.isPresent()) {
                return result.get();
            } else {
                pageNumber++;
                innerDtos = getBankAccountDtos(transactions, pageNumber, 20);
            }
        }
        throw new RuntimeException("Bank account not found");
    }

    private UserDto findUserDtoInPage(PagedResult<BankAccountDto> baDtos, PagedResult<UserDto> userDtos, Long id) {
        int pageNumber = 0;
        int totalPages = userDtos.getTotalPages();
        PagedResult<UserDto> innerDtos = userDtos;

        while (pageNumber < totalPages) {
            Optional<UserDto> result = innerDtos.getContent().stream().filter(b -> b.getId().equals(id)).findFirst();
            if(result.isPresent()) {
                return result.get();
            } else {
                pageNumber++;
                innerDtos = getUsersData(baDtos, pageNumber, 20);
            }
        }
        throw new RuntimeException("User not found");
    }

    @Transactional(readOnly = true)
    public Page<ReportDto> findByFilterWithCriteria(BankAccountSearchModel model) {
        Pageable pageable = reportSortChecker.getPageableAndSort(model);

        model.getCriteriaList().forEach(criteria -> reportSortChecker.columnCheck(criteria.getFieldName()));

        if (!model.getCriteriaList().isEmpty()) {
            ReportSpecificationBuilder builder = new ReportSpecificationBuilder(model.getCriteriaList());
            Page<ReportEntity> entities = databaseRepository.findAll(builder.build(), pageable);
            return entities.map(reportsMapper::convertEntityToDto);
        } else {
            return findAllWithPageable(pageable).map(reportsMapper::convertEntityToDto);
        }
    }

    private Page<ReportEntity> findAllWithPageable(Pageable pageable) {
        return databaseRepository.findAll(pageable);
    }
}
