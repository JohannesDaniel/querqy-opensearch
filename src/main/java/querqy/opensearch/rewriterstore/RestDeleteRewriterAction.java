package querqy.opensearch.rewriterstore;

import org.opensearch.action.ActionRequestBuilder;
import org.opensearch.client.OpenSearchClient;
import org.opensearch.client.node.NodeClient;
import org.opensearch.rest.BaseRestHandler;
import org.opensearch.rest.RestRequest;
import org.opensearch.rest.action.RestStatusToXContentListener;

import java.util.Collections;
import java.util.List;


public class RestDeleteRewriterAction extends BaseRestHandler {

    public static final String PARAM_REWRITER_ID = "rewriterId";

    @Override
    public String getName() {
        return "Delete a Querqy rewriter";
    }

    @Override
    public List<Route> routes() {
        return Collections.singletonList(new Route(RestRequest.Method.DELETE, "/_querqy/rewriter/{rewriterId}"));
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) {

        final DeleteRewriterRequestBuilder builder = createRequestBuilder(request, client);

        return (channel) -> builder.execute(
                new RestStatusToXContentListener<>(channel));
    }


    DeleteRewriterRequestBuilder createRequestBuilder(final RestRequest request, final NodeClient client) {
        String rewriterId = request.param(PARAM_REWRITER_ID);
        if (rewriterId == null) {
            throw new IllegalArgumentException("RestDeleteRewriterAction requires rewriterId parameter");
        }

        rewriterId = rewriterId.trim();
        if (rewriterId.isEmpty()) {
            throw new IllegalArgumentException("RestDeleteRewriterAction: rewriterId parameter must not be empty");
        }

        return new DeleteRewriterRequestBuilder(client, DeleteRewriterAction.INSTANCE,
                new DeleteRewriterRequest(rewriterId));
    }


    public static class DeleteRewriterRequestBuilder
            extends ActionRequestBuilder<DeleteRewriterRequest, DeleteRewriterResponse> {

        public DeleteRewriterRequestBuilder(final OpenSearchClient client, final DeleteRewriterAction action,
                                         final DeleteRewriterRequest request) {
            super(client, action, request);
        }

    }
}
