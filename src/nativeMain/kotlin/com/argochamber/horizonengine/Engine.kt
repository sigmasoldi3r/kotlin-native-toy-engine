package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Camera
import com.argochamber.horizonengine.graphics.Drawable
import horizon.WindowCreationCodes

/**
 * Engine central wrapper.
 */
class Engine(var camera: Camera) {
    /**
     * A response representing the result of window creation.
     * It should be OK if all went correctly.
     */
    sealed class WindowResult(val message: String) {
        object Ok : WindowResult("OK")
        object GlewError : WindowResult("Failed to initialize GLEW, aborting...")
        object GlfwError : WindowResult("Failed to initialize GLFW, aborting...")
        object IncompatibleGPUProfile : WindowResult("Incompatible GPU profile mode, aborting...")
        object Unknown : WindowResult("Window creation returned an error code that was not mapped. Library bindings might be not up to date.")
        companion object {
            val map = mapOf(
                WindowCreationCodes.FAILED_GLEW_INITIALIZATION to GlewError,
                WindowCreationCodes.FAILED_GLFW_INITIALIZATION to GlfwError,
                WindowCreationCodes.INCOMPATIBLE_PROFILE_MODE to IncompatibleGPUProfile,
                WindowCreationCodes.WINDOW_CREATION_OK to Ok
            )
        }
    }

    /**
     * Creates a window or throws if could not.
     */
    fun createWindow(w: Int, h: Int, title: String) =
        WindowResult.map[horizon.createWindow(w, h, title)] ?: WindowResult.Unknown

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
