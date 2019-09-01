#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D tex;
uniform vec4 color;

out vec4 out_Color;

void main(void){

	out_Color = color * texture(tex, pass_textureCoords);
//	if(out_Color.a < ){
//		out_Color.a = 0;
//	}
//	out_Color = vec4(0.2, pass_textureCoords.y, pass_textureCoords.x, 1);

}
