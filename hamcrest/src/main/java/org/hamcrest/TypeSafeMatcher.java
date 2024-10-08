package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;

/**
 * Convenient base class for Matchers that require a non-null value of a specific type.
 * This simply implements the null check, checks the type and then casts.
 *
 * @param <T>
 *     the matcher type.
 * @author Joe Walnes
 * @author Steve Freeman
 * @author Nat Pryce
 */
public abstract class TypeSafeMatcher<T> extends BaseMatcher<T> {

    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 1, 0);

    final private Class<?> expectedType;

    /**
     * The default constructor for simple sub types
     */
    protected TypeSafeMatcher() {
        this(TYPE_FINDER);
    }

    /**
     * Use this constructor if the subclass that implements <code>matchesSafely</code>
     * is <em>not</em> the class that binds &lt;T&gt; to a type.
     * @param expectedType The expectedType of the actual value.
     */
    protected TypeSafeMatcher(Class<?> expectedType) {
        this.expectedType = expectedType;
    }

    /**
     * Use this constructor if the subclass that implements <code>matchesSafely</code>
     * is <em>not</em> the class that binds &lt;T&gt; to a type.
     *
     * @param typeFinder A type finder to extract the type
     */
    protected TypeSafeMatcher(ReflectiveTypeFinder typeFinder) {
      this.expectedType = typeFinder.findExpectedType(getClass());
    }

    /**
     * Check if the item matches. The item will already have been checked for
     * the specific type and will never be null. Subclasses should implement this.
     *
     * @param item the type safe item to match against.
     * @return boolean true/false depending if item matches matcher.
     */
    protected abstract boolean matchesSafely(T item);

    /**
     * Describe the mismatch. The item will already have been checked for
     * the specific type and will never be null. Subclasses should override this.
     *
     * @param item
     *     the type safe item to match against.
     * @param mismatchDescription
     *     the mismatch description.
     */
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        super.describeMismatch(item, mismatchDescription);
    }

    /**
     * Methods made final to prevent accidental override.
     * If you need to override this, there's no point on extending TypeSafeMatcher.
     * Instead, extend the {@link BaseMatcher}.
     *
     * @param item
     *     the type safe item to match against.
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public final boolean matches(Object item) {
        return item != null
                && expectedType.isInstance(item)
                && matchesSafely((T) item);
    }

    @SuppressWarnings("unchecked")
    @Override
    final public void describeMismatch(Object item, Description description) {
        if (item == null) {
            super.describeMismatch(null, description);
        } else if (! expectedType.isInstance(item)) {
            description.appendText("was a ")
                       .appendText(item.getClass().getName())
                       .appendText(" (")
                       .appendValue(item)
                       .appendText(")");
        } else {
            describeMismatchSafely((T)item, description);
        }
    }

}
