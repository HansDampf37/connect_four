package bot.ratingfunctions

import bot.bots.RatingFunction
import model.Board

class NullRating : RatingFunction {
    override fun name() = "Null"

    override fun invoke(p1: Board) = 0
}