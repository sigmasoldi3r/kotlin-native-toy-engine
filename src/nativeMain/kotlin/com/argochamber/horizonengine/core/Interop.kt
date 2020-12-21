package com.argochamber.horizonengine.core

import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents

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
