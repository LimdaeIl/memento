package com.natural.memento.commons.response;

import java.util.List;

public record CursorResult<T>(
        List<T> items,
        CursorMeta cursor
) {
    public static <T> CursorResult<T> of(List<T> items, CursorMeta cursor) {
        return new CursorResult<>(items, cursor);
    }
}
