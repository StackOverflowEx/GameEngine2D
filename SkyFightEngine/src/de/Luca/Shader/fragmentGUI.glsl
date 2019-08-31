#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D tex;
uniform vec4 color;

out vec4 out_Color;

void main(void){

	out_Color = color * texture(tex, pass_textureCoords);
	out_Color = vec4(1, pass_textureCoords.y, pass_textureCoords.x, 1);

}
