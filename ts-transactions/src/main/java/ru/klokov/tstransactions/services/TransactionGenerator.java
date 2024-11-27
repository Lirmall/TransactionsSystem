package ru.klokov.tstransactions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.klokov.tstransactions.entities.TransactionEntity;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;
import ru.klokov.tstransactions.repositories.TransactionRepository;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionGenerator {

    private final TransactionRepository transactionRepository;

    public void transactionGenerate(LocalDate start, LocalDate end, Integer count) throws IOException {

        String insertQuery = "insert into transactions.transactions (id, sender_id, recipient_id, amount, type, status, transaction_date)\n" +
                "values ('%s', %s, %s, %s, %s, %s, '%s');";

        List<LocalDateTime> transactionDates = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LocalDateTime transactionDate = generateRandomLocalDateTime(
                    LocalDateTime.of(start, LocalTime.of(0, 0, 0)),
                    LocalDateTime.of(end, LocalTime.of(23, 59, 59))
            );
            transactionDates.add(transactionDate);
        }

        int equals = 1;
        transactionDates.sort(LocalDateTime::compareTo);

            for (LocalDateTime transactionDate : transactionDates) {

                UUID id = UUID.randomUUID();
                Long senderId = generateRandomLong(1L, 8L);
                Long recipientId = generateRandomLong(1L, 8L);

                while (recipientId.equals(senderId)) {
                    log.info("{} equals!", equals);
                    equals++;
                    recipientId = generateRandomLong(1L, 8L);
                }

                Double amount = generateRandomDouble(0.1, 10000.0);
                Long type = generateRandomLong(1L, 5L);
                Long status = generateRandomLong(1L, 6L);

                TransactionEntity entity = new TransactionEntity();
                entity.setId(id);
                entity.setSenderId(senderId);
                entity.setRecipientId(recipientId);
                entity.setAmount(amount);
                entity.setType(TransactionType.getById(type));
                entity.setStatus(TransactionStatus.getById(status));
                entity.setTransactionDate(transactionDate);

                transactionRepository.save(entity);
            }
    }

    private long generateRandomLong(long min, long max) {
        Random random = new Random();
        return min + (long)(random.nextDouble() * (max - min));
    }

    private double generateRandomDouble(double min, double max) {
        Random random = new Random();
        double randomDouble = min + random.nextDouble() * (max - min);
        return Math.round(randomDouble * 100.0) / 100.0;
    }

    private LocalDateTime generateRandomLocalDateTime(LocalDateTime min, LocalDateTime max) {
        Random random = new Random();
        long minEpochSecond = min.toEpochSecond(min.atZone(ZoneId.systemDefault()).getOffset());
        long maxEpochSecond = max.toEpochSecond(max.atZone(ZoneId.systemDefault()).getOffset());
        long randomEpochSecond = minEpochSecond + (long)(random.nextDouble() * (maxEpochSecond - minEpochSecond));
        int nanoAdjustment = random.nextInt(1_000_000_000);
        return LocalDateTime.ofEpochSecond(randomEpochSecond, nanoAdjustment, ZoneOffset.UTC);
    }
}
