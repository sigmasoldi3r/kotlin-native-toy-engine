package com.argochamber.horizonengine

import com.argochamber.horizonengine.graphics.Drawable
import com.argochamber.horizonengine.graphics.Model
import com.argochamber.horizonengine.graphics.Shader
import com.argochamber.horizonengine.graphics.Transforms
import com.argochamber.horizonengine.math.toRadians
import com.argochamber.horizonengine.math.Matrix
import com.argochamber.horizonengine.math.Vector
import com.argochamber.horizonengine.scene.Node

open class Spatial : Node() {
//    val transform = Mat4()
}

class Game {
    var root = Node()
    val drawQueue = mutableListOf<Drawable>()

    fun draw() {
        drawQueue.forEach { it.draw() }
    }
}

class Test(private val model: Model, private val shader: Shader) : Drawable {
    private var i = 0f
    override fun draw() {
        i += 0.001f
        shader.bind()
//        shader["MVP"].set(getMVP())
        // TODO: Make MVP work!
        shader["MVP"].set(Matrix.rotate(Vector.FRONT, i))
        model.draw()
    }
}

fun getMVP(): Matrix {
    val target = Vector.of()
    val position = Vector.of(4f, 3f, 3f)
    val view = Transforms.lookAt(position, target, Vector.UP)
    val projection = Transforms.perspective(90f.toRadians(), 4f / 3f, .1f, 100f)
    val model = Matrix.identity()
    return projection * view * model
    // MVP Thing ^
}

fun start() {
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
        void main(){
          color = vec3(1,0,0);
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