package ru.klokov.tstransactions.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorDto {

    private LocalDate start;
    private LocalDate end;
    private Integer count;
}
