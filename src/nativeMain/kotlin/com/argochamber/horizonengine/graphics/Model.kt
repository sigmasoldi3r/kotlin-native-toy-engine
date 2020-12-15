package com.argochamber.horizonengine.graphics

import kotlinx.cinterop.CValue

/**
 * Simple model object.
 */
class Model(val model: CValue<horizon.Model>) {
    companion object {
        /**
         * Loads the model from disk.
         */
        fun load(path: String) = Model(horizon.loadModel(path))
    }

    /**
     * Makes a draw call for this model.
     */
    fun draw(texture: Texture) {
        horizon.drawModel(model, texture.texture)
    }
}
