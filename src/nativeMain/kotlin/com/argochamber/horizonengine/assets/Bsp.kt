package com.argochamber.horizonengine.assets

import com.argochamber.horizonengine.core.Closeable
import com.argochamber.horizonengine.core.extracting
import com.argochamber.horizonengine.math.Vector
import horizon.getBSPEntityLump
import kotlinx.cinterop.*

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

    /**
     * BSP Lump entry generic information.
     */
    class LumpAddress(lump: CValue<horizon.BspLump>) {
        val offset = lump.extracting { it.offset }
        val length = lump.extracting { it.length }
    }

    /**
     * Reads entities.
     */
    fun readEntities(header: Header): EntitiesLump {
        val address = header.entities
        val str = ByteArray(address.length).let {
            getBSPEntityLump(file, address.offset, address.length, it.refTo(0))
            it.toKString()
        }
        val list = mutableListOf<Map<String, String>>()
        var map = mutableMapOf<String, String>()
        val pair = "\"(.+?)\"\\s*\"(.+?)\"".toRegex()
        for (line in str.lineSequence()) {
            when (line) {
                "{" -> map = mutableMapOf()
                "}" -> list.add(map)
                else -> pair.matchEntire(line)?.let {
                    val (_, key, value) = it.groupValues
                    map[key] = value
                }
            }
        }
        return EntitiesLump(list)
    }

    /**
     * Contains a list of entity definitions.
     */
    class EntitiesLump(val entities: List<Map<String, String>>)

    /**
     * Reads all the vertices in the BSP file.
     */
    fun readVertices(header: Header): VerticesLump {
        val list = FloatArray(4).let { raw ->
            val list = mutableListOf<Vector>()

            list
        }
            return VerticesLump(list.toTypedArray())
        }
    }

    /**
     * Contains all the vertices declared in the BSP file.
     */
    class VerticesLump(val vertices: Array<Vector>)

    /**
     * BSP file header, basically contains a closed list of Lumps.
     */
    class Header(private val header: CValue<horizon.BspHeader>) {
        /**
         * Gets the nth lump address.
         * @param at Maximum value is 14, minimum 0!
         */
        private fun getLump(at: Int) = LumpAddress(horizon.getBSPLumpFromHeader(header, at))
        val entities = getLump(0)
        val planes = getLump(1)
        val textures = getLump(2)
        val vertices = getLump(3)
        val visibility = getLump(4)
        val nodes = getLump(5)
        val texInfo = getLump(6)
        val faces = getLump(7)
        val lighting = getLump(8)
        val clipNodes = getLump(9)
        val leaves = getLump(10)
        val markSurfaces = getLump(11)
        val edges = getLump(12)
        val surfaceEdges = getLump(13)
        val models = getLump(14)
        operator fun component1() = entities
        operator fun component2() = planes
        operator fun component3() = textures
        operator fun component4() = vertices
        operator fun component5() = visibility
        operator fun component6() = nodes
        operator fun component7() = texInfo
        operator fun component8() = faces
        operator fun component9() = lighting
        operator fun component10() = clipNodes
        operator fun component11() = leaves
        operator fun component12() = markSurfaces
        operator fun component13() = edges
        operator fun component14() = surfaceEdges
        operator fun component15() = models
    }

    /**
     * Reads the BSP header.
     * @see Header for more insight.
     */
    fun readHeader() = Header(horizon.getBSPHeader(file))
}
