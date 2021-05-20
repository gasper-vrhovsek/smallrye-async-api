package io.smallrye.asyncapi.runtime.io.contact;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20Contact;
import io.apicurio.datamodels.core.models.common.Contact;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

/**
 * This reads the Contact from annotations or json
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#contactObject">contactObject</a>
 */
public class ContactReader {

    private ContactReader() {
    }

    /**
     * Reads an Contact annotation.
     *
     * @param annotationValue the {@literal @}Contact annotation
     * @return Contact model
     */
    public static Contact readContact(final AnnotationValue annotationValue) {
        if (annotationValue == null) {
            return null;
        }
        //        IoLogging.logger.singleAnnotation("@Contact");
        AnnotationInstance nested = annotationValue.asNested();
        Contact contact = new Aai20Contact();

        contact.name = JandexUtil.stringValue(nested, ContactConstant.PROP_NAME);
        contact.url = JandexUtil.stringValue(nested, ContactConstant.PROP_URL);
        contact.email = JandexUtil.stringValue(nested, ContactConstant.PROP_EMAIL);

        return contact;
    }
    //
    //    /**
    //     * Reads an {@link Contact} OpenAPI node.
    //     *
    //     * @param node the json node
    //     * @return Contact model
    //     */
    //    public static Contact readContact(final JsonNode node) {
    //        if (node == null) {
    //            return null;
    //        }
    //        IoLogging.logger.singleJsonNode("Contact");
    //        Contact contact = new ContactImpl();
    //        contact.setName(JsonUtil.stringProperty(node, ContactConstant.PROP_NAME));
    //        contact.setUrl(JsonUtil.stringProperty(node, ContactConstant.PROP_URL));
    //        contact.setEmail(JsonUtil.stringProperty(node, ContactConstant.PROP_EMAIL));
    //        ExtensionReader.readExtensions(node, contact);
    //        return contact;
    //    }
}
