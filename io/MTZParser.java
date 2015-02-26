
package crowdPhase.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedList;
import crowdPhase.crystallography.spaceGroups.P1;
import crowdPhase.crystallography.spaceGroups.SpaceGroup;
import crowdPhase.fileObjects.MTZ;
import crowdPhase.utils.Debug;


/**
 * This class is for parsing a binary MTZ file and creating an MTZ object 
 * It extracts useful data from a diffraction experiment such as:
 * - The number of refections
 * - the resolution
 * - the unit cell dimensions
 * - The miller indices (h,k,l) of each reflection and their structure factor:
 * - the amplitudes (Fcalc, Fobs...) & the phases (PhiCalc, Phi...).
 * 
 * @author jorda
 * @created May 22, 2014
 * @version 1.0
 */

public class MTZParser extends BinaryFileParser {

	/* the path to the MTZ file */
	private String filename;
	private ByteOrder byo;
	
	/* infor maiton foudn in the header */
	/* the title */
	private String title;
	//the position of each field in a reflection dataitem
	private int hpos, kpos, lpos,fpos,fcalcpos,phipos;
	private int dsetoffset = 1;
	/*the number of columns/fields*/
	public int nbcol;
	/*the number of reflections */
	public int nbrefl;
	/*the resolution range*/
	public double resmin;
	public double resmax;
	
	
	/*the array containing all the column labels*/
	final private ArrayList<column> columns = new ArrayList<column>();
	final private ArrayList<dataset> datasets = new ArrayList<dataset>();
	private LinkedList<reflection>  reflectionList = new LinkedList<reflection>();
	/*the MTZ object returned by the parser*/
	private MTZ oMTZ; 
	private boolean isHeavyAtom;
	private boolean headerparsed = false;
	
