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
            val m = Matrix()
            m.translate(by)
            return m
        }

        /**
         * Builds a rotation matrix.
         */
        fun rotate(axis: Vector, angle: Float): Matrix {
            val m = Matrix()
            m.rotate(axis, angle)
            return m
        }

        /**
         * Builds an scaling matrix.
         */
        fun scale(by: Vector): Matrix {
            val m = Matrix()
            m.scale(by)
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

    /**
     * Performs translation of this matrix.
     */
    fun translate(by: Vector) {
        translateMatrix(ref, by.ref)
    }

    /**
     * Performs scaling of this matrix.
     */
    fun scale(by: Vector) {
        scaleMatrix(ref, by.ref)
    }

    /**
     * Performs rotation of this matrix.
     */
    fun rotate(axis: Vector, angle: Float) {
        rotateMatrix(ref, axis.ref, angle)
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
