/**
 * Higher-level operations over the Langfuse API.
 *
 * <p>
 * The generated {@link com.langfuse.api.LangfuseApi} mirrors the Langfuse REST API one endpoint at a
 * time. This package sits above it and offers the operations applications actually tend to write:
 * looking things up by name, checking whether they exist, and walking paginated collections without
 * hand-rolling the page or cursor arithmetic. Anything not covered here remains one call away through
 * {@link io.quarkiverse.langfuse.api.LangfuseOperations#api()}.
 *
 * <h2>Entry points</h2>
 * <ul>
 * <li>{@link io.quarkiverse.langfuse.api.LangfuseOperations} - the synchronous facade, injectable as
 * a CDI bean</li>
 * <li>{@link io.quarkiverse.langfuse.api.AsyncLangfuseOperations} - its Mutiny counterpart, reachable
 * either through {@link io.quarkiverse.langfuse.api.LangfuseOperations#async()} or as its own
 * standalone injectable bean</li>
 * </ul>
 *
 * <h2>Domain operations</h2>
 * <p>
 * Each domain is a pair of interfaces - one returning plain values, one returning {@code Uni}/
 * {@code Multi} - that behave identically apart from how absence is represented.
 * <ul>
 * <li>{@link io.quarkiverse.langfuse.api.ModelOperations} /
 * {@link io.quarkiverse.langfuse.api.AsyncModelOperations}</li>
 * <li>{@link io.quarkiverse.langfuse.api.DatasetOperations} /
 * {@link io.quarkiverse.langfuse.api.AsyncDatasetOperations}</li>
 * <li>{@link io.quarkiverse.langfuse.api.LlmConnectionOperations} /
 * {@link io.quarkiverse.langfuse.api.AsyncLlmConnectionOperations}</li>
 * <li>{@link io.quarkiverse.langfuse.api.ScoreConfigOperations} /
 * {@link io.quarkiverse.langfuse.api.AsyncScoreConfigOperations}</li>
 * <li>{@link io.quarkiverse.langfuse.api.EvaluationRuleOperations} /
 * {@link io.quarkiverse.langfuse.api.AsyncEvaluationRuleOperations}</li>
 * <li>{@link io.quarkiverse.langfuse.api.EvaluatorOperations} /
 * {@link io.quarkiverse.langfuse.api.AsyncEvaluatorOperations}</li>
 * </ul>
 *
 * <h2>Addressing</h2>
 * <p>
 * How a traversal says <em>which</em> part of a collection it wants lives alongside the coordinate
 * types themselves, in one subpackage per addressing model.
 * <ul>
 * <li>{@link io.quarkiverse.langfuse.api.paging} - page addressing, used by every domain above
 * except evaluation rules and evaluators</li>
 * <li>{@link io.quarkiverse.langfuse.api.cursor} - cursor addressing, used by evaluation rules and
 * evaluators, whose collections Langfuse addresses with an opaque cursor rather than a page
 * index</li>
 * </ul>
 */
package io.quarkiverse.langfuse.api;
