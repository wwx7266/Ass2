#version 130


uniform float time; /* in milliseconds */

void main(void) {
   float amp = 0.1;
   float freq = 5.0;
   float phase = 0.001 * time;
   vec4 t = gl_Vertex;
   //t.y = amp *sin(phase + freq*gl_Vertex.x)*sin(phase +freq*gl_Vertex.z);
   //t.y = amp *sin(phase + freq*gl_Vertex.x);
   
   gl_Position =  gl_ModelViewProjectionMatrix*t;
   gl_FrontColor = gl_Color;
   //gl_FrontColor = vec4(2*t.y,2*t.y,0.6+2*t.y,1);
  
}


