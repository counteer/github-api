package org.kohsuke.github;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Tests for the GitHub App Api Test
 *
 * @author Daniel Baur
 */
public class GHAppExtendedTest extends AbstractGitHubWireMockTest {

    private static final String APP_SLUG = "ghapi-test-app-4";

    /**
     * Create default GHAppExtendedTest instance
     */
    public GHAppExtendedTest() {
    }

    /**
     * Tests App creation via the App Manifest Flow.
     *
     * The used code defined below was only valid for a short time, meaning that you can not replay the test against the
     * GitHub API. Use the stored wire snapshot for executing those tests.
     *
     * @throws IOException
     *             An IOException has occurred.
     */
    @Test
    public void createAppByManifestFlowTest() throws IOException {
        snapshotNotAllowed();
        GHAppFromManifest appFromManifest = gitHub.createAppFromManifest("46fbe5453b245dee21b96753f80eace209a3cf01");

        assertThat(appFromManifest.getClientId(), equalTo("Iv1.1c63d0b87c03d42e"));
        assertThat(appFromManifest.getWebhookSecret(), equalTo("f4dafa9b05d8248d81f65f0e6cb108cb8bb76a0c"));
        assertThat(appFromManifest.getClientSecret(), equalTo("f4b60603e85b3965492b393bca0809a914dcdf18"));
        // Do not assert on PEM headers to avoid triggering secret scanners. The WireMock snapshot uses a safe placeholder.
        assertThat(appFromManifest.getPem(), equalTo("REDACTED_TEST_KEY"));
    }

    /**
     * Gets the GitHub App by its slug.
     *
     * @throws IOException
     *             An IOException has occurred.
     */
    @Test
    public void getAppBySlugTest() throws IOException {
        GHApp app = gitHub.getApp(APP_SLUG);

        assertThat(app.getId(), is((long) 330762));
        assertThat(app.getSlug(), equalTo(APP_SLUG));
        assertThat(app.getName(), equalTo("GHApi Test app 4"));
        assertThat(app.getExternalUrl(), equalTo("https://github.com/organizations/hub4j-test-org"));
        assertThat(app.getHtmlUrl().toString(), equalTo("https://github.com/apps/ghapi-test-app-4"));
        assertThat(app.getDescription(), equalTo("An app to test the GitHub getApp(slug) method."));
    }

}

