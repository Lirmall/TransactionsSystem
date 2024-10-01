package ru.klokov.tscommon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResult<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public PagedResult(Page<T> pageOfContent) {
        this.content = pageOfContent.getContent();
        this.totalPages = pageOfContent.getTotalPages();
        this.number = pageOfContent.getNumber();
        this.size = pageOfContent.getSize();
        this.totalElements = pageOfContent.getTotalElements();
    }
}
