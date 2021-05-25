package io.smallrye.asyncapi.runtime.scanner;

import com.fasterxml.jackson.databind.JsonNode;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import io.smallrye.asyncapi.api.AsyncApiConfig;

import java.util.HashMap;
import java.util.Map;

public class AnnotationScannerContext {
    private final AsyncApiConfig config;
    private final FilteredIndexView index;
    private final Aai20Document asyncApi;
    private final Map<String, JsonNode> schemasMap;

    public AnnotationScannerContext(AsyncApiConfig config, FilteredIndexView index,
            Aai20Document asyncApi) {
        this.config = config;
        this.index = index;
        this.asyncApi = asyncApi;
        this.schemasMap = new HashMap<>();
    }

    public Aai20Document getAsyncApi() {
        return this.asyncApi;
    }

    public FilteredIndexView getIndex() {
        return index;
    }

    public AsyncApiConfig getConfig() {
        return config;
    }

    public Map<String, JsonNode> getSchemasMap() {
        return schemasMap;
    }
}