	/**
	 * Constructor. 
	 * @param filename the path to the MTZ file to parse
	 */
	public MTZParser(String filename) {
		this.filename = filename;
		this.byo = ByteOrder.nativeOrder();
		this.oMTZ = new MTZ();
		isHeavyAtom = false;
	}
	
	
	/**
	 * Reads the mtz file and parses it in 3 steps:
	 * 1 - Gets the data from the header, verifies if it is an MTZ file and gets the offset location of the header block
	 * 2 - Parses the header and retrieves the column labels corresponding to each field found in reflections data
	 * 3 - Parses the reflection data (h,k,l and magnitude values) and store the reflections in an array
	 * 
	 * The final step builds an MTZ fileobject containing all the reflections. 
	 * @return an MTZ object
	 * @see MTZ
	 */
	public MTZ parse() {
		
		
		Debug.print("Parsing the MTZ file.");
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));//use bufferedinputstream to allow some mark/reset methods
			int count = 0;
			int nbyte = 1;// the byte position
			boolean startrefl = false;// the flag to start to parse the
										// reflections
			byte[] fourbytes = new byte[4];
			byte[] eightybytes = new byte[80];
			byte[] headeroffset = new byte[4];// header location in bytes (4
												// bytes)
			int headeroffsetloc; // the header location as an integer(number of
									// data items)
			ByteBuffer byb;

			while (dis.available() > 0 && !startrefl) {

				byte bread = dis.readByte();
				fourbytes[count] = bread;

				if (count % 3 == 0 && count != 0) {// 4 bytes have been read
					if (nbyte == 4) {// MTZ
						String MTZheader = new String(fourbytes);
						if (!MTZheader.startsWith("MTZ ")) {
							System.out.println("The file is not a valid MTZ format.");
							System.exit(0);
						}
						Debug.print(MTZheader);
					} else if (nbyte == 8) {// 4 bytes for header records
											// location
						headeroffset = fourbytes;
					} else if (nbyte == 12) {// machine stamp
						byb = ByteBuffer.wrap(fourbytes);
						int stamp = byb.order(ByteOrder.BIG_ENDIAN).getInt();
						String stampstr = Integer.toHexString(stamp);
						Debug.print("machineStamp:" + stampstr);
						switch (stampstr.charAt(0)) {
						case '1':
						case '3':
							if (byo.equals(ByteOrder.LITTLE_ENDIAN)) {
								byo = ByteOrder.BIG_ENDIAN;
							}
							break;
						case '4':
							if (byo.equals(ByteOrder.BIG_ENDIAN)) {
								byo = ByteOrder.LITTLE_ENDIAN;
							}
							break;
						}

					} else if (nbyte > 12 && !startrefl) {// turn the reflection
															// flag on
						int junk = (fourbytes[0] << 24) + (fourbytes[1] << 16)
								+ (fourbytes[2] << 8) + (fourbytes[3] << 0);
						if (junk != 0) {
							startrefl = true;
						} else {
							// System.out.println("junk");
						}
					}
					count = -1;
					fourbytes = new byte[4];
				}

				count++;
				nbyte++;

			}
			/** Go to the header */
			byb = ByteBuffer.wrap(headeroffset);
			headeroffsetloc = byb.order(byo).getInt();// retrieve the header
														// location
			
			// skip to header and parse
			dis.skip((headeroffsetloc-3 ) * 4);// go the headeroffset in bytes
												// (*4 bytes)
			
			//sanity check: Did we skip enough bytes?
			
			dis.mark(4);//create a mark allowing to read 4 bytes upstream
			dis.read(fourbytes, 0, 4);
			String check=new String(fourbytes);
			while(!check.startsWith("TITL")) {
				dis.mark(4);//create a mark allowing to read 4 bytes upstream
				dis.read(fourbytes, 0, 4);
				check=new String(fourbytes);
			}
			if (check.startsWith("TITL")) {
			dis.reset();
			}
			// parse the header
			for (Boolean parsing = true; parsing; dis.read(eightybytes, 0, 80)) {// read
																					// every
																					// 80
																					// bytes
				String mtzstrhead = new String(eightybytes);
				//Debug.print(mtzstrhead);
				parsing = parse_header(mtzstrhead);// send these eighty bites as a string to the parser
				
			}

			this.parseReflections();
			oMTZ.setTitle(this.title);
			
			dis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		Debug.print("Parsing of the MTZ file finished.");
		return this.oMTZ;
	}
	
	/***
	 * Parses the reflections, following the columns scheme found in the header, and stores them in an arrayList.
	 */
	private void parseReflections() {
		DataInputStream dis;

		try {
			dis = new DataInputStream(new FileInputStream(filename));// reopen
																		// the
																		// file
																		// to
																		// reset
																		// the
																		// offset
			int offset = 0;
			byte[] refbytes = new byte[4];
			ByteBuffer byb;
			
			//initialize the values for the column indexes  of interest
			hpos=kpos=lpos=fpos=fcalcpos=phipos=-1;
			//Calculate the column indexes in the reflections dataItems
			this.defineColumnPositions();
			
			if (hpos < 0 || kpos < 0 || lpos < 0) {
              String message = "Fatal error in MTZ file - no H K L column indexes present\n";
               Debug.print(message);
               System.exit(0);
            }
			
			// skip the first 80 bytes containing the MTZ record and basic info
			dis.skipBytes(80);

			for (int i = 0; i < nbrefl; i++) {// loop over the reflections
				reflection refl = new reflection();
				for (int j = 0; j < nbcol; j++) {// loop over the columns
					dis.read(refbytes, offset, 4);
					byb = ByteBuffer.wrap(refbytes);
					
					float value=byb.order(byo).getFloat();
					//Get the miller indices values
					if (j==hpos) {
						refl.h=value;
					}else if(j==kpos) {
						refl.k=value;
					}else if (j==lpos) {
						refl.l=value;
					}
					//Get the structure factor amplitude
					if(fpos!=-1 && j==fpos && !Double.isNaN(value) ) {
						refl.f=value;
					}else if (fcalcpos!=-1 && j==fcalcpos  && value>-1 ) {//get the calculated amplitude if fobs is missing or if said so
					  refl.fcalc=value;
					}
					
					//Get the structure factor phase
					if(phipos!=-1 && j==phipos) {
						refl.phi=value;
					}
				}
				//store the reflection in the list
				reflectionList.add(refl);
			}
			//print out the reflections
			for (reflection r:reflectionList) {
				Debug.print("REFL - "+r);
			}
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Defining the Dataset subclass
	 * ****************************************
	 * @author jorda
	 * @created May 22, 2014
	 * @version
	 ******************************************
	 */
	private class dataset {
		@SuppressWarnings("unused")
		public String project;
		@SuppressWarnings("unused")
		public String dataset;
		@SuppressWarnings("unused")
		public double lambda;
		public double[] cell = new double[6];
	}
	/**
	 * Defining the column subclass
	 * ****************************************
	 * @author jorda
	 * @created May 22, 2014
	 * @version
	 ******************************************
	 */
	private class column {

		public String label;
		@SuppressWarnings("unused")
		public char type;
		@SuppressWarnings("unused")
		public int id;
		@SuppressWarnings("unused")
		public double min, max;
	}
	public class reflection  {
		public float h,k,l,f=-1,fcalc=-1,phi=-1;
		
		public String toString () {
			return "h: "+h+"\tk: "+k+"\tl: "+l+"\tfobs: "+f+"\tfcalc: "+fcalc+"\tphi: "+phi;
		}
	}

	private static enum Header {

		VERS, TITLE,CELL, NCOL, SORT, SYMINF, SYMM, RESO, VALM, COL, COLUMN, NDIF, PROJECT, CRYSTAL, DATASET, DCELL, DWAVEL, BATCH, END, NOVALUE;

		public static Header toHeader(String str) {
			try {
				return valueOf(str);
			} catch (Exception ex) {
				return NOVALUE;
			}
		}
	}
	
	/**
	 * The main method parsing each line of the binary MTZ file
	 * 
	 * @param str the string to parse
	 * @return a boolean
	 */
	private Boolean parse_header(String str) {
		Boolean parsing = true;
		column col;
		dataset dset;
		int ndset;
		str=str.trim();
		String[] strsplit = str.split("\\s+");
		
		if (headerparsed) {
			
			if (Header.toHeader(strsplit[0].trim()) == Header.END) {// when it reaches
																// the last
																// record,
																// returns false
				return false;
			} else {
				return true;
			}
		}

		switch (Header.toHeader(strsplit[0])) {// switch on different keyword
												// types
		case TITLE:
			title = str.substring(5);
			break;
		case NCOL:
			nbcol = Integer.parseInt(strsplit[1]);
			nbrefl = Integer.parseInt(strsplit[2]);
			break;
		case SYMINF:
			String sg = str.split("'")[1];
			//remove the white spaces
			sg = sg.replaceAll("\\s+","");
			//and replace the dashes with underscores, dashes are often found for the 'bar' in centrosymmetric space groups
			sg = sg.replaceAll("-","_");
			//check if the specified space group has a class defined
			@SuppressWarnings("rawtypes")
			Class SGClass;
			try {
				//IMPORTANT: check the whole path to the class
				SGClass= Class.forName("crowdPhase.crystallography.spaceGroups."+sg);
			}catch ( ClassNotFoundException ex){
				SGClass= P1.class; //set by default to P1 if the class does not exist
			}
			try {
				this.oMTZ.setSpaceGroup((SpaceGroup)SGClass.newInstance());
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			break;
		case RESO:
			double r1 = Math.sqrt(1.0 / Float.parseFloat(strsplit[1]));
			double r2 = Math.sqrt(1.0 / Float.parseFloat(strsplit[2]));
			resmin = Math.max(r1, r2);// minimum resolution
			resmax = Math.min(r1, r2);// maximum resolution
			break;

		case NDIF:// number of datasets
			// int ndif = Integer.parseInt(strsplit[1]);
			break;
		case COL:
		case COLUMN:
			/*
			 * example of column line in the header:
			 * COLUMN H H 0.0000 39.0000 0
			 * 
			 * strsplit[0] -> COLUMN //keyword
			 * strsplit[1] -> H //label
			 * strsplit[2] -> H //type
			 * strsplit[3] -> 0.0000 //min
			 * strsplit[4] -> 39.0000 //max
			 * strsplit[5] -> 0 //datasetoffset
			 */
			
			ndset = 0;
			if (strsplit.length>5) {
				Integer.parseInt(strsplit[5]);
				if (ndset == 0) {
					dsetoffset = 0;
				}
			}
			col = new column();
			columns.add(col);
			col.label = strsplit[1].trim();
			col.type = strsplit[2].charAt(0);
			col.id = ndset;
			col.min = Double.parseDouble(strsplit[3]);
			col.max = Double.parseDouble(strsplit[4]);
			break;
		case PROJECT:
			ndset = Integer.parseInt(strsplit[1]);
			if (ndset == 0) {
				dsetoffset = 0;
			}
			try {
				dset = (dataset) datasets.get(ndset - dsetoffset);
			} catch (IndexOutOfBoundsException e) {
				dset = new dataset();
				datasets.add(dset);
			}
			dset.project = strsplit[2];
			break;
		case DATASET:
			ndset = Integer.parseInt(strsplit[1]);
			if (ndset == 0) {
				dsetoffset = 0;
			}
			try {
				dset = (dataset) datasets.get(ndset - dsetoffset);
			} catch (IndexOutOfBoundsException e) {
				dset = new dataset();
				datasets.add(dset);
			}
			dset.dataset = strsplit[2];
			break;
		case CELL:
			
			double a = Double.parseDouble(strsplit[1]);
			double b = Double.parseDouble(strsplit[2]);
			double c = Double.parseDouble(strsplit[3]);
			double alpha = Double.parseDouble(strsplit[4]);
			double beta = Double.parseDouble(strsplit[5]);
			double gamma = Double.parseDouble(strsplit[6]);
			this.oMTZ.setCellDim(a, b, c, alpha, beta, gamma);
			
			break;
		case DCELL:
			ndset = Integer.parseInt(strsplit[1]);
			if (ndset == 0) {
				dsetoffset = 0;
			}
			try {
				dset = (dataset) datasets.get(ndset - dsetoffset);
			} catch (IndexOutOfBoundsException e) {
				dset = new dataset();
				datasets.add(dset);
			}
			dset.cell[0] = Double.parseDouble(strsplit[2]);
			dset.cell[1] = Double.parseDouble(strsplit[3]);
			dset.cell[2] = Double.parseDouble(strsplit[4]);
			dset.cell[3] = Double.parseDouble(strsplit[5]);
			dset.cell[4] = Double.parseDouble(strsplit[6]);
			dset.cell[5] = Double.parseDouble(strsplit[7]);
			break;
		case DWAVEL:
			ndset = Integer.parseInt(strsplit[1]);
			if (ndset == 0) {
				dsetoffset = 0;
			}
			try {
				dset = (dataset) datasets.get(ndset - dsetoffset);
			} catch (IndexOutOfBoundsException e) {
				dset = new dataset();
				datasets.add(dset);
			}
			dset.lambda = Double.parseDouble(strsplit[2]);
			break;
		case END:
			headerparsed = true;
			parsing = false;
			break;
		default:
			break;
		}

		return parsing;
	}
	
	/**
	 * Retrieve the position of each column, following their positions in the columns parsed from the header
	 */
	private void defineColumnPositions() {
		int nc = 0;
		for (column col:columns) {
			if(col.label.equalsIgnoreCase("H")) {
				hpos =nc; 
			}else if (col.label.equalsIgnoreCase("K")) {
				kpos = nc;
			}else if (col.label.equalsIgnoreCase("L")) {
				lpos=nc;
			}else if (col.label.equalsIgnoreCase("FP") || col.label.equalsIgnoreCase("F")) {
				fpos=nc;
			}else if (col.label.equalsIgnoreCase("FC") || col.label.equalsIgnoreCase("FCalc") ) {
				fcalcpos=nc;
			}else if ((col.label.equalsIgnoreCase("PHICalc") || col.label.equalsIgnoreCase("PHIBest")  || col.label.equalsIgnoreCase("PHIC") || col.label.equalsIgnoreCase("PHASE"))) {
				phipos=nc;
			}else if (col.label.equalsIgnoreCase("DANO") && isHeavyAtom) {//anomalous differences for the heavy atoms in a derivative
				phipos=nc;
			}
			nc++;
		}
	}
	
	/**
	 * Static method for testing the opening/parsing of MTZ files
	 * @param args
	 */
	public static void main(String[] args) {
		Debug.set(true);
		MTZParser mp = new MTZParser("/home/jorda/phaseworkspace/examples/mtz/capsids/4llf-sf-sorted_sf45-scaled.mtz");
		mp= new MTZParser("/home/jorda/phaseworkspace/examples/mtz/centrosymmetric/4ttm-sf_sfall_8A.mtz");
		//mp= new MTZParser("../examples/mtz/capsids/3ref.mtz");
		//mp.setFpriority(MTZParser.Fcalcpriority);
		
		MTZ omtz = mp.parse();
		omtz.setReflectionList(mp.getReflectionList(), MTZ.Fobspriority);
		
		System.out.println("SGROUP: "+omtz.getSpaceGroup().getName());
		System.out.println("Nb of reflections: "+omtz.getReflectionList().size());
	}

	/** GETTERS AND SETTERS */
	public LinkedList<reflection> getReflectionList() {
		return reflectionList;
	}

	public boolean isHeavyAtom() {
		return isHeavyAtom;
	}

	public void setHeavyAtom(boolean isHeavyAtom) {
		this.isHeavyAtom = isHeavyAtom;
	}


	
}
