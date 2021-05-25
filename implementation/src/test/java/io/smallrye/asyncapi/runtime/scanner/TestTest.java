package io.smallrye.asyncapi.runtime.scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import io.apicurio.datamodels.core.models.common.Schema;
import io.smallrye.asyncapi.api.AsyncApiConfig;
import io.smallrye.asyncapi.runtime.io.schema.SchemaFactory;
import io.smallrye.asyncapi.runtime.scanner.model.TestModel;
import io.smallrye.asyncapi.runtime.scanner.model.User;
import io.smallrye.asyncapi.runtime.scanner.test1.Plane;
import io.smallrye.asyncapi.runtime.scanner.test1.PlaneStandalone;
import io.smallrye.asyncapi.runtime.scanner.test1.SupersonicPlane;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.Type;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestTest extends IndexScannerTestBase {

    @Test
    public void test() throws ClassNotFoundException, IOException {
        String className = getClass().getPackage().getName() + ".test1.EventApp";
        // TODO currently we have to add all classes to the indexOf method, could we just scan a package?
        Index testIndex = indexOf(Plane.class, PlaneStandalone.class, SupersonicPlane.class, Class.forName(className));

        AsyncApiAnnotationScanner scanner = new AsyncApiAnnotationScanner(emptyConfig(), testIndex);
//        String[] expectedNames = { "123", "ABC", "DEF", "GHI", "KLM", "XYZ" };

        Aai20Document result = scanner.scan();
        printToConsole(result);
    }

    @Test
    public void schemasSchemasSchemas() throws ClassNotFoundException {
        Index testIndex = indexOf(TestModel.class, User.class);
        AsyncApiConfig config = emptyConfig();
        FilteredIndexView filteredIndexView = new FilteredIndexView(testIndex, config);
        AnnotationScannerContext annotationScannerContext = new AnnotationScannerContext(config, filteredIndexView, new Aai20Document());

        Schema schema = SchemaFactory.typeToSchema(annotationScannerContext,
                Type.create(DotName.createSimple(TestModel.class.getName()), Type.Kind.CLASS));

        System.out.println(schema.toString());
    }

    @Test
    public void jsonSchemaGenerator() {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7, OptionPreset.PLAIN_JSON)
                .with(Option.DEFINITIONS_FOR_ALL_OBJECTS);
        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);


        JsonNode jsonSchema = generator.generateSchema(TestModel.class);
        JsonNode arrayListSchema = generator.generateSchema(List.class, User.class);

        System.out.println(jsonSchema.toString());
        System.out.println(arrayListSchema.toString());
    }
}
