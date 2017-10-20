#version 130
// gl_Color values are calculated in the vertex shaders 
// and interpolated for each fragment
void main()
{
	gl_FragColor = gl_Color;
}
