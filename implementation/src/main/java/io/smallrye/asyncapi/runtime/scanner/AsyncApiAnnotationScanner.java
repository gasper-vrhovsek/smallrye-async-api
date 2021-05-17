/*
 * Copyright 2019 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.smallrye.asyncapi.runtime.scanner;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import io.smallrye.asyncapi.api.AsyncApiConfig;
import io.smallrye.asyncapi.spec.annotations.AsyncAPIDefinition;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Scans a deployment (using the archive and jandex annotation index) for relevant annotations. These
 * annotations, if found, are used to generate a valid AsyncAPI model.
 *
 * @author eric.wittmann@gmail.com
 */
public class AsyncApiAnnotationScanner {
    public static final DotName DOTNAME_ASYNC_API_DEFINITION = DotName.createSimple(AsyncAPIDefinition.class.getName());
    private static Logger LOG = Logger.getLogger(AsyncApiAnnotationScanner.class);
    private final AnnotationScannerContext annotationScannerContext;

    /**
     * Constructor.
     *
     * @param config AsyncApiConfig instance
     * @param index  IndexView of deployment
     */
    public AsyncApiAnnotationScanner(AsyncApiConfig config, IndexView index) {
        FilteredIndexView filteredIndexView;
        if (index instanceof FilteredIndexView) {
            filteredIndexView = FilteredIndexView.class.cast(index);
        } else {
            filteredIndexView = new FilteredIndexView(index, config);
        }
        this.annotationScannerContext = new AnnotationScannerContext(config, filteredIndexView, new Aai20Document());
    }

    /**
     * Scan the deployment for relevant annotations. Returns an AsyncAPI data model that was
     * built from those found annotations.
     *
     * @return Document generated from scanning annotations
     */
    public Aai20Document scan() {
        LOG.debug("Scanning deployment for Async Annotations.");
        // TODO other annotation scanners?

        // TODO sort tags and maps?

        return scanMicroProfileAsyncApiAnnotations();
    }

    private Aai20Document scanMicroProfileAsyncApiAnnotations() {
        // Init new AsyncAPI (Aai20Document) doc
        Aai20Document asyncApi = this.annotationScannerContext.getAsyncApi();

        // Register custom schemas if available
        // TODO
        //        SchemaRegistry schemaRegistry = SchemaRegistry.newInstance(annotationScannerContext);

        // Find all OpenAPIDefinition annotations at the package level
        ScannerLogging.logger.scanning("AsyncAPI");
        processPackageAsyncAPIDefinitions(annotationScannerContext, asyncApi);

        processClassSchemas(annotationScannerContext);

        return asyncApi;
    }

    private Aai20Document processPackageAsyncAPIDefinitions(final AnnotationScannerContext context,
            Aai20Document asyncApi) {

        List<AnnotationInstance> packageDefs = context.getIndex()
                .getAnnotations(DOTNAME_ASYNC_API_DEFINITION)
                .stream()
                .filter(this::annotatedClasses)
                //                .filter(annotation -> annotation.target().asClass().name().withoutPackagePrefix()
                //                        .equals("package-info")) // TODO add package-infos to annotation packages
                .collect(Collectors.toList());

        return asyncApi;
    }

    private void processClassSchemas(final AnnotationScannerContext context) {
        //        CurrentScannerInfo.register(null);
        //
        //        context.getIndex()
        //                .getAnnotations(SchemaConstant.DOTNAME_SCHEMA)
        //                .stream()
        //                .filter(this::annotatedClasses)
        //                .map(annotation -> Type.create(annotation.target().asClass().name(), Type.Kind.CLASS))
        //                .forEach(type -> SchemaFactory.typeToSchema(context, type, context.getExtensions()));
    }

    private boolean annotatedClasses(AnnotationInstance annotation) {
        return Objects.equals(annotation.target().kind(), AnnotationTarget.Kind.CLASS);
    }

    private void sortMaps(Aai20Document aai) {
        // Now that all paths have been created, sort them (we don't have a better way to organize them).
        // AAI doesn't have paths.
        //        sort(aai.getPaths(), Paths::getPathItems, Paths::setPathItems);

        // TODO openapi implementation is different because it uses different models
        //        final Components components = aai.components;
        //
        //        sort(components, AaiComponents::getCallbacks, Components::setCallbacks);
        //        sort(components, Components::getExamples, Components::setExamples);
        //        sort(components, Components::getHeaders, Components::setHeaders);
        //        sort(components, Components::getLinks, Components::setLinks);
        //        sort(components, Components::getParameters, Components::setParameters);
        //        sort(components, Components::getRequestBodies, Components::setRequestBodies);
        //        sort(components, Components::getResponses, Components::setResponses);
        //        sort(components, Components::getSchemas, Components::setSchemas);
        //        sort(components, Components::getSecuritySchemes, Components::setSecuritySchemes);
    }
}
