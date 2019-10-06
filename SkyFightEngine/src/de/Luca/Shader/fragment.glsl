#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D tex;
uniform float alpha = 1;

out vec4 out_Color;

void main(void){

	out_Color = texture(tex, pass_textureCoords);
	out_Color.w = out_Color.w * alpha;
//	out_Color = vec4(textureCoords.x, textureCoords.y, 0, 1);

}
