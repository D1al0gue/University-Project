// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 5
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.*; //I imported this for pi and E

/**
 * Calculator for Cambridge-Polish Notation expressions
 * (see the description in the assignment page)
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template provides the method to read an expression and turn it into a tree.
 * You have to write the method to evaluate an expression tree.
 *  and also check and report certain kinds of invalid expressions
 */

public class CPNCalculator{

    /**
     * Setup GUI then run the calculator
     */
    public static void main(String[] args){
        CPNCalculator calc = new CPNCalculator();
        calc.setupGUI();

        //WRITE HERE WHICH PARTS OF THE ASSIGNMENT YOU HAVE COMPLETED
        // so the markers know what to look for.
        UI.println("""
         Replace this text (in the main method):
         core (and constants using java.math)
         completion
      
         --------------------
         """);

        calc.runCalculator();
    }

    /**
     * Setup the GUI
     */
    public void setupGUI(){
        UI.addButton("Clear", UI::clearText); 
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
    }

    /**
     * Run the calculator:
     * loop forever:  (a REPL - Read Eval Print Loop)
     *  - read an expression,
     *  - evaluate the expression,
     *  - print out the value
     * Invalid expressions could cause errors when reading or evaluating
     * The try-catch prevents these errors from crashing the program - 
     *  the error is caught, and a message printed, then the loop continues.
     */
    public void runCalculator(){
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        UI.println("Valid operations: +, -, *, /, ^, sqrt, log, ln, sin, cos, tan, avg, ");
        while (true){
            UI.println();
            try {
                GTNode<ExpElem> expr = readExpr();
                double value = evaluate(expr);
                UI.println(" -> " + value);
            }catch(Exception e){UI.println("Something went wrong! "+e);}
        }
    }

