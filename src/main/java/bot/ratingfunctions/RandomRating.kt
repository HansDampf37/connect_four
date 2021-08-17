package bot.ratingfunctions

import bot.RatingFunction
import model.Board

class RandomRating(val range: IntRange): RatingFunction {
    override fun name(): String = "Random"

    override fun invoke(board: Board): Int {
        return range.random()
    }
}