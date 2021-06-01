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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;

import io.apicurio.datamodels.asyncapi.models.AaiChannelItem;
import io.apicurio.datamodels.asyncapi.models.AaiMessage;
import io.apicurio.datamodels.asyncapi.models.AaiSchema;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import io.smallrye.asyncapi.api.AsyncApiConfig;
import io.smallrye.asyncapi.api.util.MergeUtil;
import io.smallrye.asyncapi.runtime.io.JsonUtil;
import io.smallrye.asyncapi.runtime.io.channel.ChannelConstant;
import io.smallrye.asyncapi.runtime.io.channel.ChannelReader;
import io.smallrye.asyncapi.runtime.io.components.ComponentReader;
import io.smallrye.asyncapi.runtime.io.info.InfoReader;
import io.smallrye.asyncapi.runtime.io.message.ApiMessageConstant;
import io.smallrye.asyncapi.runtime.io.message.MessageReader;
import io.smallrye.asyncapi.runtime.io.schema.SchemaConstant;
import io.smallrye.asyncapi.runtime.io.schema.SchemaReader;
import io.smallrye.asyncapi.runtime.io.server.ServerReader;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import io.smallrye.asyncapi.spec.annotations.AsyncAPIDefinition;

/**
 * Scans a deployment (using the archive and jandex annotation index) for relevant annotations. These
 * annotations, if found, are used to generate a valid AsyncAPI model.
 *
 * @author eric.wittmann@gmail.com
 */
public class AsyncApiAnnotationScanner {
    public static final DotName DOTNAME_ASYNC_API_DEFINITION = DotName.createSimple(AsyncAPIDefinition.class.getName());

    public static final String PROP_ID = "id";
    public static final String PROP_INFO = "info";
    public static final String PROP_SERVERS = "servers";
    public static final String PROP_CHANNELS = "channels";
    public static final String PROP_COMPONENTS = "components";
    public static final String PROP_TAGS = "tags";
    public static final String PROP_EXTERNAL_DOCS = "externalDocs";

    private static Logger LOG = Logger.getLogger(AsyncApiAnnotationScanner.class);
    private final AnnotationScannerContext annotationScannerContext;
    private final SchemaGenerator schemaGenerator;

    private ClassLoader classLoader = null;

