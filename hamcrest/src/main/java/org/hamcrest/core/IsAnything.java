package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A matcher that always returns <code>true</code>.
 *
 * @param <T> the matched value type
 */
public class IsAnything<T> extends BaseMatcher<T> {

    private final String message;

    /**
     * Constructor, best called from {@link #anything()}.
     */
    public IsAnything() {
        this("ANYTHING");
    }

    /**
     * Constructor, best called from {@link #anything(String)}.
     * @param message matcher description
     */
    public IsAnything(String message) {
        this.message = message;
    }

    /**
     * Always returns true.
     * @param o the object against which the matcher is evaluated.
     * @return true
     */
    @Override
    public boolean matches(Object o) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(message);
    }

    /**
     * Creates a matcher that always matches, regardless of the examined object.
     *
     * @return The matcher.
     */
    public static Matcher<Object> anything() { return new IsAnything<>(); }

    /**
     * Creates a matcher that always matches, regardless of the examined object, but describes
     * itself with the specified {@link String}.
     *
     * @param description
     *     a meaningful {@link String} used when describing itself
     * @return The matcher.
     */
    public static Matcher<Object> anything(String description) {
        return new IsAnything<>(description);
    }

}
