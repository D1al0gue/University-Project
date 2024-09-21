// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 4
 * Name:
 * Username:
 * ID:
 */

/**
 * Implements a binary tree that represents the Morse code symbols, named after its inventor
 *   Samuel Morse.
 * Each Morse code symbol is formed by a sequence of dots and dashes.
 *
 * A Morse code chart has been provided with this assignment. This chart only contains the 26 letters
 * and 10 numerals. These are given in alphanumerical order. 
 *
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class MorseCode {

    public SymbolNode root = new SymbolNode(null,           // root of the morse code binary tree;
                                                           new SymbolNode("E",                             
                                                           new SymbolNode("I"),
                                                           new SymbolNode("A")),
                                                           new SymbolNode("T",                             
                                                           new SymbolNode("N"),
                                                           new SymbolNode("M")));

    /**
     * Setup the GUI and creates the morse code with characters up to 2 symbols
     */
    public static void main(String[] args){
        new MorseCode().setupGUI();

        //WRITE HERE WHICH PARTS OF THE ASSIGNMENT YOU HAVE COMPLETED
        // so the markers know what to look for.
        UI.println("""
         print tree,
         decode,
         addCoreCode
         addComplCode
         loadfile (something's wrong)
         drawTree
         Encode
         --------------------
         """);
         
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Print Tree", this::printTree);
        UI.addTextField("Decode ", this::decode);
        UI.addTextField("Add Code (Core)", this::addCodeCore);
        UI.addTextField("Add Code (Compl)", this::addCodeCompl);
        UI.addButton("Load File", this::loadFile);
        UI.addButton("draw tree", this::drawTree);
        UI.addTextField("Encode", this::encode);
        UI.addButton("Reset", ()->{root = new SymbolNode();});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,400);
        UI.setDivider(0.35);
    }

    
    /**
     * Decode a code by starting at the top (root), and working
     * down the tree following the dot or dash nodes according to the
     * code
     */
    public void decode(String code) {
        if ( ! isValidCode(code)){return;}
        /*# YOUR CODE HERE */
        SymbolNode node = root;

        for(int i = 0; i < code.length(); i++){

            if(code.startsWith("-")){
                node = node.getDashChild();
            } else {
                node =node.getDotChild();
            }

            if (node == null){
                //add data
                UI.println("There is no symbol for that code");
                return;
            }
        }
        UI.println(node.getSymbol());
    }

    /**  
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "dot" subtree, and then
     * its "dash" subtree.
     * Each node should be indented by how deep it is in the tree.
     * Needs a recursive "helper method" which is passed a node and an indentation string.
     *  (The indentation string will be a string of space characters plus the morse code leading
     *  to the node)
     */

    String indentation = " ";

    public void printTree(){
        /*# YOUR CODE HERE */
        UI.clearText();
        if (root == null){
            UI.println("nothing to print");
            return;
        }
        UI.println("Morse code Tree: ");

        printTreeHelper(root,indentation);
        UI.println("=======================");
    }

    private void printTreeHelper(SymbolNode node, String indent){
        //node = root;
        if (node == null){return;}
        if (root != null && node.getSymbol() != null){
            //UI.println(indent + node.getSymbol() + " = " + node.getSymbol());
            UI.println(indent + " = " + node.getSymbol());
        }

        if (node.getSymbol() == null){
            UI.println(indent);
        }
    
        printTreeHelper(node.getDotChild(), " " + indent + ".");
        printTreeHelper(node.getDashChild(), " " + indent + "-");
    }
       

    /**
     * Add a new code to the tree (as long as it will be in a node just below an existing node).
     * Follows the code down the tree (like decode)
     * If it finds a node for the code, then it reports the current symbol for that code
     * If it it gets to a node where there is no child for the next . or - in the code then
     *  If this is the last . or - in the code, it asks for a new symbol and adds a new node
     *  If there is more than one . or - left in the code, it gives up and says it can't add it.
     * For example,
     *  If it is adding the code (.-.) and the tree has "A" (.-) but doesn't have (.-.),
     *   then it should ask for the symbol (R) add a child node with R
     *  If it is adding the code (.-..) and the tree has "A" (.-) but doesn't have (.-.),
     *   then it would not attempt to add a node for (.-..) (L) because that would require
     *   adding more than one node.
     */
    public void addCodeCore(String code) {
        if (!isValidCode(code)) {
            return;
        }
    
        SymbolNode node = root;
    
        for (int i = 0; i < code.length() - 1; i++) {
            char c = code.charAt(i);
    
            if (node.getDashChild() == null && node.getDotChild() == null) {
                UI.println("Code " + code + " is already in the tree with the " + node.getSymbol() + " symbol");
                return;
            } else {
                if (c == '-') {
                    if (node.getDashChild() == null) {
                        // If the next node is null, create a new node with null symbol
                        SymbolNode newNode = new SymbolNode();
                        node.setDashChild(newNode);
                    }
                    node = node.getDashChild();
                } else if (c == '.') {
                    if (node.getDotChild() == null) {
                        // If the next node is null, create a new node with null symbol
                        SymbolNode newNode = new SymbolNode();
                        node.setDotChild(newNode);
                    }
                    node = node.getDotChild();
                }
            }
        }
    
        // If this is the last . or - in the code, ask for a new symbol and add a new node
        char lastChar = code.charAt(code.length() - 1);
        if (lastChar == '-') {
            if (node.getDashChild() == null) {
                String newSymbol = UI.askString("Enter the symbol for code " + code);
                SymbolNode newNode = new SymbolNode(newSymbol);
                node.setDashChild(newNode);
                UI.println(newSymbol +" has successfully added");
            }
        } else if (lastChar == '.') {
            if (node.getDotChild() == null) {
                String newSymbol = UI.askString("Enter the symbol for code " + code);
                SymbolNode newNode = new SymbolNode(newSymbol);
                node.setDotChild(newNode);
                UI.println(newSymbol + " has successfully been added");
            }
        }
    }

    // COMPLETION ======================================================
    /**
     * Grow the tree by allowing the user to add any symbol, whether there is a path leading to it.
     * Like addCodeCore, it starts at the top (root), and works its way down the tree
     *  following the dot or dash nodes according to the given sequence of the code.
     * If an intermediate node does not exist, it needs to be created with a text set to null.
     */
    public void addCodeCompl(String code) {
        if (!isValidCode(code)) {
            return;
        }
    
        SymbolNode node = root;
    
        for (int i = 0; i < code.length() - 1; i++) {
            char c = code.charAt(i);
    
            if (c == '-') {
                if (node.getDashChild() == null) {
                    SymbolNode newNode = new SymbolNode();  // Create a new node with null symbol
                    node.setDashChild(newNode);
                }
                node = node.getDashChild();
            } else if (c == '.') {
                if (node.getDotChild() == null) {
                    SymbolNode newNode = new SymbolNode();  // Create a new node with null symbol
                    node.setDotChild(newNode);
                }
                node = node.getDotChild();
            }
        }
    
        // If this is the last . or - in the code, ask for a new symbol and add a new node
        char lastChar = code.charAt(code.length() - 1);
        if (lastChar == '-') {
            SymbolNode newNode = new SymbolNode();
            node.setDashChild(newNode);
            UI.println("Node for " + code + " has been successfully created with a null symbol");
        } else if (lastChar == '.') {
            SymbolNode newNode = new SymbolNode();
            node.setDotChild(newNode);
            UI.println("Node for " + code + " has been successfully created with a null symbol");
        }
    }

    /**
     * Load a collection of symbols and codes from a file.
     * Each line contains a symbol and the corresponding morse code.
     * The file should have the format: symbol code
     */
    public void loadFile() {
        try {
            String fileName = UIFileChooser.open("Choose a file to load");
            if (fileName == null) {
                return; // User canceled the file chooser
            }

            Path filePath = Paths.get(fileName);
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String symbol = parts[0].toUpperCase(); // Ensure symbol is in uppercase
                    String code = parts[1];

                    // Use addCodeCompl to add the symbol and code to the tree
                    addCodeCompl(code);

                    // Set the symbol for the corresponding node
                    SymbolNode node = findNodeForCode(root, code);
                    if (node != null) {
                        node.setSymbol(symbol);
                        UI.println("Loaded: " + symbol + " with code: " + code);
                    } else {
                        UI.println("Error loading: " + symbol + " with code: " + code);
                    }
                } else {
                    UI.println("Invalid line in the file: " + line);
                }
            }

        } catch (IOException e) {
            UI.println("Error reading file: " + e.getMessage());
        }
    }



    /**
    * Helper method to find the node for a given code in the tree.
    */
    private SymbolNode findNodeForCode(SymbolNode node, String code) {
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '-') {
                if (node.getDashChild() == null) {
                    return null; // Node not found
                }
                node = node.getDashChild();
            } else if (c == '.') {
                if (node.getDotChild() == null) {
                    return null; // Node not found
                }
                node = node.getDotChild();
            }
        }
        return node;
    }

    /**
 * Draw the tree in the graphics window.
 * Nodes are displayed as rectangles with text, and lines connect parent nodes to their child nodes.
 * Dots are on the left edges, dashes are on the right edges.
 */
