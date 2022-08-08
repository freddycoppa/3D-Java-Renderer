package graphics;

public class Triangle {
	public Vector a, b, c;
	
	public Triangle(Vector a, Vector b, Vector c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Override
	public String toString() {
		return "[\n" + this.a.toString() + ",\n" + this.b.toString() + ",\n" + this.c.toString() + "\n]\n";
	}
}
