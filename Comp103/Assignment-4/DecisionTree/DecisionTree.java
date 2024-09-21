// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 4
 * Name:
 * Username:
 * ID:
 */

/**
 * Implements a decision tree that asks a user yes/no questions to determine a decision.
 * Eg, asks about properties of an animal to determine the type of animal.
 * 
 * A decision tree is a tree in which all the internal nodes have a question, 
 * The answer to the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have the decision (the kind of animal in the example tree).
 *
 * The decision tree may be a predermined decision tree, or it can be a "growing"
 * decision tree, where the user can add questions and decisions to the tree whenever
 * the tree gives a wrong answer.
 *
 * In the growing version, when the program guesses wrong, it asks the player
 * for another question that would help it in the future, and adds it (with the
 * correct answers) to the decision tree. 
 *
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;

public class DecisionTree {

    public DTNode theTree;    // root of the decision tree;

    /**
     * Setup the GUI and make a sample tree
     */
    public static void main(String[] args){
        DecisionTree dt = new DecisionTree();
        dt.setupGUI();
        dt.loadTree("sample-animal-tree.txt");
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Load Tree", ()->{loadTree(UIFileChooser.open("File with a Decision Tree"));});
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Run Tree", this::runTree);
        UI.addButton("Grow Tree", this::growTree);
        UI.addButton("Save Tree", this::saveTree);  // for completion
        // UI.addButton("Draw Tree", this::drawTree);  // for challenge
        UI.addButton("Reset", ()->{loadTree("sample-animal-tree.txt");});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**  
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree,
     * and then its "no" subtree.
     * Needs a recursive "helper method" which is passed a node.
     * 
     * COMPLETION:
     * Each node should be indented by how deep it is in the tree.
     * The recursive "helper method" is passed a node and an indentation string.
     *  (The indentation string will be a string of space characters)
     */
    public void printTree(){
        UI.clearText();
        /*# YOUR CODE HERE */

        printTreeHelper(theTree, "", "");
    }

    private void printTreeHelper(DTNode node, String indent, String start) {
        if (node != null) {
            
            /*DO CORE WITHOUT INDENTATION */

            if (node.isAnswer()){
                UI.println(indent + node.getText());
            } else{
                UI.println(indent + node.getText() + "?");
            }


            if (node.getYes() != null) { // Use getYes() and getNo() instead of getYesChild() and getNoChild()
                printTreeHelper(node.getYes(), indent + "  " , "y:");
            }
            if (node.getNo() != null) {
                printTreeHelper(node.getNo(), indent + "  " , "n:");
            }
        }
    }

    /**
     * Run the tree by starting at the top (of theTree), and working
     * down the tree until it gets to a leaf node (a node with no children)
     * If the node is a leaf it prints the answer in the node
     * If the node is not a leaf node, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     */
    public void runTree() {
        /*# YOUR CODE HERE */
        DTNode currentNode = theTree;
        while (!currentNode.isAnswer()) {
            Boolean answer = UI.askBoolean(currentNode.getText() + "? (Y/N)");
            if (answer) {
                currentNode = currentNode.getYes();
            } else {
                currentNode = currentNode.getNo();
            }
        }
        UI.println("The answer is: " + currentNode.getText());
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
        /*# YOUR CODE HERE */
        //growTreeHelper(theTree, null, false);

        DTNode currentNode = theTree;
        while (!currentNode.isAnswer()) {
            Boolean answer = UI.askBoolean(currentNode.getText() + "? (Y/N)");
            if (answer) {
                currentNode = currentNode.getYes();
            } else {
                currentNode = currentNode.getNo();
            }
        }    
        boolean isCorrect = UI.askBoolean("I think I know. Is it a " + currentNode.getText() + "? ");
        if (!isCorrect){
            String correctAnswer = UI.askString("OK, what should the answer be? ");
            UI.println("Oh. I can't distinguish a " + currentNode.getText() + " from a " + correctAnswer);
            UI.println("Tell me something that's true for a " + correctAnswer + " but not for a " + currentNode.getText() + "?");
            String distinguishingProperty = UI.askString("Property: ");
       
            String textNode = currentNode.getText();
            currentNode.setText(distinguishingProperty);
            currentNode.setChildren(new DTNode(correctAnswer), new DTNode(textNode));
            
            UI.println("Thank you, I've updated my decision tree");

            return;
            
        } 
        UI.println("I'm glad I guessed it correctly!");
    }

    // You will need to define methods for the Completion and Challenge parts.

    // Written for you

    /** 
     * Loads a decision tree from a file.
     * Each line starts with either "Question:" or "Answer:" and is followed by the text
     * Calls a recursive method to load the tree and return the root node,
     *  and assigns this node to theTree.
     */
    public void loadTree (String filename) { 
        if (!Files.exists(Path.of(filename))){
            UI.println("No such file: "+filename);
            return;
        }
        try{theTree = loadSubTree(new ArrayDeque<String>(Files.readAllLines(Path.of(filename))));}
        catch(IOException e){UI.println("File reading failed: " + e);}
    }

    /**
     * Loads a tree (or subtree) from a Scanner and returns the root.
     * The first line has the text for the root node of the tree (or subtree)
     * It should make the node, and 
     *   if the first line starts with "Question:", it loads two subtrees (yes, and no)
     *    from the scanner and add them as the  children of the node,
     * Finally, it should return the  node.
     */
    public DTNode loadSubTree(Queue<String> lines){
        Scanner line = new Scanner(lines.poll());
        String type = line.next();
        String text = line.nextLine().trim();
        DTNode node = new DTNode(text);
        if (type.equals("Question:")){
            DTNode yesCh = loadSubTree(lines);
            DTNode noCh = loadSubTree(lines);
            node.setChildren(yesCh, noCh);
        }
        return node;

    }


    /**
 * Save the decision tree to a file in a way that it can be loaded back in (by the loadTree() method).
 * The method will need to be recursive.
 */
public void saveTree(String filename) {
    try {
        PrintStream stream = new PrintStream(UIFileChooser.save(filename)); 
        saveTreeHelper(theTree, stream);
        stream.close();

        UI.println("Tree saved! ");
    } catch (IOException e) {
        UI.println("Error saving tree to file: " + e.getMessage());
    }
}

private void saveTreeHelper(DTNode node, PrintStream stream) {
    if (node != null) {
        if (node.isAnswer()) {
            stream.println("Answer: " + node.getText());
        } else {
            stream.println("Question: " + node.getText());
        }
        saveTreeHelper(node.getYes(), stream);
        saveTreeHelper(node.getNo(), stream);
    }
}

}