public void drawTree() {
    UI.clearGraphics();
    if (root != null) {
        drawTreeHelper(root, 400, 50, 0);
    }
}

/**
 * Helper method to recursively draw the tree.
 */
private void drawTreeHelper(SymbolNode node, double x, double y, int depth) {
    if (node != null) {
        node.draw(x, y);

        //got a bit of help with AI
        double nextX = x - Math.pow(2, 5 - depth) * SymbolNode.WIDTH / 2;
        double nextY = y + 50;

        if (node.getDotChild() != null) {
            UI.drawLine(x, y, nextX, nextY);
            //recursion to draw the dot side
            drawTreeHelper(node.getDotChild(), nextX, nextY, depth + 1);
        }

        //got a bit of help with AI
        nextX = x + Math.pow(2, 5 - depth) * SymbolNode.WIDTH / 2;
        nextY = y + 50;

        if (node.getDashChild() != null) {
            UI.drawLine(x, y, nextX, nextY);
            //recursion to draw the dash side
            drawTreeHelper(node.getDashChild(), nextX, nextY, depth + 1);
        }
    }
}

    /**
     * Encode a symbol and print the Morse code sequence associated with it.
     * It searches the tree for a node with that symbol, recording (or reconstructing) the sequence of dots and dashes
     * that lead to that symbol, and then outputs the code.
     */
    public void encode(String symbol) {
        String code = encodeHelper(root, symbol, "");
        if (code != null) {
            UI.println("Morse Code for " + symbol + ": " + code);
        } else {
            UI.println("Symbol " + symbol + " not found in the tree");
        }
    }

    /**
     * Helper method to recursively search for the symbol in the tree and construct the Morse code.
     */
    private String encodeHelper(SymbolNode node, String symbol, String currentCode) {
        if (node == null) {
            return null;
        }

        if (node.getSymbol() != null && node.getSymbol().equals(symbol)) {
            return currentCode;
        }

        String dotCode = encodeHelper(node.getDotChild(), symbol, currentCode + ".");
        if (dotCode != null) {
            return dotCode;
        }

        String dashCode = encodeHelper(node.getDashChild(), symbol, currentCode + "-");
        return dashCode;
    }



    // Utility methods ===============================================

    /**
     * Checks whether the code is a sequence of . and - only.
     */
    public boolean isValidCode (String code) {
        if (code.equals("")){
            UI.println("Code is empty");
            return false;
        }
        for (int index=0; index<code.length(); index++){
            char c = code.charAt(index);
            if (c != '-' && c != '.'){
                UI.println("Code has an invalid character: "+c);
                return false;
            }
        }
        return true;
    }
}
