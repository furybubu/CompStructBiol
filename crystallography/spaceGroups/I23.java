package crowdPhase.crystallography.spaceGroups;

import crowdPhase.basic.Matrix;
import crowdPhase.basic.Matrix3x3;
import crowdPhase.crystallography.GridPoint;
import crowdPhase.crystallography.crystalSystems.CSCubic;

/**
 * This class defines the Space Group I23.
 * 
 * ****************************************
 * @author jorda
 * @created May 23, 2014
 * @version
 ******************************************
 */
public class I23 extends SpaceGroup{
	
	
	public I23 () {
		super("I23");
		super.crystalSystem = new CSCubic();
	}

	@Override
	public void defineSymmetry() {
		double[][] sym1 = {{1,0,0},{0,1,0},{0,0,1}};
		double[][] sym2 = {{-1,0,0},{0,-1,0},{0,0,1}};
		double[][] sym3 = {{-1,0,0},{0,1,0},{0,0,-1}};
		double[][] sym4 = {{1,0,0},{0,-1,0},{0,0,-1}};
		double[][] sym5 = {{0,0,1},{1,0,0},{0,1,0}};
		double[][] sym6 = {{0,0,1},{-1,0,0},{0,-1,0}};
		double[][] sym7 = {{0,0,-1},{-1,0,0},{0,1,0}};
		double[][] sym8 = {{0,0,-1},{1,0,0},{0,-1,0}};
		double[][] sym9 = {{0,1,0},{0,0,1},{1,0,0}};	 
		double[][] sym10 = {{0,-1,0},{0,0,1},{-1,0,0}};
		double[][] sym11 = {{0,1,0},{0,0,-1},{-1,0,0}};
		double[][] sym12 = {{0,-1,0},{0,0,-1},{1,0,0}};
		
		//{column{row,row,row},column{row,row,row},column{row,row,row}}
		//		double[][] sym1 = {{1,0,0},{0,1,0},{0,0,1}};
		//		double[][] sym2 = {{-1,0,0},{0,-1,0},{0,0,1}};
		//		double[][] sym3 = {{-1,0,0},{0,1,0},{0,0,-1}};
		//		double[][] sym4 = {{1,0,0},{0,-1,0},{0,0,-1}};
		//		double[][] sym5 = {{0,1,0},{0,0,1},{1,0,0}};
		//		double[][] sym6 = {{0,-1,0},{0,0,-1},{1,0,0}};
		//		double[][] sym7 = {{0,-1,0},{0,0,1},{-1,0,0}};
		//		double[][] sym8 = {{0,1,0},{0,0,-1},{0,-1,0}};
		//		double[][] sym9 = {{0,0,1},{1,0,0},{0,1,0}};	 
		//		double[][] sym10 = {{0,0,-1},{-1,0,0},{0,1,0}};
		//		double[][] sym11 = {{0,0,-1},{1,0,0},{0,-1,0}};
		//		double[][] sym12 = {{0,0,1},{-1,0,0},{0,-1,0}};
		
		this.AddSymOperator(new Matrix3x3(sym1));
		this.AddSymOperator(new Matrix3x3(sym2));
		this.AddSymOperator(new Matrix3x3(sym3));
		this.AddSymOperator(new Matrix3x3(sym4));
		this.AddSymOperator(new Matrix3x3(sym5));
		this.AddSymOperator(new Matrix3x3(sym6));
		this.AddSymOperator(new Matrix3x3(sym7));
		this.AddSymOperator(new Matrix3x3(sym8));
		this.AddSymOperator(new Matrix3x3(sym9));
		this.AddSymOperator(new Matrix3x3(sym10));
		this.AddSymOperator(new Matrix3x3(sym11));
		this.AddSymOperator(new Matrix3x3(sym12));
		double [][] trans1 = {{0.5},{0.5},{0.5}};
		double [][] trans2 = {{0.5},{0.5},{0.5}};
		double [][] trans3 = {{0.5},{0.5},{0.5}};
		double [][] trans4 = {{0.5},{0.5},{0.5}};
		double [][] trans5 = {{0.5},{0.5},{0.5}};
		double [][] trans6 = {{0.5},{0.5},{0.5}};
		double [][] trans7 = {{0.5},{0.5},{0.5}};
		double [][] trans8 = {{0.5},{0.5},{0.5}};
		double [][] trans9 = {{0.5},{0.5},{0.5}};
		double [][] trans10 = {{0.5},{0.5},{0.5}};
		double [][] trans11 = {{0.5},{0.5},{0.5}};
		double [][] trans12 = {{0.5},{0.5},{0.5}};
		this.AddTransOperator(new Matrix(trans1));
		this.AddTransOperator(new Matrix(trans2));
		this.AddTransOperator(new Matrix(trans3));
		this.AddTransOperator(new Matrix(trans4));
		this.AddTransOperator(new Matrix(trans5));
		this.AddTransOperator(new Matrix(trans6));
		this.AddTransOperator(new Matrix(trans7));
		this.AddTransOperator(new Matrix(trans8));
		this.AddTransOperator(new Matrix(trans9));
		this.AddTransOperator(new Matrix(trans10));
		this.AddTransOperator(new Matrix(trans11));
		this.AddTransOperator(new Matrix(trans12));
		
	}

	@Override
	public void defineOrigins() {
		//only one origin in I23
		GridPoint og = new GridPoint();
		og.x=0;
		og.y=0;
		og.z=0;
		this.AddOrigin(og);
	}
	
}
