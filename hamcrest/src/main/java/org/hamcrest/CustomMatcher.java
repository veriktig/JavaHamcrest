package org.hamcrest;

/**
 * Utility class for writing one off matchers.
 * For example:
 * <pre>
 * Matcher&lt;String&gt; aNonEmptyString = new CustomMatcher&lt;String&gt;("a non empty string") {
 *   public boolean matches(Object object) {
 *     return ((object instanceof String) &amp;&amp; !((String) object).isEmpty();
 *   }
 * };
 * </pre>
 * <p>
 * This class is designed for scenarios where an anonymous inner class
 * matcher makes sense. It should not be used by API designers implementing
 * matchers. See {@link CustomTypeSafeMatcher} for a type safe variant of
 * this class that you probably want to use.
 *
 * @author Neil Dunn
 * @param <T> The type of object being matched.
 * @see CustomTypeSafeMatcher
 */
public abstract class CustomMatcher<T> extends BaseMatcher<T> {

    private final String fixedDescription;

    /**
     * Constructor
     * @param description the description of this matcher
     */
    public CustomMatcher(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description should be non null!");
        }
        this.fixedDescription = description;
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(fixedDescription);
    }

}
