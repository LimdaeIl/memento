package com.natural.memento.commons.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResult<T>(
        List<T> items,
        PageMeta page
) {
    public static <T> PagedResult<T> of(List<T> items, PageMeta page) {
        return new PagedResult<>(items, page);
    }

    public static <T> PagedResult<T> from(Page<T> page) {
        PageMeta meta = new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
        return new PagedResult<>(page.getContent(), meta);
    }
}