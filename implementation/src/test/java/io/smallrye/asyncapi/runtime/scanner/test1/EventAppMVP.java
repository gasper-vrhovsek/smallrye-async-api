package io.smallrye.asyncapi.runtime.scanner.test1;

import io.smallrye.asyncapi.spec.annotations.AsyncAPIDefinition;
import io.smallrye.asyncapi.spec.annotations.Operation;
import io.smallrye.asyncapi.spec.annotations.channels.ChannelItem;
import io.smallrye.asyncapi.spec.annotations.components.Components;
import io.smallrye.asyncapi.spec.annotations.components.CorrelationId;
import io.smallrye.asyncapi.spec.annotations.components.Message;
import io.smallrye.asyncapi.spec.annotations.enums.SchemaType;
import io.smallrye.asyncapi.spec.annotations.identifier.Identifier;
import io.smallrye.asyncapi.spec.annotations.info.Info;
import io.smallrye.asyncapi.spec.annotations.media.Schema;
import io.smallrye.asyncapi.spec.annotations.media.SchemaProperty;
import io.smallrye.asyncapi.spec.annotations.security.SecurityRequirement;
import io.smallrye.asyncapi.spec.annotations.servers.Server;
import io.smallrye.asyncapi.spec.annotations.tags.Tag;

@AsyncAPIDefinition(id = @Identifier(id = "AirlinesEventDrivenApp"), info = @Info(title = "AirlinesApp", version = "0.1", description = "Airline application"), servers = {
        @Server(name = "Production AMQP", url = "https://prod.amqp.airlines.com/", description = "Airlines production AMQP server", protocol = "amqp", protocolVersion = "0.9.1", security = @SecurityRequirement(name = "api_key", scopes = {}))
}, channels = {
        @ChannelItem(name = "plane/checkin", publish = @Operation(summary = "Inform when a plane has checked in", operationId = "123", message = @Message(ref = "#/components/messages/CheckInPlane"))

        ),
        @ChannelItem(name = "plane/checkout", subscribe = @Operation(summary = "Receives a message when planeis checked out", operationId = "321", message = @Message(ref = "#/components/messages/CheckOutPlane")))
}, components = @Components(schemas = {
        @Schema(name = "Plane", type = SchemaType.OBJECT, implementation = Plane.class),
        @Schema(name = "SupersonicPlane", type = SchemaType.OBJECT, implementation = SupersonicPlane.class),
        @Schema(name = "PlaneInline", type = SchemaType.OBJECT, properties = {
                @SchemaProperty(name = "id", type = SchemaType.INTEGER, format = "int64"),
                @SchemaProperty(name = "model", type = SchemaType.STRING)
        })
}, messages = {
        @Message(name = "CheckInPlane", description = "Check in a plane", title = "CheckInPlane", contentType = "application/json", tags = {
                @Tag(name = "plane"),
                @Tag(name = "check in"),
                @Tag(name = "airline")
        }, headers = @Schema(type = SchemaType.OBJECT, properties = {
                @SchemaProperty(name = "correlationId", type = SchemaType.STRING, description = "Correlation ID set by application"),
                @SchemaProperty(name = "applicationInstanceId", type = SchemaType.STRING, description = "Unique ID for a given application instance")
        }), payload = @Schema(type = SchemaType.OBJECT, properties = {
                @SchemaProperty(name = "plane", ref = "#/components/schemas/plane")
        }), correlationId = @CorrelationId(description = "Default correlation ID", location = "$message.header#/correlationId")),
        @Message(name = "CheckOutPlane", description = "Check out a plane", title = "CheckOutPlane", contentType = "application/json", tags = {
                @Tag(name = "plane"),
                @Tag(name = "check out"),
                @Tag(name = "airline")
        }, headers = @Schema(type = SchemaType.OBJECT, properties = {
                @SchemaProperty(name = "correlationId", type = SchemaType.STRING, description = "Correlation ID set by application"),
                @SchemaProperty(name = "applicationInstanceId", type = SchemaType.STRING, description = "Unique ID for a given application instance")
        }), payload = @Schema(type = SchemaType.OBJECT, properties = {
                @SchemaProperty(name = "plane", ref = "#/components/schemas/plane")
        }))
}))
public class EventAppMVP {
}
