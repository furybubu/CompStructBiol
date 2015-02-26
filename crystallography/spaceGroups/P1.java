package crowdPhase.crystallography.spaceGroups;

import crowdPhase.basic.Matrix;
import crowdPhase.basic.Matrix3x3;
import crowdPhase.crystallography.GridPoint;
import crowdPhase.crystallography.UnitCell;
import crowdPhase.crystallography.crystalSystems.CSTriclinic;
import crowdPhase.crystallography.fourierTransforms.DiscreteFourierTransform;

/**
 * This class defines the Space Group P1.
 * 
 * ****************************************
 * @author jorda
 * @created May 23, 2014
 * @version
 ******************************************
 */
public class P1 extends SpaceGroup{
	
	/**
	 * Constructor
	 */
	public P1 () {
		super("P1");
		super.crystalSystem = new CSTriclinic();
	}

	/**
	 * Override the abstract methods of the Space group class
	 */
	
	@Override
	public void defineSymmetry() {
		double[][] sym1 = {{1,0,0},{0,1,0},{0,0,1}};
		this.AddSymOperator(new Matrix3x3(sym1));	
		double [][] trans1 = {{0},{0},{0}};
		this.AddTransOperator(new Matrix(trans1));
	}

	@Override
	public void defineOrigins() {
		UnitCell uc = DiscreteFourierTransform.unitcell;
		double gridspacing = DiscreteFourierTransform.gridspacing;
		
		GridPoint og;
		
		boolean endLoopA=false,endLoopB=false,endLoopC=false;
		
		//in P1 , any gridpoint can be the origin
		for (double x=0;x<=uc.a;x+=gridspacing) {			// take in account any grid point
			endLoopB=false;								
			endLoopC=false;
			for (double y=0;y<=uc.b;y+=gridspacing) {		
				endLoopC=false;
				for (double z=0;z<=uc.c;z+=gridspacing) {	//
														//set the coordinates as fractional coordinates
					double xx=x/uc.a;
					double yy=y/uc.b;
					double zz=z/uc.c;
					
					//add to the origin choices
					og = new GridPoint();
					og.x=xx;
					og.y=yy;
					og.z=zz;
					this.AddOrigin(og);
					
					if (z+gridspacing>=uc.c && !endLoopC) {
						z=uc.c-gridspacing;
						endLoopC=true;
					}
					
				}
				
				if (y+gridspacing>=uc.b && !endLoopB) {
					y=uc.b-gridspacing;
					endLoopB=true;
				 }	
			}
			 if(x+gridspacing>=uc.a && !endLoopA) {
					x=uc.a-gridspacing;
					endLoopA=true;
				}
		}
	}
	
}