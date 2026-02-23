package org.kohsuke.github;

// TODO: Auto-generated Javadoc
/**
 * Current state of {@link GHPullRequestReview}.
 */
public enum GHPullRequestReviewState {

    /** The approved. */
    APPROVED,

    /** The changes requested. */
    CHANGES_REQUESTED,

    /** The commented. */
    COMMENTED,

    /** The dismissed. */
    DISMISSED,

    /** The pending. */
    PENDING;

    /**
     * Action string.
     *
     * @return the string
     */
    String action() {
        GHPullRequestReviewEvent e = toEvent();
        return e == null ? null : e.action();
    }

    /**
     * To event.
     *
     * @return the GH pull request review event
     */
    GHPullRequestReviewEvent toEvent() {
        return switch (this) {
            case PENDING -> GHPullRequestReviewEvent.PENDING;
            case APPROVED -> GHPullRequestReviewEvent.APPROVE;
            case CHANGES_REQUESTED -> GHPullRequestReviewEvent.REQUEST_CHANGES;
            case COMMENTED -> GHPullRequestReviewEvent.COMMENT;
            case DISMISSED -> null;
        };
    }
}
