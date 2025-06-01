package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;

public class CircleCommand extends PaintCommand {
	private Point centre;
	private int radius;
	
	public CircleCommand(Point centre, int radius){
		this.centre = centre;
		this.radius = radius;
	}
	public Point getCentre() { return centre; }
	public void setCentre(Point centre) { 
		this.centre = centre; 
		this.setChanged();
		this.notifyObservers();
	}
	public int getRadius() { return radius; }
	public void setRadius(int radius) { 
		this.radius = radius; 
		this.setChanged();
		this.notifyObservers();
	}


	@Override
	public String getPaintSaveFileString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Circle\n");
		sb.append("\tcolor:").append(this.getRed()).append(",")
				.append(this.getGreen()).append(",")
				.append(this.getBlue()).append("\n");
		sb.append("\tfilled:").append(this.isFill()).append("\n");
		sb.append("\tcenter:(").append(centre.x).append(",").append(centre.y).append(")\n");
		sb.append("\tradius:").append(radius).append("\n");
		sb.append("EndCircle\n");
		return sb.toString();
	}


	public void execute(GraphicsContext g){
		int x = this.getCentre().x;
		int y = this.getCentre().y;
		int radius = this.getRadius();
		if(this.isFill()){
			g.setFill(this.getColor());
			g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		} else {
			g.setStroke(this.getColor());
			g.strokeOval(x-radius, y-radius, 2*radius, 2*radius);
		}
	}
}
