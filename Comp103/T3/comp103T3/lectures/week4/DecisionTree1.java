/**
 * Implements a decision tree that asks a user yes/no questions to determine a decision.
 * Eg, asks about properties of an animal to determine the type of animal.
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;

public class DecisionTreeComplete {

    // root of the decision tree;
    public DTNode theTree; //= new DTNode("Cat");

    /** Set up the interface */
    public void setupGUI(){
        UI.addButton("Run Tree", this::runTree);
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Grow Tree", this::growTree);
        //UI.addButton("Draw Tree", this::drawTree);  // for challenge
        UI.addButton("Initialise", this::initialiseTree);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**
     * Run the tree by starting at the top (of theTree), and working
     * down the tree until it gets to a leaf node (a node with no children)
     * If the node is a leaf it prints the answer in the node
     * If the node is not a leaf node, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     */
    public void runTree() {
        if (theTree==null){ UI.println("No decision tree"); return; }

        DTNode node = theTree;
        while (!node.isAnswer()){
            if (UI.askBoolean(node.getText())) {
                node = node.getYes();
            }
            else {
                node = node.getNo();
            }
        }
        // node has no children, so display the answer
        UI.println("The answer is: "+ node.getText());

    }

    /**  
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree, and then
     * its "no" subtree.
     * Each node should be indented by how deep it is in the tree.
     * Needs a recursive "helper method" which is passed a node and an indentation string.
     *  (The indentation string will be a string of space characters)
     */

    String myIndent = "  ";

    public void printTree(){
        UI.clearText();
        if (theTree==null){ 
            UI.println("Nothing to print!"); 
            return; 
        }
        printSubTree(theTree, myIndent);
        UI.println("===================");
    }

    /**
     * Recursively print a subtree, given the node at its root
     *  - print the text in the node with the given indentation
     *  - if it is a question node, then 
     *    print its two subtrees with increased indentation
     */
    public void printSubTree(DTNode node, String indent){
        if (node.isAnswer()){
            UI.println(indent + node.getText());
        }
        else {
            UI.println(indent + node.getText());
            printSubTree(node.getYes(), indent+myIndent);
            printSubTree(node.getNo(), indent+ myIndent);
        }
    }

    /**
     * Grow the tree by allowing the user to extend the tree.
     * Like runTree, it starts at the top (of theTree), and works its way down the tree
     *  until it finally gets to a leaf node. 
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it prints the decision, and asks if it is right.
     * If it was wrong, it
     *  - asks the user what the decision should have been,
     *  - asks for a question to distinguish the right decision from the wrong one
     *  - changes the text in the node to be the question
     *  - adds two new children (leaf nodes) to the node with the two decisions.
     */
    public void growTree () {
        if (theTree==null){ UI.println("No decision tree"); return; }
        DTNode node = theTree;

        while (!node.isAnswer()) {
            if (UI.askBoolean(node.getText())) {
                node = node.getYes();
            }
            else {
                node = node.getNo();
            }
        }
        // node is a leaf/answer
        String guess = node.getText();
        boolean ans = UI.askBoolean("I think I know. Is it a " + guess+"?");
        if (ans){
            UI.println("Yay!  I got it right");
        }
        else {
            String answer = UI.askString("OK, what should the answer be?");
            UI.println("Oh. I can't distinguish a "+ guess + " from a "+ answer);
            UI.println("Give me a question whose answer is yes for a "+answer+" but no for a "+guess+"?");
            String question = UI.askString("Question: "); 
            node.setText(question);
            node.setChildren(new DTNode(answer), new DTNode(guess));
            UI.println("\nThank you! I've updated my decision tree.");
        }

    }

    /** Set up an initial tree, three questions deep */
    public void initialiseTree(){
        theTree = new DTNode("Does it have fur?",
            new DTNode("Does it bark?",
                new DTNode("Dog"),
                new DTNode("Does it purr?",
                    new DTNode("Cat"),
                    new DTNode("Rat"))),
            new DTNode("Does it have feathers?",
                new DTNode("Does it fly?",
                    new DTNode("Kea"),
                    new DTNode("Kiwi")),
                new DTNode("Does it have legs?",
                    new DTNode("Skink"),
                    new DTNode("Eel"))));
    }

    /** main: call setupGUI */
    public static void main(String[] args){
        new DecisionTreeComplete().setupGUI();
    }

}
