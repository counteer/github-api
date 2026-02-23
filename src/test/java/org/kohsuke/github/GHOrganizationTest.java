package org.kohsuke.github;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHOrganization.Permission;
import org.kohsuke.github.GHOrganization.RepositoryRole;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.kohsuke.github.ExternalGroupsTestingSupport.*;
import static org.kohsuke.github.ExternalGroupsTestingSupport.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO: Auto-generated Javadoc

/**
 * The Class GHOrganizationTest.
 */
public class GHOrganizationTest extends AbstractGitHubWireMockTest {

    /** The Constant GITHUB_API_TEMPLATE_TEST. */
    public static final String GITHUB_API_TEMPLATE_TEST = "github-api-template-test";

    /** The Constant GITHUB_API_TEST. */
    public static final String GITHUB_API_TEST = "github-api-test";

    /** The Constant TEAM_NAME_CREATE. */
    public static final String TEAM_NAME_CREATE = "create-team-test";

    /**
     * Create default GHOrganizationTest instance
     */
    public GHOrganizationTest() {
    }

    /**
     * Clean up team.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @BeforeEach
    @AfterEach
    public void cleanUpTeam() throws IOException {
        // Cleanup is only needed when proxying
        if (!mockGitHub.isUseProxy()) {
            return;
        }

        GHTeam team = getNonRecordingGitHub().getOrganization(GITHUB_API_TEST_ORG).getTeamByName(TEAM_NAME_CREATE);
        if (team != null) {
            team.delete();
        }

        getNonRecordingGitHub().getOrganization(GITHUB_API_TEST_ORG).enableOrganizationProjects(true);
    }

    /**
     * Test are organization projects enabled.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testAreOrganizationProjectsEnabled() throws IOException {
        // Arrange
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        // Act
        boolean result = org.areOrganizationProjectsEnabled();

        // Assert
        assertTrue(result);
    }

    /**
     * Test create all args team.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateAllArgsTeam() throws IOException {
        String REPO_NAME = "github-api";
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        GHTeam team = org.createTeam(TEAM_NAME_CREATE)
                .description("Team description")
                .maintainers("bitwiseman")
                .repositories(REPO_NAME)
                .privacy(GHTeam.Privacy.CLOSED)
                .parentTeamId(3617900)
                .create();
        assertEquals("Team description", team.getDescription());
        assertEquals(GHTeam.Privacy.CLOSED, team.getPrivacy());
    }

    /**
     * Test create repository.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepository() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repository = org.createRepository(GITHUB_API_TEST)
                .description("a test repository used to test kohsuke's github-api")
                .homepage("http://github-api.kohsuke.org/")
                .team(org.getTeamByName("Core Developers"))
                .private_(false)
                .create();
        assertNotNull(repository);
    }

    /**
     * Test create repository with template repository null.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepositoryFromTemplateRepositoryNull() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        assertThrows(NullPointerException.class, () -> {
            org.createRepository(GITHUB_API_TEST).fromTemplateRepository(null).owner(GITHUB_API_TEST_ORG).create();
        });
    }

    /**
     * Test create repository when repository template is not a template.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepositoryWhenRepositoryTemplateIsNotATemplate() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository templateRepository = org.getRepository(GITHUB_API_TEMPLATE_TEST);

        assertThrows(IllegalArgumentException.class, () -> {
            org.createRepository(GITHUB_API_TEST)
                    .fromTemplateRepository(templateRepository)
                    .owner(GITHUB_API_TEST_ORG)
                    .create();
        });
    }

    /**
     * Test create repository with auto initialization.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepositoryWithAutoInitialization() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repository = org.createRepository(GITHUB_API_TEST)
                .description("a test repository used to test kohsuke's github-api")
                .homepage("http://github-api.kohsuke.org/")
                .team(org.getTeamByName("Core Developers"))
                .autoInit(true)
                .create();
        assertNotNull(repository);
        assertNotNull(repository.getReadme());
    }

    /**
     * Test create repository with parameter is template.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepositoryWithParameterIsTemplate() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEMPLATE_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHTeam team = org.getTeamByName("Core Developers");

        int requestCount = mockGitHub.getRequestCount();
        GHRepository repository = org.createRepository(GITHUB_API_TEMPLATE_TEST)
                .description("a test template repository used to test kohsuke's github-api")
                .homepage("http://github-api.kohsuke.org/")
                .team(team)
                .autoInit(true)
                .isTemplate(true)
                .create();
        assertNotNull(repository);
        assertEquals(requestCount + 1, mockGitHub.getRequestCount());

        assertNotNull(repository.getReadme());
        assertEquals(requestCount + 2, mockGitHub.getRequestCount());

        // isTemplate() does not call populate() from create
        assertTrue(repository.isTemplate());
        assertEquals(requestCount + 2, mockGitHub.getRequestCount());

        repository = org.getRepository(GITHUB_API_TEMPLATE_TEST);

        // first isTemplate() does not call populate()
        assertTrue(repository.isTemplate());
        assertEquals(requestCount + 3, mockGitHub.getRequestCount());

        // second isTemplate() does not call populate()
        assertTrue(repository.isTemplate());
        assertEquals(requestCount + 3, mockGitHub.getRequestCount());
    }

    /**
     * Test create repository with template.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepositoryWithTemplate() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repository = org.createRepository(GITHUB_API_TEST)
                .fromTemplateRepository(GITHUB_API_TEST_ORG, GITHUB_API_TEMPLATE_TEST)
                .owner(GITHUB_API_TEST_ORG)
                .create();

        assertNotNull(repository);
        assertNotNull(repository.getReadme());

    }

    /**
     * Test create repository with template.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateRepositoryWithTemplateAndGHRepository() throws IOException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository templateRepository = org.getRepository(GITHUB_API_TEMPLATE_TEST);

        GHRepository repository = org.createRepository(GITHUB_API_TEST)
                .fromTemplateRepository(templateRepository)
                .owner(GITHUB_API_TEST_ORG)
                .create();

        assertNotNull(repository);
        assertNotNull(repository.getReadme());

    }

    /**
     * Test create a repository from a template with all branches included
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             Signals that Thread.sleep() was interrupted
     */

