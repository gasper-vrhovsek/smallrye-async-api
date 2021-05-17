package io.smallrye.asyncapi.runtime.scanner;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import io.smallrye.asyncapi.api.AsyncApiConfig;
import org.jboss.jandex.IndexView;

public class AnnotationScannerContext {
    private final AsyncApiConfig config;
    private final FilteredIndexView index;
    private final Aai20Document asyncApi;

    public AnnotationScannerContext(AsyncApiConfig config, FilteredIndexView index,
            Aai20Document asyncApi) {
        this.config = config;
        this.index = index;
        this.asyncApi = asyncApi;
    }

    public Aai20Document getAsyncApi() {
        return this.asyncApi;
    }

    public FilteredIndexView getIndex() {
        return index;
    }
}
