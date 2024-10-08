package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.core.IsNot.not;

/**
 * Is the value null?
 *
 * @param <T> the matched value type
 */
public class IsNull<T> extends BaseMatcher<T> {

    /**
     * Constructor, best called from {@link #nullValue()},
     * {@link #nullValue(Class)}, {@link #notNullValue()},
     * or {@link #notNullValue(Class)}.
     */
    public IsNull() {
    }

    @Override
    public boolean matches(Object o) {
        return o == null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("null");
    }

    /**
     * <p>Creates a matcher that matches if examined object is <code>null</code>.
     * </p>
     * For example:
     * <pre>{@code assertThat(cheese, is(nullValue())}</pre>
     * @return The matcher
     */
    public static Matcher<Object> nullValue() {
        return new IsNull<>();
    }

    /**
     * A shortcut to the frequently used <code>not(nullValue())</code>.
     * For example:
     * <pre>assertThat(cheese, is(notNullValue()))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(not(nullValue())))</pre>
     *
     * @return The matcher.
     */
    public static Matcher<Object> notNullValue() {
        return not(nullValue());
    }

    /**
     * <p>Creates a matcher that matches if examined object is <code>null</code>.
     * Accepts a single dummy argument to facilitate type inference.
     * </p>
     * For example:
     * <pre>{@code assertThat(cheese, is(nullValue(Cheese.class))}</pre>
     *
     * @param <T> the matcher type.
     * @param type dummy parameter used to infer the generic type of the returned matcher
     * @return The matcher.
     */
    public static <T> Matcher<T> nullValue(Class<T> type) {
        return new IsNull<>();
    }

    /**
     * A shortcut to the frequently used <code>not(nullValue(X.class)). Accepts a
     * single dummy argument to facilitate type inference.</code>.
     * For example:
     * <pre>assertThat(cheese, is(notNullValue(X.class)))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(not(nullValue(X.class))))</pre>
     *
     * @param <T>
     *     the matcher type.
     * @param type
     *     dummy parameter used to infer the generic type of the returned matcher
     * @return The matcher.
     */
    public static <T> Matcher<T> notNullValue(Class<T> type) {
        return not(nullValue(type));
    }

}
