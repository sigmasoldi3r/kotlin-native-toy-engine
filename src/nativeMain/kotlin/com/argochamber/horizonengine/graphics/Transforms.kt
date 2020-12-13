package com.argochamber.horizonengine.graphics

import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector
import kotlinx.cinterop.refTo

/**
 * Matrix build operations bound to view and graphic transforms.
 */
object Transforms {
    /**
     * Creates a view matrix that is looking at target, from the given position,
     * and using the "up" vector as your local up direction.
     */
    fun lookAt(position: Vector, target: Vector, up: Vector): Matrix {
        val data = Matrix.raw()
        horizon.lookAt(position.ref, target.ref, up.ref, data.refTo(0))
        return Matrix(data)
    }

    /**
     * Creates a perspective projection matrix.
     */
    fun perspective(fov: Float, ratio: Float, near: Float, far: Float): Matrix {
        val data = Matrix.raw()
        horizon.perspective(fov, ratio, near, far, data.refTo(0))
        return Matrix(data)
    }
}
