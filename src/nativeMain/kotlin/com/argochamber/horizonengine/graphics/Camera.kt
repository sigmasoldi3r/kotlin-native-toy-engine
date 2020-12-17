package com.argochamber.horizonengine.graphics

import com.argochamber.horizonengine.math.Matrix

/**
 * Common interface for cameras.
 * They're updated at the first of the loop in order to cache results.
 */
interface Camera {
    /**
     * Compute the view-projection matrix.
     */
    fun getViewProjection(): Matrix
}