package io.quarkiverse.langfuse.api;

import io.quarkiverse.langfuse.api.cursor.Cursor;
import io.quarkiverse.langfuse.api.cursor.CursorResult;

/**
 * Fetches a single batch of a cursor-addressed Langfuse collection.
 *
 * @param <T> the type of the items in the batch
 */
@FunctionalInterface
interface CursorFetcher<T> {
    CursorResult<T> fetch(Cursor cursor);
}
