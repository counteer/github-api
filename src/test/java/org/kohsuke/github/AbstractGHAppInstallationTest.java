package org.kohsuke.github;

import com.google.common.collect.ImmutableSet;
import io.jsonwebtoken.Jwts;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.authorization.AuthorizationProvider;
import org.kohsuke.github.extras.authorization.JWTTokenProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * The Class AbstractGHAppInstallationTest.
 */
public class AbstractGHAppInstallationTest extends AbstractGitHubWireMockTest {

    private static String ENV_GITHUB_APP_ID = "GITHUB_APP_ID";
    private static String ENV_GITHUB_APP_JWK_PATH = "GITHUB_APP_JWK_PATH";
    private static String ENV_GITHUB_APP_ORG = "GITHUB_APP_ORG";
    private static String ENV_GITHUB_APP_REPO = "GITHUB_APP_REPO";

    private static String TEST_APP_ID_1 = "82994";
    private static String TEST_APP_ID_2 = "83009";
    private static String TEST_APP_ID_3 = "89368";

    protected static String privateKeyPEM;

    static {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            Base64.Encoder encoder = Base64.getMimeEncoder(64, new byte[]{ 10 });
            privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" + encoder.encodeToString(kp.getPrivate().getEncoded())
                    + "\n-----END PRIVATE KEY-----";
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to generate test keypair", e);
        }
    }

    /** The jwt provider 1. */
    protected final AuthorizationProvider jwtProvider1;

    /** The jwt provider 2. */
    protected final AuthorizationProvider jwtProvider2;

    /** The jwt provider 3. */
    protected final AuthorizationProvider jwtProvider3;

    /**
     * Instantiates a new abstract GH app installation test.
     */
    protected AbstractGHAppInstallationTest() {
        String appId = System.getenv(ENV_GITHUB_APP_ID);
        String appJwkPath = System.getenv(ENV_GITHUB_APP_JWK_PATH);
        try {
            if (appId != null && appJwkPath != null) {
                jwtProvider1 = new JWTTokenProvider(appId, Paths.get(appJwkPath));
                jwtProvider2 = jwtProvider1;
                jwtProvider3 = jwtProvider1;
            } else {
                // We use three different ways to initialize the provider to test all constructors
                File tempKeyFile = File.createTempFile("ghapi-test-app-", ".pem");
                tempKeyFile.deleteOnExit();
                try (FileWriter fw = new FileWriter(tempKeyFile)) {
                    fw.write(privateKeyPEM);
                }

                jwtProvider1 = new JWTTokenProvider(TEST_APP_ID_1, tempKeyFile);
                jwtProvider2 = new JWTTokenProvider(TEST_APP_ID_2, tempKeyFile.toPath());
                jwtProvider3 = new JWTTokenProvider(TEST_APP_ID_3, privateKeyPEM);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("These should never fail", e);
        }
    }

    private String createJwtToken(String appId) {
        try {
            String keyPEM = privateKeyPEM.replaceAll("(?m)^--.*", "") // remove comments from PEM to allow decoding
                    .replaceAll("\\s", "");

            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyPEM));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8);

            return Jwts.builder()
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                    .setIssuer(appId)
                    .signWith(privateKey)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error creating JWT token.", e);
        }
    }

    /**
     * Gets the app installation with token.
     *
     * @param jwtToken
     *            the jwt token
     * @return the app installation with token
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected GHAppInstallation getAppInstallationWithToken(String jwtToken) throws IOException {
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring("Bearer ".length());
        }

        GitHub gitHub = getGitHubBuilder().withJwtToken(jwtToken)
                .withEndpoint(mockGitHub.apiServer().baseUrl())
                .build();

        GHApp app = gitHub.getApp();

        GHAppInstallation appInstallation;
        if (ImmutableSet.of(TEST_APP_ID_1, TEST_APP_ID_2, TEST_APP_ID_3).contains(Long.toString(app.getId()))) {
            List<GHAppInstallation> installations = app.listInstallations().toList();
            appInstallation = installations.stream()
                    .filter(it -> it.getAccount().login.equals("hub4j-test-org"))
                    .findFirst()
                    .get();
        } else {
            // We may be processing a custom JWK, for a custom GHApp: fetch a relevant repository dynamically
            appInstallation = app.getInstallationByRepository(System.getenv(ENV_GITHUB_APP_ORG),
                    System.getenv(ENV_GITHUB_APP_REPO));
        }

        // TODO: this is odd
        // appInstallation
        // .setRoot(getGitHubBuilder().withAppInstallationToken(appInstallation.createToken().create().getToken())
        // .withEndpoint(mockGitHub.apiServer().baseUrl())
        // .build());

        return appInstallation;
    }

}
