package ru.klokov.tsreports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klokov.tsreports.repositories.GetReportsRepository;
import ru.klokov.tsreports.repositories.ReportsDatabaseRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsDatabaseRepository databaseRepository;
    private final GetReportsRepository getReportsRepository;
}
