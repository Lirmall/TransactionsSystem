package ru.klokov.tsreports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

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

        ConcurrentHashMap<Long, BankAccountDto> bankAccountDtoCacheMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, UserDto> userDtoCacheMap = new ConcurrentHashMap<>();

        PagedResult<TransactionDto> innerTransactionDtoPage;
        int transactionPage = 0;
        do {
            innerTransactionDtoPage = getTransactionsByPeriod(periodDto, pageNumber, pageSize);
            PagedResult<BankAccountDto> innerBankAccountDtos = getBankAccountDtos(innerTransactionDtoPage, 0, pageSize);
            PagedResult<UserDto> innerUserDtos = getUsersData(innerBankAccountDtos, 0, pageSize);
            List<ReportEntity> innerEntities = createReportEntities2(innerTransactionDtoPage, innerUserDtos, innerBankAccountDtos, bankAccountDtoCacheMap, userDtoCacheMap);

            if (innerTransactionDtoPage.getContent().isEmpty()) {
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

            log.info("Save {} list of reports", pageNumber);
            pageNumber++;

            try {
                saveListOfReports(innerEntities);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } while (transactionPage < innerTransactionDtoPage.getTotalPages());

        log.info("Fill reports to DB ends at {}", LocalDateTime.now());
        log.info("Method works {} milliseconds", System.currentTimeMillis() - startTime);
    }

    public void concurrentFillAllOrNewReportsToDB(@Nullable Integer pageSize, @Nullable Integer threadsCount) {
        long startTime = System.currentTimeMillis();
        log.info("Fill reports to DB with concurrent starts at {}", LocalDateTime.now());
        PeriodDto periodDto = new PeriodDto();
        Optional<LocalDateTime> optionalLastReportDate = databaseRepository.getReportEntityWithMaxTransactionDate();

        LocalDateTime lastReportDate = optionalLastReportDate.orElse(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 1));

        log.info("{}", lastReportDate);

        periodDto.setPeriodStart(lastReportDate);
        periodDto.setPeriodEnd(LocalDateTime.now().minusDays(1L).withHour(23).withMinute(59).withSecond(59).withNano(999999999));

        log.info("{}", periodDto.getPeriodEnd());

        int pageNumber = 0;
        int innerPageSize = pageSize == null ? 2000 : pageSize;

        PagedResult<TransactionDto> firstPage = getTransactionsByPeriod(periodDto, pageNumber, innerPageSize);
        int totalPages = firstPage.getTotalPages();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        log.info("Available processors {}", totalPages);

        if (threadsCount == null || threadsCount > availableProcessors) {
            threadsCount = availableProcessors;
        }

        log.info("Threads {}", threadsCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        List<Future<Void>> futures = new ArrayList<>();

        AtomicLong maxEndTimeMillis = new AtomicLong();

        ConcurrentHashMap<Long, BankAccountDto> bankAccountDtoCacheMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, UserDto> userDtoCacheMap = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(totalPages);

        for (int page = 0; page < totalPages; page++) {
            int finalPageNumber = page;
            Future<Void> future = executor.submit(() -> {

                PagedResult<TransactionDto> innerTransactionDtoPage = getTransactionsByPeriod(periodDto, finalPageNumber, innerPageSize);
                PagedResult<BankAccountDto> innerBankAccountDtos = getBankAccountDtos(innerTransactionDtoPage, 0, innerPageSize);
                PagedResult<UserDto> innerUserDtos = getUsersData(innerBankAccountDtos, 0, innerPageSize);

                List<ReportEntity> innerEntities = createReportEntities2(innerTransactionDtoPage, innerUserDtos, innerBankAccountDtos, bankAccountDtoCacheMap, userDtoCacheMap);

                if (!innerEntities.isEmpty()) {
                    log.info("Save {} reports from page {}", innerEntities.size(), finalPageNumber);
                    saveListOfReportsWithTime(innerEntities, latch);
                }

                maxEndTimeMillis.updateAndGet(currentMax -> Math.max(currentMax, System.currentTimeMillis()));
                log.info("max end time millis is {}", maxEndTimeMillis);
                return null;  // Возвращаем null, так как результат не нужен
            });

            futures.add(future);
        }

        try {
            for (Future<Void> future : futures) {
                future.get();  // Ожидаем выполнения каждой задачи
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error during parallel task execution", e);
        } finally {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("Error during waiting for threads to finish", e);
            }
            executor.shutdown();  // Завершаем работу пула потоков
        }

        log.info("Method with concurrent works {} milliseconds", maxEndTimeMillis.get() - startTime);
    }

    //но вроде как такое поведение идет по-умолчанию
    @Transactional(rollbackFor = RuntimeException.class)
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


    @Transactional(rollbackFor = RuntimeException.class)
    protected void saveListOfReportsWithTime(List<ReportEntity> reportEntities, CountDownLatch latch) {
        Random random = new Random();
        int randomNumber = random.nextInt(5);
        log.info("random number is {}", randomNumber);
        if (randomNumber == 1) {
            log.warn("Try to fail");
            latch.countDown();
            log.info("latch {}", latch.getCount());
            throw new RuntimeException("Fail to record");
        }

        databaseRepository.saveAll(reportEntities);
        log.info("Saved!");
        latch.countDown();
        log.info("latch {}", latch.getCount());
    }

    @Transactional
    public void clearReports() {
//        databaseRepository.deleteAll();
        databaseRepository.truncateReports();
        log.info("Reports DB cleared");
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

        for (TransactionDto transaction : transactionDtos.getContent()) {
            Optional<BankAccountDto> optSenderBA = bankAccountDtos.getContent().stream().filter(b -> b.getId().equals(transaction.getSenderId())).findFirst();
            Optional<BankAccountDto> optRecBA = bankAccountDtos.getContent().stream().filter(b -> b.getId().equals(transaction.getRecipientId())).findFirst();

            if (optSenderBA.isEmpty() || optRecBA.isEmpty()) {
                throw new RuntimeException("Sender or recipient bank account is empty");
            }

            Optional<UserDto> optSender = userDtos.getContent().stream().filter(u -> u.getId().equals(optSenderBA.get().getOwnerUserId())).findFirst();
            Optional<UserDto> optRecipient = userDtos.getContent().stream().filter(u -> u.getId().equals(optRecBA.get().getOwnerUserId())).findFirst();

            if (optSender.isEmpty() || optRecipient.isEmpty()) {
                throw new RuntimeException("Sender or recipient is empty");
            }

            UserDto sender = optSender.get();
            UserDto recipient = optSender.get();

            entities.add(reportsMapper.convertDtosToReport(transaction, sender, recipient));
        }

        return entities;
    }

    private List<ReportEntity> createReportEntities2(PagedResult<TransactionDto> transactionDtos, PagedResult<UserDto> userDtos, PagedResult<BankAccountDto> bankAccountDtos,
                                                     ConcurrentHashMap<Long, BankAccountDto> bankAccountDtosCache, ConcurrentHashMap<Long, UserDto> userDtosCache) {
        List<ReportEntity> entities = new ArrayList<>();


        for (TransactionDto transaction : transactionDtos.getContent()) {
            BankAccountDto senderBA = findBankAccountDtoInPageWithCache(transactionDtos, bankAccountDtos, transaction.getSenderId(), bankAccountDtosCache);
            BankAccountDto recipientBA = findBankAccountDtoInPageWithCache(transactionDtos, bankAccountDtos, transaction.getRecipientId(), bankAccountDtosCache);

            UserDto sender = findUserDtoInPageWithCacheMap(bankAccountDtos, userDtos, senderBA.getOwnerUserId(), userDtosCache);
            UserDto recipient = findUserDtoInPageWithCacheMap(bankAccountDtos, userDtos, recipientBA.getOwnerUserId(), userDtosCache);

            entities.add(reportsMapper.convertDtosToReport(transaction, sender, recipient));
        }

        return entities;
    }

    private BankAccountDto findBankAccountDtoInPageWithCache(PagedResult<TransactionDto> transactions, PagedResult<BankAccountDto> dtos, Long bankAccountId, Map<Long, BankAccountDto> bankAccountDtosCache) {
        if (bankAccountDtosCache.containsKey(bankAccountId)) {
//            log.info("Bank account with id {} found in cache", bankAccountId);
            return bankAccountDtosCache.get(bankAccountId);
        } else {
            BankAccountDto result = findBankAccountDtoInPage(transactions, dtos, bankAccountId);
            bankAccountDtosCache.put(bankAccountId, result);
            log.info("Bank account with id {} get from rest query and added to cache. Cache size is {}", bankAccountId, bankAccountDtosCache.keySet().size());
            return result;
        }
    }

    private BankAccountDto findBankAccountDtoInPage(PagedResult<TransactionDto> transactions, PagedResult<BankAccountDto> dtos, Long bankAccountId) {
        int pageNumber = 0;
        int totalPages = dtos.getTotalPages();
        PagedResult<BankAccountDto> innerDtos = dtos;

        while (pageNumber < totalPages) {
            Optional<BankAccountDto> result = innerDtos.getContent().stream().filter(b -> b.getId().equals(bankAccountId)).findFirst();
            if (result.isPresent()) {
                return result.get();
            } else {
                pageNumber++;
                innerDtos = getBankAccountDtos(transactions, pageNumber, 20);
            }
        }
        throw new RuntimeException("Bank account not found");
    }

    private UserDto findUserDtoInPageWithCacheMap(PagedResult<BankAccountDto> baDtos, PagedResult<UserDto> userDtos, Long id, ConcurrentHashMap<Long, UserDto> userDtosCache) {
        if (userDtosCache.containsKey(id)) {
            return userDtosCache.get(id);
        } else {
            UserDto result = findUserDtoInPage(baDtos, userDtos, id);
            userDtosCache.put(id, result);
            log.info("User with id {} get from rest query and added to cache. Cache size is {}", id, userDtosCache.keySet().size());
            return result;
        }
    }

    private UserDto findUserDtoInPage(PagedResult<BankAccountDto> baDtos, PagedResult<UserDto> userDtos, Long id) {
        int pageNumber = 0;
        int totalPages = userDtos.getTotalPages();
        PagedResult<UserDto> innerDtos = userDtos;

        while (pageNumber < totalPages) {
            Optional<UserDto> result = innerDtos.getContent().stream().filter(b -> b.getId().equals(id)).findFirst();
            if (result.isPresent()) {
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
