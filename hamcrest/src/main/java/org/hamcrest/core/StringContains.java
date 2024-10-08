package org.hamcrest.core;

import org.hamcrest.Matcher;

/**
 * Tests if the argument is a string that contains a specific substring.
 */
public class StringContains extends SubstringMatcher {

    /**
     * Constructor, best used with {@link #containsString(String)}.
     * @param substring the expected substring.
     */
    public StringContains(String substring) { this(false, substring); }

    /**
     * Constructor, best used with {@link #containsString(String)}  or
     * {@link #containsStringIgnoringCase(String)}.
     * @param ignoringCase whether to ignore case when matching
     * @param substring the expected substring.
     */
    public StringContains(boolean ignoringCase, String substring) {
        super("containing", ignoringCase, substring);
    }

    @Override
    protected boolean evalSubstringOf(String s) {
        return converted(s).contains(converted(substring));
    }

    /**
     * Creates a matcher that matches if the examined {@link String} contains the specified
     * {@link String} anywhere.
     * For example:
     * <pre>assertThat("myStringOfNote", containsString("ring"))</pre>
     *
     * @param substring
     *     the substring that the returned matcher will expect to find within any examined string
     * @return The matcher.
     */
    public static Matcher<String> containsString(String substring) {
        return new StringContains(false, substring);
    }

    /**
     * Creates a matcher that matches if the examined {@link String} contains the specified
     * {@link String} anywhere, ignoring case.
     * For example:
     * <pre>assertThat("myStringOfNote", containsStringIgnoringCase("Ring"))</pre>
     *
     * @param substring
     *     the substring that the returned matcher will expect to find within any examined string
     * @return The matcher.
     */
    public static Matcher<String> containsStringIgnoringCase(String substring) {
        return new StringContains(true, substring);
    }

}
