package com.argochamber.horizonengine.core

import kotlinx.cinterop.*

/**
 * Use the extracting function to extract a value directly.
 */
inline fun <reified T : CStructVar, R> CValue<T>.extracting(extractor: (T) -> R): R {
    var value: R? = null
    this.useContents {
        value = extractor(this)
    }
    return value!!
}

/**
 * Wrapper class for unmanaged pointer objects.
 */
class Disposing<T: CPointed>(private val ptr: CPointer<T>, private val disposer: (CPointer<T>) -> Unit) {
    /**
     * Runs the block wrapping the object.
     */
    fun <R>disposing(block: (CPointer<T>) -> R): R {
        try {
            return block(ptr)
        } finally {
            disposer(ptr)
        }
    }
}

/**
 * Wraps the pointer into a disposing object.
 */
fun <T: CPointed>CPointer<T>.wrap(disposer: (CPointer<T>) -> Unit) = Disposing(this, disposer)
