package com.argochamber.horizonengine.math

import horizon.rotateMatrix
import horizon.scaleMatrix
import horizon.translateMatrix
import kotlinx.cinterop.refTo

/**
 * An abstract interface for 4x4 matrix.
 */
class Matrix(data: FloatArray = raw()): NFloatHeap(data) {
    companion object {
        fun raw() = FloatArray(4*4){0f}

        /**
         * Builds a translation matrix.
         */
        fun translate(by: Vector): Matrix {
            val m = identity()
            m.translate(by)
            return m
        }

        /**
         * Builds a rotation matrix.
         */
        fun rotate(axis: Vector, angle: Float): Matrix {
            val m = identity()
            m.rotate(axis, angle)
            return m
        }

        /**
         * Builds an scaling matrix.
         */
        fun scale(by: Vector): Matrix {
            val m = identity()
            m.scale(by)
            return m
        }

        /**
         * Creates the identity matrix.
         */
        fun identity(): Matrix {
            val m = Matrix()
            m[0, 0] = 1f
            m[1, 1] = 1f
            m[2, 2] = 1f
            m[3, 3] = 1f
            return m
        }
    }

    /**
     * Returns the matrix product of two matrices.
     */
    operator fun times(rhs: Matrix): Matrix {
        val data = raw()
        horizon.matrixProduct(ref, rhs.ref, data.refTo(0))
        return Matrix(data)
    }

    operator fun get(i: Int, j: Int) = data[i + j * 4]
    operator fun set(i: Int, j: Int, value: Float) {
        data[i + j * 4] = value
    }

    /**
     * Performs translation of this matrix.
     */
    fun translate(by: Vector): Matrix {
        translateMatrix(ref, by.ref)
        return this
    }

    /**
     * Performs scaling of this matrix.
     */
    fun scale(by: Vector): Matrix {
        scaleMatrix(ref, by.ref)
        return this
    }

    /**
     * Performs rotation of this matrix.
     */
    fun rotate(axis: Vector, angle: Float): Matrix {
        rotateMatrix(ref, axis.ref, angle)
        return this
    }

    override fun toString(): String {
        return (0..3).joinToString("\n") {
            val a = data[it * 4]
            val b = data[it * 4 + 1]
            val c = data[it * 4 + 2]
            val d = data[it * 4 + 3]
            "$a\t$b\t$c\t$d"
        }
    }
}
