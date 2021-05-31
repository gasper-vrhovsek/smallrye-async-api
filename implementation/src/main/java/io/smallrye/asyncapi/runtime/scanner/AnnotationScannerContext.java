package io.smallrye.asyncapi.runtime.scanner;

import java.util.LinkedHashMap;
import java.util.Map;

import io.apicurio.datamodels.asyncapi.models.AaiSchema;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import io.smallrye.asyncapi.api.AsyncApiConfig;

public class AnnotationScannerContext {
    private final AsyncApiConfig config;
    private final FilteredIndexView index;
    private final Aai20Document asyncApi;
    private final Map<String, AaiSchema> definitionSchemaMap;

    public AnnotationScannerContext(AsyncApiConfig config, FilteredIndexView index,
            Aai20Document asyncApi) {
        this.config = config;
        this.index = index;
        this.asyncApi = asyncApi;

        this.definitionSchemaMap = new LinkedHashMap<>();
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

    public void addDefinitionSchema(String key, AaiSchema definitionAaiSchema) {
        definitionSchemaMap.put(key, definitionAaiSchema);
    }

    public Map<String, AaiSchema> getDefinitionSchemaMap() {
        return definitionSchemaMap;
    }
}
