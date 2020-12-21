package com.argochamber.horizonengine.core

/**
 * A discriminated union that represents a value that might not be present due to an error.
 * Similar to a nullable result but accompanied with an exception case.
 * Results might also be nullable (Nullable and erroneous are not necessarily mutually exclusive).
 */
sealed class Maybe<T> {
    companion object {
        /**
         * Build a successful operation.
         */
        fun <T>success(value: T): Maybe<T> = Success(value)

        /**
         * Build an erroneous operation.
         */
        fun <T>failure(cause: Throwable): Maybe<T> = Error(cause)

        /**
         * Runs the block catching all exceptions.
         * In any case it will return the result of the block as a Maybe<T>.
         */
        fun <T>capturing(fn: () -> T): Maybe<T> = try {
            success(fn())
        } catch (e: Throwable) {
            failure(e)
        }
    }

    abstract val error: Boolean
    abstract val success: Boolean

    /**
     * Unwraps the value, leading to exceptions if the result is not correct.
     * @throws Throwable throws if the operation is erroneous.
     */
    abstract fun unwrap(): T

    /**
     * A safe approach for unwrapping the value, in case of error just takes the argument.
     * In any case does not throw.
     */
    abstract fun unwrapOr(other: T): T

    /**
     * Attempts to unwrap the object or calls the supplier function, in case of error.
     */
    abstract fun unwrapOr(supplier: () -> T): T

    /**
     * If there's an error, it returns the throwable object, if not, returns null.
     */
    abstract fun errorOrNull(): Throwable?

    /**
     * Runs the block if the
     */
    abstract fun <R>fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R

    private class Error<T>(val cause: Throwable) : Maybe<T>() {
        override val error: Boolean get() = true
        override val success: Boolean get() = false
        override fun unwrap(): T = throw cause
        override fun unwrapOr(other: T) = other
        override fun unwrapOr(supplier: () -> T): T = supplier()
        override fun errorOrNull(): Throwable = cause
        override fun <R> fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R = onFailure(cause)
    }

    private class Success<T>(val value: T) : Maybe<T>() {
        override val error: Boolean get() = false
        override val success: Boolean get() = true
        override fun unwrap(): T = value
        override fun unwrapOr(other: T) = value
        override fun unwrapOr(supplier: () -> T): T = value
        override fun errorOrNull(): Throwable? = null
        override fun <R> fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R = onSuccess(value)
    }
}
