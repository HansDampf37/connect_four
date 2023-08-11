package bot.bots.tree

class Minimax {
    companion object {
        fun <T : GameState> run(t: Tree<T>): Int {
            return step(t.root, true)
        }

        private fun <T : GameState> step(current: T, maximize: Boolean): Int {
            if (current.isLeaf) {
                return 0
            }
            for (child in current) step(child, !maximize)
            current.value = if (maximize) Integer.MIN_VALUE else Integer.MAX_VALUE
            var curIndex = -1
            for (i in current.indices) {
                val greaterThan = current[i].value >= current.value
                if (greaterThan && maximize || !greaterThan && !maximize) {
                    current.value = current[i].value
                    curIndex = current[i].lastMoveWasColumn
                }
            }
            if (current.value > 0) current.value--
            else current.value++
            return curIndex
        }
    }
}