package io.smallrye.asyncapi.runtime.io.schema;

import org.jboss.jandex.ArrayType;
import org.jboss.jandex.ClassType;
import org.jboss.jandex.Type;

import io.apicurio.datamodels.asyncapi.models.AaiSchema;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Schema;
import io.apicurio.datamodels.core.models.common.Schema;
import io.smallrye.asyncapi.runtime.scanner.AnnotationScannerContext;
import io.smallrye.asyncapi.spec.annotations.enums.SchemaType;

public class SchemaFactory {

    /**
     * Converts a Jandex type to a {@link Schema} model.
     *
     * @param context scanning context
     * @param type the implementation type of the item to scan
     * @return Schema model
     */
    public static Schema typeToSchema(final AnnotationScannerContext context, Type type) {
        AaiSchema schema = null;

        // TODO not currently supporting JAXB xml types
        //        if (TypeUtil.isWrappedType(type)) {
        //            // Recurse using the optional's type
        //            return typeToSchema(context, TypeUtil.unwrapType(type), extensions);
        //        } else
        // TODO not currently supporting custom scanners
        //        if (CurrentScannerInfo.isWrapperType(type)) {
        //            // Recurse using the wrapped type
        //            return typeToSchema(context, CurrentScannerInfo.getCurrentAnnotationScanner().unwrapType(type), extensions);
        //        } else
        if (type.kind() == Type.Kind.ARRAY) {
            schema = new Aai20Schema();
            schema.type = SchemaType.ARRAY.toString();
            ArrayType array = type.asArrayType();
            int dimensions = array.dimensions();
            Type componentType = array.component();

            if (dimensions > 1) {
                // Recurse using a new array type with dimensions decremented
                schema.items = typeToSchema(context, ArrayType.create(componentType, dimensions - 1));
            } else {
                // Recurse using the type of the array elements
                schema.items = typeToSchema(context, componentType);
            }
        } else if (type.kind() == Type.Kind.CLASS) {
            schema = introspectClassToSchema(context, type.asClassType(), true);
        }
        // TODO primitives and else
        //        } else if (type.kind() == Type.Kind.PRIMITIVE) {
        //            schema = OpenApiDataObjectScanner.process(type.asPrimitiveType());
        //        } else {
        //            schema = otherTypeToSchema(context, type, extensions);
        //        }

        return schema;
    }

    /**
     * Introspect the given class type to generate a Schema model. The boolean indicates
     * whether this class type should be turned into a reference.
     *
     * @param context scanning context
     * @param ctype
     * @param schemaReferenceSupported
     */
    private static AaiSchema introspectClassToSchema(final AnnotationScannerContext context, ClassType ctype,
            boolean schemaReferenceSupported) {

        //        if (CurrentScannerInfo.isScannerInternalResponse(ctype)) {
        //            return null;
        //        }

        //        SchemaRegistry schemaRegistry = SchemaRegistry.currentInstance();

        return new Aai20Schema();
        //        if (schemaReferenceSupported && schemaRegistry.hasSchema(ctype)) {
        //            return schemaRegistry.lookupRef(ctype);
        //        } else if (!schemaReferenceSupported && schemaRegistry != null && schemaRegistry.hasSchema(ctype)) {
        //            // Clone the schema from the registry using mergeObjects
        //            return MergeUtil.mergeObjects(new SchemaImpl(), schemaRegistry.lookupSchema(ctype));
        //        } else if (context.getScanStack().contains(ctype)) {
        //            // Protect against stack overflow when the type is in the process of being scanned.
        //            return SchemaRegistry.registerReference(ctype, null, new SchemaImpl());
        //        } else {
        //            Schema schema = OpenApiDataObjectScanner.process(context, ctype);
        //
        //            if (schemaReferenceSupported) {
        //                return schemaRegistration(context, ctype, schema);
        //            } else {
        //                return schema;
        //            }
        //        }
    }
}
