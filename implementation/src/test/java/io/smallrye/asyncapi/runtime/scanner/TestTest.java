package io.smallrye.asyncapi.runtime.scanner;

import io.apicurio.datamodels.asyncapi.v2.models.Aai20Document;
import org.jboss.jandex.Index;
import org.junit.Test;

import java.io.IOException;

public class TestTest extends IndexScannerTestBase {

    @Test
    public void test() throws ClassNotFoundException, IOException {
        String className = getClass().getPackage().getName() + ".test1.EventApp";
        Index testIndex = indexOf(Class.forName(className));

        //        Index index = indexOf(Class.forName(getClass().getPackage().getName() + ".sorttest1.package-info"),
        //                Class.forName(getClass().getPackage().getName() + ".sorttest2.package-info"));

        AsyncApiAnnotationScanner scanner = new AsyncApiAnnotationScanner(emptyConfig(), testIndex);
        String[] expectedNames = { "123", "ABC", "DEF", "GHI", "KLM", "XYZ" };

        Aai20Document result = scanner.scan();
        printToConsole(result);

        //        assertArrayEquals(expectedNames, result.getComponents().getCallbacks().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getExamples().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getHeaders().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getLinks().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getParameters().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getRequestBodies().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getResponses().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getSchemas().keySet().toArray());
        //        assertArrayEquals(expectedNames, result.getComponents().getSecuritySchemes().keySet().toArray());
    }
}
