package io.smallrye.asyncapi.runtime.io.info;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20Info;
import io.apicurio.datamodels.core.models.common.Info;
import io.smallrye.asyncapi.runtime.io.contact.ContactReader;
import io.smallrye.asyncapi.runtime.io.license.LicenseReader;
import io.smallrye.asyncapi.runtime.util.JandexUtil;

/**
 * This reads the Info from annotations or json
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#infoObject">infoObject</a>
 */
public class InfoReader {

    public static final String PROP_TITLE = "title";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_TERMS_OF_SERVICE = "termsOfService";

    private InfoReader() {
    }

    /**
     * Annotation to Info
     *
     * @param annotationValue the {@literal @}Info annotation
     * @return Info model
     */
    public static Info readInfo(final AnnotationValue annotationValue) {
        if (annotationValue == null) {
            return null;
        }
        //        IoLogging.logger.annotation("@Info");
        AnnotationInstance nested = annotationValue.asNested();

        Info info = new Aai20Info();

        info.title = JandexUtil.stringValue(nested, PROP_TITLE);
        info.description = JandexUtil.stringValue(nested, PROP_DESCRIPTION);
        info.termsOfService = JandexUtil.stringValue(nested, PROP_TERMS_OF_SERVICE);
        info.contact = ContactReader.readContact(nested.value(InfoConstant.PROP_CONTACT));
        info.license = LicenseReader.readLicense(nested.value(InfoConstant.PROP_LICENSE));
        info.version = JandexUtil.stringValue(nested, "version");

        return info;
    }

    // TODO if we need to read from json?
    //    /**
    //     * Reads an {@link Info} OpenAPI node.
    //     *
    //     * @param node the json node
    //     * @return Info model
    //     */
    //    public static Info readInfo(final JsonNode node) {
    //        if (node == null) {
    //            return null;
    //        }
    //        IoLogging.logger.singleJsonNode("Info");
    //
    //        Info info = new InfoImpl();
    //        info.setTitle(JsonUtil.stringProperty(node, InfoConstant.PROP_TITLE));
    //        info.setDescription(JsonUtil.stringProperty(node, InfoConstant.PROP_DESCRIPTION));
    //        info.setTermsOfService(JsonUtil.stringProperty(node, InfoConstant.PROP_TERMS_OF_SERVICE));
    //        info.setContact(ContactReader.readContact(node.get(InfoConstant.PROP_CONTACT)));
    //        info.setLicense(LicenseReader.readLicense(node.get(InfoConstant.PROP_LICENSE)));
    //        info.setVersion(JsonUtil.stringProperty(node, InfoConstant.PROP_VERSION));
    //        ExtensionReader.readExtensions(node, info);
    //        return info;
    //    }
}
