package crowdPhase.fileObjects;
import java.util.LinkedList;

import crowdPhase.core.PhaseReflection;
import crowdPhase.crystallography.UnitCell;
import crowdPhase.crystallography.spaceGroups.SpaceGroup;
import crowdPhase.io.MTZParser;
import crowdPhase.io.MTZParser.reflection;

/**
 * Defines an MTZ file object. Contains most of the info found in a MTZ file, it is typically created upon parsing 
 * an MTZ file with the @see MTZparser
 * 
 * ****************************************
 * @author jorda
 * @created Feb 25, 2014
 * @version
 ******************************************
 */
public class MTZ {

	/* The title of the MTZ file */
	private String title;
	/* The resolution of of these data collected during the diffraction experiment */
	private double reso;
	/* the dimensions of the predicted unit cell */
	public UnitCell UC;
	/* A list of all the reflections */
	private LinkedList<PhaseReflection> reflectionList;
	/* the predicted crystallographic space group */
	private SpaceGroup spaceGroup;

	//Values of a flag defining the type of amplitude that has to be considered
	public static int Fobspriority = 1;
	public static int Fcalcpriority= 2;
	
	public MTZ() {
		reflectionList= new LinkedList<PhaseReflection> ();
	}
	
	
	/***************************************************
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * *************************************************	
	 */
	
	
	/**
	 * Sets a list of reflections, most of the time generated by the MTZ parser
	 * @see MTZParser
	 * @param refs the LinkedList of reflections
	 */
	public void setReflectionList(LinkedList<reflection> refs,int priority) {
		reflectionList = new LinkedList<PhaseReflection>();
		for (reflection refl : refs) {
			PhaseReflection prefl= new PhaseReflection();
			prefl.setH((int)refl.h);
			prefl.setK((int)refl.k);
			prefl.setL((int)refl.l);
			if (priority==MTZ.Fobspriority) {
				prefl.setF(refl.f);
			}else if (priority==MTZ.Fcalcpriority){
				prefl.setF(refl.fcalc);
			}
			int phi=0;
			if (refl.phi!=0) {
				phi=Math.round(refl.phi);
			}
			prefl.setDecodedPhase((int)phi);
			reflectionList.add(prefl);
		}
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setReso(double reso) {
		this.reso = reso;
	}
	
	
	public void setCellDim (double a,double b,double c,double alpha,double beta, double gamma) {
		double[] cellDim = new double[6];
		cellDim[0]=a;
		cellDim[1]=b;
		cellDim[2]=c;
		cellDim[3]=alpha;
		cellDim[4]=beta;
		cellDim[5]=gamma;
		UC = new UnitCell(cellDim);
	}
	public void setSpaceGroup(SpaceGroup sg) {
		this.spaceGroup = sg;
		this.spaceGroup.UC = this.UC;
		//Define the accepted origins according to the unit cell
		this.spaceGroup.defineOrigins();
	}

	
	public SpaceGroup getSpaceGroup() {
		return spaceGroup;
	}


	public String getTitle() {
		return title;
	}
	
	public double getReso() {
		return reso;
	}
	public LinkedList<PhaseReflection> getReflectionList() {
		return reflectionList;
	}
}