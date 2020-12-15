package com.argochamber.horizonengine.graphics

import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector

/**
 * Transforms are used to compute the math behind matrix transform.
 */
class Transform {
//    private var _position = Vector.ZERO
//    var position get() = _position; set(value) { _position = value; updateAffine() }
//    private var _scale = Vector.ZERO
//    var scale get() = _scale; set(value) { _scale = value; updateAffine() }
//    private var _rotation = Vector.ZERO
//    var rotation get() = _rotation; set(value) { _rotation = value; updateAffine() }

    var position = Vector.ZERO
    var rotation = Vector.ZERO
    var scale = Vector.ZERO

//    var affine = Matrix.identity()
    val affine get() = Matrix.rotate(Vector.FRONT, rotation.z)
        .rotate(Vector.UP, rotation.y)
        .rotate(Vector.RIGHT, rotation.x)
        .scale(scale)
        .translate(position)

    /**
     * Updates the affine.
     */
//    private fun updateAffine() {
//        affine = Matrix.rotate(Vector.FRONT, rotation.z)
//            .rotate(Vector.UP, rotation.y)
//            .rotate(Vector.RIGHT, rotation.x)
//            .scale(scale)
//            .translate(position)
//    }
}