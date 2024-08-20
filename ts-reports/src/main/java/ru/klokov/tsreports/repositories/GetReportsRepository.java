package ru.klokov.tsreports.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetReportsRepository {
    private final RestTemplate restTemplate;
}
