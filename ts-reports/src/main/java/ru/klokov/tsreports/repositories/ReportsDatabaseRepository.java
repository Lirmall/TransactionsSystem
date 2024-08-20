package ru.klokov.tsreports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.klokov.tsreports.entities.ReportEntity;

import java.util.UUID;

public interface ReportsDatabaseRepository extends JpaRepository<ReportEntity, UUID> {
}
