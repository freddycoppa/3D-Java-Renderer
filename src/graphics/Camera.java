package graphics;

public class Camera {
	public static final boolean HORIZONTAL = true, VERTICAL = false;
	
	Vector normal, observer;
	private Vector[] bases; // 0th index is x, 1st index is z
	private static final Vector zAxis = new Vector(0, 0, 1);
	
	public Camera(Vector normal, Vector observer, Vector[] bases) {
		this.normal = normal;
		this.observer = observer;
		this.bases = bases;
	}
	
	public Camera() {
		this(
			new Vector(0.0f, 1.0f, 0.0f), 
			new Vector(0.0f, 0.0f, 0.0f), 
			new Vector[] {
				new Vector(1.0f, 0.0f, 0.0f), 
				new Vector(0.0f, 0.0f, 1.0f)
			}
		);
	}
	
	public Vector point() {
		return this.normal.plus(this.observer);
	}
	
	public void rotate(boolean axis, double theta) {
		if (axis) {
			this.normal.rotate(zAxis, theta);
			this.bases[0].rotate(zAxis, theta);
			this.bases[1].rotate(zAxis, theta);
		}
		else {
			this.normal.rotate(this.bases[0], theta);
			this.bases[1].rotate(this.bases[0], theta);
		}
	}
	
	public void move(double d) {
		this.observer.add(normal.times(d));
	}
	
	private double getSign(Vector v) {
		return Math.signum(normal.dot(v.minus(this.point())));
	}
	
	public Vector translate(Vector v) {
		Vector translation = null;
		if (getSign(v) != getSign(observer)) {
			Vector n = this.normal, m = this.observer.minus(v), p = this.point();
			double d = -n.dot(p);
			double mdotn = m.dot(n);
			translation = new Vector(
					v.x*(m.y*n.y + m.z*n.z) - m.x*(v.y*n.y + v.z*n.z + d), 
					v.y*(m.z*n.z + m.x*n.x) - m.y*(v.z*n.z + v.x*n.x + d),
					v.z*(m.x*n.x + m.y*n.y) - m.z*(v.x*n.x + v.y*n.y + d)
				).times(1 / mdotn).subtract(p);
		}
		return translation;
	}
	
	public Vector2D project(Vector translation, int width, int height) {
	    Vector2D projection = new Vector2D();
		int side = width < height ? width : height;
		projection.x = (width  / 2) + (int) Math.floor((+ translation.x*bases[1].z - translation.z*bases[1].x) * side);
		projection.y = (height / 2) - (int) Math.floor((- translation.x*bases[0].z + translation.z*bases[0].x) * side);
		return projection;
	}
}
