/**
 * Page addressing for the higher-level Langfuse operations layer.
 *
 * <p>
 * Used by every domain in {@link io.quarkiverse.langfuse.api} except evaluation rules and evaluators,
 * whose collections Langfuse addresses with an opaque cursor instead - see
 * {@link io.quarkiverse.langfuse.api.cursor}.
 * <ul>
 * <li>{@link io.quarkiverse.langfuse.api.paging.Page} - a coordinate: a 1-based index and a size</li>
 * <li>{@link io.quarkiverse.langfuse.api.paging.PageSelection} - which pages to visit: {@code all},
 * {@code from}, {@code only}, {@code range}, {@code rangeClosed}</li>
 * <li>{@link io.quarkiverse.langfuse.api.paging.PagedResult} - a page of items plus the totals
 * Langfuse reports</li>
 * </ul>
 */
package io.quarkiverse.langfuse.api.paging;
