package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main extends Canvas implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	
	static class Line {
		Vector a, b;
		
		Line(Vector a, Vector b) {
			this.a = a;
			this.b = b;
		}
	}
	
	private int width, height;
	private Camera camera;
	
	private List<Triangle> mesh /*= {
			new Triangle(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(1, 1, 0)),
			new Triangle(new Vector(0, 0, 0), new Vector(1, 1, 0), new Vector(1, 0, 0)),
			
			new Triangle(new Vector(1, 0, 0), new Vector(1, 1, 0), new Vector(1, 1, 1)),
			new Triangle(new Vector(1, 0, 0), new Vector(1, 1, 1), new Vector(1, 0, 1)),
			
			new Triangle(new Vector(1, 0, 1), new Vector(1, 1, 1), new Vector(0, 1, 1)),
			new Triangle(new Vector(1, 0, 1), new Vector(0, 1, 1), new Vector(0, 0, 1)),
			
			new Triangle(new Vector(0, 0, 1), new Vector(0, 1, 1), new Vector(0, 1, 0)),
			new Triangle(new Vector(0, 0, 1), new Vector(0, 1, 0), new Vector(0, 0, 0)),
			
			new Triangle(new Vector(0, 1, 0), new Vector(0, 1, 1), new Vector(1, 1, 1)),
			new Triangle(new Vector(0, 1, 0), new Vector(1, 1, 1), new Vector(1, 1, 0)),
			
			new Triangle(new Vector(1, 0, 1), new Vector(0, 0, 1), new Vector(0, 0, 0)),
			new Triangle(new Vector(1, 0, 1), new Vector(0, 0, 0), new Vector(1, 0, 0))
	}*/;
	
	private Line[] cube = {
			new Line(new Vector(0, 0, 0), new Vector(1, 0, 0)),
			new Line(new Vector(1, 0, 0), new Vector(1, 1, 0)),
			new Line(new Vector(1, 1, 0), new Vector(0, 1, 0)),
			new Line(new Vector(0, 1, 0), new Vector(0, 0, 0)),
			new Line(new Vector(0, 0, 0), new Vector(0, 0, 1)),
			new Line(new Vector(1, 0, 0), new Vector(1, 0, 1)),
			new Line(new Vector(1, 1, 0), new Vector(1, 1, 1)),
			new Line(new Vector(0, 1, 0), new Vector(0, 1, 1)),
			new Line(new Vector(0, 0, 1), new Vector(1, 0, 1)),
			new Line(new Vector(1, 0, 1), new Vector(1, 1, 1)),
			new Line(new Vector(1, 1, 1), new Vector(0, 1, 1)),
			new Line(new Vector(0, 1, 1), new Vector(0, 0, 1))
	};
	
	Main(int width, int height) {
		this.width = width;
		this.height = height;
		this.camera = new Camera();
		Vector displacement = new Vector(5, 5, 5);
		for (var l : this.cube) {
			l.a.add(displacement);
			l.b.add(displacement);
		}
		
		this.mesh = List.of(
		    new Triangle(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(1, 1, 0)),
			new Triangle(new Vector(0, 0, 0), new Vector(1, 1, 0), new Vector(1, 0, 0)),
			
			new Triangle(new Vector(1, 0, 0), new Vector(1, 1, 0), new Vector(1, 1, 1)),
			new Triangle(new Vector(1, 0, 0), new Vector(1, 1, 1), new Vector(1, 0, 1)),
			
			new Triangle(new Vector(1, 0, 1), new Vector(1, 1, 1), new Vector(0, 1, 1)),
			new Triangle(new Vector(1, 0, 1), new Vector(0, 1, 1), new Vector(0, 0, 1)),
			
			new Triangle(new Vector(0, 0, 1), new Vector(0, 1, 1), new Vector(0, 1, 0)),
			new Triangle(new Vector(0, 0, 1), new Vector(0, 1, 0), new Vector(0, 0, 0)),
			
			new Triangle(new Vector(0, 1, 0), new Vector(0, 1, 1), new Vector(1, 1, 1)),
			new Triangle(new Vector(0, 1, 0), new Vector(1, 1, 1), new Vector(1, 1, 0)),
			
			new Triangle(new Vector(1, 0, 1), new Vector(0, 0, 1), new Vector(0, 0, 0)),
			new Triangle(new Vector(1, 0, 1), new Vector(0, 0, 0), new Vector(1, 0, 0)));
		
		
		 /* this.mesh = new ArrayList<Triangle>();
		  
		  try { File teapot = new
		  File("C:\\dev\\java\\graphics\\src\\graphics\\teapot.obj.txt"); Scanner
		  scanner = new Scanner(teapot); List<Vector> vertices = new
		  LinkedList<Vector>(); while (scanner.hasNext()) { char type =
		  scanner.next().charAt(0); if (type == 'v') { double x = scanner.nextDouble(),
		  z = scanner.nextDouble(), y = scanner.nextDouble(); vertices.add(new
		  Vector(x, y, z)); } else if (type == 'f') { this.mesh.add(new Triangle(
		  vertices.get(scanner.nextInt() - 1), vertices.get(scanner.nextInt() - 1),
		  vertices.get(scanner.nextInt() - 1) )); } scanner.nextLine(); }
		  scanner.close(); } catch (FileNotFoundException e) { e.printStackTrace(); }*/
		 
	}
	
	/*
	 * private void drawLine(Vector p, Vector q, Graphics gfx) { Vector2D a =
	 * this.camera.project(p, this.width, this.height), b = this.camera.project(q,
	 * this.width, this.height); gfx.drawLine(a.x, a.y, b.x, b.y); }
	 */
	
	private void drawTriangle(Triangle translation, Graphics gfx, double brightness) {
		Vector2D a = this.camera.project(translation.a, this.width, this.height), 
				 b = this.camera.project(translation.b, this.width, this.height),
				 c = this.camera.project(translation.c, this.width, this.height);
		Polygon p = new Polygon();
		p.addPoint(a.x, a.y);
		p.addPoint(b.x, b.y);
		p.addPoint(c.x, c.y);
		int x = (int) Math.floor(brightness * 255);
		if (x > 255) x = 255;
		else if (x < 0) x = 0;
		gfx.setColor(new Color(255, 255, 255));
		gfx.drawPolygon(p);
	}
	
	// TODO depth buffer
	Map<Double, Triangle> depthMap = new HashMap<Double, Triangle>();
	
	private void drawMesh(List<Triangle> mesh, Graphics gfx) {
		depthMap.clear();
		for (var tri : mesh) {
			depthMap.put((tri.a.y + tri.b.y + tri.c.y) / 3, tri);
		}
		List<Double> depths = new ArrayList<Double>(depthMap.keySet());
		depths.sort(new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				return Double.compare(o1, o2);
			}
			
		});
		Collections.reverse(depths);
		for (var f : depths) {
			var tri = depthMap.get(f);
			//Vector normal = tri.b.minus(tri.a).cross(tri.c.minus(tri.a));
		    //normal.scale(1 / normal.length());
			var translation = new Triangle(this.camera.translate(tri.a), this.camera.translate(tri.b), this.camera.translate(tri.c));
			if ((translation.a != null) && (translation.b != null) && (translation.c != null)) 
				/*
				 * if
				 * (translation.b.minus(translation.a).cross(translation.c.minus(translation.a))
				 * .dot(this.camera.normal) < 0)
				 */ 
			    	this.drawTriangle(translation, gfx, tri.b.minus(tri.a).cross(tri.c.minus(tri.a)).dot(this.camera.normal.times(-1)));
		}
	}
	
	private void renderMesh(List<Triangle> mesh, Graphics gfx) {
		for (var tri : mesh) {
			var translation = new Triangle(this.camera.translate(tri.a), this.camera.translate(tri.b), this.camera.translate(tri.c));
			if ((translation.a != null) && (translation.b != null) && (translation.c != null)) 
				//if (translation.b.minus(translation.a).cross(translation.c.minus(translation.a)).dot(this.camera.normal) < 0)
			{
				Vector2D a = this.camera.project(translation.a, this.width, this.height), 
						 b = this.camera.project(translation.b, this.width, this.height),
						 c = this.camera.project(translation.c, this.width, this.height);
				Polygon p = new Polygon();
				p.addPoint(a.x, a.y);
				p.addPoint(b.x, b.y);
				p.addPoint(c.x, c.y);
				gfx.setColor(Color.white);
				gfx.drawPolygon(p);
			}
		}
	}
	
	private void drawLines(Line[] lines, Graphics gfx) {
		for (var line : lines) {
			var translation =  new Line(this.camera.translate(line.a), this.camera.translate(line.b));
			if ((translation.a != null) && (translation.b != null)) {
				Vector2D a = this.camera.project(translation.a, this.width, this.height), 
						 b = this.camera.project(translation.b, this.width, this.height);
				gfx.setColor(Color.WHITE);
				gfx.drawLine(a.x, a.y, b.x, b.y);
			}
		}
	}
	
	private Thread thread;
	private boolean running;

	public void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0.0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1.0 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;

			while (unprocessedSeconds > secondsPerTick) {
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					System.out.println(frames + " fps");
					previousTime += 1000;
					frames = 0;
				}
			}

			if (ticked) {
				render();
				frames++;
			}
			render();
			frames++;
		}
	}
	
	private boolean paused = true;
	
	private static final Vector axis = new Vector(0, 1, 0);

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		this.renderMesh(this.mesh, g);
		if (!paused) for (var triangle : this.mesh) {
			triangle.a.rotate(axis, 0.001);
			triangle.b.rotate(axis, 0.001);
			triangle.c.rotate(axis, 0.001);
		}
		g.dispose();
		bs.show();
	}
	
	/*private void render() {
		System.out.println(this.mesh.get(0).toString());
		this.mesh.get(0).a.rotate(axis, 0.1);
		this.mesh.get(0).b.rotate(axis, 0.1);
		this.mesh.get(0).c.rotate(axis, 0.1);
		System.out.println(this.mesh.get(0).toString());
		System.exit(0);
	}*/

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			this.camera.rotate(Camera.VERTICAL, +0.05f);
            break;
        case KeyEvent.VK_DOWN:
        	this.camera.rotate(Camera.VERTICAL, -0.05f);
            break;
        case KeyEvent.VK_LEFT:
        	this.camera.rotate(Camera.HORIZONTAL, +0.05f);
            break;
        case KeyEvent.VK_RIGHT :
        	this.camera.rotate(Camera.HORIZONTAL, -0.05f);
            break;
        case KeyEvent.VK_W:
        	this.camera.move(+0.5f);
        	break;
        case KeyEvent.VK_S:
        	this.camera.move(-0.5f);
        	break;
        case KeyEvent.VK_K:
        	this.paused = !this.paused;
        	break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {
		final int width = 1200;
		final int height = 675;
		final String title = "Lines";
		Main obj = new Main(width, height);
		JFrame frame = new JFrame();
		frame.add(obj);
		frame.addKeyListener(obj);
		frame.pack();
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		obj.start();
	}
}