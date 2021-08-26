package bot.bots.tree

import kotlinx.coroutines.*
import java.lang.Integer.min
import kotlin.math.max

class AlphaBetaPruning {
    companion object {
        fun <T : GameState> run(t: Tree<T>): Int {
            assert(t.root.size == IntRange(0, t.root.board.WIDTH - 1).filter { t.root.board.stillSpaceIn(it) }.size)
            if (!t.minimaxRequired) return (t.root.first { it.value == t.root.value } as GameState).lastMoveWasColumn
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
                return minColumn
            }
        }
    }
}