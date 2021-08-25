package bot.bots.tree

import java.lang.Integer.min
import kotlin.math.max

class AlphaBetaPruning {
    companion object {
        fun <T : GameState> run(t: Tree<T>): Int {
            return step(t.root, Integer.MIN_VALUE, Integer.MAX_VALUE, true)
        }

        private fun <T : GameState> step(node: T, alpha: Int, beta: Int, maximisingPlayer: Boolean): Int {
            var alpha1 = alpha
            var beta1 = beta
            if (node.isLeaf) return 0
            if (maximisingPlayer) {
                var maxColumn = -1
                var maxValue = Integer.MIN_VALUE
                for (i in node.indices) {
                    step(node[i], alpha1, beta1, false)
                    val value = node[i].value
                    if (value > maxValue) {
                        maxValue = value
                        maxColumn = node[i].lastMoveWasColumn
                    }
                    alpha1 = max(alpha1, maxValue)
                    if (beta1 <= alpha1) break
                }
                node.value = maxValue
                return maxColumn
            } else {
                var minColumn = -1
                var minValue = Integer.MAX_VALUE
                for (i in node.indices) {
                    step(node[i], alpha1, beta1, true)
                    val value = node[i].value
                    if (value < minValue) {
                        minValue = value
                        minColumn = node[i].lastMoveWasColumn
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