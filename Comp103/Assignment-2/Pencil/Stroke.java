import ecs100.*;
import java.awt.Color;

public class Stroke {
    private double lastX;
    private double lastY;
    private Color strokeColor;
    private double x;
    private double y;
    private double thickness;

    public Stroke(double x, double y, double lastX, double lastY, double thickness, Color strokeColor) {
        this.x = x;
        this.y = y;
        this.lastX = lastX;
        this.lastY = lastY;
        this.thickness = thickness;
        this.strokeColor = strokeColor;
    }

    public void getWidth() {
        double width = Math.abs(this.x - this.lastX);
        System.out.println("Width: " + width);
    }

    public void drawStroke() {
        UI.setColor(strokeColor);
        UI.drawLine(this.lastX, this.lastY, this.x, this.y);
    }
}