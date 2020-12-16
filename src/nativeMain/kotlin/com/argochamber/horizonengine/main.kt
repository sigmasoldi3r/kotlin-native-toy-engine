package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Model
import com.argochamber.horizonengine.graphics.Shader
import com.argochamber.horizonengine.graphics.Texture
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.math.Vector
import com.argochamber.horizonengine.scene.PerspectiveCamera
import com.argochamber.horizonengine.scene.Spatial

val cam = PerspectiveCamera(90f.toRadians(), 1024f / 768f, .1f, 100f)

class Test(private val model: Model, private val shader: Shader, private val texture: Texture) : Spatial(), Drawable {
    var tint = Vector.WHITE
    var i = 0f
    override fun draw() {
        texture.bind()
        shader.bind()
        shader["MVP"].set(cam.project(Matrix.identity()))
        shader["tint"].set(tint)
        model.draw()
        cam.position.x = kotlin.math.sin(i) * 3f
        cam.position.z = kotlin.math.cos(i) * 3f
        i += 0.005f
    }
}

fun main() {
    val engine = Engine()
    engine.createWindow(1024, 768, "Horizon Engine v1.0").let { result ->
        if (result !== Engine.WindowResult.Ok) error(result.message)
    }
    cam.position.x = 3f
    cam.position.y = 3f
    cam.position.z = 3f
    val unlit = Shader.compile(
        """
        #version 330 core
        layout(location = 0) in vec3 vertexPosition_modelspace;
        layout(location = 1) in vec2 vertexUV;
        out vec2 uv;
        uniform mat4 MVP;
        void main(){
          gl_Position =  MVP * vec4(vertexPosition_modelspace, 1);
          uv = vertexUV;
        }
        """,
        """
        #version 330 core
        in vec2 uv;
        out vec3 color;
        uniform vec3 tint;
        uniform sampler2D texture;
        void main(){
          color = tint * texture(texture, uv).rgb;
        }
        """) ?: error("Could not compile the shader.")
    val model = Model.load("assets/spaceplane.obj")
    val texture = Texture.load("assets/player_body.png") ?: error("Could not load the texture.")
    val test = Test(model, unlit, texture)
//    val test2 = Test(model, unlit, texture)
//    test2.transform.position = Vector.of(1f, 0f, 0f)
//    engine.enableDraw(test2)
    test.scale.x = 2f
    engine.enableDraw(test)
    engine.start()
}
