package bot.bots.tree

import model.procedure.ConsoleOutput
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.pow

/**
 * Utilized to represent a states of a 4-gewinnt-match. Node A is the child of node B <-> it exists a move that you can do in state B that brings you to state A
 */
@Suppress("UNCHECKED_CAST")
class Tree<T : Node>(_root: T) : Iterable<T> {
    private val leavesLock = ReentrantLock()

    var root : T = _root
    set(value) {
        field = value
        leavesLock.withLock {
            leaves = HashSet(leaves.filter { it.isDescendantOf(value) })
        }
        // this.size = size()
    }

    /**
     * All nodes in the tree without children
     */
    var leaves: HashSet<T> = HashSet()
    private set

    /**
     * Counter that gets updated estimates the tree size. Doesn't keep track of duplicates.
     */
    //var size: Int = 1
    //    private set

    val avgDepth: Float
        get() {
            var depthSum = 0
            for (n in leaves) {
                depthSum += n.generation()
            }
            return depthSum.toFloat() / leaves.size.toFloat()
        }

    val varianceInDepth: Float
        get() {
            val mean = avgDepth
            var mse = 0f
            for (n in leaves) {
                mse += (mean - n.generation()).pow(2)
            }
            return mse / leaves.size.toFloat()
        }

    var minimaxRequired = true
    set(value) {
        field = value
        if (ConsoleOutput.treeTraversal) println("AlphaBetaPruning inactive")
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
     * Calculates the tree's size
     */
    fun size(): Int {
        return 1 + root.amountDescendants
    }

    /**
     * Adds a child to a parent-node in a tree
     */
    fun addChild(parent: T, child: T) {
        if (parent.isLeaf) leavesLock.withLock { leaves.remove(parent) }
        parent.children.add(child)
        child.parent = parent
        addLeaf(child)
        //size += child.amountDescendants + 1
    }

    /**
     * removes a child from the given nodes children list
     */
    fun removeChild(parent: T, child: T) {
        if (parent.children.remove(child)) {
            if (child.isLeaf) leaves.remove(child)
            child.parent = child
            if (parent.isLeaf) addLeaf(parent)
            //size -= child.amountDescendants + 1
        } else {
            throw IllegalArgumentException("Parent is not a direct parent of child")
        }
    }

    /**
     * deletes all children nodes for a given node
     */
    fun makeLeave(node: T) {
        //size -= node.amountDescendants - 1
        leaves.removeAll { node.isParentOf(it) }
        node.children.clear()
        addLeaf(node)
    }

    /**
     * Sets this trees root node to the former roots [move]th child and prunes the tree's nodes that are not descending from
     * the new root node.
     * @param move the index of the new root node
     */
    fun step(move: Int) {
        val newRoot = root.first { (it as GameState).lastMoveWasColumn == move } as T
        removeChild(root, newRoot)
        root = newRoot
        //leaves.removeAll { !it.isDescendantOf(root) && it != root }
        //if (leaves.isEmpty()) leaves.add(root)
        //size = size()
    }

    override fun toString(): String {
        return root.toString()
    }

    fun leavesToString(): String {
        return leaves.joinToString { ", " }
    }

    /**
     * adds a given node to the leaves list
     *
     * @param node a node
     */
    private fun addLeaf(node: T) {
        leavesLock.withLock {
            if (!leaves.contains(node)) {
                leaves.add(node)
            }
        }
    }

    override fun iterator(): Iterator<T> {
        return DFS()
    }

    fun bfs(): Iterator<T> {
        return BFS()
    }

    fun dfs(): Iterator<T> {
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