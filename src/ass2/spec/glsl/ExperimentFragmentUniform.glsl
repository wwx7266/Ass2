#version 130

//Highest y value at top of screen
uniform vec2 windowSize;
uniform float time;

//Highest y value at top of screen
void halfRed(void){
    if(gl_FragCoord.y < windowSize.y/2.0){
        gl_FragColor = vec4(1,0,0,1);
    } else {
        gl_FragColor = vec4(1,1,0,1);
    }
}

void colorGradient(void){
    gl_FragColor = vec4(gl_FragCoord.x / windowSize[0], gl_FragCoord.y / windowSize[1], 0, 1);     
}

void circle(void){
	//float l = length(gl_FragCoord.xy); //In raw screen co-ordinates
	//Make coordinates go from -1*aspect..1*aspect and -1/aspect..1/aspect
	vec2 p = (gl_FragCoord.xy * 2.0 - windowSize) / min(windowSize.x, windowSize.y);
	float l = length(p);  //length from 0,0, (now the centre)
	
	if (l < 1){
	    gl_FragColor = vec4(1,1,0,1);
	} else {
	    gl_FragColor = vec4(0,0,0,1);
	}
}

void star(void){
	vec2 p = (gl_FragCoord.xy * 2.0 - windowSize) / min(windowSize.x, windowSize.y);
	//Range -PI..PI
	float a = atan(p.y,p.x) * 20;
    float intensity = sin(a);
	gl_FragColor = vec4(0.5*intensity,0.1*intensity,0.9*intensity,1);
	//gl_FragColor = vec4(1.0*intensity,1.0*intensity,1.0*intensity,1);
}

void spiral(void){
	vec2 p = (gl_FragCoord.xy * 2.0 - windowSize) / min(windowSize.x, windowSize.y);
	float a = atan(p.y,p.x) * 20;
	float l = length(p);
    float intensity = sin(a + float(l * 50.0));
	gl_FragColor = vec4(0.5*intensity,0.1*intensity,0.9*intensity,1);
}

void timeBasedSpiral(void){
	vec2 p = (gl_FragCoord.xy * 2.0 - windowSize) / min(windowSize.x, windowSize.y);
	float l = length(p);
	float a = atan(p.y,p.x) * 2.0;
    float intensity = sin(a * 20. + floor(l * 20.0) * (time/1000.0));
	gl_FragColor = vec4(0.5*intensity,0.1*intensity,0.9*intensity,1);

}

uniform int NUM_STEPS = 50;
uniform float ZOOM_FACTOR = 2.0;
uniform float X_OFFSET = 0.5;

void mandelbrot(void){
	vec2 z;
    float x,y;
    int steps;
    float normalizedX = (gl_FragCoord.x - windowSize[0]/2.0) / windowSize[0] * ZOOM_FACTOR *
                        (windowSize[0] / windowSize[1]) - X_OFFSET;
    float normalizedY = (gl_FragCoord.y - windowSize[1]/2.0) / windowSize[1] * ZOOM_FACTOR;
 
    z.x = normalizedX;
    z.y = normalizedY;
 
    for (int i=0;i<NUM_STEPS;i++) {
 
		steps = i;
 
        x = (z.x * z.x - z.y * z.y) + normalizedX;
        y = (z.y * z.x + z.x * z.y) + normalizedY;
 
        if((x * x + y * y) > 4.0) {
		  break;
		}
 
        z.x = x;
        z.y = y;
 
    }
 
    if (steps == NUM_STEPS-1) {
      gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    } else {
      gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}


void main(void){
    mandelbrot();
}