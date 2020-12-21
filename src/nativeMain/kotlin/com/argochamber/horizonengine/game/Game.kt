package com.argochamber.horizonengine.game

import com.argochamber.horizonengine.core.Maybe
import com.argochamber.horizonengine.graphics.Camera
import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Texture
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.scene.PerspectiveCamera
import horizon.WindowCreationCodes

/**
 * Engine central wrapper.
 */
class Game private constructor(var camera: Camera) {
    companion object {
        /**
         * Tries to create an instance of the game, initializing all the graphics and interfacing schemes.
         */
        fun create(w: Int, h: Int, title: String): Maybe<Game> {
            val result = horizon.createWindow(w, h, title)
            if (result == WindowCreationCodes.WINDOW_CREATION_OK) {
                return Maybe.success(Game(PerspectiveCamera(90f.toRadians(), w.toFloat()/h.toFloat(), .1f, 100f)))
            }
            return Maybe.failure(WindowResult.map[result] ?: WindowResult.Unknown)
        }
    }

    /**
     * A response representing the result of window creation.
     * It should be OK if all went correctly.
     */
    sealed class WindowResult(message: String) : Exception(message) {
        object GlewError : WindowResult("Failed to initialize GLEW, aborting...")
        object GlfwError : WindowResult("Failed to initialize GLFW, aborting...")
        object IncompatibleGPUProfile : WindowResult("Incompatible GPU profile mode, aborting...")
        object Unknown : WindowResult("Window creation returned an error code that was not mapped. Library bindings might be not up to date.")
        companion object {
            val map = mapOf(
                WindowCreationCodes.FAILED_GLEW_INITIALIZATION to GlewError,
                WindowCreationCodes.FAILED_GLFW_INITIALIZATION to GlfwError,
                WindowCreationCodes.INCOMPATIBLE_PROFILE_MODE to IncompatibleGPUProfile
            )
        }
    }

    // Assets
    private val _textures = mutableMapOf<String, Texture>()
    val textures: Map<String, Texture> get() = _textures
    fun supplyTexture(name: String, texture: Texture) {
        _textures[name] = texture
    }

    private val drawQueue = mutableListOf<Drawable>()

    /**
     * Enables the drawing of this drawable object.
     */
    fun enableDraw(drawable: Drawable) {
        drawQueue.add(drawable)
    }

    /**
     * Removes the drawable element from the drawing queue, thus, disabling the draw of it.
     */
    fun disableDraw(drawable: Drawable) {
        drawQueue.remove(drawable)
    }

    /**
     * Starts the game loop.
     */
    fun start() {
        horizon.drawLoopSetup()
        do {
            horizon.clear()
            val vp = camera.getViewProjection()
            for (it in drawQueue) { it.draw(0f, vp) }
            horizon.processEvents()
        } while (horizon.shouldNotExit())
    }
}
