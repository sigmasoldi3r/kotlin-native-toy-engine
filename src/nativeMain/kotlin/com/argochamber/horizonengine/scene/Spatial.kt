package com.argochamber.horizonengine.scene

import com.argochamber.horizonengine.graphics.Transform
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector

/**
 * A spatial node that has a space transform.
 */
open class Spatial : Node() {
    /**
     * Local transform.
     */
    var transform = Transform()

    /**
     * Global transform.
     */
    val globalTransform: Matrix get() {
        val parent = this.parent
        if (parent is Spatial) {
            return transform.affine * parent.globalTransform
        }
        return transform.affine
    }

    var position: Vector get() = transform.position; set(value) { transform.position = value }
    var rotation: Vector get() = transform.rotation; set(value) { transform.rotation = value }
    var scale: Vector get() = transform.scale; set(value) { transform.scale = value }
}
