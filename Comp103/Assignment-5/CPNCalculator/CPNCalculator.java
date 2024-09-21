// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 5
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;

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
        calc.runCalculator();
    }

    /** Setup the gui */
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
    

     public double evaluate(GTNode<ExpElem> expr) {
        if (expr == null) {
            return Double.NaN;
        }

        ExpElem element = expr.getItem();
        String operator = element.operator;

        if (operator.equals("#")) {
            // Handle numbers
            return element.value;
        } else if (operator.equals("+")) {
            // Handle addition (+)
            double result = 0.0;
            for (GTNode<ExpElem> child : expr) {
                result += evaluate(child);
            }
            return result;
        } else if (operator.equals("-")) {
            // Handle subtraction (-)
            if (expr.numberOfChildren() < 2) {
                System.out.println("Error: Operator '-' requires at least two operands.");
                return Double.NaN;
            }
            double result = evaluate(expr.getChild(0));
            for (int i = 1; i < expr.numberOfChildren(); i++) {
                result -= evaluate(expr.getChild(i));
            }
            return result;
        } else if (operator.equals("*")) {
            // Handle multiplication (*)
            double result = 1.0;
            for (GTNode<ExpElem> child : expr) {
                result *= evaluate(child);
            }
            return result;
        } else if (operator.equals("/")) {
            // Handle division (/)
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
        } else if (operator.equals("^")) {
            // Handle exponentiation (^)
            if (expr.numberOfChildren() != 2) {
                System.out.println("Error: Operator '^' requires exactly two operands.");
                return Double.NaN;
            }
            double base = evaluate(expr.getChild(0));
            double exponent = evaluate(expr.getChild(1));
            return Math.pow(base, exponent);
        } else if (operator.equals("sqrt")) {
            // Handle square root (sqrt)
            if (expr.numberOfChildren() != 1) {
                System.out.println("Error: Operator 'sqrt' requires exactly one operand.");
                return Double.NaN;
            }
            double operand = evaluate(expr.getChild(0));
            return Math.sqrt(operand);
        } else if (operator.equals("log")) {
            // Handle logarithm (log)
            if (expr.numberOfChildren() != 2) {
                System.out.println("Error: Operator 'log' requires exactly two operands.");
                return Double.NaN;
            }
            double base = evaluate(expr.getChild(0));
            double value = evaluate(expr.getChild(1));
            return Math.log(value) / Math.log(base);
        } else if (operator.equals("log")) {
            // Handle logarithm (log)
            if (expr.numberOfChildren() != 2) {
                System.out.println("Error: Operator 'log' requires exactly two operands.");
                return Double.NaN;
            }
            double base = evaluate(expr.getChild(0));
            double value = evaluate(expr.getChild(1));
            if (base <= 0 || base == 1) {
                System.out.println("Error: Invalid base for logarithm.");
                return Double.NaN;
            }
            if (value <= 0) {
                System.out.println("Error: Invalid value for logarithm.");
                return Double.NaN;
            }
            return Math.log(value) / Math.log(base);
        } else if (operator.equals("ln")) {
            // Handle natural logarithm (ln)
            if (expr.numberOfChildren() != 1) {
                System.out.println("Error: Operator 'ln' requires exactly one operand.");
                return Double.NaN;
            }
            double operand = evaluate(expr.getChild(0));
            return Math.log(operand);
        } else if (operator.equals("sin")) {
            // Handle sine (sin)
            if (expr.numberOfChildren() != 1) {
                System.out.println("Error: Operator 'sin' requires exactly one operand.");
                return Double.NaN;
            }
            double angle = evaluate(expr.getChild(0));
            return Math.sin(angle);
        } else if (operator.equals("cos")) {
            // Handle cosine (cos)
            if (expr.numberOfChildren() != 1) {
                System.out.println("Error: Operator 'cos' requires exactly one operand.");
                return Double.NaN;
            }
            double angle = evaluate(expr.getChild(0));
            return Math.cos(angle);
        } else if (operator.equals("tan")) {
            // Handle tangent (tan)
            if (expr.numberOfChildren() != 1) {
                System.out.println("Error: Operator 'tan' requires exactly one operand.");
                return Double.NaN;
            }
            double angle = evaluate(expr.getChild(0));
            return Math.tan(angle);
        } else if (operator.equals("dist")) {
            // Handle distance (dist)
            int numOperands = expr.numberOfChildren();
            if (numOperands != 4 && numOperands != 6) {
                System.out.println("Error: Operator 'dist' requires either four or six operands.");
                return Double.NaN;
            }
            double x1 = evaluate(expr.getChild(0));
            double y1 = evaluate(expr.getChild(1));
            double z1 = numOperands == 6 ? evaluate(expr.getChild(2)) : 0;
            double x2 = evaluate(expr.getChild(numOperands == 6 ? 3 : 2));
            double y2 = evaluate(expr.getChild(numOperands == 6 ? 4 : 3));
            double z2 = numOperands == 6 ? evaluate(expr.getChild(5)) : 0;
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
        } else if (operator.equals("avg")) {
            // Handle average (avg)
            if (expr.numberOfChildren() < 1) {
                System.out.println("Error: Operator 'avg' requires at least one operand.");
                return Double.NaN;
            }
            double sum = 0.0;
            int count = 0;
            for (GTNode<ExpElem> child : expr) {
                sum += evaluate(child);
                count++;
            }
            return sum / count;
        } else if (operator.equals("PI")) {
            return Math.PI;
        } else if (operator.equals("E")) {
            return Math.E;
        } else {
            System.out.println("Error: The operator '" + operator + "' is invalid.");
            return Double.NaN;
        }
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

