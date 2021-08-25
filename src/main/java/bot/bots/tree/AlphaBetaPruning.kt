package bot.bots.tree

import kotlinx.coroutines.*
import java.lang.Integer.min
import kotlin.math.max

class AlphaBetaPruning {
    companion object {
        fun <T : GameState> run(t: Tree<T>): Int {
            if (!t.minimaxRequired) return t.root.value
            return runBlocking {
                step(
                    t.root, Integer.MIN_VALUE, Integer.MAX_VALUE,
                    maximisingPlayer = true,
                    firstLevel = true
                )
            }
        }

        private suspend fun <T : GameState> step(
            node: T,
            alpha: Int,
            beta: Int,
            maximisingPlayer: Boolean,
            firstLevel: Boolean
        ): Int {
            var alpha1 = alpha
            var beta1 = beta
            if (node.isLeaf) return 0
            if (maximisingPlayer) {
                var maxColumn = -1
                var maxValue = Integer.MIN_VALUE
                if (firstLevel) {
                    val futures = ArrayList<Deferred<Int>>()
                    for (child in node) {
                        if (firstLevel) futures.add(GlobalScope.async {
                            step(
                                child,
                                alpha1,
                                beta1,
                                false,
                                firstLevel = false
                            )
                        })
                    }
                    futures.forEach { it.await() }
                } else {
                    for (child in node) {
                        step(child, alpha1, beta1, false, firstLevel = false)
                    }
                }
                for (child in node) {
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
                if (firstLevel) {
                    val futures = ArrayList<Deferred<Int>>()
                    for (child in node) {
                        if (firstLevel) futures.add(GlobalScope.async {
                            step(
                                child,
                                alpha1,
                                beta1,
                                true,
                                firstLevel = false
                            )
                        })
                    }
                    futures.forEach { it.await() }
                } else {
                    for (child in node) {
                        step(child, alpha1, beta1, true, firstLevel = false)
                    }
                }
                for (child in node) {
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