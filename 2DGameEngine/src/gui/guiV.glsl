#version 400 core

in vec2 position;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;

out vec2 textureCoords;

void main (void){

	gl_Position =  projectionMatrix * transformationMatrix * vec4(position, 0, 1.0);
	textureCoords = vec2(position.x, -position.y);
}
