#version 400 core

in vec2 position;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

out vec2 textureCoords;

void main(void){

	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 0, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);

}
