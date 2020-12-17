package com.argochamber.horizonengine.math

import kotlinx.cinterop.refTo

/**
 * Native operated 4 component vector.
 * Data is contiguous, so it can be used as a 3-component also.
 */
class Vector(data: FloatArray = raw()): NFloatHeap(data) {
    companion object {
        fun raw() = FloatArray(4){0f}

        val ONE get() = of(1f)
        val ZERO get() = of()
        val RIGHT get() = of(1f, 0f, 0f)
        val UP get() = of(0f, 1f, 0f)
        val FRONT get() = of(0f, 0f, 1f)

        // Colors
        val BLACK get() = of(0f)
        val WHITE get() = of(1f)
        val RED get() = of(1f, 0f, 0f)
        val GREEN get() = of(0f, 1f, 0f)
        val BLUE get() = of(0f, 0f, 1f)
        val TRANSPARENT get() = of(1f, 1f, 1f, 0f)

        /**
         * Builds a new vector.
         */
        fun of(x: Float = 0f, y: Float = x, z: Float = y, w: Float = 1f): Vector {
            return Vector(floatArrayOf(x, y, z, w))
        }
    }

    var x: Float get() = data[0]; set(value) {data[0] = value}
    var y: Float get() = data[1]; set(value) {data[1] = value}
    var z: Float get() = data[2]; set(value) {data[2] = value}
    var w: Float get() = data[3]; set(value) {data[3] = value}
    var r: Float get() = x; set(value) {x = value}
    var g: Float get() = y; set(value) {y = value}
    var b: Float get() = z; set(value) {z = value}
    var a: Float get() = w; set(value) {w = value}

    operator fun times(rhs: Vector): Vector {
        val data = raw()
        horizon.vectorTimesVector(ref, rhs.ref, data.refTo(0))
        return Vector(data)
    }

    operator fun times(rhs: Float): Vector {
        val data = raw()
        horizon.vectorDividedScalar(ref, rhs, data.refTo(0))
        return Vector(data)
    }

    operator fun plus(rhs: Vector): Vector {
        val data = raw()
        horizon.vectorPlusVector(ref, rhs.ref, data.refTo(0))
        return Vector(data)
    }

    operator fun div(rhs: Float): Vector {
        val data = raw()
        horizon.vectorDividedScalar(ref, rhs, data.refTo(0))
        return Vector(data)
    }

    override fun toString(): String {
        return "( $x $y $z )"
    }
}
