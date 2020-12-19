package com.argochamber.horizonengine.graphics.shaders

import com.argochamber.horizonengine.graphics.Bindable
import com.argochamber.horizonengine.graphics.Shader

class DefaultUnlit : Bindable {
    val shader = Shader.building {
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
            uniform sampler2D texture;
            void main(){
              color = tint * texture(texture, uv).rgb;
            }
        """
        uniform += "tint"
        uniform += "MVP"
    } ?: error("Could not compile the shader.")

    val mvp = shader["MVP"]
    val tint = shader["tint"]

    override fun bind() {
        shader.bind()
    }
}
