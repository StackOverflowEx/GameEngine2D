#version 400 core

in vec2 position;
//in vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

out vec2 pass_textureCoords;

void main(void){

	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 0, 1.0);
	pass_textureCoords = vec2((position.x), 1 - (position.y));
//	pass_textureCoords = textureCoords;

}
