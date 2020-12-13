package com.argochamber.horizonengine.math

import kotlinx.cinterop.refTo

/**
 * Native operated 3 component vector.
 */
class Vector(data: FloatArray = raw()): NFloatHeap(data) {
    companion object {
        fun raw() = FloatArray(3){0f}

        val RIGHT = of(1f, 0f, 0f)
        val UP = of(0f, 1f, 0f)
        val FRONT = of(0f, 0f, 1f)

        /**
         * Builds a new vector.
         */
        fun of(x: Float = 0f, y: Float = x, z: Float = y): Vector {
            return Vector(floatArrayOf(x, y, z))
        }
    }

    var x: Float get() = data[0]; set(value) {data[0] = value}
    var y: Float get() = data[1]; set(value) {data[1] = value}
    var z: Float get() = data[2]; set(value) {data[2] = value}

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
