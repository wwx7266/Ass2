package ass2.spec;

import com.jogamp.opengl.GL2;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    private MyTexture Mytextures[] = new MyTexture[12];
    private int frames = 0;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {



        int upper_x = (int) Math.ceil(x);
        int lower_x = (int) Math.floor(x);
        int upper_z = (int) Math.ceil(z);
        int lower_z = (int) Math.floor(z);

        if((int)x == x || (int)z == z)
            return(getGridAltitude((int)x, (int)z));

//        int[] lP = {lower_x, lower_z};
//        int[] uP = {upper_x, upper_z};

        double ratio_a = (getGridAltitude(upper_x, upper_z) - getGridAltitude(lower_x, upper_z))/(upper_x - lower_x);
        double ratio_b = (getGridAltitude(upper_x, lower_z) - getGridAltitude(lower_x, lower_z))/(upper_x - lower_x);

        double distance_a = (ratio_a * (x - lower_x) + getGridAltitude(lower_x, upper_z));
        double distance_b = (ratio_b * (x - lower_x) + getGridAltitude(lower_x, lower_z));
        double altitude = ((distance_b-distance_a)/(upper_z-lower_z) * (z - lower_z) + distance_a);
//        System.out.println("altitude is : " + altitude);

        return altitude;
    }

    public void draw(GL2 gl, MyTexture[] texture) {
        frames += 1;
        if(frames > 1000)
            frames = 0;

        int index = 12+(this.frames/10)%5;
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glPushMatrix();

        float matAmb[] = {0.2f, 0.2f, 0.2f, 1.0f};
        float matdiff[] = { 0.2f, 1f, 0f, 1.0f };

        //gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0].getTextureId());
        // Material properties of teapot
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmb,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matdiff,0);

        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[index].getTextureId());

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);



        //draw the mesh
        for(int z = 0; z<myAltitude.length-1; z++)
            for(int x = 0; x<myAltitude.length-1; x++){
                double[] p1 = new double[] {x+1, altitude(x+1, z), z};
                double[] p2 = new double[] {x, altitude(x, z), z};
                double[] p3 = new double[] {x, altitude(x, z+1), z+1};
                double[] normal = MathUtil.normal(p1, p2, p3);
                gl.glBegin(GL2.GL_TRIANGLES);
                gl.glNormal3d(normal[0],normal[1],normal[2]);
                gl.glTexCoord2d(0, 0);
                gl.glVertex3d(p1[0], p1[1], p1[2]);
                gl.glTexCoord2d(1, 0);
                gl.glVertex3d(p2[0], p2[1], p2[2]);
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(p3[0], p3[1], p3[2]);
                gl.glEnd();

                p1 = new double[] {x+1, getGridAltitude(x+1, z), z};
                p2 = new double[] {x, getGridAltitude(x, z+1), z+1};
                p3 = new double[] {x+1, getGridAltitude(x+1, z+1), z+1};
                normal = MathUtil.normal(p1, p2, p3);
                gl.glBegin(GL2.GL_TRIANGLES);
                gl.glNormal3d(normal[0],normal[1],normal[2]);
                gl.glTexCoord2d(0, 0);
                gl.glVertex3d(p1[0], p1[1], p1[2]);
                gl.glTexCoord2d(1, 0);
                gl.glVertex3d(p2[0], p2[1], p2[2]);
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(p3[0], p3[1], p3[2]);
                gl.glEnd();


            }

//        gl.glDisable(GL2.GL_TEXTURE_2D);
//        gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_FALSE);

        for(Tree t:this.myTrees){
            t.draw(gl, texture);
        }

        for(Road r:this.roads()){
            r.draw(gl, texture);
        }
        gl.glPopMatrix();
    }
    	
    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param
     * @param
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        road.setMyTerrain(this);
        myRoads.add(road);        
    }

    public void setMyTexture(MyTexture textures[]){
        this.Mytextures = textures;
    }

}
