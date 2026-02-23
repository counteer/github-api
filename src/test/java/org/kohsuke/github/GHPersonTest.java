package org.kohsuke.github;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// TODO: Auto-generated Javadoc
/**
 * The Class GHPersonTest.
 *
 * @author Martin van Zijl
 */
public class GHPersonTest extends AbstractGitHubWireMockTest {

    /**
     * Create default GHPersonTest instance
     */
    public GHPersonTest() {
    }

    /**
     * Test fields for organization.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFieldsForOrganization() throws Exception {
        GHRepository repo = getRepository();
        GHUser owner = repo.getOwner();
        assertEquals("Organization", owner.getType());
        assertNotNull(owner.isSiteAdmin());
    }

    /**
     * Test fields for user.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFieldsForUser() throws Exception {
        GHUser user = gitHub.getUser("kohsuke2");
        assertEquals("User", user.getType());
        assertNotNull(user.isSiteAdmin());
    }

    private GHRepository getRepository(GitHub gitHub) throws IOException {
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
