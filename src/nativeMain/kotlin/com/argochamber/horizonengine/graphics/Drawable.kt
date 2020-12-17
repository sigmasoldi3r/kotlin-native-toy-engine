package com.argochamber.horizonengine.graphics

import com.argochamber.horizonengine.math.Matrix

/**
 * Objects that can be draw onscreen.
 */
interface Drawable {
    /**
     * Receive the camera transform.
     * @param delta The amount of time passed after the last draw call.
     * @param vp The transform matrix computed by the current camera.
     */
    fun draw(delta: Float, vp: Matrix)
}
