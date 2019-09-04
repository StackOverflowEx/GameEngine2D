#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D tex;
uniform sampler2D afterTex;
uniform float blendFactor;

out vec4 out_Color;

void main(void){

	vec4 texColor = texture(tex, pass_textureCoords);
	vec4 afterTexColor = texture(afterTex, pass_textureCoords);
	out_Color = mix(texColor, afterTexColor, blendFactor);
//	out_Color = vec4(textureCoords.x, textureCoords.y, 0, 1);

}
