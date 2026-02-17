package org.kohsuke.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.recording.RecordSpecBuilder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * JUnit 5 Jupiter extension that wraps {@link GitHubWireMockRule} and reproduces the
 * per-test lifecycle previously provided by the JUnit 4 @Rule.
 */
public class GitHubWireMockExtension implements BeforeEachCallback, AfterEachCallback {

    private final GitHubWireMockRule rule;

    public GitHubWireMockExtension(WireMockConfiguration options) {
        this(() -> options);
    }

    public GitHubWireMockExtension(Supplier<WireMockConfiguration> optionsSupplier) {
        this.rule = new GitHubWireMockRule(optionsSupplier.get());
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        // Initialize servers and set up per-test state using the test method name
        String methodName = context.getRequiredTestMethod().getName();
        this.rule.startForMethod(methodName);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        this.rule.stopForMethod();
    }

    // Delegate API used by tests
    public WireMockServer apiServer() {
        return rule.apiServer();
    }

    public int getRequestCount() {
        return rule.getRequestCount();
    }

    public String mapToMockGitHub(String url) {
        return rule.mapToMockGitHub(url);
    }

    public boolean isUseProxy() {
        return rule.isUseProxy();
    }

    public void customizeRecordSpec(Consumer<RecordSpecBuilder> customizeRecordSpec) {
        rule.customizeRecordSpec(customizeRecordSpec);
    }

    public boolean isTestWithOrg() {
        return rule.isTestWithOrg();
    }

    public boolean isTakeSnapshot() {
        return rule.isTakeSnapshot();
    }

    public String getMethodName() {
        return rule.getMethodName();
    }
}