    /**
     * Constructor.
     *
     * @param config AsyncApiConfig instance
     * @param index IndexView of deployment
     */
    public AsyncApiAnnotationScanner(AsyncApiConfig config, IndexView index) {
        FilteredIndexView filteredIndexView;
        if (index instanceof FilteredIndexView) {
            filteredIndexView = FilteredIndexView.class.cast(index);
        } else {
            filteredIndexView = new FilteredIndexView(index, config);
        }
        this.annotationScannerContext = new AnnotationScannerContext(config, filteredIndexView, new Aai20Document());

        // Schema generator JsonSchema of components
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7,
                OptionPreset.PLAIN_JSON)
                        .with(Option.DEFINITIONS_FOR_ALL_OBJECTS);
        SchemaGeneratorConfig schemaGeneratorConfig = configBuilder.build();
        schemaGenerator = new SchemaGenerator(schemaGeneratorConfig);
    }

    public AsyncApiAnnotationScanner(AsyncApiConfig asyncApiConfig, IndexView index, ClassLoader classLoader) {
        this(asyncApiConfig, index);
        this.classLoader = classLoader;
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

        Aai20Document aai20Document = scanMicroProfileAsyncApiAnnotations();
        return aai20Document;
    }

    private Aai20Document scanMicroProfileAsyncApiAnnotations() {
        Aai20Document asyncApi = this.annotationScannerContext.getAsyncApi();

        // Find all OpenAPIDefinition annotations at the package level
        ScannerLogging.logger.scanning("AsyncAPI");
        processPackageAsyncAPIDefinitions(annotationScannerContext, asyncApi);

        processClassSchemas(annotationScannerContext, asyncApi);
        processContextDefinitionReferencedSchemas(annotationScannerContext, asyncApi);

        processClassMessageItems(annotationScannerContext, asyncApi);

        processClassChannelItems(annotationScannerContext, asyncApi);

        //        ObjectMapper objectMapper = JsonUtil.MAPPER;
        //        try {
        //            // TODO remove output
        //            String asString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(asyncApi);
        //            String componentsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(asyncApi.components);
        //            String schemasString = objectMapper.writerWithDefaultPrettyPrinter()
        //                    .writeValueAsString(asyncApi.components.schemas);
        //            System.out.println("Scanned asyncapi document = \n" + asString);
        //            System.out.println("Scanned components = \n" + componentsString);
        //            System.out.println("Scanned schemas = \n" + schemasString);
        //        } catch (JsonProcessingException e) {
        //            e.printStackTrace();
        //        }

        return asyncApi;
    }

    private void processContextDefinitionReferencedSchemas(AnnotationScannerContext context, Aai20Document asyncApi) {
        Map<String, AaiSchema> definitionSchemaMap = context.getDefinitionSchemaMap();
        definitionSchemaMap.forEach((key, aaiSchema) -> {
            // TODO handle duplicates? merge maybe? what about simpleName vs full package names?
            asyncApi.components.schemas.put(key, aaiSchema);
        });
    }

    private Aai20Document processPackageAsyncAPIDefinitions(final AnnotationScannerContext context,
            Aai20Document asyncApi) {

        Collection<AnnotationInstance> annotations = context.getIndex()
                .getAnnotations(DOTNAME_ASYNC_API_DEFINITION);
        List<AnnotationInstance> packageDefs = annotations
                .stream()
                .filter(this::annotatedClasses)
                .collect(Collectors.toList());

        // Here we have packageDefs, now to build the AsyncAPI
        for (AnnotationInstance packageDef : packageDefs) {
            Aai20Document packageAai = new Aai20Document();

            packageAai.id = JandexUtil.stringValue(packageDef, PROP_ID);
            packageAai.info = InfoReader.readInfo(packageDef.value(PROP_INFO));
            packageAai.servers = ServerReader.readServers(packageDef.value(PROP_SERVERS)).orElse(null);
            packageAai.channels = ChannelReader.readChannels(packageDef.value(PROP_CHANNELS)).orElse(null);
            packageAai.components = ComponentReader.readComponents(context, packageDef.value(PROP_COMPONENTS));

            // TODO for tags we need to be able to handle REFS (and for other stuff aswell)
            //            packageAai.tags = TagReader.readTags(context, packageDef.value(PROP_TAGS)).orElse(null);
            //            packageAai.externalDocs = ExternalDocsReader.readExternalDocs(context, packageDef.value(PROP_EXTERNAL_DOCS));

            MergeUtil.merge(asyncApi, packageAai);
        }
        return asyncApi;
    }

    private Aai20Document processClassMessageItems(AnnotationScannerContext context, Aai20Document asyncApi) {
        List<AnnotationInstance> messages = context.getIndex()
                .getAnnotations(ApiMessageConstant.DOTNAME_MESSAGE)
                .stream()
                .filter(this::annotatedClasses)
                .collect(Collectors.toList());

        messages.forEach(message -> {
            AaiMessage messageItem = MessageReader.readMessage(message);
            //            context.addMessageDefinition() // TODO
        });

        return null;
    }

    private Aai20Document processClassChannelItems(AnnotationScannerContext context, Aai20Document asyncApi) {
        List<AnnotationInstance> channels = context.getIndex()
                .getAnnotations(ChannelConstant.DOTNAME_CHANNEL)
                .stream()
                .filter(this::annotatedClasses)
                .collect(Collectors.toList());

        channels.forEach(channel -> {
            AaiChannelItem channelItem = ChannelReader.readChannel(channel);
            // TODO merge, in V1 we'll support only class annotated channels
            asyncApi.channels.put(channelItem.getName(), channelItem);
        });
        return asyncApi;
    }

    private void processClassSchemas(final AnnotationScannerContext context, Aai20Document aaiDocument) {
        ObjectMapper mapper = JsonUtil.MAPPER;

        Map<String, ObjectNode> collect = context.getIndex()
                .getAnnotations(SchemaConstant.DOTNAME_SCHEMA)
                .stream()
                .filter(this::annotatedClasses)
                .collect(Collectors.toMap(
                        annotationInstance -> annotationInstance.target().asClass().simpleName(),
                        o -> {
                            try {
                                String className = o.target().asClass().name().toString();
                                if (classLoader != null) {
                                    return schemaGenerator.generateSchema(classLoader.loadClass(className));
                                } else {
                                    return schemaGenerator.generateSchema(Class.forName(className));
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            return mapper.createObjectNode();
                        }));

        collect.forEach((s, jsonNodes) -> {
            // Read and save definitions of each scanned schema node, if any
            // These definitions are later inserted under `#/components/schemas`
            JsonNode definitions = jsonNodes.get("definitions");
            if (definitions != null) {
                // extract this to a method
                Iterator<Map.Entry<String, JsonNode>> defFieldsIterator = definitions.fields();
                while (defFieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> definition = defFieldsIterator.next();

                    String key = definition.getKey();
                    AaiSchema definitionAaiSchema = SchemaReader.readSchema(definition.getValue(), true);
                    context.addDefinitionSchema(key, definitionAaiSchema);
                }
            }
            AaiSchema aaiSchema = SchemaReader.readSchema(jsonNodes, true);
            aaiDocument.components.schemas.put(s, aaiSchema);
        });
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
