package io.smallrye.asyncapi.runtime.io;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.apicurio.datamodels.core.models.Document;
import io.apicurio.datamodels.core.models.Node;

public abstract class JsonMarshalIgnoreMixin {
    @JsonIgnore
    public Document _ownerDocument;
    @JsonIgnore
    public Node _parent;
}
