package ass2.spec;


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.awt.*;
import java.nio.FloatBuffer;

//Implements a surface of revolution
//Takes a 2D profile and sweeps it around the y-axis

public class RevSphereVBO extends GameObject{
	private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;

    private int maxStacks = 20;
    private int maxSlices = 30;
    private int maxVertices = maxStacks*(maxSlices+1)*2;
	FloatBuffer verticesBuffer = FloatBuffer.allocate(maxVertices*3);
	FloatBuffer normalsBuffer = FloatBuffer.allocate(maxVertices*3);

    private int numStacks = 9;
    private int numSlices = 30;
//    private int bufferIds[] = new int[1];


    public  RevSphereVBO(GL2 gl){
		bufferIds = new int[1];
    	generateBuffers(gl);

    }





    public void draw(GL2 gl) {

    	generateBuffers(gl);

    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

    	// Enable two vertex arrays: co-ordinates and color.
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glNormalPointer(GL.GL_FLOAT, 0, maxVertices*3*Float.SIZE/8);
    	for(int i=0; i < numStacks; i++ ){
    		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP,i*(maxSlices+1)*2,(numSlices+1)*2);
    	}
    	//gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP,0,maxVertices);

    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

}

