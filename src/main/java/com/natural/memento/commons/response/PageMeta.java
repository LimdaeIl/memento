package com.natural.memento.commons.response;


public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static PageMeta of(int page, int size, long totalElements) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        boolean hasPrevious = page > 0;
        boolean hasNext = page + 1 < totalPages;
        return new PageMeta(page, size, totalElements, totalPages, hasNext, hasPrevious);
    }
}
