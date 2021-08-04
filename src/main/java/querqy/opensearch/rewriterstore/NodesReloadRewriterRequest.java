package querqy.opensearch.rewriterstore;

import org.opensearch.action.support.nodes.BaseNodeRequest;
import org.opensearch.action.support.nodes.BaseNodesRequest;
import org.opensearch.common.io.stream.StreamInput;
import org.opensearch.common.io.stream.StreamOutput;

import java.io.IOException;

public class NodesReloadRewriterRequest extends BaseNodesRequest<NodesReloadRewriterRequest> {

    private final String rewriterId;

    public NodesReloadRewriterRequest(final String rewriterId, final String... nodesIds) {
        super(nodesIds);
        this.rewriterId = rewriterId;
    }

    public NodesReloadRewriterRequest(final StreamInput in) throws IOException {
        super(in);
        rewriterId = in.readString();
    }

    @Override
    public void writeTo(final StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(rewriterId);
    }

    public NodeRequest newNodeRequest() {
        return new NodeRequest(rewriterId);
    }

    public String getRewriterId() {
        return rewriterId;
    }


    public static class NodeRequest extends BaseNodeRequest {

        String rewriterId;

        public NodeRequest(final StreamInput in) throws IOException {
            super(in);
            rewriterId = in.readString();
        }

        public NodeRequest(final String rewriterId) {
            super();
            this.rewriterId = rewriterId;
        }

        @Override
        public void writeTo(final StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeString(rewriterId);
        }

        public String getRewriterId() {
            return rewriterId;
        }

    }
}
