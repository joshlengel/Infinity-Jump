#version 330

precision mediump float;

out vec4 fragmentColor;

uniform vec4 color;

void main() {
	fragmentColor = color;
}