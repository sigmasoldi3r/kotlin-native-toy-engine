package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Drawable
import horizon.WindowCreationCodes

/**
 * Engine central wrapper.
 */
class Engine {

    sealed class WindowResult(val message: String) {
        object Ok : WindowResult("OK")
        object GlewError : WindowResult("Failed to initialize GLEW, aborting...")
        object GlfwError : WindowResult("Failed to initialize GLFW, aborting...")
        object IncompatibleGPUProfile : WindowResult("Incompatible GPU profile mode, aborting...")
    }

    /**
     * Creates a window or throws if could not.
     */
    fun createWindow(w: Int, h: Int, title: String) = when (horizon.createWindow(w, h, title)) {
        WindowCreationCodes.FAILED_GLEW_INITIALIZATION -> WindowResult.GlewError
        WindowCreationCodes.FAILED_GLFW_INITIALIZATION -> WindowResult.GlfwError
        WindowCreationCodes.INCOMPATIBLE_PROFILE_MODE -> WindowResult.IncompatibleGPUProfile
        WindowCreationCodes.WINDOW_CREATION_OK -> WindowResult.Ok
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
        horizon.setupInput()
        do {
            horizon.clear()
            drawQueue.forEach(Drawable::draw)
            horizon.processEvents()
        } while (horizon.shouldNotExit())
    }
}
