package ass2.spec;


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.nio.FloatBuffer;



//Implements a surface of revolution
//Takes a 2D profile and sweeps it around the y-axis

public class SunVBO extends GameObject {
	private float LOWESTSKYCOLOR=21;
	private float LOWESTEMM = 21;
	private float skyColor = LOWESTSKYCOLOR;
//	private int
	private float[] center = new float[2];
	private int distance = 0;
	private float emm = LOWESTEMM;
	private float colorChange = (float) ((255-LOWESTEMM)/(90/0.2));
	private float skyColorChange = (float) ((255-LOWESTSKYCOLOR)/(90/0.2));
    float lightPos0[] = new float[4];
	private double angle = 0;

  	float matAmbAndDifL[] = {1f, 1f, 1f, 1.0f};
	public SunVBO(GL2 gl, float x, float z, float[] pos){
		this.center[0] = x;
		this.center[1] = z;
		bufferIds = new int[2];
	    lightPos0[0] = pos[0];
	    lightPos0[1] = pos[1];
	    lightPos0[2] = pos[2];
	    lightPos0[3] = 1f;
		this.generateBuffers(gl);

	}


	public void updateCoordinateAndLight(GL2 gl){
		angle = MathUtil.normalizeAngle(angle+0.2);
		rotatePoint();
		if(angle>0)
			gl.glEnable(GL2.GL_LIGHT0);
		else
			gl.glDisable(GL2.GL_LIGHT0);

    	if(angle>=0 && angle <= 90)  {
    		emm= emm+colorChange;
    		skyColor += skyColorChange;

    	}
    	if(angle > 90 && angle<180) {
    		emm = emm-colorChange;
      		skyColor -= skyColorChange;

    	}
    	else if(angle<=0){
    		emm = LOWESTEMM;
    		skyColor = LOWESTSKYCOLOR;
    	};
	}


	public void rotatePoint(){

		float px =center[0]*2+distance;
    	float py = 0;
    	float ox = center[0];
    	float oy = 0;
    	float redians = (float) Math.toRadians(angle);
    	lightPos0[0] = (float) ((Math.cos(redians) * (px-ox) - Math.sin(redians) * (py-oy) + ox));
    	lightPos0[1] =(float)( (Math.sin(redians) * (px-ox) + Math.cos(redians) * (py-oy) + oy));
	}

    public float[] getLight(){
    	float[] emmLi = {emm/255,emm/255,emm/255,1f};
    	return(emmLi);
    }

    public float[] getSkyColor(){
    	float[] emmLi = {1/111,skyColor/255,skyColor/255,1f};
    	return(emmLi);
    }

    public float[] getLightPosition(){

    	return(lightPos0);
    }
    public void draw(GL2 gl, MyTexture[] texture) {
		updateCoordinateAndLight(gl);

		if(this.angle>=0){
	    	gl.glPushMatrix();{

	    		gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
	    		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[4].getTextureId());

	        	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, getLight(),0);


		    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

		    	// Enable two vertex arrays: co-ordinates and color.
		    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		    	gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

		    	// Specify locations for the co-ordinates and color arrays.
		    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
		    	gl.glNormalPointer(GL.GL_FLOAT, 0, maxVertices*3*Float.SIZE/8);
		        gl.glEnable(GL2.GL_TEXTURE_GEN_S);
			    gl.glEnable(GL2.GL_TEXTURE_GEN_T);


			    gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
		        gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
		    	for(int i=0; i < maxStacks; i++ ){
		    		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP,i*(maxSlices+1)*2,(maxSlices+1)*2);
		    	}

		    	gl.glDisable(GL2.GL_TEXTURE_GEN_S);
			    gl.glDisable(GL2.GL_TEXTURE_GEN_T);

		    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	    }
	    gl.glPopMatrix();
    	}
    }
}
