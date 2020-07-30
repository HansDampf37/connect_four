package bot.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import model.procedure.ConsoleOutput;

/**
 * Utilized to represent a states of a 4-gewinnt-match. Node A is the child of node B <-> it exists a move that you can do in state B that brings you to state A
 */
public class Tree implements Iterable<Node> {
    /**
     * Root node
     */
    private Node root;
    /**
     * All nodes in the tree without children
     */
    private List<Node> leaves = new ArrayList<>();

    /**
     * Constructor
     *
     * @param depth  amount of levels
     * @param spread amount of children per node
     */
    public Tree(int depth, int spread) {
        root = new Node(this);
        addChildNodesToTree(root, spread, depth, 0);
        if (ConsoleOutput.treeInitiation) System.out.println(this);
    }

    /**
     * Recursive method that builds the tree.
     * If the current level == the depth the current node is added to leaves.
     * Else <spread> children are generated and in each of them this method is called again but with lvl = lvl + 1
     *
     * @param current current node
     * @param spread  amount of children per node
     * @param depth   amount of levels
     * @param lvl     current level
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
        return root.toString();
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
     *
     * @param node a node
     */
    void removeLeave(Node node) {
        leaves.remove(node);
    }

    /**
     * adds a given node to the leaves list
     *
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
     *
     * @return the tree's root
     */
    public Node getRoot() {
        return root;
    }

    @Override
    public Iterator<Node> iterator() {
        return new DFS();
    }

    private class DFS implements Iterator<Node> {
        private HashMap<Node, Node> parentsForNode = new HashMap<>();
        private HashMap<Node, Integer> indexForNode = new HashMap<>();
        private Node current;

        @Override
        public boolean hasNext() {
            return current == null || !current.equals(leaves.get(leaves.size() - 1));
        }

        @Override
        public Node next() {
            if (current == null) {
                return (current = root);
            } else if (current.isLeave() ||
                    (indexForNode.containsKey(current) && current.getAmountOfChildren() <= indexForNode.get(current))) {
                //rollback
                current = parentsForNode.get(current);
                return next();
            } else {
                if (!indexForNode.containsKey(current)) indexForNode.put(current, 0);
                int nextInd = indexForNode.get(current);
                Node next = current.getChild(nextInd);
                indexForNode.put(current, nextInd + 1);
                parentsForNode.put(next, current);
                current = next;
                return current;
            }
        }
    }
}