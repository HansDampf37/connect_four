package bot.ratingfunctions

import bot.bots.RatingFunction
import model.Board

class RandomRating(val range: IntRange): RatingFunction {
    override fun name(): String = "Random"

    override fun invoke(board: Board): Int {
        return range.random()
    }
}