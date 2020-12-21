package com.argochamber.horizonengine.core

/**
 * Abstraction of descriptors that might need to be closed at some point.
 */
interface Closeable {
    /**
     * Closes the underlying descriptor.
     */
    fun close()
}

/**
 * Uses the closeable object and calls close at the end,
 * despite if the function throws or not.
 */
fun <T : Closeable> T.using(fn: T.() -> Unit) {
    try {
        fn()
    } finally {
        close()
    }
}
