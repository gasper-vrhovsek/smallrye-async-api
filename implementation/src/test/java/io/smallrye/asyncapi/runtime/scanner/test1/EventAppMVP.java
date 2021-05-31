package io.smallrye.asyncapi.runtime.scanner.test1;

import io.smallrye.asyncapi.spec.annotations.AsyncAPIDefinition;
import io.smallrye.asyncapi.spec.annotations.identifier.Identifier;
import io.smallrye.asyncapi.spec.annotations.info.Info;
import io.smallrye.asyncapi.spec.annotations.servers.Server;

@AsyncAPIDefinition(
        id = @Identifier(id = "AirlinesEventDrivenApp"),
        info = @Info(title = "AirlinesApp", version = "0.1", description = "Airline application"),
        servers = {
                @Server(
                        name = "Production AMQP",
                        url = "https://prod.amqp.airlines.com/",
                        description = "Airlines production AMQP server",
                        protocol = "amqp",
                        protocolVersion = "0.9.1"
                )
        })
public class EventAppMVP {
}
