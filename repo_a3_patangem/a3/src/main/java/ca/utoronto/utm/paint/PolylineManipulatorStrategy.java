package ca.utoronto.utm.paint;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PolylineManipulatorStrategy extends ShapeManipulatorStrategy {
    private PolylineCommand polylineCommand;
    private boolean isDrawing;

    PolylineManipulatorStrategy(PaintModel paintModel) {
        super(paintModel);
        this.isDrawing = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            Point startPoint = new Point((int) e.getX(), (int) e.getY());
            if (!isDrawing) {
                polylineCommand = new PolylineCommand();
                polylineCommand.setWidth(2.0);
                this.addCommand(polylineCommand);
                isDrawing = true;
            }
            polylineCommand.add(startPoint);
        } else if (e.getButton() == MouseButton.SECONDARY && isDrawing) {
            isDrawing = false;
            polylineCommand.finalizeLine();
            polylineCommand = null;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isDrawing && polylineCommand != null) {
            Point nextPoint = new Point((int) e.getX(), (int) e.getY());
            polylineCommand.updatePreview(nextPoint);
        }
    }
}
