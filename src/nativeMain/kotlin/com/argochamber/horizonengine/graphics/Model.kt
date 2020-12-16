package com.argochamber.horizonengine.graphics

import kotlinx.cinterop.CValue

/**
 * Simple model object.
 */
class Model(private val model: CValue<horizon.Model>) {
    companion object {
        /**
         * Loads the model from disk.
         */
        fun load(path: String) = Model(horizon.loadModelObj(path))
    }

    /**
     * Makes a draw call for this model.
     */
    fun draw() {
        horizon.drawModel(model)
    }
}
