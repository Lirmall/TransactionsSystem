package ru.klokov.tstransactions.specifications.sort;

import org.springframework.stereotype.Component;
import ru.klokov.tscommon.specifications.sort.PageableAndSortChecker;

import java.util.Arrays;
import java.util.List;

@Component
public class TransactionSortChecker implements PageableAndSortChecker {

    @Override
    public List<String> getColumnMapping() {
        return Arrays.asList(
                "id",
                "senderId",
                "recipientId",
                "amount",
                "type",
                "status",
                "transactionDate"
        );
    }
}
