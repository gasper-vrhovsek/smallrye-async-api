package io.smallrye.asyncapi.runtime.io.license;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20License;
import io.apicurio.datamodels.core.models.common.License;
import io.smallrye.asyncapi.runtime.util.JandexUtil;

/**
 * This reads the License from annotations or json
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#licenseObject">licenseObject</a>
 */
public class LicenseReader {

    private LicenseReader() {
    }

    /**
     * Reads an License annotation.
     *
     * @param annotationValue the {@literal @}License annotation
     * @return License model
     */
    public static License readLicense(final AnnotationValue annotationValue) {
        if (annotationValue == null) {
            return null;
        }
        //        IoLogging.logger.singleAnnotation("@License");
        AnnotationInstance nested = annotationValue.asNested();
        License license = new Aai20License();
        license.name = JandexUtil.stringValue(nested, LicenseConstant.PROP_NAME);
        license.url = JandexUtil.stringValue(nested, LicenseConstant.PROP_URL);
        return license;
    }
    //
    //    /**
    //     * Reads an {@link License} OpenAPI node.
    //     *
    //     * @param node the json node
    //     * @return License model
    //     */
    //    public static License readLicense(final JsonNode node) {
    //        if (node == null) {
    //            return null;
    //        }
    //        IoLogging.logger.singleJsonNode("License");
    //        License license = new LicenseImpl();
    //        license.setName(JsonUtil.stringProperty(node, LicenseConstant.PROP_NAME));
    //        license.setUrl(JsonUtil.stringProperty(node, LicenseConstant.PROP_URL));
    //        ExtensionReader.readExtensions(node, license);
    //        return license;
    //    }
}
