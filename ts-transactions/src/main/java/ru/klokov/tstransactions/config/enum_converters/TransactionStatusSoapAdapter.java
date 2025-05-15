package ru.klokov.tstransactions.config.enum_converters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;

public class TransactionStatusSoapAdapter extends XmlAdapter<String, TransactionStatus> {
    @Override
    public TransactionStatus unmarshal(String status) {
        return TransactionStatus.getByName(status);
    }

    @Override
    public String marshal(TransactionStatus status) {
        return status.getName();
    }
}
