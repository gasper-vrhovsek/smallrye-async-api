package io.smallrye.asyncapi.runtime.io.components;

import java.util.HashMap;

import org.jboss.jandex.AnnotationValue;

import io.apicurio.datamodels.asyncapi.models.AaiComponents;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Components;
import io.smallrye.asyncapi.runtime.scanner.AnnotationScannerContext;

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

        // !!! Currently we'll just create empty components with schemas placeholder
        AaiComponents components = new Aai20Components();

        components.schemas = new HashMap<>(); // Currently we'll generate schemas with SchemaGenerator and append them later

        //        components.channelBindings = null; // TODO ChannelBindingReader

        //        components.messages = null;// TODO MessageReader

        //        components.securitySchemes = null; // TODO SecuritySchemaReader

        //        components.parameters = null; // TODO ParameterReader

        //        components.correlationIds = null; // TODO CorrelationIdReader

        //        components.operationTraits = null; // TODO OperationTraitsReader

        //        components.messageTraits = null; // TODO MessageTraitReader

        //        components.serverBindings = null; // TODO ServerBindingReader

        //        components.operationBindings = null; // TODO OperationBindingReader

        //        components.messageBindings = null; // TODO MessageBindingReader

        //        components.setCallbacks(
        //                CallbackReader.readCallbacks(context, nested.value(ComponentsConstant.PROP_CALLBACKS)));

        return components;
    }
}
