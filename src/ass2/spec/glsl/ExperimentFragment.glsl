#version 130
//Test shader that does nothing good
//Demo of different features of the glsl language/syntax
//We can use this kind of strategy to debug
//Check a condition and make it an obvious color if it is true

void main (void) {	
    
     //gl_FragColor = gl_Color;
 
     mat4 m = mat4(1.0,2.0,3.0,4.0,
                   5.0,6.0,7.0,8.0,
                   9.0,10.0,11.0,
                   12.0,13.0,14.0,15.0,16.0);
                   
     //If m[0][1] is 2 then all fragments will be green
     //otherwise they will be gray
     
 	 if(abs(m[0][1] - 2.0) < 0.00001){
 	    gl_FragColor = vec4(0.0,1.0,0.0,1.0);
 	 } else {	
	    gl_FragColor = vec4(0.5,0.5,0.5,1);
     }  	
     
     /*
     //If the fragment > 100 in screen co-ordinates make it yellow
     //otherwise make it red
     if (gl_FragCoord.y > 100){
         gl_FragColor = vec4(1.0,1.0,0.0,1.0);
     } else {
         gl_FragColor = vec4(1.0,0.0,0.0,1.0);
     }
     */
     
}

