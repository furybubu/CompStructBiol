package crowdPhase.basic;


/**
 * Defines a basic point with cartesian coordinates
 * 
 * ****************************************
 * @author jorda
 * @created Dec 7, 2012
 * @version 0.1
 ******************************************
 */
public class Point implements Cloneable{
	
	public double x;
	public double y;
	public double z;
	
	
	public Point(){
		
	}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void translate (double x, double y, double z) {
		this.x+=x;
		this.y+=y;
		this.z+=z;
	}
	public void translate (Vector v) {
		this.x+=v.x;
		this.y+=v.y;
		this.z+=v.z;
	}
	
	public void roundCoordinates(int nbDecimals) {
		this.x= (double)Math.round((this.x) * Math.pow(10,nbDecimals)) / Math.pow(10,nbDecimals);
		this.y= (double)Math.round((this.y) * Math.pow(10,nbDecimals)) / Math.pow(10,nbDecimals);
		this.z= (double)Math.round((this.z) * Math.pow(10,nbDecimals)) / Math.pow(10,nbDecimals);
	}
	
	public double getDistance(Point p) {
		return (Math.sqrt(Math.pow((x-p.x),2)+Math.pow((y-p.y),2)+Math.pow((z-p.z),2)));
	} 
	public double getSquaredDistance(Point p) {
		return ((Math.pow((x-p.x),2)+Math.pow((y-p.y),2)+Math.pow((z-p.z),2)));
	} 
	
	/** GETTERS AND SETTERS **/
	
	public double getX() {	return x;	}

	public void setX(double x) {		this.x = x;	}

	public double getY() {		return y;	}

	public void setY(double y) {		this.y = y;	}

	public double getZ() {		return z;	}

	public void setZ(double z) {		this.z = z; }
	
	public Vector getVector() {	Vector v = new Vector(x, y, z);	return v;	}
	
	
	/** OBJECT OVERRIDES */
	


	@Override
	public String toString() {
		return "x=" + x + ", y=" + y + ", z=" + z + " ";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		Point other = (Point) obj;
		
		if (x!=other.x)
			return false;
		if (y!=other.y)
			return false;
		if (z!=other.z)
			return false;
		/*if (Math.abs(x-other.x)>=0.001) {
			return false;
		}
		if (Math.abs(y-other.y)>=0.001)
			return false;
		if (Math.abs(z-other.z)>=0.001)
			return false;
		return true;*/
		return true;
	}

	
	public Object clone() {
		try
		{
			return super.clone();
		}
		catch(Exception e){ return null; }
		}

}
