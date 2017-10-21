package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;

import static ass2.spec.MathUtil.normalizeAngle;
import static java.lang.System.exit;


/**
 * COMMENT: Comment Game
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static boolean debug =false;
    private Terrain myTerrain;
    private double distance;
    private double angle;
    private Camera camera;
    private boolean disableGlobalCamera = false;
    private Avatar myAvatar;
    private MyTexture[] myTexture= new MyTexture[17];
    private boolean firstPerson = false;
    private SunVBO  sun = null;
    private Portal portal = null;
    public Game(Terrain terrain) {
        super("Assignment 2");
        myTerrain = terrain;

    }

    /**
     * Run the game.
     *
     */
    public void run() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel panel = new GLJPanel();
        panel.addGLEventListener(this);
        panel.addKeyListener(this);
        panel.setFocusable(true);
        // Add an animator to call 'display' at 60fps
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        getContentPane().add(panel);
        setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Load a level file and display it.
     *
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        if(sun == null){
            exit(0);
        }
        GL2 gl = drawable.getGL().getGL2();
        float[] sky = sun.getSkyColor();
        gl.glClearColor(sky[0], sky[1], sky[2], 1);
//        gl.glClearColor(0f, 0f, 0f, 1);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        this.camera.cameraActivate(gl,angle, distance, disableGlobalCamera, firstPerson);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, this.sun.getLight(),0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, this.sun.getLightPosition(),0);
        //gl.glDisable(GL2.GL_LIGHT0);
        gl.glPushMatrix();{

            myTerrain.draw(gl, this.myTexture);
            sun.draw(gl, this.myTexture);
            portal.draw(gl,this.myTexture);
            if(!firstPerson) myAvatar.draw(gl,this.myTexture);
        }gl.glPopMatrix();

    }



    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        float globAmb[] = { 1, 1, 1, 1.0f };
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);


        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.

        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glEnable(GL.GL_TEXTURE_2D);

        myTexture[0] = new MyTexture(gl, "./src/ass2/spec/Texture/grass.bmp", "bmp", true);
        myTexture[1] = new MyTexture(gl, "./src/ass2/spec/Texture/leaves2.png", "png", true);
        myTexture[2] = new MyTexture(gl, "./src/ass2/spec/Texture/trunk.png", "png", true);
        myTexture[3] = new MyTexture(gl, "./src/ass2/spec/Texture/Road.png", "png", true);
        myTexture[4] = new MyTexture(gl, "./src/ass2/spec/Texture/sun.png", "png", true);
        myTexture[5] = new MyTexture(gl, "./src/ass2/spec/Texture/head.png", "png", true);
        myTexture[6] = new MyTexture(gl, "./src/ass2/spec/Texture/nose.png", "png", true);
        myTexture[7] = new MyTexture(gl, "./src/ass2/spec/Texture/body.png", "png", true);
        myTexture[8] = new MyTexture(gl, "./src/ass2/spec/Texture/legandshoulder.png", "png", true);
        myTexture[9] = new MyTexture(gl, "./src/ass2/spec/Texture/hat.png", "png", true);
        myTexture[10] = new MyTexture(gl, "./src/ass2/spec/Texture/Lights.png", "png", true);
        myTexture[11] = new MyTexture(gl, "./src/ass2/spec/Texture/portal.png", "png", true);
        myTexture[12] = new MyTexture(gl, "./src/ass2/spec/Texture/wave1.png", "png", true);
        myTexture[13] = new MyTexture(gl, "./src/ass2/spec/Texture/wave2.png", "png", true);
        myTexture[14] = new MyTexture(gl, "./src/ass2/spec/Texture/wave3.png", "png", true);
        myTexture[15] = new MyTexture(gl, "./src/ass2/spec/Texture/wave4.png", "png", true);
        myTexture[16] = new MyTexture(gl, "./src/ass2/spec/Texture/wave5.png", "png", true);

        this.myTerrain.setMyTexture(myTexture);
        portal = new Portal(this.myTerrain);
        myAvatar = new Avatar(myTerrain, gl);
        camera = new Camera(myTerrain,myAvatar);
        sun = new SunVBO(gl, (float)(this.myTerrain.size().getWidth()-1)/2,(float)(this.myTerrain.size().getHeight()-1)/2,this.myTerrain.getSunlight());
        if(sun == null){
            exit(0);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        //You can use an orthographic camera
        GLU glu = new GLU();

        glu.gluPerspective(60.0, (float)width/(float)height, 1.0, 20.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);





    }
    @Override
    public void keyPressed(KeyEvent ev) {
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (disableGlobalCamera==true) break;
                if (distance > -30.0 ) distance -= 1;
                break;
            case KeyEvent.VK_DOWN:
                if (disableGlobalCamera==true) break;
                if (distance < 0.0) distance += 1;
                break;
            case KeyEvent.VK_RIGHT:
                if (disableGlobalCamera==true) break;
                angle -= 5;
                angle = MathUtil.normalizeAngle(angle);
                break;
            case KeyEvent.VK_LEFT:
                if (disableGlobalCamera==true) break;
                angle += 5;
                angle = MathUtil.normalizeAngle(angle);
                break;

            case KeyEvent.VK_W:
                myAvatar.walk(0.1);
                hitPortal();
                break;
            case KeyEvent.VK_S:
                myAvatar.walk(-0.1);
                hitPortal();
                break;
            case KeyEvent.VK_A:
                myAvatar.rotateFacing(-10);
                break;
            case KeyEvent.VK_D:
                myAvatar.rotateFacing(10);
                break;
            case KeyEvent.VK_SPACE:
                myAvatar.jump_up();
                break;
            case KeyEvent.VK_T:
                disableGlobalCamera= !disableGlobalCamera;
                break;

            case KeyEvent.VK_R:
                if(disableGlobalCamera==true){
                    System.out.println("success");
                    firstPerson= !firstPerson;
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    private void hitPortal(){
        double increasement = 0;
        double radian = 0;
        double[] now = myAvatar.getCoordinate();
        double[] door1 = portal.getPortalCoordinate();
        double[] door2 = portal.getTeleCoordinate();
        increasement = 0.3;
        if(0<=(now[0]-door1[0])&&(now[0]-door1[0])<=0.5&&Math.abs(now[2]-door1[2])<=0.2){

            myAvatar.makeAngle(normalizeAngle(myAvatar.getAngle()-180));
            radian = Math.toRadians(myAvatar.getAngle());
            double[] p = {door2[0] + Math.cos(radian) * increasement, 0,door2[2] + Math.sin(radian) * increasement  };
            myAvatar.setCoordinate(p);
//
//            coordinate[0] =door2[0] + Math.cos(radian) * increasement;
//            coordinate[2] =door2[2] + Math.sin(radian) * increasement;
        }
        else if(0<=(now[0]-door2[0])&&(now[0]-door2[0])<=0.5&&Math.abs(now[2]-door2[2])<=0.2){
            myAvatar.makeAngle(normalizeAngle(myAvatar.getAngle()-180));
            radian = Math.toRadians(myAvatar.getAngle());
//            coordinate[0] =door1[0] + Math.cos(radian) * increasement;
//            coordinate[2] =door1[2] + Math.sin(radian) * increasement;
            double[] p = {door1[0] + Math.cos(radian) * increasement, 0,door1[2] + Math.sin(radian) * increasement  };
            myAvatar.setCoordinate(p);
        }
    }
}
