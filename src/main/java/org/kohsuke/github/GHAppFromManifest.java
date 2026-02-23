package org.kohsuke.github;

/**
 * A GitHub App with the additional attributes returned during its creation.
 * <p>
 * Compatible with Java 25.
 *
 * @author Daniel Baur
 * @see GitHub#createAppFromManifest(String)
 */
public record GHAppFromManifest(
        String clientId,
        String clientSecret,
        String pem,
        String webhookSecret) {
}
