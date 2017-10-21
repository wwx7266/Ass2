package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {

	private Terrain myTerrain;
	private double angle;
	private double distance;
	private Avatar myAvatar;
	private boolean disableGlobalCamera = false;
	private boolean firstPerson = false;


	public Camera(Terrain myTerrain, Avatar myAvatar){
		this.myTerrain = myTerrain;
		this.myAvatar = myAvatar;
		//this.angle = 0;
		//this.distance = 0;
	}



	public void cameraActivate(GL2 gl,double angle, double distance, boolean disableGlobalCamera, boolean firstPerson){
		GLU glu= new GLU();
		this.disableGlobalCamera = disableGlobalCamera;
		if(disableGlobalCamera==true){
			double[] myAvatarCoorn = myAvatar.getCoordinate();

			double radius = Math.toRadians(this.myAvatar.getAngle());
			//System.out.println("gradient= "+gradient);
			double[] currentP = new double[2];
			currentP[0] = myAvatarCoorn[0];
			currentP[1] = myAvatarCoorn[2];
			//double[] solution = solution(gradient,currentP);

			System.out.println("currX = "+myAvatarCoorn[0]);
			System.out.println("currZ= "+myAvatarCoorn[2]);
//			System.out.println(solution[1]);
//
//			System.out.println(solution[2]);
//
//			System.out.println(solution[3]);
			double eyeX = myAvatarCoorn[0]-3*Math.cos(radius);
			double eyeZ = myAvatarCoorn[2]-3*Math.sin(radius);

			double obX = myAvatarCoorn[0]+3*Math.cos(radius);
			double obZ = myAvatarCoorn[2]+3*Math.sin(radius);
			System.out.println("eyeX ="+eyeX);
			System.out.println("eyeZ ="+eyeZ);
			System.out.println("obX ="+obX);
			System.out.println("obZ ="+obZ);

			if(!firstPerson) glu.gluLookAt(eyeX, myTerrain.altitude(myAvatarCoorn[0], myAvatarCoorn[2])+2, eyeZ, obX, myTerrain.altitude(myAvatarCoorn[0], myAvatarCoorn[2])+2, obZ, 0.0, 1.0, 0.0);
			else glu.gluLookAt(myAvatarCoorn[0], myTerrain.altitude(myAvatarCoorn[0], myAvatarCoorn[2])+2, myAvatarCoorn[2], obX, myTerrain.altitude(myAvatarCoorn[0], myAvatarCoorn[2])+2, obZ, 0.0, 1.0, 0.0);
		}
		else{
			//System.out.println("T disabledt");
			this.angle = angle;
			this.distance = distance;

			//glu.gluLookAt(centerX, 1, 15, centerX, 0.0, centerZ, 0.0, 1.0, 0.0);
			float centerX = (float)((this.myTerrain.size().getWidth()-1)/2);
			float centerZ = (float)((this.myTerrain.size().getHeight()-1)/2);

			float[] roatatePoints = this.rotatePoint(centerX, (float)(15+distance));
			float rotateX = roatatePoints[0];
			float rotateZ = roatatePoints[1];

			//System.out.println("rotatex = "+rotateX);
			//System.out.println("rotatez = "+rotateZ);
			if(rotateX>=0 && rotateX<=this.myTerrain.size().getWidth()-1&&rotateZ>=0 && rotateZ<=this.myTerrain.size().getHeight()-1) {
				//System.out.println("rotateatt= "+ myTerrain.altitude(rotateX, rotateZ));
				glu.gluLookAt(rotateX, myTerrain.altitude(rotateX, rotateZ)+2, rotateZ, centerX, myTerrain.altitude(rotateX, rotateZ)+1, centerZ, 0.0, 1.0, 0.0);
			}else  glu.gluLookAt(rotateX, 2, rotateZ, centerX, 0.0, centerZ, 0.0, 1.0, 0.0);
		}

	}





	public float[] rotatePoint(float px, float py){
		float ox = (float)((this.myTerrain.size().getWidth()-1)/2);
		float oy = (float)((this.myTerrain.size().getHeight()-1)/2);
		float[] points = new float[2];
		float redians = (float) Math.toRadians(angle);
		points[0]= (float) (Math.cos(redians) * (px-ox) - Math.sin(redians) * (py-oy) + ox);
		points[1]= (float) (Math.sin(redians) * (px-ox) + Math.cos(redians) * (py-oy) + oy);
		return points;

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
				angle = MathUtil.normalizeAngle(angle);
				break;
			case KeyEvent.VK_LEFT:
				if (disableGlobalCamera==true) break;
				angle += 5;
				angle = MathUtil.normalizeAngle(angle);
				break;

//
//			case KeyEvent.VK_W:
//				myAvatar.move(0.1);;
//				break;
//			case KeyEvent.VK_S:
//				myAvatar.move(-0.1);;
//				break;
//			case KeyEvent.VK_A:
//				myAvatar.changeAngle(-5);
//				break;
//			case KeyEvent.VK_D:
//				myAvatar.changeAngle(5);
//				break;
//			case KeyEvent.VK_T:
//				disableGlobalCamera= !disableGlobalCamera;
//				break;
//
//			case KeyEvent.VK_R:
//				if(disableGlobalCamera==true){
//					System.out.println("success");
//					firstPerson= !firstPerson;
//				}
//				break;
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
}
