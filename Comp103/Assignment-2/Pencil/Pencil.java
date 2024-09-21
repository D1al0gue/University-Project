// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 2
 * Name: David Pranata Timothy Nangoi
 * Username: nangoidavi
 * ID: 300604132
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;

public class Pencil {
    private Stack<ArrayList<Point>> strokes = new Stack<>();
    private Stack<ArrayList<Point>> undoneStrokes = new Stack<>();
    private ArrayList<Point> currentStroke = new ArrayList<>();

    private Color currentColor = Color.black; //I put this as default color & width
    private float currentWidth = 3.0f;

    // Point class to represent individual points of the stroke
    private class Point {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Setup the GUI
     */
    public void setupGUI() {
        UI.setMouseMotionListener(this::doMouse);
        UI.addButton("Undo", this::undo);
        UI.addButton("Redo", this::redo);
        UI.addButton("Quit", UI::quit);
        UI.setLineWidth(currentWidth);
        UI.setDivider(0.0);
    }

    /**
     * Respond to mouse events
     */
    public void doMouse(String action, double x, double y) {
        if (action.equals("pressed")) {
            currentStroke = new ArrayList<>();
            currentStroke.add(new Point(x, y));
        } else if (action.equals("dragged")) {
            currentStroke.add(new Point(x, y));
            UI.setColor(currentColor);
            UI.setLineWidth(currentWidth);
            UI.drawLine(currentStroke.get(currentStroke.size() - 2).x,
                    currentStroke.get(currentStroke.size() - 2).y,
                    x, y);
        } else if (action.equals("released")) {
            strokes.push(new ArrayList<>(currentStroke));
            currentStroke.clear();
            undoneStrokes.clear();
        }
    }

    /**
     * Undo the last stroke
     */
    public void undo() {
        if (!strokes.isEmpty()) {
            undoneStrokes.push(strokes.pop());
            redrawAll();
        }
    }

    /**
     * Redo the last undone stroke
     */
    public void redo() {
        if (!undoneStrokes.isEmpty()) {
            strokes.push(undoneStrokes.pop());
            redrawAll();
        }
    }

    /**
     * Change the current drawing color
     */
    public void changeColor(String colorStr) {
        currentColor = Color.decode(colorStr);
    }

    /**
     * Change the current pen width
     */
    public void changeWidth(double width) {
        currentWidth = (float) width;
    }

    /**
     * Redraw all the strokes
     */
    public void redrawAll() {
        UI.clearGraphics();
        for (ArrayList<Point> stroke : strokes) {
            for (int i = 1; i < stroke.size(); i++) {
                UI.setColor(currentColor); //temporary (WIP)
                UI.setLineWidth(currentWidth); //temporary code placeholder

                UI.drawLine(stroke.get(i - 1).x, stroke.get(i - 1).y, stroke.get(i).x, stroke.get(i).y);
            }
        }
    }

    public static void main(String[] arguments) {
        Pencil pencil = new Pencil();
        pencil.setupGUI();
        UI.addButton("Black", () -> pencil.changeColor("#000000"));
        UI.addButton("Red", () -> pencil.changeColor("#FF0000"));
        UI.addSlider("Width", 1, 10, 3, pencil::changeWidth);
    }
}
