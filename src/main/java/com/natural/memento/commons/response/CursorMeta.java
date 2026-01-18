package com.natural.memento.commons.response;

public record CursorMeta(
        String nextCursor,
        boolean hasNext,
        int limit
) {
    public static CursorMeta of(String nextCursor, boolean hasNext, int limit) {
        return new CursorMeta(nextCursor, hasNext, limit);
    }
}
