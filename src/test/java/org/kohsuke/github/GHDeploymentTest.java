package org.kohsuke.github;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.Matchers.*;

// TODO: Auto-generated Javadoc
/**
 * The Class GHDeploymentTest.
 *
 * @author Martin van Zijl
 */
public class GHDeploymentTest extends AbstractGitHubWireMockTest {

    /**
     * Create default GHDeploymentTest instance
     */
    public GHDeploymentTest() {
    }

    /**
     * Test get deployment by id object payload.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetDeploymentByIdObjectPayload() throws IOException {
        GHRepository repo = getRepository();
        GHDeployment deployment = repo.getDeployment(178653229);

        assertThat(deployment, notNullValue());
        assertThat(deployment.getId(), is(178653229L));
        assertThat(deployment.getEnvironment(), is("production"));
        assertThat(deployment.getPayloadMap(), aMapWithSize(4));
    }

    /**
     * Test get deployment by id string payload.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetDeploymentByIdStringPayload() throws IOException {
        final GHRepository repo = getRepository();
        final GHDeployment deployment = repo.getDeployment(178653229);
        assertThat(deployment, notNullValue());
        assertThat(deployment.getId(), equalTo(178653229L));
        assertThat(deployment.getEnvironment(), equalTo("production"));
        assertThat(deployment.getPayload(), equalTo("custom"));
        assertThat(deployment.getPayloadObject(), equalTo("custom"));
        assertThat(deployment.getRef(), equalTo("main"));
        assertThat(deployment.getSha(), equalTo("3a09d2de4a9a1322a0ba2c3e2f54a919ca8fe353"));
        assertThat(deployment.getTask(), equalTo("deploy"));
        assertThat(deployment.getOriginalEnvironment(), equalTo("production"));
        assertThat(deployment.isProductionEnvironment(), equalTo(false));
        assertThat(deployment.isTransientEnvironment(), equalTo(true));
        assertThat(deployment.getStatusesUrl().toString(),
                endsWith("/repos/hub4j-test-org/github-api/deployments/178653229/statuses"));
        assertThat(deployment.getRepositoryUrl().toString(), endsWith("/repos/hub4j-test-org/github-api"));
    }

    private GHRepository getRepository(final GitHub gitHub) throws IOException {
        return gitHub.getOrganization("hub4j-test-org").getRepository("github-api");
    }

    /**
     * Gets the repository.
     *
     * @return the repository
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected GHRepository getRepository() throws IOException {
        return getRepository(gitHub);
    }
}
