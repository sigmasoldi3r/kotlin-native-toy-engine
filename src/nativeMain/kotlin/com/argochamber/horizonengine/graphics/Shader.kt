package com.argochamber.horizonengine.graphics

import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector
import kotlinx.cinterop.CValue
import kotlinx.cinterop.toBoolean
import kotlinx.cinterop.useContents

/**
 * Shader graphics wrapper.
 */
class Shader(val shader: CValue<horizon.Shader>) {
    companion object {
        /**
         * Compiles a simple shader model consisting on vertex and fragment only.
         */
        fun compile(vertex: String, fragment: String): Shader? {
            val result = horizon.compileShaderProgram(vertex, fragment)
            if (horizon.isShaderOk(result)) {
                return Shader(result)
            }
            return null
        }
    }

    /**
     * Program parameter location.
     */
    class Uniform(private val uniform: CValue<horizon.Uniform>) {
        fun set(matrix: Matrix) {
            horizon.setUniformMatrix(uniform, 1, false, matrix.ref)
        }
        fun set(vector: Vector) {
            horizon.setUniformVector(uniform, 1, vector.ref)
        }
        fun set(value: Float) {
            horizon.setUniformFloat(uniform, value)
        }
    }

    /**
     * Gets the uniform at the given name.
     */
    operator fun get(name: String) = Uniform(horizon.getUniform(shader, name))

    /**
     * Binds this shader program to the context.
     * Usually simple render pipelines will invoke it by reference.
     */
    fun bind() {
        horizon.bindShader(shader)
    }
}
