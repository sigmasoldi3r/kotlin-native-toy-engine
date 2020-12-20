package com.argochamber.horizonengine.graphics

import kotlinx.cinterop.CValue

/**
 * Texture object.
 */
class Texture(private val texture: CValue<horizon.Texture>) {
    companion object {

        /**
         * Loads the PNG texture from disk.
         */
        fun loadPNGFromDisk(from: String): Texture? {
            val texture = horizon.loadTexturePNG(from)
            if (horizon.isTextureOk(texture))
                return Texture(texture)
            return null
        }
    }

    fun bind() {
        horizon.bindTexture(texture)
    }
}
