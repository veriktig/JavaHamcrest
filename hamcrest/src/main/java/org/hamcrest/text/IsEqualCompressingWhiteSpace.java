package org.hamcrest.text;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Tests if a string is equal to another string, compressing any changes in whitespace.
 */
public class IsEqualCompressingWhiteSpace extends TypeSafeMatcher<String> {

    // TODO: Replace String with CharSequence to allow for easy interoperability between
    //       String, StringBuffer, StringBuilder, CharBuffer, etc (joe).

    private final String string;

    /**
     * Constructor, best called from {@link #equalToCompressingWhiteSpace(String)}.
     * @param string the expected string
     */
    public IsEqualCompressingWhiteSpace(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Non-null value required");
        }
        this.string = string;
    }

    /**
     * Gets the string
     * @return the string
     */
    protected String getString() {
        return string;
    }

    @Override
    public boolean matchesSafely(String item) {
        return stripSpaces(string).equals(stripSpaces(item));
    }

    @Override
    public void describeMismatchSafely(String item, Description mismatchDescription) {
      mismatchDescription.appendText("was ").appendValue(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string equal to ")
                .appendValue(string)
                .appendText(" compressing white space");
    }

    /**
     * Strips spaces
     * @param toBeStripped the string to be stripped
     * @return the stripped string
     */
    public String stripSpaces(String toBeStripped) {
        return toBeStripped.replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
    }

    /**
     * @deprecated {@link #equalToCompressingWhiteSpace(String)}
     * @param expectedString
     *     the expected value of matched strings
     * @return The matcher.
     */
    public static Matcher<String> equalToIgnoringWhiteSpace(String expectedString) {
        return new IsEqualCompressingWhiteSpace(expectedString);
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is equal to
     * the specified expectedString, when whitespace differences are (mostly) ignored.  To be
     * exact, the following whitespace rules are applied:
     * <ul>
     *   <li>all leading and trailing whitespace of both the expectedString and the examined string are ignored</li>
     *   <li>any remaining whitespace, appearing within either string, is collapsed to a single space before comparison</li>
     * </ul>
     * For example:
     * <pre>assertThat("   my\tfoo  bar ", equalToCompressingWhiteSpace(" my  foo bar"))</pre>
     *
     * @param expectedString
     *     the expected value of matched strings
     * @return The matcher.
     */
    public static Matcher<String> equalToCompressingWhiteSpace(String expectedString) {
        return new IsEqualCompressingWhiteSpace(expectedString);
    }

}
