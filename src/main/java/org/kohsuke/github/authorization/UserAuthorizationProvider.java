package org.kohsuke.github.authorization;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface for all user-related authorization providers.
 *
 * {@link AuthorizationProvider}s can apply to a number of different account types. This interface applies to providers
 * for user accounts, ones that have a login or should query the "/user" endpoint for the login matching this
 * credential.
 */
public interface UserAuthorizationProvider extends AuthorizationProvider {

    /**
     * Gets the user login name.
     *
     * @return an {@link Optional} containing the user login for this provider, or {@link Optional#empty()} if the
     *         login value should be queried from the "/user" endpoint or is otherwise unknown.
     */
    Optional<String> getLogin() throws IOException;
}
