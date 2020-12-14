package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Model
import com.argochamber.horizonengine.graphics.Shader
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.math.Vector
import com.argochamber.horizonengine.scene.PerspectiveCamera
import com.argochamber.horizonengine.scene.Spatial

val cam = PerspectiveCamera(90f.toRadians(), 1024f / 768f, .1f, 100f)

class Test(private val model: Model, private val shader: Shader) : Spatial(), Drawable {
    var tint = Vector.WHITE
    var i = 0f
    override fun draw() {
        shader.bind()
        shader["MVP"].set(cam.project(globalTransform))
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
    val shader = Shader.compile(
        """
        #version 330 core
        layout(location = 0) in vec3 vertexPosition_modelspace;
        uniform mat4 MVP;
        void main(){
          gl_Position =  MVP * vec4(vertexPosition_modelspace,1);
        }
        """,
        """
        #version 330 core
        out vec3 color;
        uniform vec3 tint;
        void main(){
          color = tint;
        }
        """) ?: error("Could not compile the shader.")
    val model = Model.load("assets/cube.json")
    val test = Test(model, shader)
    engine.enableDraw(test)
    engine.start()
}
