package com.argochamber.horizonengine.scene

open class Node {
    var parent: Node? = null
    val children = mutableListOf<Node>()

    fun add(node: Node) {
        node.parent = this
        children.add(node)
    }
}
