package ru.klokov.tsreports.specifications.sort;

import org.springframework.stereotype.Component;
import ru.klokov.tscommon.specifications.sort.PageableAndSortChecker;

import java.util.Arrays;
import java.util.List;

@Component
public class ReportSortChecker implements PageableAndSortChecker {

    @Override
    public List<String> getColumnMapping() {
        return Arrays.asList(
                "id",
                "senderUserId",
                "senderUserUsername",
                "senderUserBankAccountId",
                "amount",
                "recipientUserId",
                "recipientUserUsername",
                "recipientUserBankAccountId",
                "typeId",
                "typeName",
                "statusId",
                "statusName",
                "transactionDate"
        );
    }

    @Override
    public String getDefaultSortColumn() {
        return "transactionDate";
    }
}
