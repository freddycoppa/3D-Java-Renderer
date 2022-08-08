package graphics;

public class Vector {
	double x, y, z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector that) {
		this(that.x, that.y, that.z);
	}
	
	public double length() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector scale(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		
		return this;
	}
	
	public Vector add(Vector that) {
		this.x += that.x;
		this.y += that.y;
		this.z += that.z;
		
		return this;
	}
	
	public Vector subtract(Vector that) {
		this.x -= that.x;
		this.y -= that.y;
		this.z -= that.z;
		
		return this;
	}
	
	public Vector rotate(Vector axis, double theta) {
		Vector p = axis.times(this.dot(axis) / axis.dot(axis));
		Vector m = this.minus(p);
		Vector n = axis.cross(m).scale(1 / axis.length());
		Vector r = m.times(Math.cos(theta)).plus(n.times(Math.sin(theta))).plus(p);
		this.x = r.x;
		this.y = r.y;
		this.z = r.z;
		/*Vector k = axis.times(1.0f / axis.length());
		Vector result = this.times((double) Math.cos(theta));
		result.add(k.cross(this).scale((double) Math.sin(theta)));
		result.add(k.scale(k.dot(this) * (double) (1.0 - Math.cos(theta))));
		this.x = result.x;
		this.y = result.y;
		this.z = result.z;*/
		return this;
	}
	
	public Vector times(double scalar) {
		return new Vector(this).scale(scalar);
	}
	
	public Vector plus(Vector that) {
		return new Vector(this).add(that);
	}
	
	public Vector minus(Vector that) {
		return new Vector(this).subtract(that);
	}
	
	public Vector rotatedBy(Vector axis, double theta) {
		Vector p = axis.times(this.dot(axis) / axis.dot(axis));
		Vector m = this.minus(p);
		Vector n = axis.cross(m).scale(1 / axis.length());
		return m.times(Math.cos(theta)).plus(n.times(Math.sin(theta))).plus(p);
	}
	
	public Vector cross(Vector that) {
		double x = this.y*that.z - this.z*that.y;
		double y = this.z*that.x - this.x*that.z;
		double z = this.x*that.y - this.y*that.x;
		return new Vector(x, y, z);
	}
	
	public double dot(Vector that) {
		return this.x * that.x + this.y * that.y + this.z * that.z;
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
