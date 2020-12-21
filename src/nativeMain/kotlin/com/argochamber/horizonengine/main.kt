package com.argochamber.horizonengine

import com.argochamber.horizonengine.assets.Wad
import com.argochamber.horizonengine.core.using
import com.argochamber.horizonengine.game.Game
import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Model
import com.argochamber.horizonengine.graphics.Shader
import com.argochamber.horizonengine.graphics.Texture
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.math.Vector
import com.argochamber.horizonengine.scene.PerspectiveCamera
import com.argochamber.horizonengine.scene.Spatial

class Test(private val game: Game, private val model: Model, private val shader: Shader) : Spatial(), Drawable {
    var tint = Vector.WHITE
    override fun draw(delta: Float, vp: Matrix) {
        game.textures["engine.missing"]?.bind()
        shader.bind()
        shader["MVP"]?.set(vp * transform.affine)
        shader["tint"]?.set(tint)
        model.draw()
    }
}

fun main() {
    val game = Game.create(1024, 768, "Horizon Engine v1.0").unwrap()
    val cam = game.camera as? PerspectiveCamera ?: error("The camera attribute is not configured properly!")
    cam.position.y = 3f
    Wad.bulkLoad(game, listOf(
        "assets/engine.wad" to "engine",
        "assets/halflife.wad" to "halflife"))
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
    val model = Model.load("assets/cube.obj")
//    val model = Model.load("assets/spaceplane.obj")
//    val texture = Texture.load("assets/player_body.png") ?: error("Could not load the texture.")
    val range = (0 until 1)
    range.forEach { x ->
        range.forEach { y ->
            range.forEach { z ->
                val test = Test(game, model, unlit)
                test.position.x = x.toFloat()
                test.position.y = y.toFloat()
                test.position.z = z.toFloat()
//                test.scale = Vector.of(.25f)
                game.enableDraw(test)
            }
        }
    }
    game.start()
}
