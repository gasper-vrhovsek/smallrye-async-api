package io.smallrye.asyncapi.runtime.io.server;

import io.apicurio.datamodels.asyncapi.models.AaiServer;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Server;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Reading the Server annotation and json node
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#serverObject">serverObject</a>
 */
public class ServerReader {

    private ServerReader() {
    }

    /**
     * Reads any Server annotations.The annotation value is an array of Server annotations.
     *
     * @param annotationValue an Array of {@literal @}Server annotations
     * @return a List of Server models
     */
    public static Optional<Map<String, AaiServer>> readServers(final AnnotationValue annotationValue) {
        if (annotationValue != null) {
            //            IoLogging.logger.annotationsArray("@Server");
            AnnotationInstance[] nestedArray = annotationValue.asNestedArray();
            Map<String, AaiServer> serverMap = new HashMap<>();
            for (AnnotationInstance serverAnno : nestedArray) {
                AaiServer server = readServer(serverAnno);
                serverMap.put(server.getName(), server);
            }
            return Optional.of(serverMap);
        }
        return Optional.empty();
    }

    //    /**
    //     * Reads a list of {@link Server} OpenAPI nodes.
    //     *
    //     * @param node the json array
    //     * @return a List of Server models
    //     */
    //    public static Optional<List<Server>> readServers(final JsonNode node) {
    //        if (node != null && node.isArray()) {
    //            IoLogging.logger.jsonArray("Server");
    //            ArrayNode nodes = (ArrayNode) node;
    //            List<Server> rval = new ArrayList<>(nodes.size());
    //            for (JsonNode serverNode : nodes) {
    //                rval.add(readServer(serverNode));
    //            }
    //            return Optional.of(rval);
    //        }
    //        return Optional.empty();
    //    }

    /**
     * Reads a single Server annotation.
     *
     * @param annotationValue the {@literal @}Server annotation
     * @return a Server model
     */
    public static AaiServer readServer(final AnnotationValue annotationValue) {
        if (annotationValue != null) {
            return readServer(annotationValue.asNested());
        }
        return null;
    }

    /**
     * Reads a single Server annotation.
     *
     * @param annotationInstance the {@literal @}Server annotations instance
     * @return Server model
     */
    public static AaiServer readServer(final AnnotationInstance annotationInstance) {
        if (annotationInstance != null) {
            //            IoLogging.logger.singleAnnotation("@Server");
            AaiServer server = new Aai20Server(JandexUtil.stringValue(annotationInstance, "name"));
            server.url = JandexUtil.stringValue(annotationInstance, "url");
            server.protocol = JandexUtil.stringValue(annotationInstance, "protocol");
            server.protocolVersion = JandexUtil.stringValue(annotationInstance, "protocolVersion");
            server.description = JandexUtil.stringValue(annotationInstance, "description");
            server.variables = null;// TODO ServerVariablesReader
            server.security = null;// TODO ServerSecurityRequirementsReader
            server.bindings = null;// TODO ServerBindingsReader
            return server;
        }
        return null;
    }
    //
    //    /**
    //     * Reads a list of {@link Server} OpenAPI nodes.
    //     *
    //     * @param node the json array
    //     * @return a List of Server models
    //     */
    //    public static Server readServer(final JsonNode node) {
    //        if (node != null && node.isObject()) {
    //            IoLogging.logger.singleJsonNode("Server");
    //            Server server = new ServerImpl();
    //            server.setUrl(JsonUtil.stringProperty(node, ServerConstant.PROP_URL));
    //            server.setDescription(JsonUtil.stringProperty(node, ServerConstant.PROP_DESCRIPTION));
    //            server.setVariables(ServerVariableReader.readServerVariables(node.get(ServerConstant.PROP_VARIABLES)));
    //            ExtensionReader.readExtensions(node, server);
    //            return server;
    //        }
    //        return null;
    //    }
    //
    //    // helper methods for scanners
    //    public static List<AnnotationInstance> getServerAnnotations(final AnnotationTarget target) {
    //        return JandexUtil.getRepeatableAnnotation(target,
    //                ServerConstant.DOTNAME_SERVER,
    //                ServerConstant.DOTNAME_SERVERS);
    //    }

}
