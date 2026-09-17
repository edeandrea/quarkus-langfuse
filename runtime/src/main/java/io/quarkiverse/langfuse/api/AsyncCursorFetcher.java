package io.quarkiverse.langfuse.api;

import io.quarkiverse.langfuse.api.cursor.Cursor;
import io.quarkiverse.langfuse.api.cursor.CursorResult;
import io.smallrye.mutiny.Uni;

/**
 * Asynchronously fetches a single batch of a cursor-addressed Langfuse collection.
 *
 * @param <T> the type of the items in the batch
 */
@FunctionalInterface
interface AsyncCursorFetcher<T> {
    Uni<CursorResult<T>> fetch(Cursor cursor);
}
