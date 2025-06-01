package ca.utoronto.utm.paint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OllamaPaint extends Ollama{
    public OllamaPaint(String host){
        super(host);
    }
    /**
     * Post-process response to extract only the Paint Save File content
     * @param response the raw response from Ollama
     * @return cleaned Paint Save File content
     */
    private String cleanResponse(String response) {
        Pattern pattern = Pattern.compile("(Paint Save File Version 1\\.0.*?End Paint Save File)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).trim(); // Extract the entire match (from start to end of Paint Save File)
        } else {
            throw new IllegalStateException("Response does not contain a valid Paint Save File.");
        }
    }

    /**
     * Ask llama3 to generate a new Paint File based on the given prompt
     * @param prompt
     * @param outFileName name of new file to be created in users home directory
     */
    public void newFile(String prompt, String outFileName) {
        String format = FileIO.readResourceFile("paintSaveFileFormat.txt");
        String system = "You must respond strictly with the Paint Save File content without any extra commentary or explanation. The Paint Save File should be formatted as specified below. Respond only with the file content, starting with 'Paint Save File Version 1.0' and ending with 'End Paint Save File'. " + format;
        String response = this.call(system, prompt);
        String cleanedResponse = cleanResponse(response);
        FileIO.writeHomeFile(cleanedResponse, outFileName);
    }

    /**
     * Ask llama3 to generate a new Paint File based on a modification of inFileName and the prompt
     * @param prompt the user-supplied prompt
     * @param inFileName the Paint File Format file to be read and modified to outFileName
     * @param outFileName name of new file to be created in users home directory
     */
    public void modifyFile(String prompt, String inFileName, String outFileName) {
        String format = FileIO.readResourceFile("paintSaveFileFormat.txt");
        String system = "You must respond strictly with the Paint Save File content without any extra commentary or explanation. The Paint Save File should be formatted as specified below. Respond only with the file content, starting with 'Paint Save File Version 1.0' and ending with 'End Paint Save File'. " + format;
        String f = FileIO.readHomeFile(inFileName);
        String fullPrompt = "Modify the following Paint File based on this instruction: " + prompt +
                "\nPAINT FILE START\n" + f + "\nPAINT FILE END";
        String response = this.call(system, fullPrompt);
        String cleanedResponse = cleanResponse(response);
        FileIO.writeHomeFile(cleanedResponse, outFileName);
    }

    /**
     * Create a new Paint Save File with a face pattern using simple shapes
     * @param outFileName the name of the new file in the user's home directory
     */
    @Override
    public void newFile1(String outFileName) {
        String prompt = "You are to create a simple face pattern using basic geometric shapes. Follow these 7 examples to understand the structure of the shapes used in the face.\n" +
                "\n" +
                "**Examples for Creating Face Patterns**:\n" +
                "\n" +
                "1. **Face 1**:\n" +
                "   - Draw a large circle with a radius of 50 at (100, 100) for the face.\n" +
                "   - Draw two smaller circles for the eyes: one at (80, 80) with radius 10 and another at (120, 80) with radius 10.\n" +
                "   - Draw a brown polyline as a smile from (80, 120) to (120, 120).\n" +
                "\n" +
                "2. **Face 2**:\n" +
                "   - Draw an oval for the face with width 120 and height 100, centered at (150, 150).\n" +
                "   - Draw two circles for the eyes at (130, 130) and (170, 130), each with radius 10.\n" +
                "   - Draw a curved polyline for the smile from (130, 180) to (170, 180).\n" +
                "\n" +
                "3. **Face 3**:\n" +
                "   - Draw a square with side length 100 at (200, 200) for the face.\n" +
                "   - Draw two smaller circles for eyes at (220, 220) and (270, 220) with radius 8.\n" +
                "   - Draw a triangle below the eyes with vertices at (220, 270), (250, 290), and (270, 270) to represent a nose.\n" +
                "\n" +
                "4. **Face 4**:\n" +
                "   - Draw a circle for the face with radius 60 at (300, 100).\n" +
                "   - Add two triangles as ears: one on each side of the circle at (240, 100) and (360, 100).\n" +
                "   - Draw a straight line below the eyes from (280, 140) to (320, 140) to represent a mouth.\n" +
                "\n" +
                "5. **Face 5**:\n" +
                "   - Draw an oval with width 100 and height 80 at (400, 150).\n" +
                "   - Add two squares as eyes at (380, 130) and (420, 130), each with side length 10.\n" +
                "   - Draw a semi-circle below the eyes, curving upward, from (380, 190) to (420, 190).\n" +
                "\n" +
                "6. **Face 6**:\n" +
                "   - Draw a rectangle for the face with width 120 and height 150 at (500, 250).\n" +
                "   - Draw two circles as eyes at (520, 280) and (580, 280) with radius 15.\n" +
                "   - Draw a curved polyline as a smile, starting at (520, 330), curving upward, and ending at (580, 330).\n" +
                "\n" +
                "7. **Face 7**:\n" +
                "   - Draw a hexagon for the face, centered at (600, 400) with side length 40.\n" +
                "   - Add two small circles for eyes at (580, 370) and (620, 370) with radius 8.\n" +
                "   - Draw a straight line as a smile from (580, 420) to (620, 420).\n" +
                "\n" +
                "**New Face to Create**:\n" +
                "Draw a face using a large shape (circle, oval, or square), two smaller circles for eyes, and a polyline or arc for a smile. Use coordinates similar to the examples but feel free to change colors and slightly vary sizes.\n" +
                "Ensure the file starts with 'Paint Save File Version 1.0' and ends with 'End Paint Save File'.";

        newFile(prompt, outFileName);
    }
    /**
     * Create a new Paint Save File that represents a simple pattern of interlocking circles
     * @param outFileName the name of the new file in the user's home directory
     */
    @Override
    public void newFile2(String outFileName) {
        String prompt = "You are to create a simple pattern using interlocking circles, inspired by the Audi logo. The following 7 examples demonstrate how to create different patterns with interlocking or overlapping shapes, using only circles, rectangles, squiggles, and polylines.\n" +
                "\n" +
                "**Examples for Creating Simple Interlocking Patterns**:\n" +
                "\n" +
                "1. **Pattern 1**:\n" +
                "   - Draw four interlocking circles in a horizontal line, starting at (100, 100).\n" +
                "   - Each circle should have a radius of 30, and they should overlap each other, sharing a part of their edge.\n" +
                "\n" +
                "2. **Pattern 2**:\n" +
                "   - Draw three interlocking circles arranged vertically, centered at (200, 200).\n" +
                "   - Each circle should have a radius of 25, and they should overlap such that the bottom of one circle touches the top of the next.\n" +
                "\n" +
                "3. **Pattern 3**:\n" +
                "   - Draw four interlocking circles in a diamond pattern, centered around (300, 300).\n" +
                "   - Each circle should have a radius of 20, overlapping their edges to form the diamond.\n" +
                "\n" +
                "4. **Pattern 4**:\n" +
                "   - Draw two interlocking circles and place a rectangle in between them, starting at (400, 150).\n" +
                "   - The circles should have a radius of 40, and the rectangle should have a width of 20 and height of 80, appearing as if the rectangle passes through the two circles.\n" +
                "\n" +
                "5. **Pattern 5**:\n" +
                "   - Draw five overlapping circles in a row, starting at (100, 400).\n" +
                "   - Each circle should have a radius of 20, and they should overlap just enough to create a chain-like effect.\n" +
                "\n" +
                "6. **Pattern 6**:\n" +
                "   - Draw four circles interlocked in a square pattern, with the first circle starting at (500, 200).\n" +
                "   - Each circle should have a radius of 30, and their edges should overlap to form a square.\n" +
                "\n" +
                "7. **Pattern 7**:\n" +
                "   - Draw three interlocking circles in a triangular formation, starting at (600, 300).\n" +
                "   - Each circle should have a radius of 25, with the circles overlapping such that they share a common point at the center of the triangle.\n" +
                "\n" +
                "**New Pattern to Create**:\n" +
                "Create a simple pattern using interlocking circles, inspired by the examples above. Use a combination of overlapping circles and feel free to add simple polylines or rectangles to make the pattern visually balanced.\n" +
                "Ensure the file starts with 'Paint Save File Version 1.0' and ends with 'End Paint Save File'.";

        newFile(prompt, outFileName);
    }


    /**
     * Create a new Paint Save File that shows  a simple forest with trees
     * @param outFileName the name of the new file in the user's home directory
     */
    @Override
    public void newFile3(String outFileName) {
        String prompt = "You are to create a simple forest scene using basic geometric shapes. Follow these 7 examples to understand how to use basic shapes to form trees and other nature elements.\n" +
                "\n" +
                "**Examples for Creating Simple Forest Scenes**:\n" +
                "\n" +
                "1. **Forest Scene 1**:\n" +
                "   - Draw three tree trunks as rectangles at (50, 300), (150, 300), and (250, 300), each with width 20 and height 60.\n" +
                "   - Draw circles on top of each trunk with radius 40 for foliage.\n" +
                "\n" +
                "2. **Forest Scene 2**:\n" +
                "   - Draw two tall triangles for pine trees with base width 60 at (100, 400) and (200, 400).\n" +
                "   - Each tree has a brown rectangle trunk of height 30 below it.\n" +
                "\n" +
                "3. **Forest Scene 3**:\n" +
                "   - Create a bush using overlapping ovals centered at (300, 350).\n" +
                "   - Add a sun using a yellow circle at (400, 100).\n" +
                "\n" +
                "4. **Forest Scene 4**:\n" +
                "   - Draw a single tree with a thick trunk of width 40 at (500, 300).\n" +
                "   - Add large foliage using an oval of height 80.\n" +
                "\n" +
                "5. **Forest Scene 5**:\n" +
                "   - Draw multiple small bushes as circles clustered at (600, 450) with radius 20.\n" +
                "   - Draw a cloud in the sky using three overlapping white ovals at (500, 100).\n" +
                "\n" +
                "6. **Forest Scene 6**:\n" +
                "   - Create two trees with trunks of height 50 at (150, 500) and (300, 500).\n" +
                "   - Draw foliage using a mix of circles and stars to add variety.\n" +
                "\n" +
                "7. **Forest Scene 7**:\n" +
                "   - Draw a lake using an irregular oval at (400, 600).\n" +
                "   - Add trees around the lake, using simple rectangles and circles for trunks and foliage.\n" +
                "\n" +
                "**New Forest Scene to Create**:\n" +
                "Draw a simple forest scene with multiple trees, a bush, and an optional lake or sun. Use coordinates and shapes similar to the examples to create a balanced and visually pleasing scene.\n" +
                "Ensure the file starts with 'Paint Save File Version 1.0' and ends with 'End Paint Save File'.";

        newFile(prompt, outFileName);
    }

    /**
     * Modify a Paint Save File to change all colors to different shades of blue
     * @param inFileName the name of the source file in the users home directory
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void modifyFile1(String inFileName, String outFileName) {
        String prompt = "Change the color of every shape to a different shade of blue (e.g., light blue, dark blue, sky blue). " +
                "Ensure the file starts with 'Paint Save File Version 1.0' and ends with 'End Paint Save File'.";
        modifyFile(prompt, inFileName, outFileName);
    }

    /**
     * Modify a Paint Save File by replacing all circles with triangles
     * @param inFileName the name of the source file in the users home directory
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void modifyFile2(String inFileName, String outFileName) {
        String prompt = "Add a rectangle between every pair of interlocking circles. The rectangle should be centered between the circles and have a width of 20 and a height of 50. " +
                "Ensure the output is strictly in Paint Save File format, beginning with 'Paint Save File Version 1.0' and ending with 'End Paint Save File'.";

        modifyFile(prompt, inFileName, outFileName);
    }
    /**
     * Modify a Paint Save File to remove all rectangles and add stars
     * @param inFileName the name of the source file in the users home directory
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void modifyFile3(String inFileName, String outFileName) {
        String prompt = "Remove all rectangles from the file. For each removed rectangle, draw a star with 5 points at the center of where the rectangle was originally located. " +
                "Assign each star a color randomly chosen from yellow, red, or green. " +
                "Ensure the file starts with 'Paint Save File Version 1.0' and ends with 'End Paint Save File'.";
        modifyFile(prompt, inFileName, outFileName);
    }

    public static void main(String [] args){
        String prompt = null;

        prompt="Draw a 100 by 120 rectangle with 4 radius 5 circles at each rectangle corner.";
        OllamaPaint op = new OllamaPaint("dh2010pc27.utm.utoronto.ca"); // Replace this with your assigned Ollama server.

        prompt="Draw a 100 by 120 rectangle with 4 radius 5 circles at each rectangle corner.";
        op.newFile(prompt, "OllamaPaintFile1.txt");
        op.modifyFile("Remove all shapes except for the circles.","OllamaPaintFile1.txt", "OllamaPaintFile2.txt" );

        prompt="Draw 5 concentric circles with different colors.";
        op.newFile(prompt, "OllamaPaintFile3.txt");
        op.modifyFile("Change all circles into rectangles.", "OllamaPaintFile3.txt", "OllamaPaintFile4.txt" );

        prompt="Draw a polyline then two circles then a rectangle then 3 polylines all with different colors.";
        op.newFile(prompt, "OllamaPaintFile4.txt");

        prompt="Modify the following Paint Save File so that each circle is surrounded by a non-filled rectangle. ";
        op.modifyFile("Change all circles into rectangles.", "OllamaPaintFile4.txt", "OllamaPaintFile5.txt" );

        for(int i=1;i<=3;i++){
            op.newFile1("PaintFile1_"+i+".txt");
            op.newFile2("PaintFile2_"+i+".txt");
            op.newFile3("PaintFile3_"+i+".txt");
        }
        for(int i=1;i<=3;i++){
            for(int j=1;j<=3;j++) {
                op.modifyFile1("PaintFile"+ i +"_"+j+ ".txt", "PaintFile"+ i +"_"+j+"_1.txt");
                op.modifyFile2("PaintFile"+ i +"_"+j+ ".txt", "PaintFile"+ i +"_"+j+"_2.txt");
                op.modifyFile3("PaintFile"+ i +"_"+j+ ".txt", "PaintFile"+ i +"_"+j+"_3.txt");
            }
        }
    }
}