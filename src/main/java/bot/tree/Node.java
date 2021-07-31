package bot.tree;

import model.procedure.ConsoleOutput;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A node in the {@link Tree tree-graph}
 */
public class Node implements List<Node> {
    /**
     * An integer associated with that node
     */
    private int val;
    /**
     * A list of children
     */
    private List<Node> children = new ArrayList<>();
    /**
     * a reference to the containing tree
     */
    private Tree tree;
    /**
     * If a Node represents an illegal state of the game it should be invisible meaning no matter what comparison is happening this node is losing
     */
    public boolean invisible;

    /**
     * Constructor
     * @param tree containing tree
     */
    public Node(Tree tree) {
        this(tree, 0);
    }

    /**
     * Constructor
     * @param tree containing tree
     * @param value associated value
     */
    private Node(Tree tree, int value) {
        this.val = value;
        this.tree = tree;
    }

    /**
     * Adds a child to a tree
     */
    void addChild() {
        if (children.isEmpty()) tree.removeLeave(this);
        Node child = new Node(tree);
        children.add(child);
        tree.addLeave(child);
    }

    /**
     * removes a child from this nodes children list
     */
    public void removeChild(Node child) {
        tree.removeLeave(child);
        children.remove(child);
        if (children.isEmpty()) tree.addLeave(this);
    }

    /**
     * gets a child with a given index
     * @param index the index
     * @return the child with given index
     */
    public Node getChild(int index) {
        return children.get(index);
    }

    /**
     * sets the value
     */
    public void setValue(int newValue) {
        val = newValue;
    }

    /**
     * return the value
     */
    public int getValue() {
        return val;
    }

    /**
     * sets this nodes value to the minimum value of all it's child's values
     */
    private void setValueToMinimumChild() {
        val = Integer.MAX_VALUE;
        for (Node child : children) if (child.isVisible() && child.getValue() < val) val = child.getValue();
    }

    /**
     * sets this nodes value to the maximum value of all it's child's values
     */
    private void setValueToMaximumChild() {
        val = Integer.MIN_VALUE;
        for (Node child : children) if (child.isVisible() && child.getValue() > val) val = child.getValue();
    }

    /**
     * recursive method that is invoked alternating with {@link #passLow()}. This method chooses the maximum value of all the children's values and sets it's own value
     * to this value. If their children aren't leaves {@link #passLow()} is invoked on them. Overall this is a simulation of a game where two players traverse
     * a tree and one of them tries to reach leaves with minimal values and the other one wants to reach leaves with maximum values.
     */
    private void passHigh() {
        for (Node child : children) {
            if (child.hasChildren()){
                child.passLow();
            }
        }
        setValueToMaximumChild();
    }

    /**
     * recursive method that is invoked alternating with {@link #passHigh()}. This method chooses the minimum value of all the children's values and sets it's own value
     * to this value. If their children aren't leaves {@link #passHigh()} is invoked on them. Overall this is a simulation of a game where two players traverse
     * a tree and one of them tries to reach leaves with minimal values and the other one wants to reach leaves with maximum values.
     */
    private void passLow() {
        for (Node child : children) {
            if (child.hasChildren()){
                child.passHigh();
            }
        }
        setValueToMinimumChild();
    }

    /**
     * This Method uses {@link #passLow()} to determine the index of the child with the best outcome for high values in a game where two players traverse
     * a tree and one of them tries to reach leaves with minimal values and the other one wants to reach leaves with maximum values.
     * @return the index of the best child
     */
    public int indexOfNodeWithBestExpectationOfHighValue() {
        int bestValue = Integer.MIN_VALUE;
        List<Integer> bestIndices = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).isVisible()) {
                children.get(i).passLow();
                if (ConsoleOutput.treeTraversal) System.out.println("Dir: " + i + ", Rating: " + children.get(i).getValue());
                if (children.get(i).getValue() > bestValue) {
                    bestValue = children.get(i).getValue();
                    bestIndices.clear();
                    bestIndices.add(i);
                } else if (children.get(i).getValue() == bestValue) {
                    bestIndices.add(i);
                }
            }
        }
        val = bestValue;
        return bestIndices.get((int)(Math.random() * bestIndices.size()));
    }

    /**
     * return true if this node has children, false otherwise
     * @return true if this node has children, false otherwise
     */
    private boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * deletes all children nodes
     */
    public void makeLeave() {
        if (children.isEmpty()) return;
        for (int i = 0; i < tree.getAmountOfLeaves(); i++) {
            if (isParentOf(tree.getLeave(i))) tree.removeLeave(tree.getLeave(i));
        }
        children = new ArrayList<>();
        tree.addLeave(this);
    }

    /**
     * returns true if this node is a (grand)*parent of the given node
     * @param other other node
     */
    private boolean isParentOf(Node other) {
        for (Node child : children) if (child.equals(other)) return true;
        for (Node child : children) if (child.isParentOf(other)) return true;
        return false;
    }

    @Override
    public String toString() {
        return recToString(0);
    }

    private String recToString(int spaceOffset) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < spaceOffset; i++) str.append(" ");
        str.append(val).append("\n");
        for (Node child : children) {
            str.append(child.recToString(spaceOffset + 4));
        }
        return str.toString();
    }

    public void setInvisible() {
        invisible = true;
    }

    private boolean isVisible() {
        return !invisible;
    }

    boolean isLeave() {
        return children.size() == 0;
    }

    int getAmountOfChildren() {
        return children.size();
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return children.contains(o);
    }

    @NotNull
    @Override
    public Iterator<Node> iterator() {
        return children.iterator();
    }

    @Override
    public void forEach(Consumer<? super Node> action) {
        children.forEach(action);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return children.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] ts) {
        return children.toArray(ts);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return children.toArray(generator);
    }

    @Override
    public boolean add(Node node) {
        return children.add(node);
    }

    @Override
    public boolean remove(Object o) {
        return children.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        return children.containsAll(collection);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Node> collection) {
        return children.addAll(collection);
    }

    @Override
    public boolean addAll(int i, @NotNull Collection<? extends Node> collection) {
        return children.addAll(i, collection);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        return children.removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super Node> filter) {
        return children.removeIf(filter);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        return children.retainAll(collection);
    }

    @Override
    public void replaceAll(UnaryOperator<Node> operator) {
        children.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super Node> c) {
        children.sort(c);
    }

    @Override
    public void clear() {
        children.clear();
    }

    @Override
    public Node get(int i) {
        return children.get(i);
    }

    @Override
    public Node set(int i, Node node) {
        return children.set(i, node);
    }

    @Override
    public void add(int i, Node node) {
        children.add(i, node);
    }

    @Override
    public Node remove(int i) {
        return children.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return children.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return children.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<Node> listIterator() {
        return children.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<Node> listIterator(int i) {
        return children.listIterator(i);
    }

    @NotNull
    @Override
    public List<Node> subList(int i, int i1) {
        return children.subList(i, i1);
    }

    @Override
    public Spliterator<Node> spliterator() {
        return children.spliterator();
    }

    @Override
    public Stream<Node> stream() {
        return children.stream();
    }

    @Override
    public Stream<Node> parallelStream() {
        return children.parallelStream();
    }
}
