package querqy.opensearch.rewriter;

import static java.util.Collections.singletonList;

import org.opensearch.action.search.SearchRequestBuilder;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.search.SearchHits;
import querqy.opensearch.QuerqyProcessor;
import querqy.opensearch.query.MatchingQuery;
import querqy.opensearch.query.QuerqyQueryBuilder;
import querqy.opensearch.query.Rewriter;
import querqy.opensearch.rewriterstore.PutRewriterAction;
import querqy.opensearch.rewriterstore.PutRewriterRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SimpleCommonRulesRewriterFactoryTest extends AbstractRewriterIntegrationTest {


    public void testBooleanInput() throws ExecutionException, InterruptedException {
        indexDocs(
                doc("id", "1", "field1", "a"),
                doc("id", "2", "field1", "a test1 some other tokens that bring down normalised tf")
        );

        final Map<String, Object> content = new HashMap<>();
        content.put("class", SimpleCommonRulesRewriterFactory.class.getName());

        final Map<String, Object> config = new HashMap<>();
        config.put("allowBooleanInput", true);
        config.put("rules", "a AND NOT b => \nUP(1000): test1");
        content.put("config", config);

        final PutRewriterRequest request = new PutRewriterRequest("common_rules", content);

        client().execute(PutRewriterAction.INSTANCE, request).get();

        QuerqyQueryBuilder querqyQuery = new QuerqyQueryBuilder(getInstanceFromNode(QuerqyProcessor.class));
        querqyQuery.setRewriters(singletonList(new Rewriter("common_rules")));
        querqyQuery.setMatchingQuery(new MatchingQuery("a"));
        querqyQuery.setMinimumShouldMatch("1");
        querqyQuery.setQueryFieldsAndBoostings(singletonList("field1"));

        SearchRequestBuilder searchRequestBuilder = client().prepareSearch(getIndexName());
        searchRequestBuilder.setQuery(querqyQuery);

        SearchResponse response = client().search(searchRequestBuilder.request()).get();
        SearchHits hits = response.getHits();

        assertEquals(2L, hits.getTotalHits().value);
        assertEquals("2", hits.getHits()[0].getSourceAsMap().get("id"));

        querqyQuery = new QuerqyQueryBuilder(getInstanceFromNode(QuerqyProcessor.class));
        querqyQuery.setRewriters(singletonList(new Rewriter("common_rules")));
        querqyQuery.setMatchingQuery(new MatchingQuery("a b"));
        querqyQuery.setMinimumShouldMatch("1");
        querqyQuery.setQueryFieldsAndBoostings(singletonList("field1"));

        searchRequestBuilder = client().prepareSearch(getIndexName());
        searchRequestBuilder.setQuery(querqyQuery);

        response = client().search(searchRequestBuilder.request()).get();
        hits = response.getHits();

        assertEquals(2L, hits.getTotalHits().value);
        assertEquals("1", hits.getHits()[0].getSourceAsMap().get("id"));

    }


}