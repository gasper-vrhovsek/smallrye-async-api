package io.smallrye.asyncapi.runtime.scanner;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.apicurio.datamodels.asyncapi.models.AaiSchema;
import io.smallrye.asyncapi.runtime.io.JsonUtil;
import io.smallrye.asyncapi.runtime.io.schema.SchemaReader;

public class SchemaReaderTest extends IndexScannerTestBase {

    @Test
    public void test() throws ClassNotFoundException, IOException {

        String schemaString = "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"definitions\":{\"ConsentStatus\":{\"type\":\"string\",\"enum\":[\"inProgress\",\"userActionRequired\",\"completed\",\"declined\",\"expired\"]}},\"type\":\"object\",\"properties\":{\"acrcId\":{\"type\":\"string\"},\"clientId\":{\"type\":\"string\"},\"consentId\":{\"type\":\"string\"},\"gidUuid\":{\"type\":\"string\"},\"requestedAt\":{\"type\":\"string\"},\"status\":{\"$ref\":\"#/definitions/ConsentStatus\"}}}";
        String propertiesString = "{\"acrcId\":{\"type\":\"string\"},\"clientId\":{\"type\":\"string\"},\"consentId\":{\"type\":\"string\"},\"gidUuid\":{\"type\":\"string\"},\"requestedAt\":{\"type\":\"string\"},\"status\":{\"$ref\":\"#/definitions/ConsentStatus\"}}";

        ObjectMapper mapper = JsonUtil.MAPPER;
        JsonNode schemaNode = mapper.readTree(schemaString);
        JsonNode propertiesNode = mapper.readTree(propertiesString);

        Optional<Map<String, AaiSchema>> propertiesOpt = SchemaReader.readSchemas(propertiesNode);

        if (propertiesOpt.isPresent()) {
            Map<String, AaiSchema> stringAaiSchemaMap = propertiesOpt.get();
            Assert.assertEquals(6, stringAaiSchemaMap.size());
        }

        AaiSchema aaiSchema = SchemaReader.readSchema(schemaNode);
        Map<String, AaiSchema> properties = aaiSchema.properties;
        Assert.assertEquals(5, properties.size());
    }
}
