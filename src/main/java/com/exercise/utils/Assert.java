package com.exercise.utils;

import java.util.Objects;

public final class Assert {
    private static final String NOT_NULL_MSG_FORMAT = "Argument '%s' may not be null";
    private static final String NOT_NEGATIVE_MSG_FORMAT = "Argument '%s' may not be negative";

    private Assert() {
        // intentionally private and blank
    }

    /**
     * Checks that the specified {@code value} is null and throws {@link NullPointerException} with a customized error message if it is.
     *
     * @param value        the value to be checked.
     * @param argumentName the name of the argument to be used in the error message.
     * @throws IllegalArgumentException if {@code value} is null.
     */

    public static <T> void requireNonNull(final T value, final String argumentName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NOT_NULL_MSG_FORMAT, argumentName));
        }
    }

    /**
     * Checks that the specified {@code value} is not negative.
     *
     * @param value        the value to be checked.
     * @param argumentName the name of the argument to be used in the error message.
     * @throws IllegalArgumentException if {@code value} is negative.
     */
    public static void requirePositiveInteger(final Integer value, final String argumentName) {
        if (Objects.isNull(value) || value <= 0) {
            throw new IllegalArgumentException(String.format(NOT_NEGATIVE_MSG_FORMAT, argumentName));
        }
    }

    /**
     * Checks that the specified {@code value} is not negative.
     *
     * @param goal the goal to be checked.
     * @param step the goal to be checked.
     * @throws IllegalArgumentException if {@code value} is negative.
     */
    public static void goalShouldBeGreaterThanStep(final Integer goal, final Integer step) {
        if (step > goal) {
            throw new IllegalArgumentException("Step can't be greater than goal");
        }
    }

}