package io.smallrye.asyncapi.runtime.io.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import io.apicurio.datamodels.asyncapi.models.AaiChannelItem;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20ChannelItem;
import io.smallrye.asyncapi.runtime.io.channel.operation.ApiOperationConstant;
import io.smallrye.asyncapi.runtime.io.channel.operation.ApiOperationReader;
import io.smallrye.asyncapi.runtime.util.JandexUtil;

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

    public static AaiChannelItem readChannel(AnnotationInstance annotationInstance) {
        if (annotationInstance != null) {

            AaiChannelItem channelItem = new Aai20ChannelItem(
                    JandexUtil.stringValue(annotationInstance, ChannelConstant.PROP_NAME));
            channelItem.description = JandexUtil.stringValue(annotationInstance, ChannelConstant.PROP_DESCRIPTION);

            if (annotationInstance.value(ApiOperationConstant.PROP_SUBSCRIBE) != null) {
                channelItem.subscribe = ApiOperationReader
                        .readOperation(annotationInstance.value(ApiOperationConstant.PROP_SUBSCRIBE).asNested(),
                                ApiOperationConstant.PROP_SUBSCRIBE);
            }
            if (annotationInstance.value(ApiOperationConstant.PROP_PUBLISH) != null) {
                channelItem.publish = ApiOperationReader
                        .readOperation(annotationInstance.value(ApiOperationConstant.PROP_PUBLISH).asNested(),
                                ApiOperationConstant.PROP_PUBLISH);
            }

            channelItem.parameters = null; //TODO ParametersReader
            channelItem.bindings = null; // TODO BindingsReader
            return channelItem;
        }
        return null;
    }

}
