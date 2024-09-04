package ru.klokov.tsreports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.klokov.tsreports.entities.ReportEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ReportsDatabaseRepository extends JpaRepository<ReportEntity, UUID> {
    @Query(value = "select max(rr.transaction_date) from reports.reports rr", nativeQuery = true)
    Optional<LocalDateTime> getReportEntityWithMaxTransactionDate();
}
