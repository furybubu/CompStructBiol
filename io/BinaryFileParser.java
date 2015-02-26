package crowdPhase.io;

/**
 * Abstract class defiing a parser of Binary Files.
 * ****************************************
 * @author jorda
 * @created Feb 25, 2014
 * @version
 ******************************************
 */
public class BinaryFileParser {
	/* the size of the buffer */
	protected static int BUFFER_SIZE = 4;
	
	
	
	protected static void processBuffer(byte[] buffer, int start, int end) {
		BinAsciiConverter.BinaryToAscii(buffer);
		//System.out.write(buffer, start, end);
		//System.out.println();
    }
}
