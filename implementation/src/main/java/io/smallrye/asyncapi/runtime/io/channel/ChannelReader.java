package io.smallrye.asyncapi.runtime.io.channel;

import io.apicurio.datamodels.asyncapi.models.AaiChannelItem;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20ChannelItem;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChannelReader {

    private ChannelReader() {
    }

    public static Optional<Map<String, AaiChannelItem>> readChannels(final AnnotationValue annotationValue) {
        if (annotationValue != null) {
            AnnotationInstance[] nestedArray = annotationValue.asNestedArray();
            Map<String, AaiChannelItem> channelMap = new HashMap<>();
            for (AnnotationInstance channelAnno : nestedArray) {
                AaiChannelItem channel = readChannel(channelAnno);
                channelMap.put(channel.getName(), channel);
            }
            return Optional.of(channelMap);

        }
        return Optional.empty();
    }

    private static AaiChannelItem readChannel(AnnotationInstance annotationInstance) {
        if (annotationInstance != null) {

            AaiChannelItem channelItem = new Aai20ChannelItem(JandexUtil.stringValue(annotationInstance, "name"));
            channelItem.description = JandexUtil.stringValue(annotationInstance, "description");
            channelItem.subscribe = null; //TODO ApiOperationReader
            channelItem.publish = null; //TODO ApiOperationReader
            channelItem.parameters = null; //TODO ParametersReader
            channelItem.bindings = null; // TODO BindingsReader
            return channelItem;
        }
        return null;
    }

}
