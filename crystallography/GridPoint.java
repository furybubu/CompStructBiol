package crowdPhase.crystallography;

import crowdPhase.basic.Point;

/**
 * A class implementing a point in the grid of an electron Density Map.
 * 
 ******************************************
 * @author jorda
 * @created Oct 31, 2012
 * @version
 ******************************************
 */
public class GridPoint extends Point  implements  Cloneable, Comparable<GridPoint>{
	
	public static int orderXYZ=0;
	public static int orderZYX=1;
	public static int sortByDensity = 2;
	
	public static int order=orderXYZ;
	
	private double density;
	private boolean flag;//a two state flag of the gridpoint
	private int relX,relY,relZ;	//the relative position in the grid, corresponding to indices
								//along each axis
	
	private double fracX,fracY,fracZ;//the fractional coordinates

	public GridPoint () {
		//
		super();
		this.density = 0;
		this.flag=false;
		relX=relY=relZ=0;
		fracX=fracY=fracZ=0;
	}
	
	public GridPoint (double x, double y, double z, double dens) {
		super(x,y,z);
		this.density=dens; 
		this.flag=false;
		relX=relY=relZ=0;
		fracX=fracY=fracZ=0;
	}

	
	public double getDensity() {		return density;	}	
	
	public void setDensity(double density) {		this.density = density;	}
	
	
	
	public boolean isFlag() {	return flag;	}

	public void setFlag(boolean flag) {		this.flag = flag;}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return super.toString()+" density: "+this.density;
	}
	
	
	public int compareTo(GridPoint p) {
		if (order==sortByDensity) {
			return Double.compare(density,p.density);
		}else if (order==orderZYX) {
			if(Double.compare(z,p.z)!=0)  return Double.compare(z,p.z);
			
		    if(Double.compare(y,p.y)!=0) return Double.compare(y,p.y);
		    
			if(Double.compare(x,p.x)!=0)  return Double.compare(x,p.x);
			return 0;
		}else {
			if(x<p.x) {
		    	return -1;
		    }else if (x>p.x) {
		    	return 1;
		    }else {
		    	if(y<p.y) {
			    	return -1;
			    }else if (y>p.y) {
			    	return 1;
			    }else {
			    	if(z<p.z) {
				    	return -1;
				    }else if (z>p.z) {
				    	return 1;
				    }
			    }
		    	return 0;
		    }
		}
	}
	
	public boolean equals (GridPoint gp) {
		return (density == gp.density && x ==gp.x && y == gp.y && z== gp.z);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(x+y+z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	
	public void setRelX(int x) { this.relX = x; }
	public void setRelY(int y) { this.relY = y; }
	public void setRelZ(int z) { this.relZ = z; }
	public int getRelX() { return this.relX; }
	public int getRelY() { return this.relY; }
	public int getRelZ() { return this.relZ; }

	public double getFracX() {		return fracX;	}
	public void setFracX(double fracX) {		this.fracX = fracX;	}
	public double getFracY() {		return fracY;	}
	public void setFracY(double fracY) {		this.fracY = fracY;	}
	public double getFracZ() {		return fracZ;	}
	public void setFracZ(double fracZ) {		this.fracZ = fracZ;	}
	
	public static int getOrderXYZ() {		return orderXYZ;	}
	public static void setOrderXYZ(int orderXYZ) {		GridPoint.orderXYZ = orderXYZ;	}
	public static int getSortByDensity() {		return sortByDensity;	}
	public static void setSortByDensity(int sortByDensity) {		GridPoint.sortByDensity = sortByDensity;	}
	public static int getOrder() {		return order;	}
	public static void setOrder(int order) {		GridPoint.order = order;	}


	public GridPoint clone() {
		GridPoint gp = new GridPoint(x,y,z,density);
		gp.setRelX(relX);
		gp.setRelY(relY);
		gp.setRelZ(relZ);
		return gp;
	}
	
	
}
