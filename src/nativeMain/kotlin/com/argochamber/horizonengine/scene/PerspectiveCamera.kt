package com.argochamber.horizonengine.scene

import com.argochamber.horizonengine.graphics.Transforms
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector

/**
 * Perspective projection camera.
 */
class PerspectiveCamera(fov: Float, ratio: Float, near: Float, far: Float) : Spatial() {
    var projection = Transforms.perspective(fov, ratio, near, far)
    var target = Vector.ZERO

    fun project(model: Matrix): Matrix {
        val view = Transforms.lookAt(transform.position, target, Vector.UP)
        return projection * view * model
    }
}
