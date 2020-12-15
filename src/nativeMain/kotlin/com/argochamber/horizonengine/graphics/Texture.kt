package com.argochamber.horizonengine.graphics

import kotlinx.cinterop.CValue

/**
 * Texture object.
 */
class Texture(val texture: CValue<horizon.Texture>) {
    companion object {
        fun load(from: String): Texture? {
            val texture = horizon.loadTexturePNG(from)
            if (horizon.isTextureOk(texture))
                return Texture(texture)
            return null
        }
    }
}
