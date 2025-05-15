package ru.klokov.tstransactions.controllers.soap;

import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.klokov.tstransactions.dtos.soap.SoapTransactionRequestDto;
import ru.klokov.tstransactions.dtos.soap.SoapTransactionResponseDto;
import ru.klokov.tstransactions.services.TransactionsService;

@EnableWs
@Endpoint
public class SoapTransactionEndpoint {
    private static final String NAMESPACE_URI = "http://tstransactions.klokov.ru/soap";

    private final TransactionsService transactionsService;

    public SoapTransactionEndpoint(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SoapTransactionRequestDto")
    @ResponsePayload
    public SoapTransactionResponseDto createSoapTransaction(@RequestPayload SoapTransactionRequestDto requestDto) {
        return transactionsService.soapCreateTransaction(requestDto);
    }
}
