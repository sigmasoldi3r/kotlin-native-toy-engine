package com.argochamber.horizonengine.graphics

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents

/**
 * Shader graphics wrapper.
 */
class Shader(private val shader: CValue<horizon.Shader>) {
    companion object {
        /**
         * Compiles a simple shader model consisting on vertex and fragment only.
         */
        fun compile(vertex: String, fragment: String): Shader? {
            val result = horizon.compileShaderProgram(vertex, fragment)
            result.useContents {
                if (id == 0u) {
                    return null
                }
            }
            return Shader(result)
        }
    }

    /**
     * Binds this shader program to the context.
     * Usually simple render pipelines will invoke it by reference.
     */
    fun bind() {
        horizon.bindShader(shader)
    }
}
