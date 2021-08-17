package bot.tree

import java.lang.Integer.min
import kotlin.math.max

class AlphaBetaPruning {
    companion object {
        fun <T : Node> run(t: Tree<T>): Int {
            return step(t.root, Integer.MIN_VALUE, Integer.MAX_VALUE, true)
        }

        private fun <T : Node> step(node: T, alpha: Int, beta: Int, maximisingPlayer: Boolean): Int {
            var alpha1 = alpha
            var beta1 = beta
            if (node.isLeaf) return node.value
            if (maximisingPlayer) {
                var maxValue = Integer.MIN_VALUE
                for (child in node.filter { !it.invisible } as Collection<T>) {
                    step(child, alpha1, beta1, false)
                    val value = child.value
                    maxValue = max(maxValue, value)
                    alpha1 = max(alpha1, maxValue)
                    if (beta1 <= alpha1) break
                }
                node.value = maxValue
                return node.indexOf(node.first { it.value == maxValue })
            } else {
                var minValue = Integer.MAX_VALUE
                for (child in node.filter { !it.invisible } as Collection<T>) {
                    step(child, alpha1, beta1, true)
                    val value = child.value
                    minValue = min(minValue, value)
                    beta1 = min(beta1, value)
                    if (beta1 <= alpha1) break
                }
                node.value = minValue
                return node.indexOf(node.first { it.value == minValue })
            }
        }
    }
}