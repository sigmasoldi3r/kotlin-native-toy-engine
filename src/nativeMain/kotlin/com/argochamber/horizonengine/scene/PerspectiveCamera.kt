package com.argochamber.horizonengine.scene

import com.argochamber.horizonengine.graphics.Camera
import com.argochamber.horizonengine.graphics.Transforms
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector

/**
 * Perspective projection camera.
 */
class PerspectiveCamera(fov: Float, ratio: Float, near: Float, far: Float) : Spatial(), Camera {
    var projection = Transforms.perspective(fov, ratio, near, far)
    var target = Vector.ZERO

    var i = 0f
    override fun getViewProjection(): Matrix {
        i += 0.005f
        position.x = kotlin.math.sin(i) * 3f
        position.z = kotlin.math.cos(i) * 3f
        return projection * Transforms.lookAt(transform.position, target, Vector.UP)
    }
}
