package com.argochamber.horizonengine.graphics

import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector
import kotlinx.cinterop.CValue

/**
 * Shader graphics wrapper.
 */
class Shader(private val shader: CValue<horizon.Shader>, private val uniforms: Map<String, Uniform>) {
    companion object {
        /**
         * Simple interface for building Shaders.
         */
        class Builder {
            interface IUniformBuilder
            {
                operator fun plusAssign(name: String)
            }
            class UniformBuilder : IUniformBuilder {
                val names = mutableListOf<String>()
                override fun plusAssign(name: String) { names.add(name) }
            }
            var vertex: String? = null
            var fragment: String? = null
            var geometry: String? = null
            var compute: String? = null
            var tessellationControl: String? = null
            var tessellationEvaluation: String? = null
            val uniform: IUniformBuilder = UniformBuilder()
        }

        /**
         * Starts the shader building process.
         * The function provides the necessary parts for the shader program, then they're fed into the graphics card
         * based on what was provided.
         */
        fun building(builder: Builder.() -> Unit): Shader? {
            val props = Builder()
            props.builder()
            val shader = horizon.createShader()
            props.fragment?.let {
                val prog = horizon.compileFragmentShaderProgram(it)
                if (!horizon.isShaderProgramOk(prog)) {
                    return@building null
                }
                horizon.attachShaderProgram(shader, prog)
            }
            props.vertex?.let {
                val prog = horizon.compileVertexShaderProgram(it)
                if (!horizon.isShaderProgramOk(prog)) {
                    return@building null
                }
                horizon.attachShaderProgram(shader, prog)
            }
            props.geometry?.let {
                val prog = horizon.compileGeometryShaderProgram(it)
                if (!horizon.isShaderProgramOk(prog)) {
                    return@building null
                }
                horizon.attachShaderProgram(shader, prog)
            }
            props.compute?.let {
                val prog = horizon.compileComputeShaderProgram(it)
                if (!horizon.isShaderProgramOk(prog)) {
                    return@building null
                }
                horizon.attachShaderProgram(shader, prog)
            }
            props.tessellationControl?.let {
                val prog = horizon.compileTessControlShaderProgram(it)
                if (!horizon.isShaderProgramOk(prog)) {
                    return@building null
                }
                horizon.attachShaderProgram(shader, prog)
            }
            props.tessellationEvaluation?.let {
                val prog = horizon.compileTessEvaluationShaderProgram(it)
                if (!horizon.isShaderProgramOk(prog)) {
                    return@building null
                }
                horizon.attachShaderProgram(shader, prog)
            }
            if (horizon.linkShader(shader)) {
                val uniforms = mutableMapOf<String, Uniform>()
                (props.uniform as Builder.UniformBuilder).names.forEach {
                    uniforms[it] = Uniform(horizon.getUniform(shader, it))
                }
                return Shader(shader, uniforms)
            }
            return null
        }

        /**
         * Loads the shader from a YML definition file.
         */
        fun fromFile(path: String): Shader? {
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
    operator fun get(name: String) = uniforms[name]

    /**
     * Binds this shader program to the context.
     * Usually simple render pipelines will invoke it by reference.
     */
    fun bind() {
        horizon.bindShader(shader)
    }
}
