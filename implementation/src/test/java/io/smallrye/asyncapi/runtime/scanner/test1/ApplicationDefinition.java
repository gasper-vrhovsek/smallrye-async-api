package io.smallrye.asyncapi.runtime.scanner.test1;

import io.smallrye.asyncapi.spec.annotations.AsyncAPIDefinition;
import io.smallrye.asyncapi.spec.annotations.Operation;
import io.smallrye.asyncapi.spec.annotations.channels.ChannelItem;
import io.smallrye.asyncapi.spec.annotations.identifier.Identifier;
import io.smallrye.asyncapi.spec.annotations.info.Info;

@AsyncAPIDefinition(
        id = @Identifier(id = "2.0.0"),
        info = @Info(title = "Test application", version = "1.0"),
        channels = {
                @ChannelItem(
                        name = "Channel1",
                        subscribe = @Operation()
                )
        }
)
public class ApplicationDefinition {
}
