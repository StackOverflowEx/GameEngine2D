#version 400 core

in vec2 position;
in vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform int facingRight = 0;

out vec2 pass_textureCoords;

void main(void){

	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 0, 1.0);
	if(facingRight == 1){
		pass_textureCoords = vec2(1-textureCoords.x, 1-textureCoords.y);
	}else {
		pass_textureCoords = vec2(textureCoords.x, 1-textureCoords.y);
	}


}
