#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D text;
uniform vec3 color;

out vec4 out_Color;

void main(void){

//	float x = clamp(pass_textureCoords.x, 0.0, 0.9999999 );
//	float y = clamp( pass_textureCoords.y, -0.9999999, -0.05 );
	vec4 sampled = vec4(1.0, 1.0, 1.0, texture(text, pass_textureCoords).r);
	out_Color = vec4(color, 1.0) * sampled;
	if(out_Color.w > 0){
		out_Color.w = 1;
	}
	//	out_Color = vec4(0.0, 1.0, 0.0, 1.0);
	//out_Color = vec4(pass_textureCoords, 0, 1);
}
