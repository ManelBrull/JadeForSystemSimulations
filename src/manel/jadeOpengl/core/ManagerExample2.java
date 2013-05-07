package manel.jadeOpengl.core;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2.GL_QUADS;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import jade.core.Agent;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.FPSAnimator;
// GL constants
// GL2 constants
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * NeHe Lesson #5: 3D Shapes
 * @author Hock-Chuan Chua
 * @version May 2012
 */
@SuppressWarnings("serial")
public class ManagerExample2 extends Agent implements GLEventListener {
	// Define constants for the top-level container
	private static String TITLE = "Tile Map";
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable
	private static final int FPS = 60; // animator's target frames per second

	private Texture[] texture; // Place to store the slices of the map
	private int tileSize = 64; // Size of the thile
	
	private float xAgent = 15.0f;
	private float yAgent = 15.0f;
	private float xAgent2 = 400.0f;
	private float yAgent2 = 15.0f;
	
	/** The entry main() method to setup the top-level container and animator */
	protected void setup() {
		// Run the GUI codes in the event-dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create the OpenGL rendering canvas
				GLCanvas canvas = new GLCanvas();
				canvas.addGLEventListener(new ManagerExample2());
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

				// Create a animator that drives canvas' display() at the specified FPS. 
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

				// Create the top-level container
				final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
				frame.getContentPane().add(canvas);
				frame.addWindowListener(new WindowAdapter() {
					@Override 
					public void windowClosing(WindowEvent e) {
						// Use a dedicate thread to run the stop() to ensure that the
						// animator stops before program exits.
						new Thread() {
							@Override 
							public void run() {
								if (animator.isStarted()) animator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				frame.setTitle(TITLE);
				frame.pack();
				frame.setVisible(true);
				animator.start(); // start the animation loop
			}
		});
	}

	// Setup OpenGL Graphics Renderer
	private GLU glu;  // for the GL Utility
	// ------ Implement methods declared in GLEventListener ------

	/**
	 * Called back immediately after the OpenGL context is initialized. Can be used 
	 * to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
		glu = new GLU();                         // get GL Utilities
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f);      // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glEnable(GL_TEXTURE_2D);
		gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
		this.loadSlices(drawable, "maps/map_1/", TextureIO.PNG, 6);
	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable is 
	 * first set to visible.
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

		if (height == 0) height = 1;   // prevent divide by zero

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
		gl.glLoadIdentity();             // reset projection matrix
		gl.glOrthof(0.0f, width, height, 0.0f, -1.0f, 1.0f);
		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		update();
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity();  // reset the model-view matrix
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GLU.GLU_FILL);
		int actTexture = 0;
		for(int y = 0; y < 7; y++){
			for(int x = 0; x < 7; x++){
				if(actTexture == 5) actTexture = 0;
				texture[actTexture].enable(gl);
				texture[actTexture].bind(gl);
				int pixelX=x*tileSize;
				int pixelY=y*tileSize;
				gl.glBegin(GL_QUADS);
				gl.glTexCoord2f(0.0f,1.0f);
				gl.glVertex2f(pixelX,pixelY);
				gl.glTexCoord2f(1.0f,1.0f);
				gl.glVertex2f(pixelX+tileSize,pixelY);
				gl.glTexCoord2f(1.0f,0.0f);
				gl.glVertex2f(pixelX+tileSize,pixelY+tileSize);
				gl.glTexCoord2f(0.0f,0.0f);
				gl.glVertex2f(pixelX,pixelY+tileSize);
				gl.glEnd();
				actTexture+=1;
			}
		}
		texture[5].enable(gl);
		texture[5].bind(gl);
		gl.glBegin(GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex2d(xAgent, yAgent);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex2d(xAgent+30, yAgent);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex2d(xAgent+30, yAgent+30);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex2d(xAgent, yAgent+30);
		gl.glEnd();
		
		texture[5].enable(gl);
		texture[5].bind(gl);
		gl.glBegin(GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex2d(xAgent2, yAgent2);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex2d(xAgent2+30, yAgent2);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex2d(xAgent2+30, yAgent2+30);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex2d(xAgent2, yAgent2+30);
		gl.glEnd();
		
	}

	/** 
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers. 
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) { }
	
	/**
	 * Update method for our variables
	 */
	private void update(){
		if(checkColision()){
			this.xAgent2+= 3.0f;
			this.yAgent2-= 3.0f;
			if(this.xAgent2 < 20.0f){
				this.xAgent2 = 400.0f;
				this.yAgent2 = 15.0f;
			}	
		}
		this.xAgent+= 1.0f;
		this.yAgent+= 1.0f;
		if(this.xAgent > 400.0f){
			this.xAgent = 15.0f;
			this.yAgent = 15.0f;
		}
		this.xAgent2-= 1.0f;
		this.yAgent2+= 1.0f;
		if(this.xAgent2 < 20.0f){
			this.xAgent2 = 400.0f;
			this.yAgent2 = 15.0f;
		}
	}
	
	private boolean checkColision(){
		if(xAgent < xAgent2 + 30 && xAgent2 < xAgent + 30 && yAgent < yAgent2 + 30)
			return yAgent2 < yAgent + 30;
		return false;
	}
	
	/**
	 * Method for loading slices
	 */
	private void loadSlices(GLAutoDrawable drawable, String path, String textureFileType, int num){
		GL2 gl = drawable.getGL().getGL2();
		this.texture = new Texture[num];
		for(int i = 0; i < num; i++){
			InputStream is = getClass().getClassLoader().getResourceAsStream(path+"tile"+i+"."+textureFileType);
			try {
				this.texture[i] = TextureIO.newTexture(is, false, textureFileType);
				// Use linear filter for texture if image is larger than the original texture
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				// Use linear filter for texture if image is smaller than the original texture
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			} catch (GLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}