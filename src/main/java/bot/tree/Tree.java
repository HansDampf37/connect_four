package bot.tree;

import java.util.ArrayList;
import java.util.List;

import bot.ConsoleOutput;
import model.Board;

/**
 * Utilized to represent a states of a 4-gewinnt-match. Node A is the child of node B <-> it exists a move that you can do in state B that brings you to state A
 */
public class Tree {
    /**
     * Root node
     */
    private Node start;
    /**
     * All nodes in the tree without children
     */
    private List<Node> leaves = new ArrayList<>();

    /**
     * Constructor
     * @param depth amount of levels
     * @param spread amount of children per node
     */
    private Tree(int depth, int spread) {
        start = new Node(this);
        addChildNodesToTree(start, spread, depth, 0);
        if (ConsoleOutput.treeInitiation) System.out.println(this);
    }

    /**
     * Constructor generates a tree with a given depth and a spread of Board.WIDTH
     * @param depth the amount of levels
     */
    public Tree(int depth) {
        this(depth, Board.WIDTH);
    }

    /**
     * Recursive method that builds the tree.
     * If the current level == the depth the current node is added to leaves. 
     * Else <spread> children are generated and in each of them this method is called again but with lvl = lvl + 1
     * @param current current node
     * @param spread amount of children per node
     * @param depth amount of levels
     * @param lvl current level
     */
    private void addChildNodesToTree(Node current, int spread, int depth, int lvl) {
        if (lvl == depth) {
            addLeave(current);
            // leaves.add(current);
        } else {
            for (int i = 0; i < spread; i++) {
                current.addChild();
                addChildNodesToTree(current.getChild(i), spread, depth, lvl + 1);
            }
        }
    }

    @Override
    public String toString() {
        return start.toString();
    }

    public String leavesToString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < leaves.size(); i++) {
            str.append(leaves.get(i).getValue()).append(i != leaves.size() - 1 ? ", " : "");
        }
        return str.toString();
    }

    /**
     * removes a given node from the leaves list
     * @param node a node
     */
    void removeLeave(Node node) {
        leaves.remove(node);
    }
    
    /**
     * adds a given node to the leaves list
     * @param node a node
     */
    void addLeave(Node node) {
        if (!leaves.contains(node)) {
            leaves.add(node);
        }
    }

    Node getLeave(int index) {
        return leaves.get(index);
    }

    int getAmountOfLeaves() {
        return leaves.size();
    }

    /**
     * returns the root-Node
     * @return the tree's root
     */
    public Node getRoot() {
        return start;
    }
}