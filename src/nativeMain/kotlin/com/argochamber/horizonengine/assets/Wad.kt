package com.argochamber.horizonengine.assets

import com.argochamber.horizonengine.core.Closeable
import com.argochamber.horizonengine.core.extracting
import com.argochamber.horizonengine.graphics.Texture
import horizon.readWADTextureData
import kotlinx.cinterop.*

/**
 * Simple WAD file descriptor.
 */
class Wad(private val file: CPointer<horizon.WadFile>): Closeable {
    companion object {
        fun from(path: String) = Wad(horizon.openWAD(path)!!)
    }

    /**
     * Reads the header.
     */
    fun readHeader() = Header(this, horizon.getWADHeader(file))

    /**
     * WAD file header data descriptor.
     */
    class Header(private val wad: Wad, private val header: CValue<horizon.WadHeader>) {
        val signature = header.extracting { it.signature.toKString() }
        val count = header.extracting { it.directoryCount }
        val offset = header.extracting { it.directoryOffset }

        /**
         * Reads all the entries of this WAD file.
         */
        fun readEntries(): List<Entry> {
            val list = mutableListOf<Entry>()
            memScoped {
                val heap = allocArray<horizon.WadEntry>(count)
                horizon.readWADEntries(wad.file, header, heap, count)
                (0 until count).forEach { i ->
                    val entry = heap[i]
                    list.add(Entry.from(entry))
                }
            }
            return list
        }
    }

    /**
     * WAD file entry.
     */
    class Entry(
        val filePosition: Int,
        val diskSize: Int,
        val size: Int,
        val type: Byte,
        val compressed: Boolean,
        val unused: Short,
        val name: String) {
        companion object {
            /**
             * Creates the entry.
             */
            fun from(data: horizon.WadEntry) = Entry(
                data.filePosition,
                data.diskSize,
                data.size,
                data.type,
                data.compressed,
                data.unused,
                data.name.toKString()
            )
        }

        /**
         * Small descriptor of the entry.
         */
        class Data(data: CValue<horizon.BspMipTexture>) {
            val height = data.extracting { it.height }
            val width = data.extracting { it.width }
            val name = data.extracting { it.name.toKString() }
            val offsets = data.extracting { (0..3).map { i -> it.mipOffset[i] } }
        }
    }

    fun Entry.loadTexture(): Pair<Texture, Entry.Data> {
        val header = Entry.Data(readWADTextureData(file, filePosition))
        println("-> ${header.name} ( ${header.width} x ${header.height} )")
        TODO("Add texture loading for actual graphics")
    }

    /**
     * Get the nth entry of this WAD file.
     */
    override fun close() {
        horizon.closeWAD(file)
    }
}