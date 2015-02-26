package crowdPhase.basic;

/**
 * A class defining a 3x3 Matrix.
 * ****************************************
 * @author jorda
 * @created May 23, 2014
 * @version
 ******************************************
 */
public class Matrix3x3 extends Matrix {

	public Matrix3x3 () {
		super(3,3);
	}
	
	public Matrix3x3(double[][] data) {
		super(data);
		if (data.length != 3 || data[0].length != 3 )
	    {
	        throw new IllegalArgumentException
	            ("Incorrect number of rows/columns.The matrix must be a 3x3 matrix.");
	    }		
	}

}
