package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Model
import com.argochamber.horizonengine.graphics.Shader
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.math.Vector
import com.argochamber.horizonengine.scene.Node
import com.argochamber.horizonengine.scene.PerspectiveCamera
import com.argochamber.horizonengine.scene.Spatial

class Game {
    var root = Node()
    val drawQueue = mutableListOf<Drawable>()

    fun draw() {
        drawQueue.forEach { it.draw() }
    }
}

val cam = PerspectiveCamera(90f.toRadians(), 1024f / 768f, .1f, 100f)

class Test(private val model: Model, private val shader: Shader) : Spatial(), Drawable {
    var tint = Vector.WHITE
    override fun draw() {
        shader.bind()
        shader["MVP"].set(cam.project(globalTransform))
        shader["tint"].set(tint)
        model.draw()
    }
}

fun start() {
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
    val model = Model.load("assets/triangle.mdl")
    val game = Game()
    val test = Test(model, shader)
    game.drawQueue.add(test)
    horizon.setupInput()
    do {
        horizon.clear()
        game.draw()
        horizon.processEvents()
    } while (horizon.shouldNotExit() != 0)
}

fun main() {
    when (horizon.createWindow(1024, 768, "Agachuu y anchoas de playa")) {
        horizon.WindowCreationCodes.FAILED_GLEW_INITIALIZATION -> error("Failed to initialize GLEW, aborting...")
        horizon.WindowCreationCodes.FAILED_GLFW_INITIALIZATION -> error("Failed to initialize GLFW, aborting...")
        horizon.WindowCreationCodes.INCOMPATIBLE_PROFILE_MODE -> error("Incompatible GPU profile mode, aborting...")
        horizon.WindowCreationCodes.WINDOW_CREATION_OK -> start()
    }
}