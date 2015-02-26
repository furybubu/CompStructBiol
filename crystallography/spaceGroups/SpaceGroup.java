package crowdPhase.crystallography.spaceGroups;
/**
 * Defines the abstract class of a space group object. It consists of the name of the space group,
 * its origin choices and and its symmetry operators in real space.
 * 
 * ****************************************
 * @author jorda
 * @created May 23, 2014
 * @version
 ******************************************
 */

import java.util.LinkedList;

import crowdPhase.basic.Matrix;
import crowdPhase.basic.Matrix3x3;
import crowdPhase.crystallography.GridPoint;
import crowdPhase.crystallography.UnitCell;
import crowdPhase.crystallography.crystalSystems.CrystalSystem;


public abstract class SpaceGroup {
	
	/* the name of the Space Group*/
	private String name;
	/* The symmetry operators in real space as defined in the ITFC vol. B */
	private LinkedList<Matrix3x3> aSymOperators;
	/* The translation operators in real space as defined in the ITFC vol. B */
	private LinkedList<Matrix> aTransOperators;
	/* The list of possible origin choices */
	private LinkedList<GridPoint> origins;
	//one of the seven crystal system 
	protected CrystalSystem crystalSystem;
	//the Unit cell
	public UnitCell UC;
	
	public SpaceGroup (String name) {
		this.name=name;
		this.aSymOperators = new LinkedList<Matrix3x3>();
		this.origins = new LinkedList<GridPoint>();
		this.aTransOperators = new LinkedList<Matrix>();
		
		//fill the array of symmetry operators 
		this.defineSymmetry();
	}
	/*
	 * A method for adding a 3x3 matrix corresponding to symmetry operator
	 */
	public void AddSymOperator (Matrix3x3 sym) {
		this.aSymOperators.add(sym);
	}
	/*
	 * A method for adding the translation vector matrix 
	 */
	public void AddTransOperator (Matrix sym) {
		this.aTransOperators.add(sym);
	}
	/*
	 * A method for adding an origin choice
	 */
	public void AddOrigin (GridPoint og) {
		this.origins.add(og);
	}


	/** GETTERS AND SETTERS **/
	
	public String getName() {		return name;	}

	public void setName(String name) {		this.name = name;	}

	public LinkedList<Matrix3x3> getaSymOperators() {		return aSymOperators;	}

	public void setaSymOperators(LinkedList<Matrix3x3> aSymOperators) {		this.aSymOperators = aSymOperators;	}
	
	public LinkedList<GridPoint> getOrigins() {		return origins;	}
	
	public void setOrigins(LinkedList<GridPoint> origins) {		this.origins = origins;	}
		
	public LinkedList<Matrix> getaTransOperators() {		return aTransOperators;
	}
	public void setaTransOperators(LinkedList<Matrix> aTransOperators) {		this.aTransOperators = aTransOperators;
	}
	
	
	public CrystalSystem getCrystalSystem() {
		return crystalSystem;
	}
	public void setCrystalSystem(CrystalSystem crystalSystem) {
		this.crystalSystem = crystalSystem;
	}
	
	
	/**
	 * Reformat a double to 5 decimals only
	 * @param d
	 * @return
	 */
	public static double roundDown3(double d) {
	    return Math.floor(d * 1e3) / 1e3;
	}
	
	/** ABSTRACT methods to be implemented by inheriting classes */
	public abstract void defineSymmetry();
	public abstract void defineOrigins();
	
	
}
