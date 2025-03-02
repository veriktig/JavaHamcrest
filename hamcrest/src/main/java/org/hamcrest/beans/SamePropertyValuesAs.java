package org.hamcrest.beans;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

import java.beans.FeatureDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;
import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;
import static org.hamcrest.beans.PropertyUtil.recordReadAccessorMethodDescriptorsFor;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * A matcher that checks if a given bean has the same property values
 * as an example bean.
 * @param <T> the matcher value type.
 * @see #samePropertyValuesAs(Object, String...)
 */
public class SamePropertyValuesAs<T> extends DiagnosingMatcher<T> {

    private final T expectedBean;
    private final Set<String> propertyNames;
    private final List<PropertyMatcher> propertyMatchers;
    private final List<String> ignoredFields;

    /**
     * Constructor, best called from {@link #samePropertyValuesAs(Object, String...)}.
     * @param expectedBean the bean object with the expected values
     * @param ignoredProperties list of property names that should be excluded from the match
     */
    @SuppressWarnings("WeakerAccess")
    public SamePropertyValuesAs(T expectedBean, List<String> ignoredProperties) {
        FeatureDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
        if (descriptors == null || descriptors.length == 0) {
            descriptors = recordReadAccessorMethodDescriptorsFor(expectedBean, Object.class);
        }

        this.expectedBean = expectedBean;
        this.ignoredFields = ignoredProperties;
        this.propertyNames = propertyNamesFrom(descriptors, ignoredProperties);
        this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors, ignoredProperties);
    }

    @Override
    protected boolean matches(Object actual, Description mismatch) {
        return isNotNull(actual, mismatch)
                && isCompatibleType(actual, mismatch)
                && hasNoExtraProperties(actual, mismatch)
                && hasMatchingValues(actual, mismatch);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same property values as " + expectedBean.getClass().getSimpleName())
                   .appendList(" [", ", ", "]", propertyMatchers);
        if (! ignoredFields.isEmpty()) {
            description.appendText(" ignoring ")
                    .appendValueList("[", ", ", "]", ignoredFields);
        }
    }

    private boolean isCompatibleType(Object actual, Description mismatchDescription) {
        if (expectedBean.getClass().isAssignableFrom(actual.getClass())) {
            return true;
        }

        mismatchDescription.appendText("is incompatible type: " + actual.getClass().getSimpleName());
        return false;
    }

    private boolean hasNoExtraProperties(Object actual, Description mismatchDescription) {
        Set<String> actualPropertyNames = propertyNamesFrom(propertyDescriptorsFor(actual, Object.class), ignoredFields);
        actualPropertyNames.removeAll(propertyNames);
        if (!actualPropertyNames.isEmpty()) {
            mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
            return false;
        }
        return true;
    }

    private boolean hasMatchingValues(Object actual, Description mismatchDescription) {
        for (PropertyMatcher propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(actual)) {
                propertyMatcher.describeMismatch(actual, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    private static <T> List<PropertyMatcher> propertyMatchersFor(T bean, FeatureDescriptor[] descriptors, List<String> ignoredFields) {
        List<PropertyMatcher> result = new ArrayList<>(descriptors.length);
        for (FeatureDescriptor descriptor : descriptors) {
            if (isNotIgnored(ignoredFields, descriptor)) {
                result.add(new PropertyMatcher(descriptor, bean));
            }
        }
        return result;
    }

    private static Set<String> propertyNamesFrom(FeatureDescriptor[] descriptors, List<String> ignoredFields) {
        HashSet<String> result = new HashSet<>();
        for (FeatureDescriptor descriptor : descriptors) {
            if (isNotIgnored(ignoredFields, descriptor)) {
                result.add(descriptor.getDisplayName());
            }
        }
        return result;
    }

    private static boolean isNotIgnored(List<String> ignoredFields, FeatureDescriptor propertyDescriptor) {
        return ! ignoredFields.contains(propertyDescriptor.getDisplayName());
    }

    @SuppressWarnings("WeakerAccess")
    private static class PropertyMatcher extends DiagnosingMatcher<Object> {
        private final Method readMethod;
        private final Matcher<Object> matcher;
        private final String propertyName;

        public PropertyMatcher(FeatureDescriptor descriptor, Object expectedObject) {
            this.propertyName = descriptor.getDisplayName();
            this.readMethod = descriptor instanceof PropertyDescriptor ?
                    ((PropertyDescriptor) descriptor).getReadMethod() :
                    ((MethodDescriptor) descriptor).getMethod();
            this.matcher = equalTo(readProperty(readMethod, expectedObject));
        }

        @Override
        public boolean matches(Object actual, Description mismatch) {
            final Object actualValue = readProperty(readMethod, actual);
            if (!matcher.matches(actualValue)) {
                mismatch.appendText(propertyName + " ");
                matcher.describeMismatch(actualValue, mismatch);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(propertyName + ": ").appendDescriptionOf(matcher);
        }
    }

    private static Object readProperty(Method method, Object target) {
        try {
            return method.invoke(target, NO_ARGUMENTS);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
        }
    }

    /**
     * <p>Creates a matcher that matches when the examined object has values for all of
     * its JavaBean properties that are equal to the corresponding values of the
     * specified bean. If any properties are marked as ignored, they will be dropped from
     * both the expected and actual bean. Note that the ignored properties use JavaBean
     * display names, for example "<code>age</code>" rather than method names such as
     * "<code>getAge</code>".
     * </p>
     * For example:
     * <pre>{@code
     * assertThat(myBean, samePropertyValuesAs(myExpectedBean))
     * assertThat(myBean, samePropertyValuesAs(myExpectedBean), "age", "height")
     * }</pre>
     *
     * @param <B> the matcher value type.
     * @param expectedBean the bean against which examined beans are compared
     * @param ignoredProperties do not check any of these named properties.
     * @return The matcher.
     */
    public static <B> Matcher<B> samePropertyValuesAs(B expectedBean, String... ignoredProperties) {
        return new SamePropertyValuesAs<>(expectedBean, asList(ignoredProperties));
    }

}
