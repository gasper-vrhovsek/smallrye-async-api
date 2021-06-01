package io.smallrye.asyncapi.runtime.io.channel.operation;

import org.jboss.jandex.AnnotationInstance;

import io.apicurio.datamodels.asyncapi.models.AaiOperation;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Operation;
import io.smallrye.asyncapi.runtime.io.message.MessageReader;
import io.smallrye.asyncapi.runtime.util.JandexUtil;

public class ApiOperationReader {

    private ApiOperationReader() {
    }

    public static AaiOperation readOperation(AnnotationInstance annotationInstance, String opType) {
        if (annotationInstance != null) {
            AaiOperation operation = new Aai20Operation(opType);
            operation.operationId = JandexUtil.stringValue(annotationInstance, ApiOperationConstant.PROP_OPERATION_ID);
            operation.summary = JandexUtil.stringValue(annotationInstance, ApiOperationConstant.PROP_SUMMARY);

            operation.message = MessageReader
                    .readMessage(annotationInstance.value(ApiOperationConstant.PROP_MESSAGE).asNested());
            return operation;
        }
        return null;
    }
}
