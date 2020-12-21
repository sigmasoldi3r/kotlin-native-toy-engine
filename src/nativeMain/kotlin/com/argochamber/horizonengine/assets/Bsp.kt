package com.argochamber.horizonengine.assets

import com.argochamber.horizonengine.core.Closeable
import kotlinx.cinterop.CPointer

/**
 * BSP file handler.
 */
class Bsp(private val file: CPointer<horizon.BspFile>) : Closeable {
    companion object {
        /**
         * Opens a BSP file from disk.
         */
        fun open(file: String) = Bsp(horizon.openBSP(file)!!)
    }

    override fun close() = horizon.closeBSP(file)
}
