package ca.utoronto.utm.paint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 *
 * @author
 *
 */
public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private PaintModel paintModel;

	/**
	 * Below are Patterns used in parsing
	 */
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");
	private Pattern pRectangleStart = Pattern.compile("^Rectangle$");
	private Pattern pRectangleEnd = Pattern.compile("^EndRectangle$");
	private Pattern pSquiggleStart = Pattern.compile("^Squiggle$");
	private Pattern pSquiggleEnd = Pattern.compile("^EndSquiggle$");

	private Pattern pPolylineStart=Pattern.compile("^Polyline$");
	private Pattern pPolylineEnd=Pattern.compile("^EndPolyline$");


	private Pattern pColor = Pattern.compile("^color:(\\d+),(\\d+),(\\d+)$");
	private Pattern pFilled = Pattern.compile("^filled:(true|false)$");
	private Pattern pCenter = Pattern.compile("^center:\\((\\d+),(\\d+)\\)$");
	private Pattern pRadius = Pattern.compile("^radius:(\\d+)$");
	private Pattern pPoint = Pattern.compile("^point:\\((-?\\d+),(-?\\d+)\\)$");
	private Pattern pCircleStart=Pattern.compile("^Circle$");
	private Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	/**
	 * Store an appropriate error message in this, including
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}

	/**
	 *
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}

	/**
	 * Parse the specified file
	 * @param fileName
	 * @return
	 */
	public boolean parse(String fileName){
		boolean retVal = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			PaintModel pm = new PaintModel();
			retVal = this.parse(br, pm);
		} catch (FileNotFoundException e) {
			error("File Not Found: "+fileName);
		} finally {
			try { br.close(); } catch (Exception e){};
		}
		return retVal;
	}

	/**
	 * Parse the specified inputStream as a Paint Save File Format file.
	 * @param inputStream
	 * @return
	 */
	public boolean parse(BufferedReader inputStream){
		PaintModel pm = new PaintModel();
		return this.parse(inputStream, pm);
	}

	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 *
	 * @param inputStream the open file to parse
	 * @param paintModel the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";

		// During the parse, we will be building one of the 
		// following commands. As we parse the file, we modify 
		// the appropriate command.

		CircleCommand circleCommand = null;
		RectangleCommand rectangleCommand = null;
		SquiggleCommand squiggleCommand = null;
		PolylineCommand polylineCommand = null;

		try {
			int state=0; Matcher m; String l;

			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) {
				l = l.replaceAll("\\s+",""); // right at the start of the while loop
				this.lineNumber++;
				System.out.println(lineNumber+" "+l+" "+state);
				switch(state){
					case 0:
						m = pFileStart.matcher(l);
						if (m.matches()) {
							state = 1;
							break;
						}
						error("Expected Start of Paint Save File");
						return false;

					case 1: // Looking for the start of a new object or end of the save file
						m = pCircleStart.matcher(l);
						if (m.matches()) {
							circleCommand = new CircleCommand(new Point(0, 0), 0);
							state = 2;
							break;
						}
						m = pFileEnd.matcher(l);
						m=pRectangleStart.matcher(l);
						if(m.matches()){
							rectangleCommand = new RectangleCommand(new Point(0, 0), new Point(0, 0));
							state=7;
							break;
						}
						m=pSquiggleStart.matcher(l);
						if(m.matches()){
							squiggleCommand = new SquiggleCommand();
							state = 12;
							break;
						}
						m=pPolylineStart.matcher(l);
						if(m.matches()){
							polylineCommand = new PolylineCommand();
							state = 17;
							break;
						}
						m=pFileEnd.matcher(l);
						if(m.matches()){
							state = 22;
							break;
						}
						if(l.isEmpty()){
							state = 1;
							break;
						}
						error("Expected Start of Shape or End Paint Save File");
						return false;

					case 2: // Parsing Circle color
						m = pColor.matcher(l);
						if (m.matches()) {
							int r = Integer.parseInt(m.group(1));
							int g = Integer.parseInt(m.group(2));
							int b = Integer.parseInt(m.group(3));
							if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
								circleCommand.setColor(javafx.scene.paint.Color.rgb(r, g, b));
								state = 3;
								break;
							}
						}
						error("Expected Circle color");
						return false;

					case 3: // Parsing Circle filled
						m = pFilled.matcher(l);
						if (m.matches()) {
							boolean filled = Boolean.parseBoolean(m.group(1));
							circleCommand.setFill(filled);
							state = 4;
							break;
						}
						error("Expected Circle filled");
						return false;

					case 4: // Parsing Circle center
						m = pCenter.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							circleCommand.setCentre(new Point(x, y));
							state = 5;
							break;
						}
						error("Expected Circle center");
						return false;

					case 5: // Parsing Circle radius
						m = pRadius.matcher(l);
						if (m.matches()) {
							int radius = Integer.parseInt(m.group(1));
							if (radius >= 0) {
								circleCommand.setRadius(radius);
								state = 6;
								break;
							}
						}
						error("Expected Circle radius");
						return false;

					case 6: // EndCircle
						m = pCircleEnd.matcher(l);
						if (m.matches()) {
							paintModel.addCommand(circleCommand);
							state = 1;
							break;
						}
						error("Expected EndCircle");
						return false;


					case 7: // Parsing Rectangle color
						m = pColor.matcher(l);
						if (m.matches()) {
							int r = Integer.parseInt(m.group(1));
							int g = Integer.parseInt(m.group(2));
							int b = Integer.parseInt(m.group(3));
							if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
								rectangleCommand.setColor(javafx.scene.paint.Color.rgb(r, g, b));
								state = 8;
								break;
							}
						}
						error("Expected Rectangle color");
						return false;

					case 8: // Parsing Rectangle filled
						m = pFilled.matcher(l);
						if (m.matches()) {
							boolean filled = Boolean.parseBoolean(m.group(1));
							rectangleCommand.setFill(filled);
							state = 9;
							break;
						}
						error("Expected Rectangle filled");
						return false;

					case 9: // Parsing Rectangle p1
						m = Pattern.compile("^p1:\\((\\d+),(\\d+)\\)$").matcher(l);
						if (m.matches()) {
							int x1 = Integer.parseInt(m.group(1));
							int y1 = Integer.parseInt(m.group(2));
							rectangleCommand.setP1(new Point(x1, y1));
							state = 10;
							break;
						}
						error("Expected Rectangle p1");
						return false;

					case 10: // Parsing Rectangle p2
						m = Pattern.compile("^p2:\\((\\d+),(\\d+)\\)$").matcher(l);
						if (m.matches()) {
							int x2 = Integer.parseInt(m.group(1));
							int y2 = Integer.parseInt(m.group(2));
							rectangleCommand.setP2(new Point(x2, y2));
							state = 11;
							break;
						}
						error("Expected Rectangle p2");
						return false;

					case 11: // EndRectangle
						m = pRectangleEnd.matcher(l);
						if (m.matches()) {
							paintModel.addCommand(rectangleCommand);
							state = 1;
							break;
						}
						error("Expected EndRectangle");
						return false;

					case 12: // Parsing Squiggle color
						m = pColor.matcher(l);
						if (m.matches()) {
							int r = Integer.parseInt(m.group(1));
							int g = Integer.parseInt(m.group(2));
							int b = Integer.parseInt(m.group(3));
							if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
								squiggleCommand.setColor(javafx.scene.paint.Color.rgb(r, g, b));
								state = 13;
								break;
							}
						}
						error("Expected Squiggle color");
						return false;

					case 13: // Parsing Squiggle filled
						m = pFilled.matcher(l);
						if (m.matches()) {
							boolean filled = Boolean.parseBoolean(m.group(1));
							squiggleCommand.setFill(filled);
							state = 14;
							break;
						}
						error("Expected Squiggle filled");
						return false;

					case 14: // Parsing points block
						if (l.equals("points")) {
							state = 15;
							break;
						}
						error("Expected points block for Squiggle");
						return false;

					case 15: // Parsing individual points or end of points block
						m = pPoint.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							squiggleCommand.add(new Point(x, y));
							break;
						}
						if (l.equals("endpoints")) {
							state = 16;
							break;
						}
						error("Expected point or end points for Squiggle");
						return false;

					case 16: // EndSquiggle
						m = pSquiggleEnd.matcher(l);
						if (m.matches()) {
							paintModel.addCommand(squiggleCommand);
							state = 1;
							break;
						}
						error("Expected EndSquiggle");
						return false;

					case 17: // Parsing Polyline color
						m = pColor.matcher(l);
						if (m.matches()) {
							int r = Integer.parseInt(m.group(1));
							int g = Integer.parseInt(m.group(2));
							int b = Integer.parseInt(m.group(3));
							if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
								polylineCommand.setColor(javafx.scene.paint.Color.rgb(r, g, b));
								state = 18;
								break;
							}
						}
						error("Expected Polyline color");
						return false;

					case 18: // Parsing Polyline filled
						m = pFilled.matcher(l);
						if (m.matches()) {
							boolean filled = Boolean.parseBoolean(m.group(1));
							polylineCommand.setFill(filled);
							state = 19;
							break;
						}
						error("Expected Polyline filled");
						return false;

					case 19: // Parsing points block
						if (l.equals("points")) {
							state = 20;
							break;
						}
						error("Expected points block for Polyline");
						return false;

					case 20: // Parsing individual points or end of points block
						m = pPoint.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							polylineCommand.add(new Point(x, y));
							break;
						}
						if (l.equals("endpoints")) {
							state = 21;
							break;
						}
						error("Expected point or end points for Polyline");
						return false;

					case 21: // EndPolyline
						m = pPolylineEnd.matcher(l);
						if (m.matches()) {
							paintModel.addCommand(polylineCommand);
							state = 1;
							break;
						}
						error("Expected EndPolyline");
						return false;

					case 22: // End of file
						if (l.isEmpty()) {
							break;
						}
						m = pFileEnd.matcher(l);
						if (m.matches()) {
							return true;
						}
						error("Extra content after End of File");
						return false;
				}
			}
		}  catch (Exception e){

		}
		return true;
	}
}
