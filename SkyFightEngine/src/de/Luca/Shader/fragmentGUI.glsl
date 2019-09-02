#version 400 core

in vec2 pass_textureCoords;
in vec4 pass_color;

uniform sampler2D tex;

out vec4 out_Color;

void main(void){

	if(pass_color.w == -1){
		out_Color = texture(tex, pass_textureCoords);
	}else {
		out_Color = pass_color;
	}
//	out_Color = vec4(1, 1, 1, 1);

}
