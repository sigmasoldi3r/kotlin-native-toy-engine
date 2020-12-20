package com.argochamber.horizonengine

import com.argochamber.horizonengine.assets.Wad
import com.argochamber.horizonengine.core.using
import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Model
import com.argochamber.horizonengine.graphics.Shader
import com.argochamber.horizonengine.graphics.Texture
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.math.Vector
import com.argochamber.horizonengine.scene.PerspectiveCamera
import com.argochamber.horizonengine.scene.Spatial

class Test(private val model: Model, private val shader: Shader, private val texture: Texture) : Spatial(), Drawable {
    var tint = Vector.WHITE

    override fun draw(delta: Float, vp: Matrix) {
        texture.bind()
        shader.bind()
        shader["MVP"]?.set(vp * transform.affine)
        shader["tint"]?.set(tint)
        model.draw()
    }
}

fun main() {
    Wad.from("F:\\SteamLibrary\\steamapps\\common\\Half-Life\\valve\\halflife.wad").using {
        val header = readHeader()
        val entries = header.readEntries()
        for (entry in entries) {
            val texture = entry.loadTexture()
        }
    }
/*
    val cam = PerspectiveCamera(90f.toRadians(), 1024f / 768f, .1f, 100f)
    val engine = Engine(cam)
    engine.createWindow(1024, 768, "Horizon Engine v1.0").let { result ->
        if (result !== Engine.WindowResult.Ok) error(result.message)
    }
    cam.position.x = 3f
    cam.position.y = 3f
    cam.position.z = 3f
    val unlit = Shader.building {
        vertex = """
            #version 330 core
            layout(location = 0) in vec3 vertex;
            layout(location = 1) in vec2 uvs;
            out vec2 uv;
            uniform mat4 MVP;
            void main(){
              gl_Position =  MVP * vec4(vertex, 1);
              uv = vec2(uvs.x, 1.0-uvs.y);
            }
        """
        fragment = """
            #version 330 core
            in vec2 uv;
            out vec3 color;
            uniform vec3 tint;
            uniform sampler2D texture_sampler;
            void main(){
              color = tint * texture(texture_sampler, uv).rgb;
            }
        """
        uniform += "tint"
        uniform += "MVP"
    } ?: error("Could not compile the shader.")
    val model = Model.load("assets/spaceplane.obj")
    val texture = Texture.load("assets/player_body.png") ?: error("Could not load the texture.")
    val range = (-3..3)
    range.forEach { x ->
        range.forEach { y ->
            range.forEach { z ->
                val test = Test(model, unlit, texture)
                test.position.x = x.toFloat()
                test.position.y = y.toFloat()
                test.position.z = z.toFloat()
                test.scale = Vector.of(.25f)
                engine.enableDraw(test)
            }
        }
    }
    engine.start()
     */
}
