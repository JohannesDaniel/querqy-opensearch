package querqy.opensearch.rewriterstore;

import org.opensearch.action.support.nodes.BaseNodeRequest;
import org.opensearch.action.support.nodes.BaseNodesRequest;
import org.opensearch.common.io.stream.StreamInput;
import org.opensearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.Optional;

public class NodesClearRewriterCacheRequest extends BaseNodesRequest<NodesClearRewriterCacheRequest> {

    private final String rewriterId;

    public NodesClearRewriterCacheRequest(final StreamInput in) throws IOException {
        super(in);
        rewriterId = in.readOptionalString();
    }

    public NodesClearRewriterCacheRequest() {
        super((String[]) null);
        rewriterId = null;
    }

    public NodesClearRewriterCacheRequest(final String rewriterId, final String... nodesIds) {
        super(nodesIds);
        this.rewriterId = rewriterId;
    }

    @Override
    public void writeTo(final StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeOptionalString(rewriterId);
    }

    public Optional<String> getRewriterId() {
        return Optional.ofNullable(rewriterId);
    }

    public NodeRequest newNodeRequest() {
        return new NodeRequest(rewriterId);
    }

    public static class NodeRequest extends BaseNodeRequest {

        final String rewriterId;

        public NodeRequest(final StreamInput in) throws IOException {
            super(in);
            rewriterId = in.readOptionalString();
        }

        public NodeRequest() {
            rewriterId = null;
        }

        public NodeRequest(final String rewriterId) {
            this.rewriterId = rewriterId;
        }

        @Override
        public void writeTo(final StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeOptionalString(rewriterId);
        }

        public Optional<String> getRewriterId() {
            return Optional.ofNullable(rewriterId);
        }

    }
}
