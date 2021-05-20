package io.smallrye.asyncapi.runtime.io.components;

import io.apicurio.datamodels.asyncapi.models.AaiComponents;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Components;
import io.smallrye.asyncapi.runtime.scanner.AnnotationScannerContext;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

/**
 * Reading the Components annotation and json node
 * 
 * @see <a href=
 *      "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#componentsObject">componentsObject</a>
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class ComponentReader {

    private ComponentReader() {
    }

    /**
     * Reads any Components annotations.
     * 
     * @param context the scanning context
     * @param annotationValue the {@literal @}Components annotation
     * @return Components model
     */
    public static AaiComponents readComponents(final AnnotationScannerContext context,
            final AnnotationValue annotationValue) {
        if (annotationValue == null) {
            return null;
        }
//        IoLogging.logger.singleAnnotation("@Components");
        AnnotationInstance nested = annotationValue.asNested();
        AaiComponents components = new Aai20Components();
        // TODO for EVERY item below, handle the case where the annotation is ref-only.  then strip the ref path and use the final segment as the name

        components.schemas = null;// TODO SchemaReader

        components.messages = null;// TODO MessageReader

        components.securitySchemes = null; // TODO SecuritySchemaReader

        components.parameters = null; // TODO ParameterReader

        components.correlationIds = null; // TODO CorrelationIdReader

        components.operationTraits = null; // TODO OperationTraitsReader

        components.messageTraits = null; // TODO MessageTraitReader

        components.serverBindings = null; // TODO ServerBindingReader

        components.channelBindings = null; // TODO ChannelBindingReader

        components.operationBindings = null; // TODO OperationBindingReader

        components.messageBindings = null; // TODO MessageBindingReader


//        components.setCallbacks(
//                CallbackReader.readCallbacks(context, nested.value(ComponentsConstant.PROP_CALLBACKS)));

        return components;
    }

//    /**
//     * Reads the {@link Components} OpenAPI nodes.
//     *
//     * @param node the json node
//     * @return Components model
//     */
//    public static Components readComponents(final JsonNode node) {
//        if (node == null || !node.isObject()) {
//            return null;
//        }
//        IoLogging.logger.singleJsonNode("Components");
//        Components components = new ComponentsImpl();
//        components.setCallbacks(CallbackReader.readCallbacks(node.get(ComponentsConstant.PROP_CALLBACKS)));
//        components.setExamples(ExampleReader.readExamples(node.get(ComponentsConstant.PROP_EXAMPLES)));
//        components.setHeaders(HeaderReader.readHeaders(node.get(ComponentsConstant.PROP_HEADERS)));
//        components.setLinks(LinkReader.readLinks(node.get(ComponentsConstant.PROP_LINKS)));
//        components.setParameters(ParameterReader.readParameters(node.get(ComponentsConstant.PROP_PARAMETERS)));
//        components.setRequestBodies(
//                RequestBodyReader.readRequestBodies(node.get(ComponentsConstant.PROP_REQUEST_BODIES)));
//        components.setResponses(ResponseReader.readResponsesMap(node.get(ComponentsConstant.PROP_RESPONSES)));
//        components.setSchemas(SchemaReader.readSchemas(node.get(ComponentsConstant.PROP_SCHEMAS)).orElse(null));
//        components.setSecuritySchemes(
//                SecuritySchemeReader.readSecuritySchemes(node.get(ComponentsConstant.PROP_SECURITY_SCHEMES)));
//        ExtensionReader.readExtensions(node, components);
//        return components;
//    }
}
