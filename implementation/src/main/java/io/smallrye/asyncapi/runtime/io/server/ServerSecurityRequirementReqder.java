package io.smallrye.asyncapi.runtime.io.server;

import io.apicurio.datamodels.asyncapi.models.AaiSecurityRequirement;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20SecurityRequirement;
import io.smallrye.asyncapi.runtime.scanner.AnnotationScannerContext;
import io.smallrye.asyncapi.runtime.util.JandexUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerSecurityRequirementReqder {
    private ServerSecurityRequirementReqder() {
    }

    public static Optional<List<AaiSecurityRequirement>> readSecurityRequirements(final AnnotationValue annotationValue) {
        if (annotationValue != null) {
            AnnotationInstance[] nestedArray = annotationValue.asNestedArray();
            List<AaiSecurityRequirement> securityRequirements = new ArrayList<>();

            for (AnnotationInstance secReqAnno : nestedArray) {
                if (!JandexUtil.isRef(secReqAnno)) {
                    securityRequirements.add(readSecurityRequirement(secReqAnno));
                }
            }
            return Optional.of(securityRequirements);
        }
        return Optional.empty();
    }

    public static AaiSecurityRequirement readSecurityRequirement(final AnnotationInstance annotationValue) {
        if (annotationValue != null) {
            AaiSecurityRequirement securityRequirement = new Aai20SecurityRequirement();
            // TODO read "custom" values from the annotation

            return securityRequirement;
        }
        return null;

    }
}
