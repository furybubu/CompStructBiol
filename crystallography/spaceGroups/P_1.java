package crowdPhase.crystallography.spaceGroups;

import crowdPhase.basic.Matrix;
import crowdPhase.basic.Matrix3x3;
import crowdPhase.crystallography.GridPoint;
import crowdPhase.crystallography.UnitCell;
import crowdPhase.crystallography.crystalSystems.CSTriclinic;
import crowdPhase.crystallography.fourierTransforms.DiscreteFourierTransform;

/**
 * This class defines the centrosymmetric Space Group P1 bar.
 * 
 * ****************************************
 * @author jorda
 * @created Dec 15, 2014
 * @version
 ******************************************
 */
public class P_1 extends SpaceGroup{
	
	
	/**
	 * Constructor
	 */
	public P_1 () {
		super("P_1");
		super.crystalSystem = new CSTriclinic();
	}

	/**
	 * Override the abstract methods of the Space group class
	 */
	
	@Override
	public void defineSymmetry() {
		double[][] sym1 = {{1,0,0},{0,1,0},{0,0,1}};
		double[][] sym2 = {{-1,0,0},{0,-1,0},{0,0,-1}};
		this.AddSymOperator(new Matrix3x3(sym1));	
		this.AddSymOperator(new Matrix3x3(sym2));	
		double [][] trans1 = {{0},{0},{0}};
		double [][] trans2 = {{0},{0},{0}};
		this.AddTransOperator(new Matrix(trans1));
		this.AddTransOperator(new Matrix(trans2));
	}

	@Override
	public void defineOrigins() {
		
		
		GridPoint og;
		//8 different origins in P_1
		 og = new GridPoint();
		 for (double x=0;x<1;x+=0.5) {
			 for (double y=0;y<1;y+=0.5) {
				 for (double z=0;z<1;z+=0.5) {
					 double[] cart =UC.fractionalToCartesian(x, y, z);
					 og.setX(roundDown3(cart[0]));
					 og.setY(roundDown3(cart[1]));
					 og.setZ(roundDown3(cart[2]));
					 this.AddOrigin(og);
				 }
			 }
		 }
		 		
	}
	
}