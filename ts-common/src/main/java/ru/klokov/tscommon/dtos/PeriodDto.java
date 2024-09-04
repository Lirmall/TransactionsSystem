package ru.klokov.tscommon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodDto {
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
}
