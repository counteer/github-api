package org.kohsuke.github;

/**
 * Represents an error in a {@code CODEOWNERS} file. See <a href=
 * "https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-code-owners">the
 * relevant documentation</a>.
 *
 * @author Michael Grant
 * @param kind
 *            the kind
 * @param source
 *            the source
 * @param suggestion
 *            the suggestion
 * @param message
 *            the message
 * @param path
 *            the path
 * @param line
 *            the line
 * @param column
 *            the column
 */
public record GHCodeownersError(String kind, String source, String suggestion, String message, String path, int line,
        int column) {

    /**
     * Gets column.
     *
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets kind.
     *
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * Gets line.
     *
     * @return the line
     */
    public int getLine() {
        return line;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets source.
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets suggestion.
     *
     * @return the suggestion
     */
    public String getSuggestion() {
        return suggestion;
    }
}
