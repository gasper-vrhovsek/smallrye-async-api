package io.smallrye.asyncapi.runtime.io.externaldocs;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20ExternalDocumentation;
import io.apicurio.datamodels.core.models.common.ExternalDocumentation;
import io.smallrye.asyncapi.runtime.scanner.AnnotationScannerContext;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

/**
 * This reads annotations and json for External Documentation
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 * @see <a href=
 * "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#externalDocumentationObject">externalDocumentationObject</a>
 */
public class ExternalDocsReader {

    private ExternalDocsReader() {
    }

    /**
     * Reads an ExternalDocumentation annotation.
     *
     * @param context         scanning context
     * @param annotationValue the {@literal @}ExternalDocumentation annotation
     * @return ExternalDocumentation model
     */
    public static ExternalDocumentation readExternalDocs(final AnnotationScannerContext context,
            final AnnotationValue annotationValue) {
        if (annotationValue == null) {
            return null;
        }
        return readExternalDocs(context, annotationValue.asNested());
    }

    /**
     * Reads an ExternalDocumentation annotation.
     *
     * @param context            scanning context
     * @param annotationInstance the {@literal @}ExternalDocumentation annotation
     * @return ExternalDocumentation model
     */
    public static ExternalDocumentation readExternalDocs(AnnotationScannerContext context,
            AnnotationInstance annotationInstance) {
        if (annotationInstance == null) {
            return null;
        }
        //        IoLogging.logger.annotation("@ExternalDocumentation");
        ExternalDocumentation externalDoc = new Aai20ExternalDocumentation();

        externalDoc.description = JandexUtil.stringValue(annotationInstance, ExternalDocsConstant.PROP_DESCRIPTION);
        externalDoc.url = JandexUtil.stringValue(annotationInstance, ExternalDocsConstant.PROP_URL);
        // TODO extensions
//        externalDoc.addExtension();
//                externalDoc.setExtensions(ExtensionReader.readExtensions(context, annotationInstance));
        return externalDoc;
    }
}