    @Test
    public void testCreateRepositoryWithTemplateAndIncludeAllBranches() throws IOException, InterruptedException {
        cleanupRepository(GITHUB_API_TEST_ORG + '/' + GITHUB_API_TEST);

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository templateRepository = org.getRepository(GITHUB_API_TEMPLATE_TEST);

        GHRepository repository = gitHub.createRepository(GITHUB_API_TEST)
                .fromTemplateRepository(templateRepository)
                .includeAllBranches(true)
                .owner(GITHUB_API_TEST_ORG)
                .create();

        assertNotNull(repository);

        // give it a moment for branches to be created
        Thread.sleep(1500);

        assertEquals(templateRepository.getBranches().keySet(), repository.getBranches().keySet());

    }

    /**
     * Test create team.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateTeam() throws IOException {
        String REPO_NAME = "github-api";
        String DEFAULT_PERMISSION = Permission.PULL.toString().toLowerCase();

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repo = org.getRepository(REPO_NAME);

        // Create team with no permission field. Verify that default permission is pull
        GHTeam team = org.createTeam(TEAM_NAME_CREATE).repositories(repo.getFullName()).create();
        assertTrue(team.getRepositories().containsKey(REPO_NAME));
        assertEquals(DEFAULT_PERMISSION, team.getPermission());
    }

    /**
     * Test create team with null perm.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testCreateTeamWithNullPerm() throws Exception {
        String REPO_NAME = "github-api";

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repo = org.getRepository(REPO_NAME);

        // Create team with access to repository. Check access was granted.
        GHTeam team = org.createTeam(TEAM_NAME_CREATE).create();

        team.add(repo);

        assertEquals(
                Permission.PULL.toString().toLowerCase(),
                repo.getTeams()
                        .stream()
                        .filter(t -> TEAM_NAME_CREATE.equals(t.getName()))
                        .findFirst()
                        .get()
                        .getPermission());
    }

    /**
     * Test create team with repo access.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateTeamWithRepoAccess() throws IOException {
        String REPO_NAME = "github-api";

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repo = org.getRepository(REPO_NAME);

        // Create team with access to repository. Check access was granted.
        GHTeam team = org.createTeam(TEAM_NAME_CREATE)
                .repositories(repo.getFullName())
                .permission(Permission.PUSH)
                .create();
        assertTrue(team.getRepositories().containsKey(REPO_NAME));
        assertEquals(Permission.PUSH.toString().toLowerCase(), team.getPermission());
    }

    /**
     * Test create team with repo perm.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testCreateTeamWithRepoPerm() throws Exception {
        String REPO_NAME = "github-api";

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repo = org.getRepository(REPO_NAME);

        // Create team with access to repository. Check access was granted.
        GHTeam team = org.createTeam(TEAM_NAME_CREATE).create();

        team.add(repo, GHOrganization.RepositoryRole.from(Permission.PUSH));

        assertEquals(
                Permission.PUSH.toString().toLowerCase(),
                repo.getTeams()
                        .stream()
                        .filter(t -> TEAM_NAME_CREATE.equals(t.getName()))
                        .findFirst()
                        .get()
                        .getPermission());

    }

    /**
     * Test create team with repo role.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateTeamWithRepoRole() throws IOException {
        String REPO_NAME = "github-api";

        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHRepository repo = org.getRepository(REPO_NAME);

        // Create team with access to repository. Check access was granted.
        GHTeam team = org.createTeam(TEAM_NAME_CREATE).create();

        RepositoryRole role = RepositoryRole.from(Permission.TRIAGE);
        team.add(repo, role);

        // 'getPermission' does not return triage even though the UI shows that value
        // assertThat(
        // repo.getTeams()
        // .stream()
        // .filter(t -> TEAM_NAME_CREATE.equals(t.getName()))
        // .findFirst()
        // .get()
        // .getPermission(),
        // equalTo(role.toString()));
    }
    /**
     * Test create visible team.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCreateVisibleTeam() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        GHTeam team = org.createTeam(TEAM_NAME_CREATE).privacy(GHTeam.Privacy.CLOSED).create();
        assertEquals(GHTeam.Privacy.CLOSED, team.getPrivacy());
    }

    /**
     * Test enable organization projects.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testEnableOrganizationProjects() throws IOException {
        // Arrange
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        // Act
        org.enableOrganizationProjects(false);

        // Assert
        assertTrue(!org.areOrganizationProjectsEnabled());
    }

    /**
     * Test get external group
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetExternalGroup() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        GHExternalGroup group = org.getExternalGroup(467431L);

        assertTrue(!isExternalGroupSummary().matches(group));

        assertEquals(467431L, group.getId());
        assertEquals("acme-developers", group.getName());
        assertNotNull(group.getUpdatedAt());

        assertNotNull(group.getMembers());
        assertTrue(membersSummary(group).containsAll(List.of(
                "158311279:john-doe_acme:John Doe:john.doe@acme.corp",
                "166731041:jane-doe_acme:Jane Doe:jane.doe@acme.corp")));

        assertNotNull(group.getTeams());
        assertTrue(teamSummary(group).contains("9891173:ACME-DEVELOPERS"));
    }

    /**
     * Test get external group for not enterprise managed organization
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetExternalGroupNotEnterpriseManagedOrganization() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        final GHIOException failure = assertThrows(GHNotExternallyManagedEnterpriseException.class,
                () -> org.getExternalGroup(12345));

        assertEquals("Could not retrieve organization external group", failure.getMessage());
    }

    /**
     * Test get user membership
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetMembership() throws IOException {
        GHOrganization org = gitHub.getOrganization("hub4j-test-org");

        GHMembership membership = org.getMembership("fv316");

        assertNotNull(membership);
        assertEquals(GHMembership.Role.ADMIN, membership.getRole());
        assertEquals(GHMembership.State.ACTIVE, membership.getState());
        assertEquals("fv316", membership.getUser().getLogin());
        assertEquals("hub4j-test-org", membership.getOrganization().login);
    }

    /**
     * Test invite user.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testInviteUser() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);
        GHUser user = gitHub.getUser("martinvanzijl2");

        // First remove the user
        if (org.hasMember(user)) {
            org.remove(user);
        }

        // Then invite the user again
        org.add(user, GHOrganization.Role.MEMBER);

        // Now the user has to accept the invitation
        // Can this be automated?
        // user.acceptInvitationTo(org); // ?

        // Check the invitation has worked.
        // assertTrue(org.hasMember(user));
    }

    /**
     * Test list external groups without pagination for non enterprise managed organization.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListExternalGroupsNotEnterpriseManagedOrganization() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        final GHNotExternallyManagedEnterpriseException failure = assertThrows(
                GHNotExternallyManagedEnterpriseException.class,
                () -> org.listExternalGroups().toList());

        assertEquals("Could not retrieve organization external groups", failure.getMessage());

        final GHError error = failure.getError();

        assertNotNull(error);
        assertEquals(EnterpriseManagedSupport.NOT_PART_OF_EXTERNALLY_MANAGED_ENTERPRISE_ERROR, error.getMessage());
        assertNotNull(error.getDocumentationUrl());
    }

    /**
     * Test list external groups with name filtering.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListExternalGroupsWithFilter() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHExternalGroup> groups = org.listExternalGroups("acme").toList();

        assertNotNull(groups);
        // In case more are added in the future
        assertTrue(groups.size() >= 4);
        assertTrue(groupSummary(groups).containsAll(List.of(
                "467430:acme-asset-owners",
                "467431:acme-developers",
                "467432:acme-product-owners",
                "467433:acme-technical-leads")));

        groups.forEach(group -> assertTrue(isExternalGroupSummary().matches(group)));
    }

    /**
     * Test list external groups with pagination.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListExternalGroupsWithPagination() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHExternalGroup> groups = org.listExternalGroups().withPageSize(2).toList();

        assertNotNull(groups);
        // In case more are added in the future
        assertTrue(groups.size() >= 4);
        assertTrue(groupSummary(groups).containsAll(List.of(
                "467430:acme-asset-owners",
                "467431:acme-developers",
                "467432:acme-product-owners",
                "467433:acme-technical-leads")));

        groups.forEach(group -> assertTrue(isExternalGroupSummary().matches(group)));

        // We are doing one request to get the organization and two to traverse the two pages
        assertTrue(mockGitHub.getRequestCount() >= 3);
    }

    /**
     * Test list external groups without pagination.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListExternalGroupsWithoutPagination() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHExternalGroup> groups = org.listExternalGroups().toList();

        assertNotNull(groups);
        // In case more are added in the future
        assertTrue(groups.size() >= 4);
        assertTrue(groupSummary(groups).containsAll(List.of(
                "467430:acme-asset-owners",
                "467431:acme-developers",
                "467432:acme-product-owners",
                "467433:acme-technical-leads")));

        groups.forEach(group -> assertTrue(isExternalGroupSummary().matches(group)));

        // We are doing one request to get the organization and one to get the external groups
        assertTrue(mockGitHub.getRequestCount() >= 2);
    }

    /**
     * Test list members with filter.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListMembersWithFilter() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHUser> admins = org.listMembersWithFilter("all").toList();

        assertNotNull(admins);
        // In case more are added in the future
        assertTrue(admins.size() >= 12);
        assertTrue(admins.stream().map(GHUser::getLogin).collect(Collectors.toList()).containsAll(List.of(
                "alexanderrtaylor",
                "asthinasthi",
                "bitwiseman",
                "farmdawgnation",
                "halkeye",
                "jberglund-BSFT",
                "kohsuke",
                "kohsuke2",
                "martinvanzijl",
                "PauloMigAlmeida",
                "Sage-Pierce",
                "timja")));
    }

    /**
     * Test list members with role.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListMembersWithRole() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHUser> admins = org.listMembersWithRole("admin").toList();

        assertNotNull(admins);
        // In case more are added in the future
        assertTrue(admins.size() >= 12);
        assertTrue(admins.stream().map(GHUser::getLogin).collect(Collectors.toList()).containsAll(List.of(
                "alexanderrtaylor",
                "asthinasthi",
                "bitwiseman",
                "farmdawgnation",
                "halkeye",
                "jberglund-BSFT",
                "kohsuke",
                "kohsuke2",
                "martinvanzijl",
                "PauloMigAlmeida",
                "Sage-Pierce",
                "timja")));
    }

    /**
     * Test list outside collaborators.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListOutsideCollaborators() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHUser> admins = org.listOutsideCollaborators().toList();

        assertNotNull(admins);
        // In case more are added in the future
        assertTrue(admins.size() >= 12);
        assertTrue(admins.stream().map(GHUser::getLogin).collect(Collectors.toList()).containsAll(List.of(
                "alexanderrtaylor",
                "asthinasthi",
                "bitwiseman",
                "farmdawgnation",
                "halkeye",
                "jberglund-BSFT",
                "kohsuke",
                "kohsuke2",
                "martinvanzijl",
                "PauloMigAlmeida",
                "Sage-Pierce",
                "timja")));
    }

    /**
     * Test list outside collaborators with filter.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListOutsideCollaboratorsWithFilter() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHUser> admins = org.listOutsideCollaboratorsWithFilter("all").toList();

        assertNotNull(admins);
        // In case more are added in the future
        assertTrue(admins.size() >= 12);
        assertTrue(admins.stream().map(GHUser::getLogin).collect(Collectors.toList()).containsAll(List.of(
                "alexanderrtaylor",
                "asthinasthi",
                "bitwiseman",
                "farmdawgnation",
                "halkeye",
                "jberglund-BSFT",
                "kohsuke",
                "kohsuke2",
                "martinvanzijl",
                "PauloMigAlmeida",
                "Sage-Pierce",
                "timja")));
    }

    /**
     * Test list security managers.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testListSecurityManagers() throws IOException {
        GHOrganization org = gitHub.getOrganization(GITHUB_API_TEST_ORG);

        List<GHTeam> securityManagers = org.listSecurityManagers().toList();

        assertNotNull(securityManagers);
        // In case more are added in the future
        assertTrue(securityManagers.size() >= 1);
        assertTrue(securityManagers.stream().map(GHTeam::getName).collect(Collectors.toList()).contains("security team"));
    }

    /**
     * Enable response templating to allow support validating pagination of external groups
     *
     * @return the updated WireMock options
     */
    @Override
    protected WireMockConfiguration getWireMockOptions() {
        return super.getWireMockOptions().extensions(templating.newResponseTransformer());
    }

}
