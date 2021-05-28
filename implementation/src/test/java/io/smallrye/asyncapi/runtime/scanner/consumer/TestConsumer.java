package io.smallrye.asyncapi.runtime.scanner.consumer;

import io.smallrye.asyncapi.runtime.scanner.model.TestModel;
import io.smallrye.asyncapi.spec.annotations.Operation;
import io.smallrye.asyncapi.spec.annotations.channels.ChannelItem;
import io.smallrye.asyncapi.spec.annotations.components.Message;
import io.smallrye.asyncapi.spec.annotations.media.Schema;

@ChannelItem(name = "test/some/queue/path", description = "Just a test consumer", subscribe = @Operation(operationId = "consumeSomeQueueEvent", message = @Message(payload = @Schema(implementation = TestModel.class))))
public class TestConsumer {
    public void handleMessage(TestModel message) {
        // process message

    }
}
