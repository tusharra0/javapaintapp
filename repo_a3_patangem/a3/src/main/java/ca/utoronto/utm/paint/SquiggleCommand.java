package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class SquiggleCommand extends PaintCommand {
	private ArrayList<Point> points=new ArrayList<Point>();
	
	public void add(Point p){ 
		this.points.add(p); 
		this.setChanged();
		this.notifyObservers();
	}
	public ArrayList<Point> getPoints(){ return this.points; }


	@Override
	public String getPaintSaveFileString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Squiggle\n");
		sb.append("\tcolor:").append(this.getRed()).append(",")
				.append(this.getGreen()).append(",")
				.append(this.getBlue()).append("\n");
		sb.append("\tfilled:false\n");
		sb.append("\tpoints\n");

		for (Point point : points) {
			sb.append("\t\tpoint:(").append(point.x).append(",").append(point.y).append(")\n");
		}

		sb.append("\tend points\n");
		sb.append("End Squiggle\n");
		return sb.toString();
	}

	
	@Override
	public void execute(GraphicsContext g) {
		ArrayList<Point> points = this.getPoints();
		g.setStroke(this.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		
	}
}
