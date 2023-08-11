package bot.bots.tree

import kotlinx.coroutines.*
import java.lang.Integer.min
import kotlin.math.max

class AlphaBetaPruning {
    companion object {
        fun <T : GameState> run(t: Tree<T>): Int {
            assert(t.root.size == IntRange(0, t.root.board.WIDTH - 1).filter { t.root.board.stillSpaceIn(it) }.size)
            if (!t.minimaxRequired)  (t.root.maxWithOrNull{ o1, o2 -> o1.value - o2.value } as GameState).lastMoveWasColumn
            return runBlocking { firstStep(t) }
        }

        @OptIn(DelicateCoroutinesApi::class)
        private suspend fun <T : GameState> firstStep(tree: Tree<T>): Int {
            val alpha = Integer.MIN_VALUE
            val beta = Integer.MAX_VALUE
            val futures = ArrayList<Deferred<Int>>()
            for (child in tree.root) {
                futures.add(GlobalScope.async {
                    step(
                        child,
                        alpha,
                        beta,
                        maximisingPlayer = false
                    )
                })
            }
            futures.forEach { it.await() }
            var maxIndex = -1
            var maxValue = Integer.MIN_VALUE
            for (child in tree.root) {
                if (child.value >= maxValue) {
                    maxValue = child.value
                    maxIndex = child.lastMoveWasColumn
                }
            }
            tree.root.value = maxValue
            if (maxValue < 0) tree.root.value++
            if (maxValue > 0) tree.root.value--
            return maxIndex
        }

        /**
         * After this function the given [node] will have the value reached through alpha beta pruning
         * @return the column of the (best/worst) move
         */
        private fun <T : GameState> step(
            node: T,
            alpha: Int,
            beta: Int,
            maximisingPlayer: Boolean,
        ): Int {
            var alpha1 = alpha
            var beta1 = beta
            if (node.isLeaf) return 0
            if (maximisingPlayer) {
                var maxColumn = -1
                var maxValue = Integer.MIN_VALUE
                for (child in node) {
                    step(child, alpha1, beta1, false)
                    val value = child.value
                    if (value >= maxValue) {
                        maxValue = value
                        maxColumn = child.lastMoveWasColumn
                    }
                    alpha1 = max(alpha1, maxValue)
                    if (beta1 <= alpha1) break
                }
                node.value = maxValue
                // let values age
                if (maxValue < 0) node.value++
                if (maxValue > 0) node.value--
                return maxColumn
            } else {
                var minColumn = -1
                var minValue = Integer.MAX_VALUE
                for (child in node) {
                    step(child, alpha1, beta1, true)
                    val value = child.value
                    if (value <= minValue) {
                        minValue = value
                        minColumn = child.lastMoveWasColumn
                    }
                    beta1 = min(beta1, value)
                    if (beta1 <= alpha1) break
                }
                node.value = minValue
                // let values age
                if (minValue < 0) node.value++
                if (minValue > 0) node.value--
                return minColumn
            }
        }
    }
}