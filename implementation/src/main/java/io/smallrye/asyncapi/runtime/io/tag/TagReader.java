package io.smallrye.asyncapi.runtime.io.tag;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20Tag;
import io.apicurio.datamodels.core.models.common.Tag;
import io.smallrye.asyncapi.runtime.io.externaldocs.ExternalDocsConstant;
import io.smallrye.asyncapi.runtime.io.externaldocs.ExternalDocsReader;
import io.smallrye.asyncapi.runtime.scanner.AnnotationScannerContext;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Reading the Tag from annotation or json
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#tagObject">tagObject</a>
 */
public class TagReader {

    private TagReader() {
    }

    /**
     * Reads any Tag annotations.The annotation
     * value is an array of Tag annotations.
     *
     * @param context         scanning context
     * @param annotationValue an array of {@literal @}Tag annotations
     * @return List of Tag models
     */
    public static Optional<List<Tag>> readTags(final AnnotationScannerContext context, final AnnotationValue annotationValue) {
        if (annotationValue != null) {
            //            IoLogging.logger.annotationsArray("@Tag");
            AnnotationInstance[] nestedArray = annotationValue.asNestedArray();
            List<Tag> tags = new ArrayList<>();
            for (AnnotationInstance tagAnno : nestedArray) {
                if (!JandexUtil.isRef(tagAnno)) {
                    tags.add(readTag(context, tagAnno));
                }
            }
            return Optional.of(tags);
        }
        return Optional.empty();
    }

    /**
     * Reads a single Tag annotation.
     *
     * @param context            scanning context
     * @param annotationInstance {@literal @}Tag annotation, must not be null
     * @return Tag model
     */
    public static Tag readTag(final AnnotationScannerContext context, final AnnotationInstance annotationInstance) {
        Objects.requireNonNull(annotationInstance, "Tag annotation must not be null");
        //        IoLogging.logger.singleAnnotation("@Tag");
        Tag tag = new Aai20Tag();

        tag.name = JandexUtil.stringValue(annotationInstance, TagConstant.PROP_NAME);
        tag.description = JandexUtil.stringValue(annotationInstance, TagConstant.PROP_DESCRIPTION);
        tag.externalDocs = ExternalDocsReader.readExternalDocs(context,
                annotationInstance.value(ExternalDocsConstant.PROP_EXTERNAL_DOCS));

        // TODO support extensions
        //        tag.setExtensions(ExtensionReader.readExtensions(context, annotationInstance));
        return tag;
    }

    //    // Helpers for scanner classes
    //    public static boolean hasTagAnnotation(final AnnotationTarget target) {
    //        return TypeUtil.hasAnnotation(target, TagConstant.DOTNAME_TAG) ||
    //                TypeUtil.hasAnnotation(target, TagConstant.DOTNAME_TAGS);
    //    }
    //
    //    public static List<AnnotationInstance> getTagAnnotations(final AnnotationTarget target) {
    //        return JandexUtil.getRepeatableAnnotation(target,
    //                TagConstant.DOTNAME_TAG,
    //                TagConstant.DOTNAME_TAGS);
    //    }

}
