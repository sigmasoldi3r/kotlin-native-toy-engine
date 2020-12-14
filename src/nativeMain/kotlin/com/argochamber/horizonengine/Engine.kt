package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Drawable

/**
 * Engine central wrapper.
 */
class Engine private constructor() {
    companion object {
        /**
         * Creates a window or throws if could not.
         */
        fun create(w: Int, h: Int, title: String) = when (horizon.createWindow(w, h, title)) {
            horizon.WindowCreationCodes.FAILED_GLEW_INITIALIZATION -> error("Failed to initialize GLEW, aborting...")
            horizon.WindowCreationCodes.FAILED_GLFW_INITIALIZATION -> error("Failed to initialize GLFW, aborting...")
            horizon.WindowCreationCodes.INCOMPATIBLE_PROFILE_MODE -> error("Incompatible GPU profile mode, aborting...")
            else -> Engine()
        }
    }

    private val drawQueue = mutableListOf<Drawable>()

    fun enableDraw(drawable: Drawable) {
        drawQueue.add(drawable)
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
        } while (horizon.shouldNotExit() != 0)
    }
}
