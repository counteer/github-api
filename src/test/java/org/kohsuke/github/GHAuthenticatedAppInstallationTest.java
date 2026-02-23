package org.kohsuke.github;

import org.junit.Test;
import org.kohsuke.github.authorization.AppInstallationAuthorizationProvider;
import org.kohsuke.github.authorization.AuthorizationProvider;
import org.kohsuke.github.authorization.ImmutableAuthorizationProvider;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

// TODO: Auto-generated Javadoc
/**
 * The Class GHAuthenticatedAppInstallationTest.
 */
public class GHAuthenticatedAppInstallationTest extends AbstractGHAppInstallationTest {

    private static final class TestAppInstallationAuthorizationProvider extends AppInstallationAuthorizationProvider {
        TestAppInstallationAuthorizationProvider(AppInstallationProvider appInstallationProvider,
                                                AuthorizationProvider authProvider) {
            super(appInstallationProvider, authProvider);
        }
    }

    /**
     * Create default GHAuthenticatedAppInstallationTest instance
     */
    public GHAuthenticatedAppInstallationTest() {
    }

    /**
     * Test list repositories two repos.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListRepositoriesTwoRepos() throws IOException {
        GHAuthenticatedAppInstallation appInstallation = gitHub.getInstallation();

        List<GHRepository> repositories = appInstallation.listRepositories().toList();

        assertThat(repositories.size(), equalTo(2));
        assertThat(repositories.stream().map(GHRepository::getName).toArray(),
                arrayContainingInAnyOrder("empty", "test-readme"));
    }

    /**
     * Gets the git hub builder.
     *
     * @return the git hub builder
     */
    @Override
    protected GitHubBuilder getGitHubBuilder() {
        AuthorizationProvider jwtProvider = ImmutableAuthorizationProvider.fromJwtToken("dummy");
        AppInstallationAuthorizationProvider provider =
                new TestAppInstallationAuthorizationProvider(
                        app -> app.getInstallationByOrganization("hub4j-test-org"),
                        jwtProvider);
        return super.getGitHubBuilder().withAuthorizationProvider(provider);
    }

}
