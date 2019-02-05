#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D text;
uniform vec3 color;

out vec4 out_Color;

void main(void){

	vec4 sampled = vec4(1.0, 1.0, 1.0, texture(text, pass_textureCoords).r);
	out_Color = vec4(color, 1.0) * sampled;
	out_Color = vec4(0.0, 1.0, 0.0, 1.0);
	//out_Color = vec4(pass_textureCoords, 0, 1);
}
