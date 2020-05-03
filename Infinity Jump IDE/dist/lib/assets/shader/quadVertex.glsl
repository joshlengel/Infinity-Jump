#version 330

layout(location = 0) in vec2 vertex;

uniform vec2 offset;
uniform vec2 scale;

uniform float aspectRatio;

void main() {
	vec2 coord = vertex * scale + offset;
	coord.y = (coord.y + 1.0) * aspectRatio - 1.0;

	gl_Position = vec4(coord, 0.0, 1.0);
}