    /**
     * Evaluate an expression and return the value
     * Returns Double.NaN if the expression is invalid in some way.
     * If the node is a number
     *  => just return the value of the number
     * or it is a named constant
     *  => return the appropriate value
     * or it is an operator node with children
     *  => evaluate all the children and then apply the operator.
     */
    public double evaluate(GTNode<ExpElem> expr){
        if (expr==null){
            return Double.NaN;
        }

        ExpElem element = expr.getItem();
        String operator = element.getOperator();
        
        /*# YOUR CODE HERE */
        
        //numbers
        if (operator.equals("#")){
            //number (1)
            return element.getValue();
        }  
        
        else if (operator.equals("+")) {
            // addition (+)
            double result = 0.0;
            for (GTNode<ExpElem> child : expr) {
                result += evaluate(child);
            }
            return result;
        } 
        
        else if (operator.equals("-")) {
            //subtraction (-)
            if (expr.numberOfChildren() < 2){
                UI.println("Invalid operation: as the operator has less than 2 valid constants");
                return Double.NaN;
            } else {
                double result = evaluate(expr.getChild(0));
                for (int i = 1; i < expr.numberOfChildren(); i++) {
                    result -= evaluate(expr.getChild(i));
                }
                return result;
            }
        }

        else if (operator.equals("*")) {
            //multiplication (*)
            double result = 1.0;
            for (GTNode<ExpElem> child : expr){
                result *= evaluate(child);
            }
            return result;
        }

        else if (operator.equals("/")) {
            //division (/)
            if (expr.numberOfChildren() < 2) {
                System.out.println("Error: Operator '/' requires at least two operands.");
                return Double.NaN;
            }
            double result = evaluate(expr.getChild(0));
            for (int i = 1; i < expr.numberOfChildren(); i++) {
                double denominator = evaluate(expr.getChild(i));
                if (denominator == 0) {
                    System.out.println("Error: Division by zero.");
                    return Double.NaN;
                }
                result /= denominator;
            }
            return result;
        } 

        else if (operator.equals("PI")) {
            //pi
            return Math.PI;
        }

        else if (operator.equals("E")) {
            //e
            return Math.E;
        }

        else if (operator.equals("^")) {
            //exponentiation (^)
            if (expr.numberOfChildren() != 2){
                UI.println("Error: ^ needs EXACTLY 2 OPERATORS!");
                return Double.NaN;
            } 
            double base = evaluate(expr.getChild(0));
            double exponent = evaluate(expr.getChild(1));
            return Math.pow(base, exponent);
        } 

        else if (operator.equals("sqrt")) {
            if (expr.numberOfChildren() != 1){
                UI.println("Error: sqrt needs EXACTLY 1 OPERATOR!");
                return Double.NaN;
            }
            double i = evaluate(expr.getChild(0));
            return Math.sqrt(i);
        }   

        else if (operator.equals("log")) {
            //logarithm (log)
            if (expr.numberOfChildren() == 1) {
                double base = 10.0;
                double value = evaluate(expr.getChild(0));
                return Math.log(value) / Math.log(base);
            }

            else if (expr.numberOfChildren() != 2) {
                UI.println("Error: log requires EXACTLY two operator.");
                return Double.NaN;
            }

            else if (expr.numberOfChildren() == 2){ //put this check for debugging
                double base = evaluate(expr.getChild(1));
                double value = evaluate(expr.getChild(0));
                if (base <= 0 || base == 1) {
                    UI.println("Error: Invalid base for logarithm.");
                    return Double.NaN;
                }
                if (value <= 0) {
                    UI.println("Error: Invalid value for logarithm.");
                    return Double.NaN;
                }
                return Math.log(value) / Math.log(base);
            }
        }

        else if (operator.equals("ln")){
            if (expr.numberOfChildren() != 1) {
                UI.println("Error: ln requires EXACTLY one operator!");
                return Double.NaN;
            }
            double i = evaluate(expr.getChild(0));
            return Math.log(i);
        }

        else if (operator.equals("sin")) {
            //sin
            if (expr.numberOfChildren() != 1){
                UI.println("Error: sin requires EXACTLY one operator!");
                return Double.NaN;
            }
            double i = evaluate(expr.getChild(0));
            return Math.sin(i);
        }

        else if (operator.equals("cos")) {
            //cos
            if (expr.numberOfChildren() != 1){
                UI.println("Error: cos requires EXACTLY one operator!");
                return Double.NaN;
            }
            double i = evaluate(expr.getChild(0));
            return Math.cos(i);
        }

        else if (operator.equals("tan")) {
            //tan
            if (expr.numberOfChildren() != 1){
                UI.println("Error: tan requires EXACTLY one operator!");
                return Double.NaN;
            }
            double i = evaluate(expr.getChild(0));
            return Math.tan(i);
        }

        else if (operator.equals("dist")) {
            //dist
            int numOperands = expr.numberOfChildren();
            if (numOperands != 4 && numOperands != 6) {
                UI.println("Error: Operator 'dist' requires either four or six operands.");
                return Double.NaN;
            }
            double x1 = evaluate(expr.getChild(0));
            double y1 = evaluate(expr.getChild(1));
            double z1 = numOperands == 6 ? evaluate(expr.getChild(2)) : 0;
            double x2 = evaluate(expr.getChild(numOperands == 6 ? 3 : 2));
            double y2 = evaluate(expr.getChild(numOperands == 6 ? 4 : 3));
            double z2 = numOperands == 6 ? evaluate(expr.getChild(5)) : 0;
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
        }

        else if (operator.equals("avg")) {
            //average
            if (expr.numberOfChildren() < 1) {
                UI.println("Error: avg needs at least 1 operator!");
                return Double.NaN;
            }
            double sum = 0.0;
            int count = 0;

            for (GTNode<ExpElem> child : expr) {
                sum += evaluate(child);
                count++;
            }
            return sum/count;
        }

        UI.println(operator + " is not a valid operator.");
        return Double.NaN;
    }

    /**
     * Reads an expression from the user and constructs the tree.
     */ 
    public GTNode<ExpElem> readExpr(){
        String expr = UI.askString("expr:");
        return readExpr(new Scanner(expr));   // the recursive reading method
    }

    /**
     * Recursive helper method.
     * Uses the hasNext(String pattern) method for the Scanner to peek at next token
     */
    public GTNode<ExpElem> readExpr(Scanner sc){
        if (sc.hasNextDouble()) {                     // next token is a number: return a new node
            return new GTNode<ExpElem>(new ExpElem(sc.nextDouble()));
        }
        else if (sc.hasNext("\\(")) {                 // next token is an opening bracket
            sc.next();                                // read and throw away the opening '('
            ExpElem opElem = new ExpElem(sc.next());  // read the operator
            GTNode<ExpElem> node = new GTNode<ExpElem>(opElem);  // make the node, with the operator in it.
            while (! sc.hasNext("\\)")){              // loop until the closing ')'
                GTNode<ExpElem> child = readExpr(sc); // read each operand/argument
                node.addChild(child);                 // and add as a child of the node
            }
            sc.next();                                // read and throw away the closing ')'
            return node;
        }
        else {                                        // next token must be a named constant (PI or E)
                                                      // make a token with the name as the "operator"
            return new GTNode<ExpElem>(new ExpElem(sc.next()));
        }
    }

}
