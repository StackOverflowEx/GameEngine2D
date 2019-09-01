#version 400 core

in vec2 position;
in vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform vec4 color = vec4(1.0, 1.0, 1.0, 1.0);

out vec2 pass_textureCoords;
out vec4 pass_color;

void main(void){

	gl_Position = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
	pass_textureCoords = textureCoords;
	pass_color = color;

}
