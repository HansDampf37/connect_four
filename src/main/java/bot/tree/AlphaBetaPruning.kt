package bot.tree

import java.lang.Integer.min
import kotlin.math.max

class AlphaBetaPruning() {

    fun run(t: Tree): Int {
        return step(t.root, Integer.MIN_VALUE, Integer.MAX_VALUE, true)
    }

    private fun step(node: Node, alpha: Int, beta: Int, maximisingPlayer: Boolean): Int {
        var alpha1 = alpha
        var beta1 = beta
        if (node.isLeave) return node.value
        if (maximisingPlayer) {
            var maxValue = Integer.MIN_VALUE
            for (child in node) {
                val value = step(child, alpha1, beta1, false)
                maxValue = max(maxValue, value)
                alpha1 = max(alpha1, maxValue)
                if (beta1 <= alpha1) break
            }
            node.value = maxValue
            return maxValue
        } else {
            var minValue = Integer.MAX_VALUE
            for (child in node) {
                val value = step(child, alpha1, beta1, true)
                minValue = min(minValue, value)
                beta1 = min(beta1, value)
                if (beta1 <= alpha1) break
            }
            node.value = minValue
            return minValue
        }
    }
}