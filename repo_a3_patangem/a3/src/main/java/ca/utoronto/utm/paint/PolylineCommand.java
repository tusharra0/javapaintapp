package ca.utoronto.utm.paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class PolylineCommand extends PaintCommand {
    private ArrayList<Point> points = new ArrayList<>();
    private Point previewPoint = null;
    private double lineWidth = 1.0;

    public void add(Point p) {
        points.add(p);
        this.setChanged();
        this.notifyObservers();
    }

    public void updatePreview(Point p) {
        this.previewPoint = p; // Update the preview point dynamically
        this.setChanged();
        this.notifyObservers();
    }

    public void finalizeLine() {
        if (previewPoint != null) {
            points.add(previewPoint); // Finalize the preview point into the polyline
            previewPoint = null;
        }
        this.setChanged();
        this.notifyObservers();
    }

    public void setWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }


    @Override
    public String getPaintSaveFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Polyline\n");
        sb.append("\tcolor:").append(this.getRed()).append(",")
                .append(this.getGreen()).append(",")
                .append(this.getBlue()).append("\n");
        sb.append("\tfilled:false\n");
        sb.append("\tpoints\n");

        for (Point point : points) {
            sb.append("\t\tpoint:(").append(point.x).append(",").append(point.y).append(")\n");
        }

        sb.append("\tend points\n");
        sb.append("End Polyline\n");
        return sb.toString();
    }

    @Override
    public void execute(GraphicsContext g) {

        g.setLineWidth(lineWidth);
        g.setStroke(this.getColor());
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g.strokeLine(p1.x, p1.y, p2.x, p2.y);
        }

        if (previewPoint != null && !points.isEmpty()) {
            Point lastPoint = points.get(points.size() - 1);
            g.strokeLine(lastPoint.x, lastPoint.y, previewPoint.x, previewPoint.y);
        }
    }
}
