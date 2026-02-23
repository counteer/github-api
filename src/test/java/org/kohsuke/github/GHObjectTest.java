package org.kohsuke.github;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: Auto-generated Javadoc
/**
 * The Class GHObjectTest.
 */
public class GHObjectTest extends org.kohsuke.github.AbstractGitHubWireMockTest {

    /**
     * Create default GHObjectTest instance
     */
    public GHObjectTest() {
    }

    /**
     * Test to string.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void test_toString() throws Exception {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        assertTrue(org.toString().contains("login=hub4j-test-org"));
        assertTrue(org.toString().contains("location=<null>"));
        assertTrue(org.toString().contains("blog=<null>"));
        assertTrue(org.toString().contains("email=<null>"));
        assertTrue(org.toString().contains("bio=<null>"));
        assertTrue(org.toString().contains("name=<null>"));
        assertTrue(org.toString().contains("company=<null>"));
        assertTrue(org.toString().contains("type=Organization"));
        assertTrue(org.toString().contains("followers=0"));
        assertTrue(org.toString().contains("hireable=false"));

        // getResponseHeaderFields is deprecated but we should not break it.
        assertNotNull(org.getResponseHeaderFields());
        assertEquals("private, max-age=60, s-maxage=60", org.getResponseHeaderFields().get("Cache-Control").get(0));
    }
}
