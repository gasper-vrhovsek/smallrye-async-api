package io.smallrye.asyncapi.runtime.io.channel;

import io.smallrye.asyncapi.spec.annotations.channels.ChannelItem;
import io.smallrye.asyncapi.spec.annotations.channels.ChannelItems;
import org.jboss.jandex.DotName;

public class ChannelConstant {

    public static final DotName DOTNAME_CHANNEL = DotName.createSimple(ChannelItem.class.getName());
    public static final DotName DOTNAME_CHANNELS = DotName.createSimple(ChannelItems.class.getName());
    public static final String PROP_NAME = "name";
    public static final String PROP_DESCRIPTION = "description";
}
