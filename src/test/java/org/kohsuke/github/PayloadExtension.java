package org.kohsuke.github;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Function;

import javax.annotation.Nonnull;

/**
 * JUnit 5 extension replacement for the former JUnit 4 {@link PayloadRule}.
 * It discovers the {@link Payload} annotation on the test method (or uses the method name)
 * and provides helpers to read the corresponding payload resource.
 */
public class PayloadExtension implements BeforeEachCallback {

    private String resourceName;
    private Class<?> testClass;
    private final String type;

    public PayloadExtension(String type) {
        this.type = type;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Payload payload = context.getRequiredTestMethod().getAnnotation(Payload.class);
        this.resourceName = (payload == null) ? context.getRequiredTestMethod().getName() : payload.value();
        this.testClass = context.getRequiredTestClass();
    }

    public byte[] asBytes() throws IOException {
        try (InputStream input = asInputStream()) {
            return input.readAllBytes();
        }
    }

    public InputStream asInputStream() throws FileNotFoundException {
        String name = resourceName.startsWith("/") ? resourceName + type : testClass.getSimpleName() + "/" + resourceName + type;
        InputStream stream = testClass.getResourceAsStream(name);
        if (stream == null) {
            throw new FileNotFoundException(String.format("Resource %s from class %s", name, testClass));
        }
        return stream;
    }

    public Reader asReader() throws FileNotFoundException {
        return new InputStreamReader(asInputStream(), Charset.defaultCharset());
    }

    public Reader asReader(Charset encoding) throws FileNotFoundException {
        return new InputStreamReader(asInputStream(), encoding);
    }

    public Reader asReader(@Nonnull Function<String, String> transformer) throws IOException {
        String payloadString = asString();
        return new StringReader(transformer.apply(payloadString));
    }

    public Reader asReader(String encoding) throws IOException {
        return new InputStreamReader(asInputStream(), encoding);
    }

    public String asString() throws IOException {
        return new String(asBytes(), Charset.defaultCharset());
    }

    public String asString(Charset encoding) throws IOException {
        return new String(asBytes(), encoding);
    }

    public String asString(String encoding) throws IOException {
        return new String(asBytes(), encoding);
    }
}
