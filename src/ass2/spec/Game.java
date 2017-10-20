package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener{

    private Terrain myTerrain;
    private MyTexture myTexture[] = new MyTexture[12];
    private Camera camera;
    private Avatar Myavatar;


    private static final long serialVersionUID = 1L;
    public static boolean debug =false;
    private double distance;
    private double angle;
    private boolean disableGlobalCamera = false;
    private Avatar myAvatar;
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
        System.out.println("goes here");

        Terrain terrain = LevelIO.load(new File(args[0]));

        Game game = new Game(terrain);

        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
        //setup stuff
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1);

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();



        //Set camera
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        this.camera.cameraActivate(gl,angle, distance, disableGlobalCamera, firstPerson);


        gl.glPushMatrix();
        // Draw terrain
        myTerrain.draw(gl, this.myTexture);
        gl.glPopMatrix();

    }

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
        float globAmb[] = {1,1,1,1.0f};
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);

        // turn on a light. Use default settings.
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.

        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glEnable(GL.GL_TEXTURE_2D);

        myTexture[0] = new MyTexture(gl, "grass6.png", "png", true);
        myTexture[1] = new MyTexture(gl, "leaves2.png", "png", true);
        myTexture[2] = new MyTexture(gl, "trunk.png", "png", true);
        myTexture[3] = new MyTexture(gl, "Road.png", "png", true);
        myTexture[4] = new MyTexture(gl, "sun.png", "png", true);
        myTexture[5] = new MyTexture(gl, "head.png", "png", true);
        myTexture[6] = new MyTexture(gl, "nose.png", "png", true);
        myTexture[7] = new MyTexture(gl, "body.png", "png", true);
        myTexture[8] = new MyTexture(gl, "legandshoulder.png", "png", true);
        myTexture[9] = new MyTexture(gl, "hat.png", "png", true);
        myTexture[10] = new MyTexture(gl, "light.png", "png", true);
        myTexture[11] = new MyTexture(gl, "portal.png", "png", true);
        this.myTerrain.setMyTexture(myTexture);
//        portal = new Portal(this.myTerrain);
//        myAvatar = new Avatar(myTerrain, gl,portal);
        camera = new Camera(myTerrain,Myavatar);

    }

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
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
                System.out.println("distance-");
                break;
            case KeyEvent.VK_DOWN:
                if (disableGlobalCamera==true) break;
                if (distance < 0.0) distance += 1;
                System.out.println("distance+");
                break;
            case KeyEvent.VK_RIGHT:
                if (disableGlobalCamera==true) break;
                angle -= 5;
                angle = normalizeAngle(angle);
                break;
            case KeyEvent.VK_LEFT:
                if (disableGlobalCamera==true) break;
                angle += 5;
                angle = normalizeAngle(angle);
                break;
            case KeyEvent.VK_W:
                myAvatar.move(0.1);;
                break;
            case KeyEvent.VK_S:
                myAvatar.move(-0.1);;
                break;
            case KeyEvent.VK_A:
                myAvatar.changeAngle(-5);
                break;
            case KeyEvent.VK_D:
                myAvatar.changeAngle(5);
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
//    	case KeyEvent.VK_L:
//    		sunlight = !sunlight;
//
//    		break;
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
    double normalizeAngle(double angle)
    {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }
}
