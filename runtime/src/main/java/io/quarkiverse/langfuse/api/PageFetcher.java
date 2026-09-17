package io.quarkiverse.langfuse.api;

import io.quarkiverse.langfuse.api.paging.Page;
import io.quarkiverse.langfuse.api.paging.PagedResult;

/**
 * Fetches a single page of a page-addressed Langfuse collection.
 *
 * <p>
 * Implemented by each domain to adapt one generated list operation - building its request and
 * mapping its response - to the shape the pagination engine consumes.
 *
 * @param <T> the type of the items on the page
 */
@FunctionalInterface
interface PageFetcher<T> {
    PagedResult<T> fetch(Page page);
}
