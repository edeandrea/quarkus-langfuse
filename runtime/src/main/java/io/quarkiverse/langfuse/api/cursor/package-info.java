/**
 * Cursor addressing for the higher-level Langfuse operations layer.
 *
 * <p>
 * Used by evaluation rules and evaluators, whose collections Langfuse addresses with an opaque cursor
 * rather than a page index. Every other domain in {@link io.quarkiverse.langfuse.api} is addressed by
 * page instead - see {@link io.quarkiverse.langfuse.api.paging}.
 * <ul>
 * <li>{@link io.quarkiverse.langfuse.api.cursor.Cursor} - a position: an opaque, server-issued value
 * (or none, for the start of the collection) and a limit</li>
 * <li>{@link io.quarkiverse.langfuse.api.cursor.CursorSelection} - which batches to visit:
 * {@code all}, {@code from}, {@code only}, {@code first}</li>
 * <li>{@link io.quarkiverse.langfuse.api.cursor.CursorResult} - a batch of items plus the cursor
 * needed to continue</li>
 * </ul>
 */
package io.quarkiverse.langfuse.api.cursor;
