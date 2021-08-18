package bot.bots.tree

import model.procedure.ConsoleOutput
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.UnaryOperator
import java.util.stream.Stream

/**
 * A node in the [tree-graph][Tree]
 */
open class Node(/*var tree: Tree<Node>? = null, */val children: MutableList<Node> = ArrayList(), var value: Int = 0) :
    MutableList<Node> {

    @Deprecated("prune tree instead", ReplaceWith("nothing"))
    private val isVisible: Boolean
        get() = !invisible
    val isLeaf: Boolean
        get() = children.size == 0

    var parent: Node = this

    val amountDescendants: Int
        get() {
            return children.size + children.sumOf { it.amountDescendants }
        }

    /**
     * If a Node represents an illegal state of the game it should be invisible meaning no matter what comparison is happening this node is losing
     */
    @Deprecated("prune tree instead")
    var invisible = false

    /* /**
      * Adds a child to a tree
      */
     fun addChild(child: Node) {
         if (children.isEmpty()) tree!!.leaves.remove(this)
         children.add(child)
         tree!!.addLeave(child)
     }

     /**
      * removes a child from this nodes children list
      */
     fun removeChild(child: Node) {
         tree!!.leaves.remove(child)
         children.remove(child)
         if (children.isEmpty()) tree!!.addLeave(this)
     }*/

    /**
     * sets this nodes value to the minimum value of all it's child's values
     */
    private fun setValueToMinimumChild() {
        value = Int.MAX_VALUE
        for (child in children) if (child.isVisible && child.value < value) value = child.value
    }

    /**
     * sets this nodes value to the maximum value of all it's child's values
     */
    private fun setValueToMaximumChild() {
        value = Int.MIN_VALUE
        for (child in children) if (child.isVisible && child.value > value) value = child.value
    }

    /**
     * recursive method that is invoked alternating with [.passLow]. This method chooses the maximum value of all the children's values and sets it's own value
     * to this value. If their children aren't leaves [.passLow] is invoked on them. Overall this is a simulation of a game where two players traverse
     * a tree and one of them tries to reach leaves with minimal values and the other one wants to reach leaves with maximum values.
     */
    private fun passHigh() {
        for (child in children) {
            if (child.hasChildren()) {
                child.passLow()
            }
        }
        setValueToMaximumChild()
    }

    /**
     * recursive method that is invoked alternating with [.passHigh]. This method chooses the minimum value of all the children's values and sets it's own value
     * to this value. If their children aren't leaves [.passHigh] is invoked on them. Overall this is a simulation of a game where two players traverse
     * a tree and one of them tries to reach leaves with minimal values and the other one wants to reach leaves with maximum values.
     */
    private fun passLow() {
        for (child in children) {
            if (child.hasChildren()) {
                child.passHigh()
            }
        }
        setValueToMinimumChild()
    }

    /**
     * This Method uses [.passLow] to determine the index of the child with the best outcome for high values in a game where two players traverse
     * a tree and one of them tries to reach leaves with minimal values and the other one wants to reach leaves with maximum values.
     * @return the index of the best child
     */
    fun indexOfNodeWithBestExpectationOfHighValue(): Int {
        var bestValue = Int.MIN_VALUE
        val bestIndices: MutableList<Int> = ArrayList()
        for (i in children.indices) {
            if (children[i].isVisible) {
                children[i].passLow()
                if (ConsoleOutput.treeTraversal) println("Dir: " + i + ", Rating: " + children[i].value)
                if (children[i].value > bestValue) {
                    bestValue = children[i].value
                    bestIndices.clear()
                    bestIndices.add(i)
                } else if (children[i].value == bestValue) {
                    bestIndices.add(i)
                }
            }
        }
        value = bestValue
        return bestIndices[(Math.random() * bestIndices.size).toInt()]
    }

    /**
     * return true if this node has children, false otherwise
     * @return true if this node has children, false otherwise
     */
    private fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

    /*/**
     * deletes all children nodes
     */
    fun makeLeave() {
        if (children.isEmpty()) return
        for (leave in tree!!.leaves) {
            if (isParentOf(leave)) tree!!.leaves.remove(leave)
        }
        children.clear()
        tree!!.addLeave(this)
    }*/

    /**
     * returns true if this node is a (grand)*parent of the given node
     * @param other other node
     */
    fun isParentOf(other: Node): Boolean {
        return other.isDescendantOf(this)
           // for (child in children) if (child == other) return true
            //for (child in children) if (child.isParentOf(other)) return true
            //return false
    }

    /**
     * returns true if this node is a (grand)*child of the given node
     * @param other other node
     */
    fun isDescendantOf(other: Node): Boolean {
        if (parent == other) return true
        if (parent == this) return false
        return parent.isDescendantOf(other)
    }

    override fun toString(): String {
        return recToString(0)
    }

    private fun recToString(spaceOffset: Int): String {
        val str = StringBuilder()
        for (i in 0 until spaceOffset) str.append(" ")
        str.append(value).append("\n")
        for (child in children) {
            str.append(child.recToString(spaceOffset + 4))
        }
        return str.toString()
    }

    override val size: Int
        get() = children.size

    override fun isEmpty(): Boolean {
        return children.isEmpty()
    }

    override operator fun contains(element: Node): Boolean {
        return children.contains(element)
    }

    override fun iterator(): MutableIterator<Node> {
        return children.iterator()
    }

    override fun forEach(action: Consumer<in Node>) {
        children.forEach(action)
    }

    override fun add(element: Node): Boolean {
        return children.add(element)
    }

    override fun remove(element: Node): Boolean {
        return children.remove(element)
    }

    override fun containsAll(elements: Collection<Node>): Boolean {
        return children.containsAll(elements)
    }

    override fun addAll(elements: Collection<Node>): Boolean {
        return children.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<Node>): Boolean {
        return children.addAll(index, elements)
    }

    override fun removeAll(elements: Collection<Node>): Boolean {
        return children.removeAll(elements)
    }

    override fun removeIf(filter: Predicate<in Node>): Boolean {
        return children.removeIf(filter)
    }

    override fun retainAll(elements: Collection<Node>): Boolean {
        return children.retainAll(elements)
    }

    override fun replaceAll(operator: UnaryOperator<Node>) {
        children.replaceAll(operator)
    }

    override fun sort(c: Comparator<in Node>) {
        children.sortWith(c)
    }

    override fun clear() {
        children.clear()
    }

    override fun get(index: Int): Node {
        return children[index]
    }

    override fun set(index: Int, element: Node): Node {
        return children.set(index, element)
    }

    override fun add(index: Int, element: Node) {
        children.add(index, element)
    }

    override fun removeAt(index: Int): Node {
        return children.removeAt(index)
    }

    override fun indexOf(element: Node): Int {
        return children.indexOf(element)
    }

    override fun lastIndexOf(element: Node): Int {
        return children.lastIndexOf(element)
    }

    override fun listIterator(): MutableListIterator<Node> {
        return children.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<Node> {
        return children.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Node> {
        return children.subList(fromIndex, toIndex)
    }

    override fun spliterator(): Spliterator<Node> {
        return children.spliterator()
    }

    override fun stream(): Stream<Node> {
        return children.stream()
    }

    override fun parallelStream(): Stream<Node> {
        return children.parallelStream()
    }
}