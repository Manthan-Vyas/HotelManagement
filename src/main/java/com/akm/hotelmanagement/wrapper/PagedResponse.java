package com.akm.hotelmanagement.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> items;
    private Pageable pageable;
    private Sort sort;
    private int number;
    private boolean first;
    private boolean last;
    private int size;
    private int numberOfElements;
    private long totalElements;
    private int totalPages;

    public PagedResponse(Page<T> pagedData, List<T> items) {
        this(
                items,
                pagedData.getPageable(),
                pagedData.getSort(),
                pagedData.getNumber(),
                pagedData.isFirst(),
                pagedData.isLast(),
                pagedData.getSize(),
                pagedData.getNumberOfElements(),
                pagedData.getTotalElements(),
                pagedData.getTotalPages()
        );
    }

    public PagedResponse(Page<T> pagedData) {
        this(
                pagedData,
                pagedData.getContent()
        );
    }
}
