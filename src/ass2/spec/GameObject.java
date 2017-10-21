package ass2.spec;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.nio.FloatBuffer;

public class GameObject {
    public int maxStacks = 20;
    public int maxSlices = 30;
    public int maxVertices = maxStacks*(maxSlices+1)*2;
    public int[] bufferIds;

    FloatBuffer verticesBuffer = FloatBuffer.allocate(maxVertices*3);
    FloatBuffer normalsBuffer = FloatBuffer.allocate(maxVertices*3);

    public GameObject(){

    }

    double r(double t){
        double x  = Math.cos(2 * Math.PI * t);
        return x;
    }

    double getY(double t){

        double y  = Math.sin(2 * Math.PI * t);
        return y;
    }

    public void generateData(){

        double deltaT;
        deltaT = 0.5/maxStacks;
        int ang;
        int delang = 360/maxSlices;
        double x1,x2,z1,z2,y1,y2;
        double radius = 0.25;
        for (int i = 0; i < maxStacks; i++)
        {
            double t = -0.25 + i*deltaT;

            for(int j = 0; j <= maxSlices; j++)
            {
                ang = j*delang;
                x1=radius * r(t)*Math.cos((double)ang*2.0*Math.PI/360.0);
                x2=radius * r(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0);
                y1 = radius * getY(t);

                z1=radius * r(t)*Math.sin((double)ang*2.0*Math.PI/360.0);
                z2= radius * r(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);
                y2 = radius * getY(t+deltaT);

                double normal[] = {x1,y1,z1};


                MathUtil.normalise(normal);

                verticesBuffer.put((float)x1);
                verticesBuffer.put((float)y1);
                verticesBuffer.put((float)z1);
                normalsBuffer.put((float)normal[0]);
                normalsBuffer.put((float)normal[1]);
                normalsBuffer.put((float)normal[2]);


                //gl.glNormal3dv(normal,0);
                //gl.glVertex3d(x1,y1,z1);
                normal[0] = x2;
                normal[1] = y2;
                normal[2] = z2;

                MathUtil.normalise(normal);
                //gl.glNormal3dv(normal,0);
                //gl.glVertex3d(x2,y2,z2);

                verticesBuffer.put((float)x2);
                verticesBuffer.put((float)y2);
                verticesBuffer.put((float)z2);
                normalsBuffer.put((float)normal[0]);
                normalsBuffer.put((float)normal[1]);
                normalsBuffer.put((float)normal[2]);

            };
        }
        verticesBuffer.rewind();
        normalsBuffer.rewind();
    }

    public void generateBuffers(GL2 gl){

        generateData();
        gl.glGenBuffers(1,bufferIds,0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
                maxVertices *3 * Float.SIZE/8 +
                        maxVertices *3 * Float.SIZE/8,
                null, GL2.GL_STATIC_DRAW);


        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0,
                maxVertices*3 *Float.SIZE/8,verticesBuffer);

        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
                maxVertices*3*Float.SIZE/8,
                maxVertices*3*Float.SIZE/8,normalsBuffer);
    }
}
