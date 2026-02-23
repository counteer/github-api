/*
 * The MIT License
 *
 * Copyright (c) 2016, Duncan Dickinson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.kohsuke.github;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Class GHLicenseTest.
 *
 * @author Duncan Dickinson
 */
public class GHLicenseTest extends AbstractGitHubWireMockTest {

    /**
     * Create default GHLicenseTest instance
     */
    public GHLicenseTest() {
    }

    /**
     * Accesses the 'kohsuke/github-api' repo using {@link GitHub#getRepository(String)} and then calls
     * {@link GHRepository#getLicense()} and checks that certain properties are correct.
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void checkRepositoryFullLicense() throws IOException {
        GHRepository repo = gitHub.getRepository("hub4j/github-api");
        GHLicense license = repo.getLicense();
        assertNotNull(license, "The license is populated");
        assertEquals("mit", license.getKey(), "The key is correct");
        assertEquals("MIT", license.getSpdxId(), "The SPDX ID is correct");
        assertEquals("MIT License", license.getName(), "The name is correct");
        assertEquals(new URL(mockGitHub.apiServer().baseUrl() + "/licenses/mit"), license.getUrl(), "The URL is correct");
        assertEquals(new URL("http://choosealicense.com/licenses/mit/"), license.getHtmlUrl(), "The HTML URL is correct");
    }

    /**
     * Accesses the 'kohsuke/github-api' repo using {@link GitHub#getRepository(String)} and checks that the license is
     * correct.
     *
     * @throws IOException
     *             if test failss
     */
    @Test
    public void checkRepositoryLicense() throws IOException {
        GHRepository repo = gitHub.getRepository("hub4j/github-api");
        GHLicense license = repo.getLicense();
        assertNotNull(license, "The license is populated");
        assertEquals("mit", license.getKey(), "The key is correct");
        assertEquals("MIT", license.getSpdxId(), "The SPDX ID is correct");
        assertEquals("MIT License", license.getName(), "The name is correct");
        assertEquals(new URL(mockGitHub.apiServer().baseUrl() + "/licenses/mit"), license.getUrl(), "The URL is correct");
    }

    /**
     * Accesses the 'atom/atom' repo using {@link GitHub#getRepository(String)} and checks that the license is correct.
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void checkRepositoryLicenseAtom() throws IOException {
        GHRepository repo = gitHub.getRepository("atom/atom");
        GHLicense license = repo.getLicense();
        assertNotNull(license, "The license is populated");
        assertEquals("mit", license.getKey(), "The key is correct");
        assertEquals("MIT", license.getSpdxId(), "The SPDX ID is correct");
        assertEquals("MIT License", license.getName(), "The name is correct");
        assertEquals(new URL(mockGitHub.apiServer().baseUrl() + "/licenses/mit"), license.getUrl(), "The URL is correct");
    }

    /**
     * Accesses the 'pomes/pomes' repo using {@link GitHub#getRepository(String)} and then calls
     * {@link GHRepository#getLicenseContent()} and checks that certain properties are correct.
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void checkRepositoryLicenseContent() throws IOException {
        GHRepository repo = gitHub.getRepository("pomes/pomes");
        GHContent content = repo.getLicenseContent();
        assertNotNull(content, "The license content is populated");
        assertEquals("file", content.getType(), "The type is 'file'");
        assertEquals("LICENSE", content.getName(), "The license file is 'LICENSE'");

        if (content.getEncoding().equals("base64")) {
            String licenseText = new String(IOUtils.toByteArray(content.read()));
            assertTrue(licenseText.contains("Apache License"),
                    "The license appears to be an Apache License");
        } else {
            fail("Expected the license to be Base64 encoded but instead it was " + content.getEncoding());
        }
    }

    /**
     * Accesses the 'bndtools/bnd' repo using {@link GitHub#getRepository(String)} and then calls
     * {@link GHRepository#getLicense()}. The description is null due to multiple licences
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void checkRepositoryLicenseForIndeterminate() throws IOException {
        GHRepository repo = gitHub.getRepository("bndtools/bnd");
        GHLicense license = repo.getLicense();
        assertNotNull(license, "The license is populated");
        assertEquals("other", license.getKey());
        assertNull(license.getDescription());
        assertNull(license.getUrl());
    }

    /**
     * Accesses the 'pomes/pomes' repo using {@link GitHub#getRepository(String)} and checks that the license is
     * correct.
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void checkRepositoryLicensePomes() throws IOException {
        GHRepository repo = gitHub.getRepository("pomes/pomes");
        GHLicense license = repo.getLicense();
        assertNotNull(license, "The license is populated");
        assertEquals("apache-2.0", license.getKey(), "The key is correct");
        assertEquals("Apache-2.0", license.getSpdxId(), "The SPDX ID is correct");
        assertEquals("Apache License 2.0", license.getName(), "The name is correct");
        assertEquals(new URL(mockGitHub.apiServer().baseUrl() + "/licenses/apache-2.0"), license.getUrl(), "The URL is correct");
    }

    /**
     * Accesses the 'dedickinson/test-repo' repo using {@link GitHub#getRepository(String)} and checks that *no* license
     * is returned as the repo doesn't have one.
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void checkRepositoryWithoutLicense() throws IOException {
        GHRepository repo = gitHub.getRepository(GITHUB_API_TEST_ORG + "/empty");
        GHLicense license = repo.getLicense();
        assertNull(license, "There is no license");
    }

    /**
     * Checks that the request for an individual license using {@link GitHub#getLicense(String)} returns expected values
     * (not all properties are checked).
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void getLicense() throws IOException {
        String key = "mit";
        GHLicense license = gitHub.getLicense(key);
        assertNotNull(license);
        assertEquals("MIT License", license.getName(), "The name is correct");
        assertEquals("MIT", license.getSpdxId(), "The SPDX ID is correct");
        assertEquals(new URL("http://choosealicense.com/licenses/mit/"),
                license.getHtmlUrl(),
                "The HTML URL is correct");
        assertTrue(license.getBody().startsWith("MIT License\n" + "\n" + "Copyright (c) [year] [fullname]\n\n"));
        assertTrue(license.getForbidden().isEmpty());
        assertTrue(license.getPermitted().isEmpty());
        assertTrue(license.getRequired().isEmpty());
        assertEquals(
                "Create a text file (typically named LICENSE or LICENSE.txt) in the root of your source code and copy the text of the license into the file. Replace [year] with the current year and [fullname] with the name (or names) of the copyright holders.",
                license.getImplementation());
        assertNull(license.getCategory());
        assertTrue(license.isFeatured());
        assertFalse(license.equals(null));
        assertTrue(license.equals(gitHub.getLicense(key)));
    }

    /**
     * Basic test to ensure that the list of licenses from {@link GitHub#listLicenses()} is returned.
     */
    @Test
    public void listLicenses() {
        Iterable<GHLicense> licenses = gitHub.listLicenses();
        assertNotNull(licenses);
        assertTrue(licenses.iterator().hasNext());
    }

    /**
     * Tests that {@link GitHub#listLicenses()} returns the MIT license in the expected manner.
     *
     * @throws IOException
     *             if test fails
     */
    @Test
    public void listLicensesCheckIndividualLicense() throws IOException {
        PagedIterable<GHLicense> licenses = gitHub.listLicenses();
        for (GHLicense lic : licenses) {
            if (lic.getKey().equals("mit")) {
                assertEquals(new URL(mockGitHub.apiServer().baseUrl() + "/licenses/mit"), lic.getUrl());
                return;
            }
        }
        fail("The MIT license was not found");
    }
}
