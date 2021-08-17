package bot.tree

import bot.GameState
import model.procedure.ConsoleOutput
import java.lang.reflect.Constructor
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * Utilized to represent a states of a 4-gewinnt-match. Node A is the child of node B <-> it exists a move that you can do in state B that brings you to state A
 *
 * @param depth  amount of levels
 * @param spread amount of children per node
 */
@Suppress("UNCHECKED_CAST")
class Tree<T : Node>(var root: T) : Iterable<T> {

    /**
     * All nodes in the tree without children
     */
    val leaves: MutableList<T> = ArrayList()

    val size: Int
        get() {
            return 1 + root.amountDescendants
        }

    init {
        leaves.addAll(this.filter { it.isLeaf })
    }

    /**
     * Constructor
     * @param depth  amount of levels
     * @param spread amount of children per node
     * @param root the root node of the tree
     * @param factory the method to call when instantiating nodes upon building the tree (depth, index in child list, parent)
     */
    constructor(depth: Int, spread: Int, root: T, factory: (Int, Int, T?) -> T) : this(root) {
        addChildNodesToTree(root, spread, depth, 0, factory)
        if (ConsoleOutput.treeInitiation) println(this)
    }

    constructor(
        depth: Int,
        spread: Int,
        factory: (Int, Int, T?) -> T
    ) : this(depth, spread, factory(0, 0, null), factory)

    /**
     * Recursive method that builds the tree.
     * If the current level == the depth the current node is added to [leaves].
     * Else <spread> children are generated and in each of them this method is called again but with lvl = lvl + 1
     *
     * @param current current node
     * @param spread  amount of children per node
     * @param depth   amount of levels
     * @param lvl     current level
    </spread> */
    private fun addChildNodesToTree(current: T, spread: Int, depth: Int, lvl: Int, factory: (Int, Int, T?) -> T) {
        if (lvl == depth) {
            addLeaf(current)
        } else {
            for (i in 0 until spread) {
                addChild(current, factory(lvl, i, current))
                addChildNodesToTree(current[i] as T, spread, depth, lvl + 1, factory)
            }
        }
    }

    /**
     * Adds a child to a parent-node in a tree
     */
    fun addChild(parent: T, child: T) {
        if (parent.isLeaf) leaves.remove(parent)
        parent.children.add(child)
        addLeaf(child)
    }

    /**
     * removes a child from the given nodes children list
     */
    fun removeChild(parent: T, child: T) {
        if (child.isLeaf) leaves.remove(child)
        parent.children.remove(child)
        if (parent.isLeaf) addLeaf(parent)
    }

    /**
     * deletes all children nodes for a given node
     */
    fun makeLeave(node: T) {
        leaves.removeAll { node.isParentOf(it) }
        node.children.clear()
        addLeaf(node)
    }

    fun step(move: Int) {
        val oldRoot: T = root
        root = oldRoot[move] as T
        for (child in leaves.filter { !root.isParentOf(it) }) leaves.remove(child)
    }

    override fun toString(): String {
        return root.toString()
    }

    fun leavesToString(): String {
        val str = StringBuilder()
        for (i in leaves.indices) {
            str.append(leaves[i].value).append(if (i != leaves.size - 1) ", " else "")
        }
        return str.toString()
    }

    /**
     * adds a given node to the leaves list
     *
     * @param node a node
     */
    private fun addLeaf(node: T) {
        if (!leaves.contains(node)) {
            leaves.add(node)
        }
    }

    override fun iterator(): Iterator<T> {
        return DFS()
    }

    @Suppress("UNCHECKED_CAST")
    private inner class DFS : Iterator<T> {
        private val list: MutableList<T> = ArrayList()
        private var curIndex = 0

        init {
            addAll(root)
        }

        private fun addAll(current: T) {
            list.add(current)
            for (child in current) {
                addAll(child as T)
            }
        }

        override fun hasNext(): Boolean {
            return curIndex <= list.size - 1
        }

        override fun next(): T {
            return list[curIndex++]
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inner class BFS : Iterator<T> {
        private val queue: Queue<T> = LinkedBlockingQueue()

        init {
            queue.add(root)
        }

        override fun hasNext(): Boolean {
            return queue.isNotEmpty()
        }

        override fun next(): T {
            queue.addAll(queue.peek().children as Collection<T>)
            return queue.remove()
        }
    }
}