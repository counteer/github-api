package org.kohsuke.github.connector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Test-only helpers for GitHub connector tests.
 */
public final class GitHubConnectorTestHelper {

    private GitHubConnectorTestHelper() {
        // no instances
    }

    /**
     * Empty request for response testing.
     */
    public static final GitHubConnectorRequest EMPTY_REQUEST = new GitHubConnectorRequest() {
        @NotNull
        @Override
        public Map<String, List<String>> allHeaders() {
            return Map.of();
        }

        @Nullable
        @Override
        public InputStream body() {
            return null;
        }

        @Nullable
        @Override
        public String contentType() {
            return null;
        }

        @Override
        public boolean hasBody() {
            return false;
        }

        @Nullable
        @Override
        public String header(String name) {
            return null;
        }

        @NotNull
        @Override
        public String method() {
            return "GET";
        }

        @NotNull
        @Override
        public URL url() {
            try {
                return new URL("https://example.invalid/");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    };
}
