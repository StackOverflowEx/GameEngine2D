#version 400 core

in vec2 textureCoords;

uniform sampler2D tex;

out vec4 out_Color;

void main(void){

	out_Color = texture(tex, textureCoords);
//	out_Color = vec4(textureCoords.x, textureCoords.y, 0, 1);

}
