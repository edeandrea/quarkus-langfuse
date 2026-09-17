package io.quarkiverse.langfuse.api;

import io.quarkiverse.langfuse.api.paging.Page;
import io.quarkiverse.langfuse.api.paging.PagedResult;
import io.smallrye.mutiny.Uni;

/**
 * Asynchronously fetches a single page of a page-addressed Langfuse collection.
 *
 * @param <T> the type of the items on the page
 */
@FunctionalInterface
interface AsyncPageFetcher<T> {
    Uni<PagedResult<T>> fetch(Page page);
}
