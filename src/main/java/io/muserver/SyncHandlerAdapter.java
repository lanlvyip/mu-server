package io.muserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SyncHandlerAdapter implements AsyncMuHandler {
    private static final Logger log = LoggerFactory.getLogger(SyncHandlerAdapter.class);
    private final List<MuHandler> muHandlers;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    SyncHandlerAdapter(List<MuHandler> muHandlers) {
        this.muHandlers = muHandlers;
    }


    public boolean onHeaders(AsyncContext ctx, Headers headers) throws Exception {
        if (headers.contains(HeaderNames.TRANSFER_ENCODING) || headers.getInt(HeaderNames.CONTENT_LENGTH, -1) > 0) {
            // There will be a request body, so set the streams
            GrowableByteBufferInputStream requestBodyStream = new GrowableByteBufferInputStream();
            ((NettyRequestAdapter) ctx.request).inputStream(requestBodyStream);
            ctx.state = requestBodyStream;
        }
        executor.submit(() -> {
            try {

                boolean handled = false;
                for (MuHandler muHandler : muHandlers) {
                    handled = muHandler.handle(ctx.request, ctx.response);
                    if (handled) {
                        break;
                    }
                }
                if (!handled) {
                    MuServerHandler.send404(ctx);
                }

                ((NettyRequestAdapter)ctx.request).clean();

            } catch (Throwable ex) {
                log.warn("Unhandled error from handler for " + this, ex);
                if (!ctx.response.hasStartedSendingData()) {
                    String errorID = "ERR-" + UUID.randomUUID().toString();
                    log.info("Sending a 500 to the client with ErrorID=" + errorID);
                    MuServerHandler.sendPlainText(ctx, "500 Server Error. ErrorID=" + errorID, 500);
                }
            } finally {
                ctx.complete();
            }
        });
        return true;
    }

    public void onRequestData(AsyncContext ctx, ByteBuffer buffer) {
        GrowableByteBufferInputStream state = (GrowableByteBufferInputStream) ctx.state;
        state.handOff(buffer);
    }

    public void onRequestComplete(AsyncContext ctx) {
        try {
            GrowableByteBufferInputStream state = (GrowableByteBufferInputStream) ctx.state;
            if (state != null) {
                state.close();
            }
            ctx.request.state(null);
        } catch (Exception e) {
            log.info("Error while cleaning up request. It may mean the client did not receive the full response for " + ctx.request, e);
        }
    }

}
