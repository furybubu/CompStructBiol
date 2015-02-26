package crowdPhase.basic;

public class Vector {

	
	public double x;
	public double y;
	public double z;
	
	public Vector(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector scale(int s) {
		return new Vector(this.x*s,this.y*s,this.z*s);
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
