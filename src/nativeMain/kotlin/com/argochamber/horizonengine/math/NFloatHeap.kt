package com.argochamber.horizonengine.math

import kotlinx.cinterop.refTo

/**
 * Abstract, contiguous memory array N-Element tuple.
 */
abstract class NFloatHeap(val data: FloatArray) {
    val ref get() = data.refTo(0)
}
